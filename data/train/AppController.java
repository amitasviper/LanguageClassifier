package utils;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by viper on 28/11/15.
 */
public class AppController extends Application {

    private static AppController instance;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("youtube");
    }

    public DatabaseReference getDatabaseReference(){
        return databaseReference;
    }

    public static synchronized AppController getInstance(){
        return instance;
    }

    public static Bitmap getThumbnail(int resourseId){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(instance.getResources(), resourseId, options);

        final int height = options.outHeight;
        final int width = options.outWidth;

        int inSampleSize = 1;
        int reqHeight = height/3;

        if(height > reqHeight){
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;

        Bitmap output = BitmapFactory.decodeResource(instance.getResources(), resourseId, options);
        if(output != null){
            return Bitmap.createBitmap(output, 0, 0, output.getWidth(), output.getHeight());
        }
        else{
            return null;
        }
    }

    public static int getImageResourceId(String technical_name){
        int imageResourceId = instance.getResources().getIdentifier(technical_name,"drawable", instance.getPackageName());
        return imageResourceId;
    }
}
