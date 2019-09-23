package com.example.praktikum;

import android.provider.BaseColumns;

public class DBHelper {

    private DBHelper() {}

    public static final class GroceryEntry implements BaseColumns {
        public static final String TABLE_NAME1 = "Einkaufsliste";
        public static final String TABLE_NAME2 = "Vorrat";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_BARCODE = "Barcode";
        public static final String COLUMN_NAME = "Name";
        public static final String COLUMN_AMOUNT = "Menge";
        public static final String COLUMN_MHD = "MHD";
        public static final String COLUMN_RESTOCK = "Restock";

    }
}