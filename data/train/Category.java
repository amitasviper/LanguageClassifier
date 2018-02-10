package com.appradar.viper.amazingfacts;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * Created by viper on 05/08/15.
 */
public class Category {
    public String category_id;
    public int start_id, current, total_facts;
    public Cursor cursor;
    Context context;

    public Category(Context context, String category_id){
        this.category_id = category_id;
        this.context = context;
        this.current = 1;

        DatabaseHelper database = new DatabaseHelper(context);
        database.openDatabase();

        cursor = database.fetchAllFactsInCategory(category_id);
        this.total_facts = cursor.getCount();
        this.start_id = database.getInitialFactId(category_id);
    }

    public Category(Context context, String category_id, int fact_id){
        this.category_id = category_id;
        this.context = context;

        DatabaseHelper database = new DatabaseHelper(context);
        database.openDatabase();

        cursor = database.fetchAllFactsInCategory(category_id);
        this.total_facts = cursor.getCount();
        this.start_id = database.getInitialFactId(category_id);

        current = fact_id - start_id + 1;

        Log.e("All Ids", "StartId: "+ start_id +" fact_id: "+ fact_id + " total_facts: " +total_facts + " current: "+current);
    }

    public String getFactBody(){
        cursor.moveToPosition(current-1);
        return cursor.getString(cursor.getColumnIndex(DatabaseHelper.FACT_CONTENT));
    }

    public String getFactId(){
        cursor.moveToPosition(current-1);
        return cursor.getString(cursor.getColumnIndex(DatabaseHelper.FACT_ID));
    }

    public void setFactPos(int position){
        current = position;
    }


    public boolean moveToNext(){
        Log.e("moveToNext", "Current : " + current);
        if( current < total_facts){
            current += 1;
            return true;
        }
        return false;
    }

    public boolean moveToPrev(){
        Log.e("moveToPrev", "Current : " + current);
        if( current > 1){
            current -= 1;
            return true;
        }
        return false;
    }
}
