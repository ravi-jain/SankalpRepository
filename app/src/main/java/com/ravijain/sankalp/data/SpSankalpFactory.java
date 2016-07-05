package com.ravijain.sankalp.data;

/**
 * Created by ravijain on 7/4/2016.
 */
public class SpSankalpFactory {


    public static SpSankalp getNewSankalp(int sankalpType, int categoryId, int itemId)
    {
        if (sankalpType == SpDataConstants.SANKALP_TYPE_TYAG) {
            return new SpTyag(categoryId, itemId);
        }
        else if (sankalpType == SpDataConstants.SANKALP_TYPE_NIYAM) {
            return new SpNiyam(categoryId, itemId);
        }
        return null;
    }

}
