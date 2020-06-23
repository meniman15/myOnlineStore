package myebay.store.filters;

import myebay.store.model.ItemInt;

import java.util.List;

public interface ItemFilter extends Filter {
    List<ItemInt> filter(List<ItemInt> items);
}