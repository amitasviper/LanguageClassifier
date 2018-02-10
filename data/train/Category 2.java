package utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by viper on 02/05/16.
 */
public class Category implements Parcelable
{
    int image_resource;
    String friendly_name, technical_name;

    public Category(int image_resource, String friendly_name, String technical_name)
    {
        this.image_resource= image_resource;
        this.friendly_name = friendly_name;
        this.technical_name = technical_name;
    }

    //the code written only for even number of categories due to shortage of time
    private static String[] friendly_name_list = {
            "Divas",
            "Wrestlemania",
            "Family",
            "Finishers",
            "Hall of Fame",
            "Nationality",
            "Real Names",
            "Royal Rumble",
            "Facts",
            "Tough Questions"
    };

    private static String[] technical_name_list = {
            "diva",
            "wrestlemania",
            "family",
            "finishers",
            "hall_of_fame",
            "nationality",
            "real_name",
            "royal_rumble",
            "facts",
            "tough_questions"
    };

    public String getTechnical_name(){
        return this.technical_name;
    }

    public String getFriendly_name(){
        return this.friendly_name;
    }


    public static String getFriendly_name(String tech_name){
        for (int i = 0; i < technical_name_list.length; i++)
        {
            if (technical_name_list[i].equalsIgnoreCase(tech_name))
                return friendly_name_list[i];
        }
        return "";
    }

    public int getImage_resource(){
        return image_resource;
    }

    //This function need to be repaired. As we are passing a fake ImageResource Id to create a new Category Object
    public static Category getCategoryObject(String category_friendly_name){
        for(int i = 0; i < friendly_name_list.length; i++){
            if ( category_friendly_name.equalsIgnoreCase(friendly_name_list[i])){
                return new Category(0, category_friendly_name, technical_name_list[i]);
            }
        }
        return null;
    }

    public static String[] getTechnical_name_list(){
        return technical_name_list;
    }

    public static String[] getFriendly_name_list(){
        return friendly_name_list;
    }


    public static final Parcelable.Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel source) {
            Category category = new Category(source.readInt(), source.readString(), source.readString());
            return category;
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(image_resource);
        dest.writeString(friendly_name);
        dest.writeString(technical_name);
    }
}
