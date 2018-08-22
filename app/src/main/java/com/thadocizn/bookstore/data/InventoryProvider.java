package com.thadocizn.bookstore.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class InventoryProvider extends ContentProvider {

    private static final int Books = 100;
    private static final int Book_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // TODO: Add 2 content URIs to URI matcher

        // The content uri will map to the integer code{@link #PETS} for multiple rows
        sUriMatcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.PATH_INVENTORY, Books);

        // The content uri will map to the integer code {@link #BOOK_ID} for a single row.
        sUriMatcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.PATH_INVENTORY + "/#", Book_ID);
    }

    private BookDbHelper bookDbHelper;

    @Override
    public boolean onCreate() {
        bookDbHelper = new BookDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = bookDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);

        switch (match) {
            case Books:
                cursor = database.query(BookContract.BookEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case Book_ID:
                selection = BookContract.BookEntry.COLUMN_BOOK_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(BookContract.BookEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case Books:
                return BookContract.BookEntry.CONTENT_LIST_TYPE;
            case Book_ID:
                return BookContract.BookEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI" + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case Books:
                return insertBook(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertBook(Uri uri, ContentValues values) {
        String nameBook = values.getAsString(BookContract.BookEntry.COLUMN_BOOK_NAME);
        if (nameBook == null) {
            throw new IllegalArgumentException("Book name required");
        }
        Integer priceBook = values.getAsInteger(BookContract.BookEntry.COLUMN_BOOK_PRICE);
        if (priceBook != null && priceBook < 0) {
            throw new IllegalArgumentException("Book price requires a number greater than 0");
        }

        Integer quantityBook = values.getAsInteger(BookContract.BookEntry.COLUMN_BOOK_QUANTITY);
        if (quantityBook != null && quantityBook < 0) {
            throw new IllegalArgumentException("Requires a valid number.");
        }

        String supplierName = values.getAsString(BookContract.BookEntry.COLUMN_SUPPLIER_NAME);
        if (supplierName == null) {
            throw new IllegalArgumentException("Supplier needs a name.");
        }

        Integer supplierPhone = values.getAsInteger(BookContract.BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER);
        if (supplierPhone != null && supplierPhone < 0) {
            throw new IllegalArgumentException("Supplier phone number is invalid");
        }

        SQLiteDatabase database = bookDbHelper.getWritableDatabase();
        long id = database.insert(BookContract.BookEntry.TABLE_NAME, null, values);
        if (id == -1) {
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase database = bookDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case Books:
                rowsDeleted = database.delete(BookContract.BookEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case Book_ID:
                selection = BookContract.BookEntry.COLUMN_BOOK_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(BookContract.BookEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case Books:
                return updateBook(uri, values, selection, selectionArgs);
            case Book_ID:
                selection = BookContract.BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateBook(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateBook(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(BookContract.BookEntry.COLUMN_BOOK_NAME)) {
            String nameBook = values.getAsString(BookContract.BookEntry.COLUMN_BOOK_NAME);
            if (nameBook == null) {
                throw new IllegalArgumentException("Book name required");
            }
        }
        if (values.containsKey(BookContract.BookEntry.COLUMN_BOOK_PRICE)) {
            Integer priceBook = values.getAsInteger(BookContract.BookEntry.COLUMN_BOOK_PRICE);
            if (priceBook != null && priceBook < 0) {
                throw new
                        IllegalArgumentException("Valid price is required .");
            }
        }

        if (values.containsKey(BookContract.BookEntry.COLUMN_BOOK_QUANTITY)) {
            Integer quantityBook = values.getAsInteger(BookContract.BookEntry.COLUMN_BOOK_QUANTITY);
            if (quantityBook != null && quantityBook < 0) {
                throw new IllegalArgumentException("Valid quantity requires valid");
            }
        }
        if (values.containsKey(BookContract.BookEntry.COLUMN_SUPPLIER_NAME)) {
            String supplierName = values.getAsString(BookContract.BookEntry.COLUMN_SUPPLIER_NAME);
            if (supplierName == null) {
                throw new IllegalArgumentException("Supplier Name requires valid");
            }
        }

        if (values.containsKey(BookContract.BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER)) {
            Integer supplierPhone = values.getAsInteger(BookContract.BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER);
            if (supplierPhone != null && supplierPhone < 0) {
                throw new
                        IllegalArgumentException("Supplier Phone requires valid number");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = bookDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(BookContract.BookEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }
}
