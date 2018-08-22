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
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.thadocizn.bookstore.data.BookContract;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_BOOK_LOADER = 0;
    @BindView(R.id.book_name_edit_text)
    EditText bookNameEditText;
    @BindView(R.id.book_price_edit_text)
    EditText bookPriceEditText;
    @BindView(R.id.book_quantity_edit_text)
    EditText bookQuantityEditText;
    @BindView(R.id.book_supplier_phone_number_edit_text)
    EditText bookSupplierPhoneNumberEditText;
    @BindView(R.id.supplier_name_edit_text)
    EditText supplierNameEditText;
    private Uri currentBookUri;
    private boolean bookChanged = false;

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            bookChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        currentBookUri = intent.getData();

        if (currentBookUri == null) {
            setTitle(getString(R.string.add_book));
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.edit_book));
            getLoaderManager().initLoader(EXISTING_BOOK_LOADER, null, this);
        }

        bookNameEditText.setOnTouchListener(touchListener);
        bookPriceEditText.setOnTouchListener(touchListener);
        bookQuantityEditText.setOnTouchListener(touchListener);
        supplierNameEditText.setOnTouchListener(touchListener);
        bookSupplierPhoneNumberEditText.setOnTouchListener(touchListener);

    }

    private void saveBook() {

        String bookName = bookNameEditText.getText().toString().trim();
        String bookPrice = bookPriceEditText.getText().toString().trim();
        String bookQuantity = bookQuantityEditText.getText().toString().trim();
        String bookSupplierName = supplierNameEditText.getText().toString().trim();
        String bookSupplierPhone = bookSupplierPhoneNumberEditText.getText().toString().trim();

        if (currentBookUri == null) {
            if (TextUtils.isEmpty(bookName)) {
                Toast.makeText(this, getString(R.string.book_name_required), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(bookPrice)) {
                Toast.makeText(this, getString(R.string.book_price_required), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(bookQuantity)) {
                Toast.makeText(this, getString(R.string.book_quantity_required), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(bookSupplierName)) {
                Toast.makeText(this, getString(R.string.supplier_name_required), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(bookSupplierPhone)) {
                Toast.makeText(this, getString(R.string.supplier_phone_required), Toast.LENGTH_SHORT).show();
                return;
            }

            ContentValues values = new ContentValues();

            values.put(BookContract.BookEntry.COLUMN_BOOK_NAME, bookName);
            values.put(BookContract.BookEntry.COLUMN_BOOK_PRICE, bookPrice);
            values.put(BookContract.BookEntry.COLUMN_BOOK_QUANTITY, bookQuantity);
            values.put(BookContract.BookEntry.COLUMN_SUPPLIER_NAME, bookSupplierName);
            values.put(BookContract.BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER, bookSupplierPhone);

            Uri newUri = getContentResolver().insert(BookContract.BookEntry.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, getString(R.string.insert_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.insert_successful),
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {

            if (TextUtils.isEmpty(bookName)) {
                Toast.makeText(this, getString(R.string.book_name_required), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(bookPrice)) {
                Toast.makeText(this, getString(R.string.book_price_required), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(bookQuantity)) {
                Toast.makeText(this, getString(R.string.book_quantity_required), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(bookSupplierName)) {
                Toast.makeText(this, getString(R.string.supplier_name_required), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(bookSupplierPhone)) {
                Toast.makeText(this, getString(R.string.supplier_phone_required), Toast.LENGTH_SHORT).show();
                return;
            }

            ContentValues values = new ContentValues();

            values.put(BookContract.BookEntry.COLUMN_BOOK_NAME, bookName);
            values.put(BookContract.BookEntry.COLUMN_BOOK_PRICE, bookPrice);
            values.put(BookContract.BookEntry.COLUMN_BOOK_QUANTITY, bookQuantity);
            values.put(BookContract.BookEntry.COLUMN_SUPPLIER_NAME, bookSupplierName);
            values.put(BookContract.BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER, bookSupplierPhone);


            int rowsAffected = getContentResolver().update(currentBookUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.update_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.update_successful),
                        Toast.LENGTH_SHORT).show();
                finish();
            }
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
            int bookNameIndex = data.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_NAME);
            int bookPriceIndex = data.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_PRICE);
            int bookQuantityIndex = data.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_QUANTITY);
            int supplierNameIndex = data.getColumnIndex(BookContract.BookEntry.COLUMN_SUPPLIER_NAME);
            int supplierPhoneIndex = data.getColumnIndex(BookContract.BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER);

            String currentBook = data.getString(bookNameIndex);
            int currentPrice = data.getInt(bookPriceIndex);
            int currentQuantity = data.getInt(bookQuantityIndex);
            String currentSupplier = data.getString(supplierNameIndex);
            int currentSupplierPhone = data.getInt(supplierPhoneIndex);

            bookNameEditText.setText(currentBook);
            bookPriceEditText.setText(Integer.toString(currentPrice));
            bookQuantityEditText.setText(Integer.toString(currentQuantity));
            supplierNameEditText.setText(currentSupplier);
            bookSupplierPhoneNumberEditText.setText(Integer.toString(currentSupplierPhone));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        bookNameEditText.setText("");
        bookPriceEditText.setText("");
        bookQuantityEditText.setText("");
        supplierNameEditText.setText("");
        bookSupplierPhoneNumberEditText.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveBook();
                return true;
            case android.R.id.home:
                if (!bookChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (!bookChanged) {
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

        showUnsavedChangesDialog(discardButtonClickListener);
    }
}
