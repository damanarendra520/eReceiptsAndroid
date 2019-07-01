package com.narendradama.ereceipts.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Receipts.db";
    public static final String RECEIPTS_TABLE_NAME = "receipt";
    public static final String RECEIPTS_COLUMN_ID = "id";
    public static final String RECEIPTS_COLUMN_RECEIPT_NUMBER = "receiptNumber";
    public static final String RECEIPTS_COLUMN_DATE = "date";
    public static final String RECEIPTS_COLUMN_AMOUNT = "amount";
    public static final String RECEIPTS_COLUMN_PLACE = "place";
    public static final String RECEIPTS_COLUMN_SAVINGS = "savings";
    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table receipt " +
                        "(id integer primary key, receiptNumber text, date text, amount text, place text,savings text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS receipt");
        onCreate(db);
    }

    public Boolean insertReceipt(String receipt, String date, String amount, String place, String savings){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("receiptNumber", receipt);
        contentValues.put("date", date);
        contentValues.put("amount", amount);
        contentValues.put("place", place);
        contentValues.put("savings", savings);

        long newRowId = db.insert(RECEIPTS_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getDataWithId(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from receipt where id="+id+"", null );
        return res;
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from receipt", null );
        return res;
    }
    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, RECEIPTS_TABLE_NAME);
        return numRows;
    }

    public boolean updateContact (Integer id, String receipt, String date, String amount, String place, String savings) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("receiptNumber", receipt);
        contentValues.put("date", date);
        contentValues.put("amount", amount);
        contentValues.put("place", place);
        contentValues.put("savings", savings);
        db.update("receipt", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteReceipt (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("receipt",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllReceipts() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from receipt", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(RECEIPTS_COLUMN_RECEIPT_NUMBER)));
            res.moveToNext();
        }
        return array_list;
    }
    public ArrayList<HashMap<String, String>> GetReceiptData(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList receiptList = new ArrayList();
        //ArrayList<HashMap<String, String>> receiptList = new ArrayList<>();
        String query = "SELECT "+RECEIPTS_COLUMN_ID+","+RECEIPTS_COLUMN_RECEIPT_NUMBER+", "+RECEIPTS_COLUMN_DATE+", "+RECEIPTS_COLUMN_AMOUNT+", "+RECEIPTS_COLUMN_PLACE+", "+RECEIPTS_COLUMN_SAVINGS+" FROM "+ RECEIPTS_TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            HashMap<String,String> user = new HashMap<>();
            user.put("ID",cursor.getString(cursor.getColumnIndex(RECEIPTS_COLUMN_ID)).toString());
            user.put("receiptNumber",cursor.getString(cursor.getColumnIndex(RECEIPTS_COLUMN_RECEIPT_NUMBER)).toString());
            user.put("date",cursor.getString(cursor.getColumnIndex(RECEIPTS_COLUMN_DATE)).toString());
            user.put("amount",cursor.getString(cursor.getColumnIndex(RECEIPTS_COLUMN_AMOUNT)).toString());
            user.put("place",cursor.getString(cursor.getColumnIndex(RECEIPTS_COLUMN_PLACE)).toString());
            user.put("savings",cursor.getString(cursor.getColumnIndex(RECEIPTS_COLUMN_SAVINGS)).toString());
            receiptList.add(user);
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return  receiptList;
    }
}
