package harvey.ggc.edu.sophieinventoryapppartone;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import harvey.ggc.edu.sophieinventoryapppartone.data.InventoryContract;
import harvey.ggc.edu.sophieinventoryapppartone.data.InventoryContract.InventoryEntry;
import harvey.ggc.edu.sophieinventoryapppartone.data.InventoryDbHelper;

public class MainActivity extends AppCompatActivity {

   // private InventoryDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        //mDbHelper = new InventoryDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    public void displayDatabaseInfo() {

        //SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_INVENTORY_PRODUCT_NAME,
                InventoryEntry.COLUMN_INVENTORY_PRICE,
                InventoryEntry.COLUMN_INVENTORY_QUANTITY,
                InventoryEntry.COLUMN_INVENTORY_SUPPLIER_NAME,
                InventoryEntry.COLUMN_INVENTORY_SUPPLIER_PHONE
        };

        Cursor cursor = getContentResolver().query(
                InventoryEntry.CONTENT_URI,
                projection,
               null,
                null,
                null);

        TextView displayView = (TextView) findViewById(R.id.text_inventory);
        try {
            displayView.setText("The inventory table contains " + cursor.getCount() + " inventory.\n\n");
            displayView.append(InventoryEntry._ID + " - " +
                    InventoryEntry.COLUMN_INVENTORY_SUPPLIER_PHONE + " - " +
                    InventoryEntry.COLUMN_INVENTORY_SUPPLIER_NAME + " - " +
                    InventoryEntry.COLUMN_INVENTORY_PRODUCT_NAME + " - " +
                    InventoryEntry.COLUMN_INVENTORY_QUANTITY + " - " +
                    InventoryEntry.COLUMN_INVENTORY_PRICE + "\n");

//change here
            int idColumnIndex = cursor.getColumnIndex(InventoryEntry._ID);
            int productColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_QUANTITY);
            int supplierColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_SUPPLIER_NAME);
            int phoneColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_SUPPLIER_PHONE);

            while (cursor.moveToNext()) {
                int currentID = cursor.getInt(idColumnIndex);
                String currentProduct = cursor.getString(productColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplier = cursor.getString(supplierColumnIndex);
                String currentPhone = cursor.getString(phoneColumnIndex);

                displayView.append(("\n" + currentID + " - " + currentProduct + " - " +
                        currentPrice + " - " + currentQuantity + " - " + currentSupplier +
                        " - " + currentPhone));
            }
        } finally {
            cursor.close();
        }
    }

    private void insertInventory() {
        //SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(InventoryEntry.COLUMN_INVENTORY_PRODUCT_NAME, "iPAD");
        values.put(InventoryEntry.COLUMN_INVENTORY_PRICE, 1000);
        values.put(InventoryEntry.COLUMN_INVENTORY_QUANTITY, 1);
        values.put(InventoryEntry.COLUMN_INVENTORY_SUPPLIER_NAME, "Apple");
        values.put(InventoryEntry.COLUMN_INVENTORY_SUPPLIER_PHONE, "6784091111");

        Uri newUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, values);
       // long newRowId = db.insert(InventoryEntry.TABLE_NAME, null, values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_insert_d_data:
                insertInventory();
                displayDatabaseInfo();
            case R.id.action_delete_entries:

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}