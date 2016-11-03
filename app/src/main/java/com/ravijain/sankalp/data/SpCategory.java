package com.ravijain.sankalp.data;

import android.content.Context;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.support.SpConstants;
import com.ravijain.sankalp.support.SpUtils;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by ravijain on 7/2/2016.
 */
public class SpCategory implements SpDataConstants {

    public static String[] tyagCategoryNames = {CATEGORY_NAME_ENTERTAINMENT, CATEGORY_NAME_TRAVEL, CATEGORY_NAME_COSMETIC};
    public static String[] foodSubCategoryNames = {SUBCATEGORY_NAME_GENERAL_GREEN, SUBCATEGORY_NAME_GREEN_VEGETABLES, SUBCATEGORY_NAME_GREEN_LEAVES,
            SUBCATEGORY_NAME_GREEN_FRUITS, SUBCATEGORY_NAME_GREEN_ABHAKSHYA, SUBCATEGORY_NAME_RAS, SUBCATEGORY_NAME_SPICES, SUBCATEGORY_NAME_ANAAJ,
            SUBCATEGORY_NAME_ABHAKSHYA};
    public static String[] niyamCategoryNames = {CATEGORY_NAME_DHARMA};
    public static String[] sankalpCategoryNames = {};
    private static ArrayList<SpCategory> defaultCategories = new ArrayList<SpCategory>();

    static {
        int id = 0;
        for (String categoryName : SpCategory.tyagCategoryNames) {
            defaultCategories.add(new SpCategory(id++, categoryName, null, SpConstants.SANKALP_TYPE_TYAG));
        }
        for (String subCategoryName : SpCategory.foodSubCategoryNames) {
            defaultCategories.add(new SpCategory(id++, CATEGORY_NAME_FOOD, subCategoryName, SpConstants.SANKALP_TYPE_TYAG));
        }
        for (String categoryName : SpCategory.niyamCategoryNames) {
            defaultCategories.add(new SpCategory(id++, categoryName, null, SpConstants.SANKALP_TYPE_NIYAM));
        }
        for (String categoryName : SpCategory.sankalpCategoryNames) {
            defaultCategories.add(new SpCategory(id++, categoryName, null, SpConstants.SANKALP_TYPE_BOTH));
        }
    }

    private int _id = -1;
    private String _categoryName;
    private String _subCategoryName;
    private int _sankalpType;

    public SpCategory(int id, String name, String subCategory, int type) {
        _id = id;
        _categoryName = name;
        _subCategoryName = subCategory;
        _sankalpType = type;
    }

    public SpCategory(String name, int type) {
        _categoryName = name;
        _sankalpType = type;
    }

    public static ArrayList<SpCategory> getDefaultCategories() {
        return defaultCategories;
    }

    public String toString() {
        return getCategoryDisplayName(null);
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

    public String getSubCategoryName() {
        return _subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this._subCategoryName = subCategoryName;
    }

    public String getCategoryDisplayName(Context context) {
        StringBuilder displayName = new StringBuilder();
        displayName.append(SpUtils.getLocalizedString(context, getCategoryName()));
        if (getSubCategoryName() != null && getSubCategoryName().length() > 0) {
            displayName.append(" :: ");
            displayName.append(SpUtils.getLocalizedString(context, getSubCategoryName()));

        }

        return displayName.toString();
    }

    public int getSankalpType() {
        return _sankalpType;
    }

    public void setSankalpType(int sankalpType) {
        this._sankalpType = sankalpType;
    }


}
