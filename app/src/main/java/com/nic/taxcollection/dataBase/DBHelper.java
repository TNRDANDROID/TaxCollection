package com.nic.taxcollection.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "New_Nursery_Garden";
    private static final int DATABASE_VERSION = 2;


    public static final String VILLAGE_TABLE_NAME = " villageTable";
    public static final String HABITATION_TABLE_NAME = " habitaionTable";

    public static final String MASTER_FIN_YEAR_TABLE = "fin_year";
    public static final String TAX_TABLE = "tax";


    private Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

    }

    //creating tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + VILLAGE_TABLE_NAME + " ("
                + "dcode INTEGER," +
                "bcode INTEGER," +
                "pvcode INTEGER," +
                "pvname TEXT)");

        db.execSQL("CREATE TABLE " + HABITATION_TABLE_NAME + " ("
                + "dcode TEXT," +
                "bcode TEXT," +
                "pvcode TEXT," +
                "habitation_code TEXT," +
                "habitation_name TEXT)");


        ///new
        db.execSQL("CREATE TABLE " + MASTER_FIN_YEAR_TABLE + " ("
                +"id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "fin_year TEXT)");
        db.execSQL("CREATE TABLE " + TAX_TABLE + " ("
                +"tax_id TEXT,"+
                "tax_name TEXT)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion >= newVersion) {
            //drop table if already exists
            db.execSQL("DROP TABLE IF EXISTS " + VILLAGE_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + HABITATION_TABLE_NAME);

            //New Tables
            db.execSQL("DROP TABLE IF EXISTS " + MASTER_FIN_YEAR_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + TAX_TABLE);

            onCreate(db);
        }
    }


}
