package myebay.store.configuration;

import myebay.store.controllers.ItemRestController;
import myebay.store.model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ItemParser {
    public static List<ItemInt> parseItemsFromJson(String json){
        List<ItemInt> items = new ArrayList<>();
        JSONObject obj = new JSONObject(json);
        if (obj.getInt("total") == 0){
            return items;
        }
        JSONArray array = obj.getJSONArray("itemSummaries");
        array.forEach(row->{
            JSONObject rowObj = ((JSONObject) row);
            String title = getTitle(rowObj);

            List<String> imgUrls = getImgUrls(rowObj);

            Price price = getPrice(rowObj);
            Price originalPrice = getOriginalPrice(rowObj);

            Seller seller = getSeller(rowObj);

            String condition = getCondition(rowObj);

            ItemLocation location = getItemLocation(rowObj);

            String link = getLink(rowObj);

            items.add(new Item(title, imgUrls, price, originalPrice, seller, condition, location,link));
        });
        return items;
    }

    private static String getLink(JSONObject rowObj) {
        return rowObj.getString("itemWebUrl");
    }

    private static String getTitle(JSONObject rowObj) {
        return rowObj.getString("title");
    }

    private static List<String> getImgUrls(JSONObject rowObj) {
        List<String> imgUrls = new ArrayList<>();
        if (!rowObj.isNull("image")){
            imgUrls.add(rowObj.getJSONObject("image").getString("imageUrl"));
        }
        JSONArray additionalImgs = rowObj.isNull("additionalImages") ? null : rowObj.getJSONArray("additionalImages");
        if (additionalImgs != null){
            for(Object jsonObject : additionalImgs){
                imgUrls.add(((JSONObject)jsonObject).getString("imageUrl"));
            }
        }
        return imgUrls;
    }

    private static Price getPrice(JSONObject rowObj) {
        return new Price(rowObj.getJSONObject("price").getFloat("value"),rowObj.getJSONObject("price").getString("currency"));
    }

    private static Price getOriginalPrice(JSONObject rowObj) {
        JSONObject marketingPrice = rowObj.isNull("marketingPrice") ? null : rowObj.getJSONObject("marketingPrice");
        Price originalPrice = null;
        if (marketingPrice != null){
            JSONObject originalPriceJson = marketingPrice.getJSONObject("originalPrice");
            originalPrice = new Price(originalPriceJson.getFloat("value"),originalPriceJson.getString("currency"));
        }
        return originalPrice;
    }

    private static Seller getSeller(JSONObject rowObj) {
        JSONObject sellerJson = rowObj.getJSONObject("seller");
        return new Seller(sellerJson.getString("username"),sellerJson.getFloat("feedbackPercentage"), sellerJson.getLong("feedbackScore"));
    }

    private static String getCondition(JSONObject rowObj) {
        return rowObj.isNull("condition") ? null :rowObj.getString("condition");
    }

    private static ItemLocation getItemLocation(JSONObject rowObj) {
        JSONObject locationJson = rowObj.getJSONObject("itemLocation");
        return new ItemLocation(locationJson.isNull("postalCode") ? "": locationJson.getString("postalCode"),locationJson.getString("country"));
    }

    public static String getOffsetLink(String json, String key){
        JSONObject obj = new JSONObject(json);
        if (!obj.isNull(key)){
            return obj.getString(key);
        }
        return null;
    }

    public static int getTotalResultsNumber(String json){
        JSONObject obj = new JSONObject(json);
        return obj.getInt("total");
    }

    public static String getCurrentHref(String json){
        JSONObject obj = new JSONObject(json);
        return obj.getString("href");
    }

    public static int getCurrentOffset(String json){
        JSONObject obj = new JSONObject(json);
        return obj.getInt("offset");
    }
}
