package com.thadocizn.bookstore;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.thadocizn.bookstore.data.BookContract;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InventoryCursorAdapter extends CursorAdapter {

    @BindView(R.id.book_text_view)
    TextView bookTextView;
    @BindView(R.id.book_quantity_text_view)
    TextView bookQuantityTextView;
    @BindView(R.id.book_price_text_view)
    TextView bookPriceTextView;
    @BindView(R.id.sale_button)
    Button saleButton;
    @BindView(R.id.edit_button)
    Button editButton;

    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);

        return view;
    }

    @Override
    public void bindView(final View view, final Context context, Cursor cursor) {
        ButterKnife.bind(this, view);

        final int columnIndexId = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_ID);
        int bookNameColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_NAME);
        int bookPriceIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_PRICE);
        int bookQuantityIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_QUANTITY);

        final String bookId = cursor.getString(columnIndexId);
        String bookName = cursor.getString(bookNameColumnIndex);
        String bookPrice = cursor.getString(bookPriceIndex);
        final String bookQuantity = cursor.getString(bookQuantityIndex);

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InventoryActivity inventoryActivity = (InventoryActivity) context;
                inventoryActivity.bookSaleCount(Integer.valueOf(bookId), Integer.valueOf(bookQuantity));
            }
        });

        bookTextView.setText(bookId + " ) " + bookName);
        bookPriceTextView.setText(context.getString(R.string.book_price) + " : " + "$" + bookPrice);
        bookQuantityTextView.setText(context.getString(R.string.book_quantity) + " : " + bookQuantity);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), EditorActivity.class);
                Uri currentBookUri = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, Long.parseLong(bookId));
                intent.setData(currentBookUri);
                context.startActivity(intent);
            }
        });
    }
}
