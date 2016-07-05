package com.ravijain.sankalp.data;

import java.util.Hashtable;
import java.util.Iterator;

/**
 * Created by ravijain on 7/2/2016.
 */
public class SpCategoryItem implements SpDataConstants {

    private int _id;
    private String _itemName;
    private String _itemDisplayName;
    private int _categoryId;

    public static String[] foodItemNames = {ITEM_NAME_POTATO, ITEM_NAME_ONION, ITEM_NAME_HONEY};
    public static String[] entertainmentItemNames = {ITEM_NAME_MOVIES, ITEM_NAME_DINING, ITEM_NAME_TV};
    public static String[] travelItemNames = {ITEM_NAME_FOREIGN_TRAVEL, ITEM_NAME_CAR};
    public static String[] dharmaItemNames = {ITEM_NAME_MALA, ITEM_NAME_DARSHAN, ITEM_NAME_KALASH,
            ITEM_NAME_SWADHYAYA, ITEM_NAME_TEERTH, ITEM_NAME_PRATIKRAMAN, ITEM_NAME_UPVAAS};

    private static Hashtable<String, SpCategoryItem> defaultItems = new Hashtable<String, SpCategoryItem>();

    static {
        int id = 0;
        Hashtable<String, SpCategory> defaultCategories = SpCategory.getDefaultCategories();
        Iterator<SpCategory> iterator = defaultCategories.values().iterator();
        while (iterator.hasNext()) {
            SpCategory category = iterator.next();
            if (category.getCategoryName().equals(CATEGORY_NAME_FOOD)) {
                for (String itemName : SpCategoryItem.foodItemNames) {
                    defaultItems.put(itemName, new SpCategoryItem(id++, itemName, itemName, category.getId()));
                }
            } else if (category.getCategoryName().equals(CATEGORY_NAME_ENTERTAINMENT)) {
                for (String itemName : SpCategoryItem.entertainmentItemNames) {
                    defaultItems.put(itemName, new SpCategoryItem(id++, itemName, itemName, category.getId()));
                }
            } else if (category.getCategoryName().equals(CATEGORY_NAME_TRAVEL)) {
                for (String itemName : SpCategoryItem.travelItemNames) {
                    defaultItems.put(itemName, new SpCategoryItem(id++, itemName, itemName, category.getId()));
                }
            } else if (category.getCategoryName().equals(CATEGORY_NAME_DHARMA)) {
                for (String itemName : SpCategoryItem.dharmaItemNames) {
                    defaultItems.put(itemName, new SpCategoryItem(id++, itemName, itemName, category.getId()));
                }
            }
        }
    }

    public SpCategoryItem(int id, String name, String displayName, int categoryId) {
        _id = id;
        _itemName = name;
        _itemDisplayName = displayName;
        _categoryId = categoryId;
    }

    public static Hashtable<String, SpCategoryItem> getDefaultCategoryItems() {
        return defaultItems;
    }

    public String toString()
    {
        return _itemName;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }


    public String getCategoryItemName() {
        return _itemName;
    }

    public void setCategoryItemName(String categoryItemName) {
        this._itemName = categoryItemName;
    }

    public String getCategoryItemDisplayName() {
        return _itemDisplayName;
    }

    public void setCategoryItemDisplayName(String categoryItemDisplayName) {
        this._itemDisplayName = categoryItemDisplayName;
    }

    public int getCategoryId() {
        return _categoryId;
    }

    public void setCategoryId(int categoryId) {
        this._categoryId = categoryId;
    }


}
