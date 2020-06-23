package myebay.store.sortOption;

import org.springframework.stereotype.Component;

@Component
public class SortAPINewlyListed implements SortAPI {

    String name = "Newest arrival";
    String value = "newlyListed";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getQueryValue() {
        return value;
    }
}
