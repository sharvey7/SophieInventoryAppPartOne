package harvey.ggc.edu.sophieinventoryapppartone;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import harvey.ggc.edu.sophieinventoryapppartone.data.InventoryContract;
import harvey.ggc.edu.sophieinventoryapppartone.data.InventoryContract.InventoryEntry;
import harvey.ggc.edu.sophieinventoryapppartone.data.InventoryDbHelper;

public class EditorActivity extends AppCompatActivity {

    private EditText mProductNameEditText;
    private EditText mPriceEditText;
    private EditText mSupplierNameEditText;
    private EditText mPhoneEditText;
    private Spinner mQuantitySpinner;

    private int mQuantity = InventoryEntry.QUANTITY_SMALL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mProductNameEditText = (EditText) findViewById(R.id.edit_product_name);
        mPriceEditText = (EditText) findViewById(R.id.edit_product_price);
        mPhoneEditText = (EditText) findViewById(R.id.edit_phone);
        mSupplierNameEditText = (EditText) findViewById(R.id.edit_supplier);
        mQuantitySpinner = (Spinner) findViewById(R.id.spinner_quantity);

        setupSpinner();
    }

    private void setupSpinner() {
        ArrayAdapter quantitySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_quantity_options, android.R.layout.simple_spinner_dropdown_item);

        quantitySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mQuantitySpinner.setAdapter(quantitySpinnerAdapter);

        mQuantitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.quantity_large))) {
                        mQuantity = InventoryEntry.QUANTITY_LARGE;
                    } else if (selection.equals(getString(R.string.quantity_medium))) {
                        mQuantity = InventoryEntry.QUANTITY_MEDIUM;
                    } else {
                        mQuantity = InventoryEntry.QUANTITY_SMALL;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mQuantity = InventoryEntry.QUANTITY_SMALL;
            }
        });

    }

    private void insertInventory() {
        String nameString = mProductNameEditText.getText().toString().trim();
        String phoneString = mPhoneEditText.getText().toString().trim();
        String supplierString = mSupplierNameEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();

        int price = Integer.parseInt(priceString);

        InventoryDbHelper mDbHelper = new InventoryDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRODUCT_NAME, nameString);
        values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_SUPPLIER_PHONE, phoneString);
        values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_SUPPLIER_NAME, supplierString);
        values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRICE, price);
        values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_QUANTITY, mQuantity);

        long newRowId = db.insert(InventoryContract.InventoryEntry.TABLE_NAME, null, values);

        if (newRowId == -1) {
            Toast.makeText(this, "Error with saving the inventory!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Inventory saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_saved:
                insertInventory();
                finish();
                return true;

            case R.id.action_deleted:
                return true;

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}