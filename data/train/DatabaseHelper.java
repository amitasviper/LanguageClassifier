package utils;

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
public class DatabaseHelper extends SQLiteOpenHelper {

    SQLiteDatabase database;
    Context context;

    private static String DATABASE_NAME = "wrestling";
    public static String TABLE_NAME = "questions";
    public static String COL_ID = "_id";
    public static String COL_CATEGORY = "category";
    public static String COL_QUESTION = "question";
    public static String COL_OPTION_A = "option_a";
    public static String COL_OPTION_B = "option_b";
    public static String COL_OPTION_C = "option_c";
    public static String COL_OPTION_D = "option_d";
    public static String COL_ANSWER = "answer";
    public static String COL_DESCRIPTION = "description";

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

    private void copyDatabase() throws IOException {
        if( checkOutFile()){
            Log.d("copyDatabase", "Database Already Copied");
        }
        else{
            this.getReadableDatabase();
            InputStream inputStream = context.getAssets().open(AppConstants.DATABASE_FILE_NAME);
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

    public Cursor fetchQuestionsInCategory(String category){
        Cursor cursor = null;
        String whereClause = COL_CATEGORY + " = ?";
        String[] whereArgs = { category };
        if(database != null) {
            cursor = database.query(TABLE_NAME, new String[]{COL_ID, COL_QUESTION, COL_OPTION_A, COL_OPTION_B, COL_OPTION_C, COL_OPTION_D, COL_ANSWER, COL_DESCRIPTION}, whereClause, whereArgs, null, null, null);
        }
        return cursor;
    }

    public Cursor fetchDescriptionInCategory(String category){
        Cursor cursor = null;
        String whereClause = COL_CATEGORY + " = ?";
        String[] whereArgs = { category };
        if(database != null) {
            cursor = database.query(TABLE_NAME, new String[]{COL_DESCRIPTION}, whereClause, whereArgs, null, null, null);
        }
        return cursor;
    }

/*    public Cursor getRandomRow(String language_id){
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
    }*/

    /*public Cursor fetchSingle(String _id, String languageId){
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
    }*/

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
