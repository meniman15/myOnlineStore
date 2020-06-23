package myebay.store.filters;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class NewConditionFilter implements APIFilter {

    String name = "Conditions";
    String filterValue = "NEW";

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<String> getOptions() {
        return new ArrayList<>(Arrays.asList("NEW","USED"));
    }

    @Override
    public String getFilterValue() {
        return this.filterValue;
    }
}
