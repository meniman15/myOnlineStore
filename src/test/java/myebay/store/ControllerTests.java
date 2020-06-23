package myebay.store;

import myebay.store.controllers.ItemRestController;
import myebay.store.filters.NewConditionFilter;
import myebay.store.filters.FilterByCountry;
import myebay.store.model.Item;
import myebay.store.model.ItemInt;
import myebay.store.model.Price;
import myebay.store.services.ItemsMgr;
import myebay.store.sortOption.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class ControllerTests {

 @Autowired
 ItemRestController itemRestController;

 @Autowired
 ItemsMgr itemsMgr;

 @BeforeEach
 void clean(){
   itemsMgr.setCurrentItems(new ArrayList<>());
   itemsMgr.setTotalResults(0);
   itemsMgr.setCurrentLink(null);
 }

 @Test
 void fetchSearchTermItems_itemsInsertedToItemsMgr(){
  String searchTerm = "shoe";
  itemRestController.getItemsByQuery(searchTerm);
  assertThat(!itemsMgr.getCurrentItems().isEmpty());
 }

 @Test
 void fetchSearchTermItemsNonValidSearchTerm_noItemsInserted(){
  String searchTerm = "dfldksanfldkanflkdanslkndf";
  itemRestController.getItemsByQuery(searchTerm);
  assertThat(itemsMgr.getCurrentItems().isEmpty());
 }

 @Test
 void filterItemsByCountry_itemsFiltered(){
  FilterByCountry filter = new FilterByCountry();
  String searchTerm = "us";
  itemRestController.getItemsByQuery(searchTerm);
  List<ItemInt> items = itemRestController.getFilteredItems(filter.getName());
  assertThat(items.stream().noneMatch(item->item.getItemLocation().getCountry().equals("US")));
 }

 @Test
 void filterItemsByCondition_itemsFiltered(){
  NewConditionFilter filter = new NewConditionFilter();
  String searchTerm = "new";
  itemRestController.getItemsByQuery(searchTerm);
  //choose only non-new items
  List<ItemInt> items = itemRestController.getFilteredItems(filter.getName());
  assertThat(items.stream().noneMatch(item->item.getCondition().equals("NEW")));
 }

 @Test
 void sortItemsByPrice_itemsSorted(){
  SortAlgorithm sorter = new SortByHighestToLowestPrice();
  Item first = new Item();
  first.setPrice(new Price(36,"USD"));
  Item second = new Item();
  second.setPrice(new Price(40,"USD"));
  itemsMgr.getCurrentItems().add(first);
  itemsMgr.getCurrentItems().add(second);
  List<ItemInt> items = itemRestController.getSortedItems(sorter.getName());
  assertThat(items.get(0).getPrice().getAmount() == 40);
 }

 @Test
 void sortItemsByAPIPriceASC_itemsSorted(){
  SortAPI sorter = new SortAPIPriceDESC();
  String searchTerm = "us";
  itemRestController.getItemsByQuery(searchTerm);
  List<ItemInt> items = itemRestController.getSortedItems(sorter.getName());
  assertThat(items.get(0).getPrice().getAmount() > items.get(1).getPrice().getAmount());
 }

 @Test
 void getNextOffsetResultWithoutFirstFetch_exceptionThrown(){
  assertThatThrownBy(() -> {
   itemRestController.getOffsetResults("next");
  }).isInstanceOf(ResponseStatusException.class);
 }

 @Test
 void getSortOptions_countCorrectNumber_success(){
   assertThat(itemRestController.getSortAlgorithmsNames().size()).isEqualTo(6);
 }

 @Test
 void getFilterOptions_countCorrectNumber_success(){
  assertThat(itemRestController.getFiltersNames().size()).isEqualTo(3);
 }

 @Test
 void getShouldPresentNext_withoutFetching_false(){
  assertThat(itemRestController.shouldPresentNextButton()).isEqualTo(false);
 }

 @Test
 void getShouldPresentNext_withFetching_true(){
  itemRestController.getItemsByQuery("us");
  assertThat(itemRestController.shouldPresentNextButton()).isEqualTo(true);
 }

 @Test
 void getShouldPresentPrev_false(){
  assertThat(itemRestController.shouldPresentPrevButton()).isEqualTo(false);
 }

 @Test
 void changeOnlyWomenShoesToTrue_fetchWomenShoes_receiveTrue(){
  itemRestController.setOnlyWomenShoes("{'isOnlyWomenShoes':'true'}");
  assertThat(itemsMgr.isOnlyWomenShoes()).isEqualTo(true);
 }
}
