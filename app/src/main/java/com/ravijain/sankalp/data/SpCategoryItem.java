package com.ravijain.sankalp.data;

import android.content.Context;

import com.ravijain.sankalp.support.SpUtils;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * Created by ravijain on 7/2/2016.
 */
public class SpCategoryItem implements SpDataConstants {

    //public static String[] foodItemNames = {ITEM_NAME_POTATO, ITEM_NAME_ONION, ITEM_NAME_HONEY};
    public static String[] entertainmentItemNames = {ITEM_GAMBLING, ITEM_HUNTING, ITEM_ADULTERY, ITEM_LOTTERY, ITEM_PAAN, ITEM_GUTKHA, ITEM_CIGARETTES, ITEM_DRUGS
            , ITEM_MOVIES, ITEM_DINING, ITEM_TV};
    public static String[] cosmeticItemNames = {ITEM_POWDER, ITEM_CREAM, ITEM_PERFUME, ITEM_SOAP, ITEM_FACEWASH, ITEM_TOOTHPASTE
            , ITEM_WOOLLEN, ITEM_SILK, ITEM_LIPSTICK, ITEM_NAIL_POLISH, ITEM_SHAMPOO, ITEM_FUR, ITEM_LEATHER};
    public static String[] travelItemNames = {ITEM_NAME_FOREIGN_TRAVEL, ITEM_NAME_CAR};
    public static String[] dharmaItemNames = {ITEM_NAME_MALA, ITEM_NAME_DARSHAN, ITEM_NAME_KALASH,
            ITEM_NAME_SWADHYAYA, ITEM_NAME_TEERTH, ITEM_NAME_PRATIKRAMAN, ITEM_NAME_UPVAAS};
    public static String[] anaajItemNames = {ITEM_WHEAT, ITEM_RICE, ITEM_JAU, ITEM_MATAR, ITEM_JVAR, ITEM_RAJMA, ITEM_CORN, ITEM_BAJRA
            , ITEM_TIL, ITEM_ARHAR_DAAL, ITEM_CHAWLA_DAAL, ITEM_MOTH_DAAL, ITEM_MASUR_DAAL, ITEM_URAD_DAAL, ITEM_MOONG_DAAL, ITEM_CHANA_DAAL,
            ITEM_SOYABEAN};
    public static String[] spicesItemNames = {ITEM_HEENG, ITEM_DAANA_METHI, ITEM_KESAR, ITEM_KHAR, ITEM_JEERA, ITEM_DHANIA, ITEM_LAL_MIRCH,
            ITEM_AMCHOOR, ITEM_SARSO, ITEM_HALDI, ITEM_AJWAYAN, ITEM_LAUNG, ITEM_SAUNF, ITEM_ILAICHI, ITEM_KALI_MIRCH, ITEM_TEJPATTA,
            ITEM_DRY_IMLI, ITEM_KACHRI, ITEM_SAUNTH, ITEM_GOOND, ITEM_DALCHINI};
    public static String[] generalGreenItemNames = {ITEM_KETHA, ITEM_SUGARCANE, ITEM_DONGRA, ITEM_KACCHA_BADAM, ITEM_JAMOON, ITEM_KHIRNI, ITEM_AWLAAN,
            ITEM_KHURMANI, ITEM_SINGHADA, ITEM_BEL, ITEM_LEMON, ITEM_IMLI};
    public static String[] greenVegetablesItemNames = {ITEM_TURAI, ITEM_PARWAL, ITEM_GREEN_CHILLI, ITEM_LAUKI, ITEM_KADDU, ITEM_LASODE, ITEM_KARONDA,
            ITEM_TINDSI, ITEM_TINDORI, ITEM_TOMATO, ITEM_BHINDI, ITEM_SEAM, ITEM_KARELA};
    public static String[] greenFruitsItemNames = {ITEM_MANGO, ITEM_BANANA, ITEM_ORANGE, ITEM_MAUSAMBI, ITEM_APPLE, ITEM_POMEGRANATE, ITEM_CHEEKU, ITEM_AMRUD,
            ITEM_KHARBOOJA, ITEM_COCONUT, ITEM_WATERMELON, ITEM_GRAPES, ITEM_SITAPHAL, ITEM_NASPATI, ITEM_ANARAS, ITEM_PAPAYA, ITEM_PLUM, ITEM_CHERRY, ITEM_LITCHIE};
    public static String[] greenLeavesItemNames = {ITEM_PUDINA, ITEM_DHANIYA, ITEM_MOOLI_PATTA, ITEM_PALAK, ITEM_TULSI, ITEM_PAAN_PATTA, ITEM_METHI_PATTA};
    public static String[] greenAbhakshyaItemNames = {ITEM_ANJEER, ITEM_POTATO, ITEM_ONION, ITEM_GARLIC, ITEM_GINGER, ITEM_SHAKARGAND, ITEM_CARROT, ITEM_MOOLI,
            ITEM_ARBI, ITEM_BRINJAL, ITEM_CABBAGE, ITEM_HONEY};
    public static String[] abhakshyaItemNames = {ITEM_TEA, ITEM_COFFEE, ITEM_BARAK};


    private static ArrayList<SpCategoryItem> defaultItems = new ArrayList<SpCategoryItem>();

    static {
        int id = 0;
        ArrayList<SpCategory> defaultCategories = SpCategory.getDefaultCategories();

        for (SpCategory category : defaultCategories) {
            if (SUBCATEGORY_NAME_ANAAJ.equals(category.getSubCategoryName())) {
                for (String itemName : SpCategoryItem.anaajItemNames) {
                    defaultItems.add(new SpCategoryItem(id++, itemName, category.getId()));
                }
            } else if (category.getCategoryName().equals(CATEGORY_NAME_ENTERTAINMENT)) {
                for (String itemName : SpCategoryItem.entertainmentItemNames) {
                    defaultItems.add(new SpCategoryItem(id++, itemName, category.getId()));
                }
            } else if (category.getCategoryName().equals(CATEGORY_NAME_TRAVEL)) {
                for (String itemName : SpCategoryItem.travelItemNames) {
                    defaultItems.add(new SpCategoryItem(id++, itemName, category.getId()));
                }
            } else if (category.getCategoryName().equals(CATEGORY_NAME_DHARMA)) {
                for (String itemName : SpCategoryItem.dharmaItemNames) {
                    defaultItems.add(new SpCategoryItem(id++, itemName, category.getId()));
                }
            } else if (category.getCategoryName().equals(CATEGORY_NAME_COSMETIC)) {
                for (String itemName : SpCategoryItem.cosmeticItemNames) {
                    defaultItems.add(new SpCategoryItem(id++, itemName, category.getId()));
                }
            } else if (SUBCATEGORY_NAME_SPICES.equals(category.getSubCategoryName())) {
                for (String itemName : SpCategoryItem.spicesItemNames) {
                    defaultItems.add(new SpCategoryItem(id++, itemName, category.getId()));
                }
            } else if (SUBCATEGORY_NAME_GENERAL_GREEN.equals(category.getSubCategoryName())) {
                for (String itemName : SpCategoryItem.generalGreenItemNames) {
                    defaultItems.add(new SpCategoryItem(id++, itemName, category.getId()));
                }
            } else if (SUBCATEGORY_NAME_GREEN_FRUITS.equals(category.getSubCategoryName())) {
                for (String itemName : SpCategoryItem.greenFruitsItemNames) {
                    defaultItems.add(new SpCategoryItem(id++, itemName, category.getId()));
                }
            } else if (SUBCATEGORY_NAME_GREEN_VEGETABLES.equals(category.getSubCategoryName())) {
                for (String itemName : SpCategoryItem.greenVegetablesItemNames) {
                    defaultItems.add(new SpCategoryItem(id++, itemName, category.getId()));
                }
            } else if (SUBCATEGORY_NAME_GREEN_LEAVES.equals(category.getSubCategoryName())) {
                for (String itemName : SpCategoryItem.greenLeavesItemNames) {
                    defaultItems.add(new SpCategoryItem(id++, itemName, category.getId()));
                }
            } else if (SUBCATEGORY_NAME_GREEN_ABHAKSHYA.equals(category.getSubCategoryName())) {
                for (String itemName : SpCategoryItem.greenAbhakshyaItemNames) {
                    defaultItems.add(new SpCategoryItem(id++, itemName, category.getId()));
                }
            } else if (SUBCATEGORY_NAME_ABHAKSHYA.equals(category.getSubCategoryName())) {
                for (String itemName : SpCategoryItem.abhakshyaItemNames) {
                    defaultItems.add(new SpCategoryItem(id++, itemName, category.getId()));
                }
            }

        }
    }

    private int _id = -1;
    private String _itemName;
    private String _itemDisplayName;
    private int _categoryId;

    public SpCategoryItem(int id, String name, int categoryId) {
        _id = id;
        _itemName = name;
        _categoryId = categoryId;
    }

    public SpCategoryItem(String name, int categoryId) {
        _itemName = name;
        _itemDisplayName = name;
        _categoryId = categoryId;
    }

    public static ArrayList<SpCategoryItem> getDefaultCategoryItems() {
        return defaultItems;
    }

    public String toString() {
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

    public String getCategoryItemDisplayName(Context context) {
        return SpUtils.getLocalizedString(context, getCategoryItemName());
    }

    public int getCategoryId() {
        return _categoryId;
    }

    public void setCategoryId(int categoryId) {
        this._categoryId = categoryId;
    }


}
