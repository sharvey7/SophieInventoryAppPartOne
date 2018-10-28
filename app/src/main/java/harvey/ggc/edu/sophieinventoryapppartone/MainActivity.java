package harvey.ggc.edu.sophieinventoryapppartone;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import harvey.ggc.edu.sophieinventoryapppartone.data.InventoryContract;
import harvey.ggc.edu.sophieinventoryapppartone.data.InventoryContract.InventoryEntry;
import harvey.ggc.edu.sophieinventoryapppartone.data.InventoryDbHelper;

public class MainActivity extends AppCompatActivity {

    private InventoryDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mDbHelper = new InventoryDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    public void displayDatabaseInfo() {

        private void displayDatabaseInfo() {
            // Create and/or open a database to read from it
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
            String [] projection = {
                    InventoryEntry._ID,
                    InventoryEntry.COLUMN_ITEM_NAME,
                    InventoryEntry.COLUMN_ITEM_PRICE,
                    InventoryEntry.COLUMN_ITEM_QUANTITY,
                    InventoryEntry.COLUMN_ITEM_SUPPLIER_NAME,
                    InventoryEntry.COLUMN_ITEM_SUPPLIER_PHONE
            };
            // Perform a query on the items table
            Cursor cursor = db.query(
                    InventoryEntry.TABLE_NAME,   // the table to query
                    projection,             // the columns to return
                    null,          // the columns for the WHERE clause
                    null,       // the values for the WHERE clause
                    null,           // don't group the rows
                    null,            // don't filter by row groups
                    null);          // the sort order
            TextView displayView = (TextView) findViewById(R.id.text_view_item);
            try {
                // Create a header in the Text View that looks like this:
                //
                // The items table contains <number of rows in Cursor> items.
                // _id - name - price - quantity - supplier name - supplier phone
                //
                // In the while loop below, iterate through the rows of the cursor and display
                // the information from each column in this order.
                displayView.setText("The items table contains " + cursor.getCount() + " items.\n\n");
                displayView.append(ItemEntry._ID + " - " +
                        InventoryEntry.COLUMN_ITEM_NAME + " - " +
                        InventoryEntry.COLUMN_ITEM_PRICE + " - " +
                        InventoryEntry.COLUMN_ITEM_QUANTITY + " - " +
                        InventoryEntry.COLUMN_ITEM_SUPPLIER_NAME + " - " +
                        InventoryEntry.COLUMN_ITEM_SUPPLIER_PHONE + "\n");
                // Figure out the index of each column
                int idColumnIndex = cursor.getColumnIndex(InventoryEntry._ID);
                int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_ITEM_NAME);
                int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_ITEM_PRICE);
                int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_ITEM_QUANTITY);
                int supplierNameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_ITEM_SUPPLIER_NAME);
                int supplierPhoneColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_ITEM_SUPPLIER_PHONE);
                // Iterate through all the returned rows in the cursor
                while (cursor.moveToNext()) {
                    // Use that index to extract the String or Int value of the word
                    // at the current row the cursor is on.
                    int currentID = cursor.getInt(idColumnIndex);
                    String currentName = cursor.getString(nameColumnIndex);
                    int currentPrice = cursor.getInt(priceColumnIndex);
                    int currentQuantity = cursor.getInt(quantityColumnIndex);
                    String currentSupplierName = cursor.getString(supplierNameColumnIndex);
                    String currentSupplierPhone = cursor.getString(supplierPhoneColumnIndex);
                    // Display the values from each column of the current row in the cursor in the TextView
                    displayView.append(("\n" + currentID + " - " +
                            currentName + " - " +
                            currentPrice + " - " +
                            currentQuantity + " - " +
                            currentSupplierName + " - " +
                            currentSupplierPhone));
                }
            } finally {
                // Always close the cursor when you're done reading from it. This releases all its
                // resources and makes it invalid.
                cursor.close();
            }
        }
    }
}