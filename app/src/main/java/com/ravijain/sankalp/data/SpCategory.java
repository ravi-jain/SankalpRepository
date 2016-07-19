package com.ravijain.sankalp.data;

import java.util.Hashtable;

/**
 * Created by ravijain on 7/2/2016.
 */
public class SpCategory implements SpDataConstants{

    private int _id;
    private String _categoryName;
    private String _categoryDisplayName;
    private int _sankalpType;

    public static String[] tyagCategoryNames = {CATEGORY_NAME_FOOD, CATEGORY_NAME_ENTERTAINMENT, CATEGORY_NAME_TRAVEL};
    public static String[] niyamCategoryNames = {CATEGORY_NAME_DHARMA};
    public static String[] sankalpCategoryNames = {CATEGORY_OTHERS};

    private static Hashtable<String, SpCategory> defaultCategories = new Hashtable<String, SpCategory>();

    static {
        int id = 0;
        for (String categoryName: SpCategory.tyagCategoryNames) {
            defaultCategories.put(categoryName, new SpCategory(id++, categoryName, categoryName, SANKALP_TYPE_TYAG));
        }
        for (String categoryName: SpCategory.niyamCategoryNames) {
            defaultCategories.put(categoryName, new SpCategory(id++, categoryName, categoryName, SANKALP_TYPE_NIYAM));
        }
        for (String categoryName: SpCategory.sankalpCategoryNames) {
            defaultCategories.put(categoryName, new SpCategory(id++, categoryName, categoryName, SANKALP_TYPE_BOTH));
        }
    }

    public SpCategory(int id, String name, String displayName, int type) {
        _id = id;
        _categoryName = name;
        _categoryDisplayName = displayName;
        _sankalpType = type;
    }

    public String toString()
    {
        return _categoryName;
    }

    public static Hashtable<String, SpCategory> getDefaultCategories()
    {
        return defaultCategories;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }


    public String getCategoryName() {
        return _categoryName;
    }

    public void setCategoryName(String categoryName) {
        this._categoryName = categoryName;
    }

    public String getCategoryDisplayName() {
        return _categoryDisplayName;
    }

    public void setCategoryDisplayName(String categoryDisplayName) {
        this._categoryDisplayName = categoryDisplayName;
    }

    public int getSankalpType() {
        return _sankalpType;
    }

    public void setSankalpType(int sankalpType) {
        this._sankalpType = sankalpType;
    }



}
