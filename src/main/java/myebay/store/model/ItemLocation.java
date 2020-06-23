package myebay.store.model;

public class ItemLocation {
    String postalCode;
    String country;

    public ItemLocation(String postalCode, String country) {
        this.postalCode = postalCode;
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
