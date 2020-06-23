package myebay.store.sortOption;

import org.springframework.stereotype.Component;

@Component
public class SortAPIPriceDESC implements SortAPI {

    String name = "Price DESC";
    String value = "-price";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getQueryValue() {
        return value;
    }
}
