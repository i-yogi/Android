package com.myandroidapps.yogi.sqliteintro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yogi on 2/22/16.
 */
public class myDB extends SQLiteOpenHelper {
    private static final int db_ver = 1;
    private static final String db_name ="shoppingKart.db";

    private static final String tb_name ="shoppingList";
    private static final String clm_id ="_ID";
    private static final String clm_item ="itemName";

    private static Context mContext;

    public myDB(Context context) {
        super(context, db_name, null, db_ver);

        mContext = context;
    }

    //Since we extended this class to inherit SQLiteOpenHelper we have to overRide following methods

    //Creates DB table for an App (SQLiteOpenHelper actually checks if table/db already exists or not)
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+tb_name+"("+clm_id+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +clm_item+" TEXT NOT NULL);");
    }

    //If version number for DB is changed then create new DB-Table
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+tb_name);
        onCreate(db);
    }

    //onCLick event Handler for the Button (specified in xml)

    public void addItem(ShoppingKart shoppingKart){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(clm_item, shoppingKart.get_itemName());

        db.insert(tb_name, null, values);
        db.close();
    }


    //To delete the TextItem entered in Input field
    public void deleteItem(String itemname){
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("DELETE FROM "+tb_name+" WHERE "+clm_item+"=\""+itemname+"\";");
    }

    public Cursor getData(){
        SQLiteDatabase db = getWritableDatabase();

        Cursor myCursor = db.rawQuery("SELECT * FROM " + tb_name + ";", null);

        if(myCursor != null){
            myCursor.moveToFirst();
        }

        return myCursor;
    }
}
