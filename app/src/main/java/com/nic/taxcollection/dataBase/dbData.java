package com.nic.taxcollection.dataBase;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nic.taxcollection.model.Tax;

import java.util.ArrayList;


public class dbData {
    private SQLiteDatabase db;
    private SQLiteOpenHelper dbHelper;
    private Context context;

    public dbData(Context context) {
        this.dbHelper = new DBHelper(context);
        this.context = context;
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    public void insert_tax_type(Tax tax) {
        ContentValues values = new ContentValues();
        values.put("tax_id", tax.getTaxId());
        values.put("tax_name", tax.getTaxName());
        long id = db.insert(DBHelper.TAX_TABLE,null,values);
        Log.d("Insert_tax", String.valueOf(id));
    }
    public void insert_tax_detail(Tax tax) {
        ContentValues values = new ContentValues();
        values.put("id", tax.getId());
        values.put("fin", tax.getFin());
        values.put("name", tax.getName());
        values.put("amount", tax.getAmount());
        long id = db.insert(DBHelper.TAX_COLLECTION_LIST_TABLE,null,values);
        Log.d("Insert_tax_collec", String.valueOf(id));
    }

    public ArrayList<Tax> get_tax_type() {
        ArrayList<Tax> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.TAX_TABLE,null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Tax card = new Tax();
                    card.setTaxId(cursor.getString(cursor
                            .getColumnIndexOrThrow("tax_id")));
                    card.setTaxName(cursor.getString(cursor
                            .getColumnIndexOrThrow("tax_name")));
                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<Tax> get_tax_collection_list() {
        ArrayList<Tax> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.TAX_TABLE,null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Tax card = new Tax();
                    card.setId(cursor.getString(cursor
                            .getColumnIndexOrThrow("id")));
                    card.setName(cursor.getString(cursor
                            .getColumnIndexOrThrow("name")));
                    card.setFin(cursor.getString(cursor
                            .getColumnIndexOrThrow("fin")));
                    card.setAmount(cursor.getString(cursor
                            .getColumnIndexOrThrow("amount")));
                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }


    public void deleteTaxCollectionTable() { db.execSQL("delete from " + DBHelper.TAX_COLLECTION_LIST_TABLE);}
    public void deleteTaxTable() { db.execSQL("delete from " + DBHelper.TAX_TABLE);}
    public void deleteTaxVillageTable() { db.execSQL("delete from " + DBHelper.VILLAGE_TABLE_NAME);}
    public void deleteHabitationTable() { db.execSQL("delete from " + DBHelper.HABITATION_TABLE_NAME);}
    public void deleteFinYearTable() { db.execSQL("delete from " + DBHelper.MASTER_FIN_YEAR_TABLE);}

    public void deleteAll() {
        deleteTaxCollectionTable();
        deleteTaxTable();
        deleteTaxVillageTable();
        deleteHabitationTable();
        deleteFinYearTable();
    }

}