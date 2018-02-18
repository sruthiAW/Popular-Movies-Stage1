package com.example.ssurendran.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ssurendran on 2/18/18.
 */

public class MoviePref {

    public static final String SORT_ORDER_KEY = "sort_order_key";
    public static final String SORT_ORDER_CHECKED_INDEX_KEY = "sort_order_checked_index_key";
    private Context context;
    private SharedPreferences preferences;

    public MoviePref(Context context) {
        this.context = context;
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private SharedPreferences.Editor getEditor(){
        return preferences.edit();
    }

    public void setSortOrder(String sortOrder){
        getEditor().putString(SORT_ORDER_KEY, sortOrder).commit();
    }

    public String getSortOrder(){
        return preferences.getString(SORT_ORDER_KEY, context.getResources().getString(R.string.popular_sort));
    }

    public void setSortDialogCheckedItem(int index){
        getEditor().putInt(SORT_ORDER_CHECKED_INDEX_KEY, index).commit();
    }

    public int getSortDialogCheckedItem(){
        return preferences.getInt(SORT_ORDER_CHECKED_INDEX_KEY, 0);
    }
}
