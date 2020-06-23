package myebay.store.model;

import java.util.List;

public interface ItemInt {
    String getTitle();
    List<String> getImgUrls();
    Price getPrice();
    Price getOriginalPrice();
    String getCondition();
    Seller getSeller();
    ItemLocation getItemLocation();
    String getLink();
}
