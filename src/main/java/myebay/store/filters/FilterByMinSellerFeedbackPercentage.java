package myebay.store.filters;

import myebay.store.model.ItemInt;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FilterByMinSellerFeedbackPercentage implements ItemFilter {

    String name = "Minimum seller feedback";
    float filterValue = 99;

    @Override
    public List<ItemInt> filter(List<ItemInt> items) {
        return items.stream().filter(item -> item.getSeller().getFeedbackPercentage() > filterValue).collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return name;
    }
}
