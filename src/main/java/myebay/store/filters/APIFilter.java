package myebay.store.filters;

import java.util.List;

public interface APIFilter extends Filter{
    List<String> getOptions();
    String getFilterValue();
}
