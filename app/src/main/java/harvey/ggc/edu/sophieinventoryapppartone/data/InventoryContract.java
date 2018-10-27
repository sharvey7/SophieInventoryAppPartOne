package harvey.ggc.edu.sophieinventoryapppartone.data;

import android.provider.BaseColumns;

public final class InventoryContract {

    private InventoryContract() {
    }

    public static abstract class InventoryEntry implements BaseColumns {

        public final static String TABLE_NAME = "inventory";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_INVENTORY_PRODUCT_NAME = "name";
        public final static String COLUMN_INVENTORY_PRICE = "price";
        public final static String COLUMN_INVENTORY_QUANTITY = "quantity";
        public final static String COLUMN_INVENTORY_SUPPLIER_NAME = "supplier";
        public final static String COLUMN_INVENTORY_SUPPLIER_PHONE = "phone";

        public static final int QUANTITY_SMALL = 1;
        public static final int QUANTITY_MEDIUM = 5;
        public static final int QUANTITY_LARGE = 10;
    }
}
