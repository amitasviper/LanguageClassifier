package utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by viper on 10/05/16.
 */
public class DefaultStorage {

    private static String TAG_MAIN_STORAGE = "wrestling_quiz";

    public static void SaveBestScore(String category_name, int value)
    {
        SharedPreferences sharedPreferences = AppController.getInstance().getSharedPreferences(TAG_MAIN_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(category_name, value);
        editor.commit();
    }

    public static int GetBestScore(String category_name)
    {
        SharedPreferences sharedPreferences = AppController.getInstance().getSharedPreferences(TAG_MAIN_STORAGE, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(category_name, 0);
    }
}
