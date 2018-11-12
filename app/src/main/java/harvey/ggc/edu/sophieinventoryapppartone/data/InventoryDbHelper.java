package harvey.ggc.edu.sophieinventoryapppartone.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import harvey.ggc.edu.sophieinventoryapppartone.data.InventoryContract.InventoryEntry;


public class InventoryDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "shelters.db";

    private static final int DATABASE_VERSION = 1;

    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_INVENTORY_TABLE = "CREATE TABLE " + InventoryEntry.TABLE_NAME +
                "("
                + InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryEntry.COLUMN_INVENTORY_PRICE + " INTEGER NOT NULL DEFAULT 0, "
                + InventoryEntry.COLUMN_INVENTORY_QUANTITY + " INTEGER NOT NULL, "
                + InventoryEntry.COLUMN_INVENTORY_PRODUCT_NAME + " TEXT NOT NULL, "
                + InventoryEntry.COLUMN_INVENTORY_SUPPLIER_NAME + " TEXT, "
                + InventoryEntry.COLUMN_INVENTORY_SUPPLIER_PHONE + " TEXT ); ";

        db.execSQL(SQL_CREATE_INVENTORY_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}