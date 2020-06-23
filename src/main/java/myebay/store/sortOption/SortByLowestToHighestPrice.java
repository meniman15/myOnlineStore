package myebay.store.sortOption;

import myebay.store.model.ItemInt;
import org.springframework.stereotype.Component;

@Component
public class SortByLowestToHighestPrice extends SortAlgorithm {

    public SortByLowestToHighestPrice(){
        super("Lowest to highest price");
    }
    @Override
    public int compare(ItemInt o1, ItemInt o2) {
        float o1Price = o1.getPrice().getAmount();
        float o2Price = o2.getPrice().getAmount();
        return Float.compare(o1Price, o2Price);
    }
}
