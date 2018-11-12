package harvey.ggc.edu.sophieinventoryapppartone;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import harvey.ggc.edu.sophieinventoryapppartone.data.InventoryContract;
import harvey.ggc.edu.sophieinventoryapppartone.data.InventoryContract.InventoryEntry;
import harvey.ggc.edu.sophieinventoryapppartone.data.InventoryDbHelper;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_INVENTORY_LOADER = 0;
    private Uri mCurrentInventoryUri;

    private EditText mProductNameEditText;
    private EditText mPriceEditText;
    private EditText mSupplierNameEditText;
    private EditText mPhoneEditText;
    private EditText mQuantityEditText;
    private Button mSalesButton;


    String sNumber;
    EditText numText;
    private boolean mInventoryHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mInventoryHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mCurrentInventoryUri = intent.getData();


        if (mCurrentInventoryUri == null) {
            setTitle("Add a product! ");
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.editor_title_edit));

            getLoaderManager().initLoader(EXISTING_INVENTORY_LOADER, null, this);
        }
        mProductNameEditText = findViewById(R.id.edit_product_name);
        mPriceEditText = findViewById(R.id.edit_product_price);
        mPhoneEditText = findViewById(R.id.edit_phone);
        mSupplierNameEditText = findViewById(R.id.edit_supplier);
        mQuantityEditText = findViewById(R.id.edit_quantity);
        mSalesButton = findViewById(R.id.saleButton);

        mProductNameEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mPhoneEditText.setOnTouchListener(mTouchListener);
        mSupplierNameEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);

        mSalesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sNumber = mPhoneEditText.getText().toString();
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel: " + sNumber));
                startActivity(callIntent);
            }
        });
        Button posButton = findViewById(R.id.quantity_pos_button);

        posButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mQuantityEditText.getText().toString().isEmpty()) {
                    mQuantityEditText.setText("0");
                }

                int quantity = Integer.valueOf(mQuantityEditText.getText().toString());

                if (quantity >= 0) {
                    quantity = quantity + 1;
                }
                mQuantityEditText.setText(Integer.toString(quantity));
            }
        });

        Button negButton = findViewById(R.id.quantity_neg_button);

        negButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mQuantityEditText.getText().toString().isEmpty()) {
                    mQuantityEditText.setText("0");
                }
                int quantity = Integer.valueOf(mQuantityEditText.getText().toString());

                if (quantity >= 1) {
                    quantity = quantity - 1;
                }
                mQuantityEditText.setText(Integer.toString(quantity));
            }
        });
    }


    private void saveInventory() {
        String productString = mProductNameEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String supplierString = mSupplierNameEditText.getText().toString().trim();
        String phoneString = mPhoneEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();

        if (TextUtils.isEmpty(productString)
                || TextUtils.isEmpty(priceString)
                || TextUtils.isEmpty(supplierString)
                || TextUtils.isEmpty(phoneString)
                || TextUtils.isEmpty(phoneString)) {
            Toast.makeText(this, getString(R.string.editor_insert_inventory_success),
                    Toast.LENGTH_LONG).show();
            return;
        }
        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRODUCT_NAME, productString);
        values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_SUPPLIER_PHONE, phoneString);
        values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_SUPPLIER_NAME, supplierString);
        values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRICE, priceString);
        values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_QUANTITY, quantityString);

        int price = 0;
        if (!TextUtils.isEmpty(priceString)) {
            price = Integer.parseInt(priceString);
        }
        values.put(InventoryEntry.COLUMN_INVENTORY_PRICE, price);

        if (mCurrentInventoryUri == null) {
            Uri newUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, getString(R.string.editor_insert_inventory_fail),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_insert_inventory_success),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(mCurrentInventoryUri, values, null, null);

            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.editor_update_pet_fail),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_update_pet_success),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentInventoryUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_deleted);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_saved:
                saveInventory();
                finish(); //make sure to close this activity only when product is successfully saved so users
                //won't to retype all previous data just because some missing fields.
                return true;

            case R.id.action_deleted:
                showDeleteConfirmationDialog();
                return true;

            case android.R.id.home:
                if (!mInventoryHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    }
                };
                showUnsavedChangeDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!mInventoryHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        showUnsavedChangeDialog(discardButtonClickListener);
    }

    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_INVENTORY_PRODUCT_NAME,
                InventoryEntry.COLUMN_INVENTORY_PRICE,
                InventoryEntry.COLUMN_INVENTORY_SUPPLIER_NAME,
                InventoryEntry.COLUMN_INVENTORY_SUPPLIER_PHONE,
                InventoryEntry.COLUMN_INVENTORY_QUANTITY};

        return new CursorLoader(this,
                mCurrentInventoryUri,
                projection,
                null,
                null,
                null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {
            int productColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_PRICE);
            int supplierColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_SUPPLIER_NAME);
            int phoneColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_SUPPLIER_PHONE);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_QUANTITY);

            String product = cursor.getString(productColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            String supplier = cursor.getString(supplierColumnIndex);
            String phone = cursor.getString(phoneColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);

            mProductNameEditText.setText(product);
            mQuantityEditText.setText(Integer.toString(quantity));
            mPriceEditText.setText(String.valueOf(price));
            mSupplierNameEditText.setText(supplier);
            mPhoneEditText.setText(phone);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mProductNameEditText.setText("");
        mPriceEditText.setText("");
        mPhoneEditText.setText("");
        mSupplierNameEditText.setText("");
        mQuantityEditText.setText("");
    }

    private void showUnsavedChangeDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_mag);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                deleteInventory();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteInventory() {
        if (mCurrentInventoryUri != null) {

            int rowsDeleted = getContentResolver().delete(mCurrentInventoryUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_inventory_fail),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_pet_success),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
}