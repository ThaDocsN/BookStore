package com.thadocizn.bookstore.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class BookContract {

    public static final String CONTENT_AUTHORITY = "com.thadocizn.bookstore";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_INVENTORY = "books";

    // To keep people from instantiating the contract class
    private BookContract() {
    }

    /**
     * Inner class that provides the values
     */
    public static final class BookEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        /* Name of database table*/
        public static final String TABLE_NAME = "books";

        /**
         * Table columns
         */
        public static final String COLUMN_BOOK_ID = BaseColumns._ID; // Used by the database TYPE INTEGER.
        public static final String COLUMN_BOOK_NAME = "book_name"; // BOOK name TYPE STRING.
        public static final String COLUMN_BOOK_PRICE = "price"; // Price of book TYPE REAL.
        public static final String COLUMN_BOOK_QUANTITY = "quantity"; // Number of books TYPE INTEGER.
        public static final String COLUMN_SUPPLIER_NAME = "supplier_name"; // Supplier name TYPE STRING
        public static final String COLUMN_SUPPLIER_PHONE_NUMBER = "phone_number"; //Suppliers phone number TYPE String


    }
}