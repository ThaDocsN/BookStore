package com.thadocizn.bookstore.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.thadocizn.bookstore.data.BookContract.BookEntry.TABLE_NAME;

public class BookDbHelper extends SQLiteOpenHelper {

    /* Database name*/
    private static final String DATABASE_NAME = "inventory.db";

    /* Database version*/
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructor {@link BookDbHelper(Context)}
     *
     * @param context context
     */
    public BookDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created the first time
     *
     * @param sqLiteDatabase database
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // The SQL statement to create the books table
        String SQL_CREATE_BOOKS_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + BookContract.BookEntry.COLUMN_BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BookContract.BookEntry.COLUMN_BOOK_NAME + " TEXT NOT NULL, "
                + BookContract.BookEntry.COLUMN_BOOK_PRICE + " REAL NOT NULL DEFAULT 0, "
                + BookContract.BookEntry.COLUMN_BOOK_QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
                + BookContract.BookEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL, "
                + BookContract.BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER + " INTEGER);";

        // Execute SQL statement
        sqLiteDatabase.execSQL(SQL_CREATE_BOOKS_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     *
     * @param db         database
     * @param oldVersion oldVersion
     * @param newVersion newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BookContract.BookEntry.TABLE_NAME);
    }
}

