package myebay.store.sortOption;

import myebay.store.model.ItemInt;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class SortBySellerBestFeedbackPercentage extends SortAlgorithm {

    public SortBySellerBestFeedbackPercentage(){
        super("Highest to lowest seller feedback percentage");
    }
    @Override
    public int compare(ItemInt o1, ItemInt o2) {
        return Float.compare(o1.getSeller().getFeedbackPercentage(),o2.getSeller().getFeedbackPercentage());
    }
}
