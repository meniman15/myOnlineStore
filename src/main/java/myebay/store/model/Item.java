package myebay.store.model;

import java.util.List;
import java.util.Objects;

public class Item implements ItemInt{

    String title;
    List<String> imgUrls;
    Price price;
    Price originalPrice;
    Seller seller;
    String condition;
    ItemLocation itemLocation;
    String link;

    public Item() {}

    public Item(String title, List<String> imgUrls, Price price, Price originalPrice, Seller seller, String condition, ItemLocation itemLocation, String link) {
        this.title = title;
        this.imgUrls = imgUrls;
        this.price = price;
        this.originalPrice = originalPrice;
        this.seller = seller;
        this.condition = condition;
        this.itemLocation = itemLocation;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Price getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Price originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public ItemLocation getItemLocation() {
        return itemLocation;
    }

    public void setItemLocation(ItemLocation itemLocation) {
        this.itemLocation = itemLocation;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(title, item.title) &&
                Objects.equals(imgUrls, item.imgUrls) &&
                Objects.equals(price, item.price) &&
                Objects.equals(originalPrice, item.originalPrice) &&
                Objects.equals(seller, item.seller) &&
                Objects.equals(condition, item.condition) &&
                Objects.equals(itemLocation, item.itemLocation) &&
                Objects.equals(link, item.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, imgUrls, price, originalPrice, seller, condition, itemLocation, link);
    }
}
