package com.thadocizn.bookstore;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.thadocizn.bookstore.data.BookContract;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_INVENTORY_LOADER = 0;
    @BindView(R.id.book_name_text)
    TextView bookNameText;
    @BindView(R.id.book_price_text)
    TextView bookPriceText;
    @BindView(R.id.book_quantity_text)
    TextView bookQuantityText;
    @BindView(R.id.increase_button)
    Button increaseButton;
    @BindView(R.id.decrease_button)
    Button decreaseButton;
    @BindView(R.id.book_supplier_name_text)
    TextView bookSupplierNameText;
    @BindView(R.id.book_supplier_phone_number_text)
    TextView bookSupplierPhoneNumberText;
    @BindView(R.id.phone_button)
    Button phoneButton;
    @BindView(R.id.delete_button)
    Button deleteButton;
    private Uri currentBookUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        currentBookUri = intent.getData();

        if (currentBookUri == null) {
            invalidateOptionsMenu();
        } else {
            getLoaderManager().initLoader(EXISTING_INVENTORY_LOADER, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                BookContract.BookEntry.COLUMN_BOOK_ID,
                BookContract.BookEntry.COLUMN_BOOK_NAME,
                BookContract.BookEntry.COLUMN_BOOK_PRICE,
                BookContract.BookEntry.COLUMN_BOOK_QUANTITY,
                BookContract.BookEntry.COLUMN_SUPPLIER_NAME,
                BookContract.BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER
        };
        return new CursorLoader(this,
                currentBookUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data == null || data.getCount() < 1) {
            return;
        }
        if (data.moveToFirst()) {

            final int idColumnIndex = data.getColumnIndex(BookContract.BookEntry._ID);
            int nameColumnIndex = data.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_NAME);
            int priceColumnIndex = data.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_PRICE);
            int quantityColumnIndex = data.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_QUANTITY);
            int supplierNameColumnIndex = data.getColumnIndex(BookContract.BookEntry.COLUMN_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = data.getColumnIndex(BookContract.BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER);

            String currentName = data.getString(nameColumnIndex);
            final int currentPrice = data.getInt(priceColumnIndex);
            final int currentQuantity = data.getInt(quantityColumnIndex);
            String currentSupplierName = data.getString(supplierNameColumnIndex);
            final int currentSupplierPhone = data.getInt(supplierPhoneColumnIndex);

            bookNameText.setText(currentName);
            bookPriceText.setText(Integer.toString(currentPrice));
            bookQuantityText.setText(Integer.toString(currentQuantity));
            bookSupplierNameText.setText(currentSupplierName);
            bookSupplierPhoneNumberText.setText(Integer.toString(currentSupplierPhone));

            decreaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    decreaseCount(idColumnIndex, currentQuantity);
                }
            });

            increaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    increaseCount(idColumnIndex, currentQuantity);
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteConfirmationDialog();
                }
            });

            phoneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phone = String.valueOf(currentSupplierPhone);
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        bookNameText.setText("");
        bookPriceText.setText("");
        bookQuantityText.setText("");
        bookSupplierNameText.setText("");
        bookSupplierPhoneNumberText.setText("");
    }

    public void decreaseCount(int id, int quantity) {
        quantity = quantity - 1;
        if (quantity >= 0) {
            updateBook(quantity);
            Toast.makeText(this, getString(R.string.quantity_change_msg), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.quantity_finish_msg), Toast.LENGTH_SHORT).show();
        }
    }

    public void increaseCount(int bookId, int bookQuantity) {
        bookQuantity = bookQuantity + 1;
        if (bookQuantity >= 0) {
            updateBook(bookQuantity);
            Toast.makeText(this, getString(R.string.quantity_change_msg), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateBook(int bookQuantity) {

        if (currentBookUri == null) {
            return;
        }
        ContentValues values = new ContentValues();
        values.put(BookContract.BookEntry.COLUMN_BOOK_QUANTITY, bookQuantity);

        if (currentBookUri == null) {
            Uri newUri = getContentResolver().insert(BookContract.BookEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, getString(R.string.insert_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.insert_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(currentBookUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.update_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.insert_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void deleteProduct() {
        if (currentBookUri != null) {
            int rowsDeleted = getContentResolver().delete(currentBookUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.delete_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.delete_product_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteProduct();
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
}
