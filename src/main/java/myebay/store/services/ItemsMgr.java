package myebay.store.services;

import myebay.store.filters.ItemFilter;
import myebay.store.model.Item;
import myebay.store.model.ItemInt;
import myebay.store.sortOption.SortAlgorithm;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//A service manager for storing current fetched items and next/previous items links
@Service
public class ItemsMgr {

    //items before sort or filter
    List<ItemInt> originalItems;
    List<ItemInt> currentItems;
    int totalResults;
    //link before sort or filter
    String originalLink;
    String currentLink;
    String prevItemsLink;
    String nextItemsLink;
    int currentOffset;

    List<ItemFilter> currentFilters;
    SortAlgorithm currentSortAlgorithm;

    boolean onlyWomenShoes;

    public ItemsMgr() {
        originalItems = new ArrayList<>();
        currentItems = new ArrayList<>();
        currentFilters = new ArrayList<>();
    }

    public List<ItemInt> getCurrentItems() {
        return currentItems;
    }

    public void setCurrentItems(List<ItemInt> currentItems) {
        this.currentItems = currentItems;
    }

    public String getNextItemsLink() {
        return nextItemsLink;
    }

    public void setNextItemsLink(String nextItemsLink) {
        this.nextItemsLink = nextItemsLink;
    }

    public String getPrevItemsLink() {
        return prevItemsLink;
    }

    public void setPrevItemsLink(String prevItemsLink) {
        this.prevItemsLink = prevItemsLink;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getCurrentOffset() {
        return currentOffset;
    }

    public void setCurrentOffset(int currentOffset) {
        this.currentOffset = currentOffset;
    }

    public List<ItemFilter> getCurrentFilters() {
        return currentFilters;
    }

    public String getOriginalLink() {
        return originalLink;
    }

    public void setOriginalLink(String originalLink) {
        this.originalLink = originalLink;
    }

    public String getCurrentLink() {
        return currentLink;
    }

    public void setCurrentLink(String currentLink) {
        this.currentLink = currentLink;
    }

    public SortAlgorithm getCurrentSortAlgorithm() {
        return currentSortAlgorithm;
    }

    public void setCurrentSortAlgorithm(SortAlgorithm currentSortAlgorithm) {
        this.currentSortAlgorithm = currentSortAlgorithm;
    }

    public List<ItemInt> getOriginalItems() {
        return originalItems;
    }

    public void setOriginalItems(List<ItemInt> originalItems) {
        this.originalItems = originalItems;
    }

    public boolean isOnlyWomenShoes() {
        return onlyWomenShoes;
    }

    public void setOnlyWomenShoes(boolean onlyWomenShoes) {
        this.onlyWomenShoes = onlyWomenShoes;
    }
}
