package com.appradar.viper.amazingfacts;

import android.app.ActionBar;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by viper on 28/7/15.
 */
public class CustomList extends SimpleCursorAdapter {
    Cursor cursor;
    Context context;

    public CustomList(Context context, int layout, Cursor cursor, String[] columns, int[] viewIds, int flags){
        super(context, layout, cursor, columns, viewIds, flags);
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        cursor.moveToPosition(position);

        if(row == null) {
            LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflator.inflate(R.layout.list_item, parent, false);

            holder = new ViewHolder(row);
            row.setTag(holder);
        }
        else{
            holder = (ViewHolder) row.getTag();
        }

        int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CATEGORY_ID)));

        holder.category_id.setText("" + id);

        holder.category_text.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CATEGORY_NAME)));

        holder.category_image.setAdjustViewBounds(true);

        //holder.category_image.setBackgroundResource(CategoryImages.getImageFromName(id));

        holder.category_image.setImageBitmap(getThumbnail(CategoryImages.getImageFromName(id)));

        Random random = new Random();
        int alpha = 50;
        int r = random.nextInt(255);
        int g = random.nextInt(200);
        int b = random.nextInt(255);

        int color = Color.argb(alpha,r,g,b);

        row.setBackgroundColor(Color.WHITE);

        //Log.d("CustomList", ":getView: Position "+position);
        return row;
    }

    private Bitmap getThumbnail(int resourseId){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resourseId, options);

        final int height = options.outHeight;
        final int width = options.outWidth;

        int inSampleSize = 1;
        int reqHeight = 64;

        if(height > reqHeight){
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;

        Bitmap output = BitmapFactory.decodeResource(context.getResources(), resourseId, options);
        if(output != null){
            return Bitmap.createBitmap(output,0,0,output.getWidth(),output.getHeight());
        }
        else{
            return null;
        }

    }

    private class ViewHolder{
        ImageView category_image;
        TextView category_text, category_id;

        public ViewHolder(View view){
            category_image = (ImageView) view.findViewById(R.id.list_category_image);
            category_text = (TextView) view.findViewById(R.id.list_category_text);
            category_id = (TextView) view.findViewById(R.id.list_category_id);
        }

    }
}
