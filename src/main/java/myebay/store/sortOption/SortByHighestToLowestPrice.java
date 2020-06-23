package myebay.store.sortOption;

import myebay.store.model.ItemInt;
import org.springframework.stereotype.Component;

@Component
public class SortByHighestToLowestPrice extends SortAlgorithm {

    public SortByHighestToLowestPrice(){
        super("Highest to lowest price");
    }

    @Override
    public int compare(ItemInt o1, ItemInt o2) {
        float o1Price = o1.getPrice().getAmount();
        float o2Price = o2.getPrice().getAmount();
        return Float.compare(o2Price, o1Price);
    }

}
