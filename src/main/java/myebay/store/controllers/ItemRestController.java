package myebay.store.controllers;

import myebay.store.configuration.ItemParser;
import myebay.store.configuration.RestTemplateConfig;
import myebay.store.filters.APIFilter;
import myebay.store.filters.Filter;
import myebay.store.filters.ItemFilter;
import myebay.store.model.ItemInt;
import myebay.store.services.ItemsMgr;
import myebay.store.sortOption.Sort;
import myebay.store.sortOption.SortAPI;
import myebay.store.sortOption.SortAlgorithm;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

//Rest controller which receives fetch requests for items, forwards them to EBAY api, and returns the results.
// The controller has additional abilities such as: sort/filter the cached items (by its own).
@RestController
@CrossOrigin("*")
public class ItemRestController {

    private static final String WOMEN_SHOES_FILTER = "&category_ids=3034";
    private static final String CLOTHING_SHOES_ACCESORIES_FILTER = "&category_ids=11450";
    private static final String EBAY_API_ITEM_SEARCH_URL_PREFIX = "https://api.ebay.com/buy/browse/v1/item_summary/search?q=";
    private static final String LIMIT_PREFIX = "&limit=";
    private static final int RESULTS_LIMIT = 50;
    private static final String SORT_PREFIX = "&sort=";
    private static final String FILTER_PREFIX = "&filter=";
    public static final String NEXT_OFFSET = "next";
    public static final String PREV_OFFSET = "prev";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ItemsMgr itemsMgr;

    @Autowired
    private List<Filter> filters;

    @Autowired
    private List<Sort> sorts;

    @GetMapping("items/{itemName}")
    public List<ItemInt> getItemsByQuery(@PathVariable String itemName){
        String fetchUrl = itemsMgr.isOnlyWomenShoes() ?  EBAY_API_ITEM_SEARCH_URL_PREFIX + itemName + WOMEN_SHOES_FILTER + LIMIT_PREFIX + RESULTS_LIMIT :
                EBAY_API_ITEM_SEARCH_URL_PREFIX + itemName + CLOTHING_SHOES_ACCESORIES_FILTER + LIMIT_PREFIX + RESULTS_LIMIT;
        updateItemsByEbayAPI(fetchUrl);
        itemsMgr.getCurrentFilters().clear();
        itemsMgr.setCurrentSortAlgorithm(null);
        itemsMgr.setOriginalItems(itemsMgr.getCurrentItems());
        itemsMgr.setOriginalLink(itemsMgr.getCurrentLink());
        return itemsMgr.getCurrentItems();
    }

    //filter items via EBAY server or by item filters.
    @GetMapping("items/filterItems/{filterName}")
    public List<ItemInt> getFilteredItems(@PathVariable String filterName){
        if (filters.stream().noneMatch(fil-> fil.getName().equals(filterName))){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No filter with the name: " +filterName+ ", was found.");
        }
        Filter filter = filters.stream().filter(fil -> fil.getName().equals(filterName)).findFirst().get();
        if (filter instanceof APIFilter && itemsMgr.getOriginalLink() != null){
            String fetchUrl = itemsMgr.getOriginalLink() + FILTER_PREFIX + filter.getName().toLowerCase() + ":{" +((APIFilter) filter).getFilterValue()+"}";
            updateItemsByEbayAPI(fetchUrl);
            itemsMgr.getCurrentFilters().clear();
        }
        else if (filter instanceof ItemFilter && !itemsMgr.getOriginalItems().isEmpty()){
            ItemFilter itemFilter = (ItemFilter) filter;
            itemsMgr.setCurrentItems(itemsMgr.getOriginalItems());
            itemsMgr.setCurrentItems(itemFilter.filter(itemsMgr.getCurrentItems()));
            itemsMgr.getCurrentFilters().clear();
            itemsMgr.getCurrentFilters().add(itemFilter);
        }
        return itemsMgr.getCurrentItems();
    }

    //valid sortOptions for API: "price","-price","newlyListed", other options are for current items sorting.
    // (they are not be relevant after fetching "next"/"prev" results.
    @GetMapping("items/sortItems/{sortOption}")
    public List<ItemInt> getSortedItems(@PathVariable String sortOption){
        if (sorts.stream().noneMatch(sort-> sort.getName().equals(sortOption))){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No sort option with the name: " +sortOption+ ", was found.");
        }
        Sort sort = sorts.stream().filter(curSort -> curSort.getName().equals(sortOption)).findFirst().get();
        if (sort instanceof SortAPI && itemsMgr.getOriginalLink() != null){
            //no sort algorithm is chosen when sortAPI is chosen.
            itemsMgr.setCurrentSortAlgorithm(null);
            String fetchUrl = itemsMgr.getOriginalLink() + SORT_PREFIX  + ((SortAPI) sort).getQueryValue();
            updateItemsByEbayAPI(fetchUrl);
        }
        else if (sort instanceof SortAlgorithm && !itemsMgr.getCurrentItems().isEmpty()){
            SortAlgorithm comparator = (SortAlgorithm) sorts.stream().filter(alg -> alg.getName().equals(sortOption)).findFirst().get();
            itemsMgr.setCurrentSortAlgorithm(comparator);
            itemsMgr.getCurrentItems().sort(comparator);
        }
        return itemsMgr.getCurrentItems();
    }

    //offsetString can be either "next"/"prev"
    @GetMapping("items/offset/{offsetString}")
    public List<ItemInt> getOffsetResults(@PathVariable String offsetString){
        if (itemsMgr.getCurrentItems().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must first fetch item results from 'items/{searchString}' before getting next/previous results.");
        }
        if ((offsetString.equals(NEXT_OFFSET) && itemsMgr.getCurrentLink().equals(itemsMgr.getNextItemsLink()) )||
                (offsetString.equals(PREV_OFFSET) && itemsMgr.getCurrentOffset() == 0)){
            //no need to re-fetch, as we reached the end of the data.
            return itemsMgr.getCurrentItems();
        }
        String fetchItemsLink = offsetString.equals(NEXT_OFFSET) ? itemsMgr.getNextItemsLink() : itemsMgr.getPrevItemsLink();
        updateItemsByEbayAPI(fetchItemsLink);
        itemsMgr.setOriginalLink(itemsMgr.getCurrentLink());
        itemsMgr.setOriginalItems(itemsMgr.getCurrentItems());
        return itemsMgr.getCurrentItems();
    }

    private void updateItemsByEbayAPI(String fetchUrl) {
        ResponseEntity<String> responseEntity;
        String json;
        URI uri =  UriComponentsBuilder.fromUriString(fetchUrl).build().encode().toUri();
        try {
            responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    RestTemplateConfig.getEntityWithHeaders(),
                    new ParameterizedTypeReference<String>() {
                    });
            json = responseEntity.getBody();
        }
        catch (HttpClientErrorException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please check your access token.",e);
        }
        assignDataToItemsMgr(json);
    }

    //parse json and assign data to the Mgr.
    private void assignDataToItemsMgr(String json) {
        List<ItemInt> items = ItemParser.parseItemsFromJson(json);
        itemsMgr.setCurrentOffset(ItemParser.getCurrentOffset(json));
        itemsMgr.setTotalResults(ItemParser.getTotalResultsNumber(json));

        itemsMgr.setCurrentLink(ItemParser.getCurrentHref(json));
        String nextOffsetLink = getActualOffsetLink(json,NEXT_OFFSET);
        itemsMgr.setNextItemsLink(nextOffsetLink);
        String prevOffsetLink = getActualOffsetLink(json,PREV_OFFSET);
        itemsMgr.setPrevItemsLink(prevOffsetLink);

        for(ItemFilter filter : itemsMgr.getCurrentFilters()){
            filter.filter(items);
        }
        if (itemsMgr.getCurrentSortAlgorithm() != null){
            items.sort(itemsMgr.getCurrentSortAlgorithm());
        }
        itemsMgr.setCurrentItems(items);
    }

    //in case we reached the last/first offset, return the current link.
    private String getActualOffsetLink(String json, String key) {
        String offsetLink = ItemParser.getOffsetLink(json,key);
        return offsetLink == null || (key.equals(NEXT_OFFSET) && !shouldPresentNextButton()) ? ItemParser.getCurrentHref(json) : offsetLink;
    }

    @GetMapping("items/offset/shouldPresentNext")
    public boolean shouldPresentNextButton(){
        return itemsMgr.getTotalResults() != 0 && itemsMgr.getCurrentOffset() + RESULTS_LIMIT < itemsMgr.getTotalResults();
    }

    @GetMapping("items/offset/shouldPresentPrev")
    public boolean shouldPresentPrevButton(){
        return itemsMgr.getCurrentOffset() - RESULTS_LIMIT > 0;
    }

    @GetMapping("items/sorts/sortNames")
    public List<String> getSortAlgorithmsNames(){
        return sorts.stream().map(Sort::getName).collect(Collectors.toList());
    }

    @GetMapping("items/filters/filterNames")
    public List<String> getFiltersNames(){
        return filters.stream().map(Filter::getName).collect(Collectors.toList());
    }

    //expects to receive a json structured like this: "isOnlyWomenShoes":"[true/false]"
    @PostMapping("items/onlyWomenShoes")
    public boolean setOnlyWomenShoes(@RequestBody String onlyWomenShoes){
        JSONObject obj = new JSONObject(onlyWomenShoes);
        boolean womenShoes = obj.getBoolean("isOnlyWomenShoes");
        itemsMgr.setOnlyWomenShoes(womenShoes);
        return womenShoes;
    }
}
