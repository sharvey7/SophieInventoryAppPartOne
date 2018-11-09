package harvey.ggc.edu.sophieinventoryapppartone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import harvey.ggc.edu.sophieinventoryapppartone.data.InventoryContract;

public class InventoryCursorAdapter extends CursorAdapter {

    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        //the views in the list layout
        TextView productTextView = (TextView) view.findViewById(R.id.product);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        final TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        TextView summaryTextView = (TextView) view.findViewById(R.id.summary);
        Button saleButton = view.findViewById(R.id.sale_button);

        int productColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_INVENTORY_QUANTITY);


        String productName = cursor.getString(productColumnIndex);
        String price = cursor.getString(priceColumnIndex);
        int quantity = cursor.getInt(quantityColumnIndex);

        if (TextUtils.isEmpty(price)) {
            price = "Unknown price!";
        }
        productTextView.setText(String.valueOf(productName));
        summaryTextView.setText(String.valueOf(price));
        quantityTextView.setText(Integer.toString(quantity));

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.valueOf(quantityTextView.getText().toString()) {

                    if (quantity > 0) {
                        quantity = quantity - 1;
                    } else {
                        Toast.makeText(context, "Click to add product to inventory", Toast.LENGTH_SHORT).show();

                        ContentValues values = new ContentValues();
                        values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_QUANTITY, quantity);

                        context.getContentResolver().update(contentUri, values, null, null);
                    }
                }
            }
        });
    }
}










