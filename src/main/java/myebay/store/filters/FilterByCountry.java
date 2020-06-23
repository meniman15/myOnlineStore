package myebay.store.filters;

import myebay.store.model.ItemInt;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FilterByCountry implements ItemFilter{

    String name = "Only from US";
    String desiredLocation = "US";

    @Override
    public List<ItemInt> filter(List<ItemInt> items) {
        return items.stream().filter(
                item-> item.getItemLocation().getCountry().equals(desiredLocation)
        ).collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }
}
