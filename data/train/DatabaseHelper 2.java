package com.appradar.viper.amazingfacts;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by viper on 13/7/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    SQLiteDatabase database;
    Context context;

    private static String DATABASE_NAME = "LION";
    public static String FACTS_TABLE = "FACTS";
    public static String FACT_ID = "_id";
    public static String FACT_CAT_ID = "CatId";
    public static String FACT_LAN_ID = "LanId";
    public static String FACT_CONTENT = "Fact";

    public static String CATEGORY_TABLE = "CATEGORIES";
    public static String CATEGORY_ID = "_id";
    public static String CATEGORY_NAME = "Category";

    public static String ENGLISH = "1";
    public static String HINDI = "2";
    public static String RUSSIAN = "3";

    private String DB_PATH;


    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
        DB_PATH = "/data/data/" + context.getPackageName() + "/databases/" + DATABASE_NAME;
        try {
            copyDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkOutFile(){
        File file = new File(DB_PATH);
        return file.exists();
    }

    public void copyDatabase() throws IOException {
        if( checkOutFile()){
            Log.d("copyDatabase", "Database Already Copied");
        }
        else{
            this.getReadableDatabase();
            InputStream inputStream = context.getAssets().open("data.db");
            OutputStream outputStream = new FileOutputStream(DB_PATH);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0){
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();
            Log.d("copyDatabase", "Database copied successfully");
        }
    }


    public void openDatabase(){
        if(checkOutFile()) {
            database = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
        }
    }

    public Cursor fetchAllFactsInCategory(String category_id){
        Cursor cursor = null;
        String whereClause = FACT_CAT_ID + " = ?";
        String[] whereArgs = { category_id };
        if(database != null) {
            cursor = database.query(FACTS_TABLE, new String[]{FACT_ID, FACT_LAN_ID, FACT_CONTENT}, whereClause, whereArgs, null, null, null);
        }
        return cursor;
    }

    public Cursor getRandomRow(String language_id){
        if(database == null) {
            Log.e("SUSHMA", "Database object Null, Returning cursor as NULL");
            return null;
        }
        Cursor cursor = database.query(FACTS_TABLE ,
                new String[] { "*" }, FACT_LAN_ID + "= ? " + " Order BY RANDOM() LIMIT 1 ", new String[]{language_id}, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchSingle(String _id, String languageId){
        Cursor cursor = null;
        String whereClause = FACT_ID + " = ? and " + FACT_LAN_ID + " = ?";
        String[] whereArgs = { _id, languageId };
        if(database != null) {
            cursor = database.query(FACTS_TABLE, new String[]{FACT_ID, FACT_LAN_ID, FACT_CONTENT}, whereClause, whereArgs, null, null, null);
        }
        return cursor;
    }

    public Cursor fetchAllSubCategories(String languageId){

        //select CATEGORIES._id, CATEGORIES.Category from CATEGORIES join FACTS where FACTS.LanId = 2 and FACTS.CatId = CATEGORIES._id GROUP By CATEGORIES._id;
        Cursor cursor = null;
        String queryString = "select " + CATEGORY_TABLE + "." + CATEGORY_ID + ", " + CATEGORY_TABLE + "." + CATEGORY_NAME + " from " + CATEGORY_TABLE + " join " + FACTS_TABLE + " where " + FACTS_TABLE + "." + FACT_LAN_ID + "=" + languageId + " and " + FACTS_TABLE + "." + FACT_CAT_ID + "=" + CATEGORY_TABLE + "." + CATEGORY_ID + " GROUP BY " + CATEGORY_TABLE + "." + CATEGORY_ID + ";";

        if(database != null) {
            cursor = database.rawQuery(queryString, null);
        }
        return cursor;
    }

    public Cursor fetchNextInCategory(String category_id, String fact_id){
        Cursor cursor = null;
        String whereClause = FACT_ID + " > ? and " + FACT_CAT_ID + " = ?";
        String[] whereArgs = { fact_id, category_id };
        if(database != null) {
            cursor = database.query(FACTS_TABLE, new String[]{FACT_ID, FACT_LAN_ID, FACT_CONTENT}, whereClause, whereArgs, null, null, null);
        }
        return cursor;
    }

    public int getFactCount(String category_id){
        Cursor cursor = null;
        String whereClause = FACT_CAT_ID + " = ?";
        String[] whereArgs = {category_id };
        if(database != null) {
            cursor = database.query(FACTS_TABLE, new String[]{FACT_ID, FACT_LAN_ID, FACT_CONTENT}, whereClause, whereArgs, null, null, null);
        }
        return cursor.getCount();
    }

    public int getInitialFactId(String category_id){
        Cursor cursor = null;
        String whereClause = FACT_CAT_ID + " = ?";
        String[] whereArgs = {category_id };
        if(database != null) {
            cursor = database.query(FACTS_TABLE, new String[]{FACT_ID, FACT_LAN_ID, FACT_CONTENT}, whereClause, whereArgs, null, null, null);
        }

        int first_id = 1;

        if(cursor.moveToNext()){
            first_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(FACT_ID)));
        }

        return first_id;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
