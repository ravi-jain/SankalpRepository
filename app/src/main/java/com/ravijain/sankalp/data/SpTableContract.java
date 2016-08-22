package com.ravijain.sankalp.data;

import android.provider.BaseColumns;

/**
 * Created by ravijain on 7/1/2016.
 */
public class SpTableContract {

    public static final class SpUserTable implements BaseColumns {
        // Table name
        public static final String TABLE_NAME = "userTable";

        public static final String COLUMN_USER_NAME = "name";
        public static final String COLUMN_USER_EMAIL = "email";
        public static final String COLUMN_USER_MOBILE = "mobile";
    }

    public static final class SpCategoryTable implements BaseColumns {
        // Table name
        public static final String TABLE_NAME = "spCategoryTable";

        public static final String COLUMN_CATEGORY_NAME = "name";
        public static final String COLUMN_CATEGORY_DISPLAYNAME = "displayName";
        public static final String COLUMN_CATEGORY_TYPE = "type";
    }

    public static final class SpItemTable  implements BaseColumns{
        // Table name
        public static final String TABLE_NAME = "spItemTable";

        public static final String COLUMN_ITEM_NAME = "name";
        public static final String COLUMN_ITEM_DISPLAYNAME = "displayName";
        public static final String COLUMN_ITEM_CATEGORY_ID = "categoryID";
    }

    /*public static final class SpTyagTable implements SpSankalpTable {
        // Table name
        public static final String TABLE_NAME = "spTyagTable";

    }

    public static final class SpNiyamTable implements SpSankalpTable {
        // Table name
        public static final String TABLE_NAME = "spNiyamTable";

    }*/

    public static interface SpSankalpTable extends BaseColumns{
        // Table name

        public static final String TABLE_NAME = "spSankalpTable";
        public static final String COLUMN_CREATION_DATE = "creationDate";
        public static final String COLUMN_SANKALP_TYPE = "sankalpType";
        public static final String COLUMN_CATEGORY_ID = "categoryID";
        public static final String COLUMN_ITEM_ID = "itemId";
        public static final String COLUMN_FROM_DATE = "fromDate";
        public static final String COLUMN_TO_DATE = "toDate";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_ISLIFETIME = "isLifetime";
        public static final String COLUMN_EXCEPTION_TARGET_ID = "exTargetId";
        public static final String COLUMN_EXCEPTION_TARGET_COUNT = "exTargetCount";
        /*public static final String COLUMN_EXCEPTION_TARGET_CURRENT_COUNT = "exTargetCurrentCount";*/
    }

    public static interface SpExTarTable extends BaseColumns{
        public static final String TABLE_NAME = "exTarTable";
        public static final String COLUMN_SANKALP_ID = "sankalpId";
        public static final String COLUMN_CURRENT_COUNT = "count";
        public static final String COLUMN_UPDATED_ON = "updatedOn";
    }


}
