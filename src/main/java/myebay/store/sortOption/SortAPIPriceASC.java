package myebay.store.sortOption;

import org.springframework.stereotype.Component;

@Component
public class SortAPIPriceASC implements SortAPI {

    String name = "Price ASC";
    String value = "price";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getQueryValue() {
        return value;
    }
}
