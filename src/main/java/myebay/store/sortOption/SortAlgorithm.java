package myebay.store.sortOption;

import myebay.store.model.ItemInt;

import java.util.Comparator;

public abstract class SortAlgorithm implements Comparator<ItemInt>, Sort {
    String name;

    public SortAlgorithm(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
