package com.sked.core.common;

import android.content.Context;

import com.sked.core.model.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class Utils {
    /**
     * Use to read json data from the assets as String
     *
     * @param context      Activity/Application context
     * @param textFileName the name name of text file relative to the inner folder path in the assets
     * @return strJSON, the json String
     */
    public static String getJSONData(Context context, String textFileName) {
        String strJSON;
        StringBuilder buf = new StringBuilder();
        InputStream inputStream;
        try {
            inputStream = context.getAssets().open(textFileName);

            BufferedReader in =
                    new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            while ((strJSON = in.readLine()) != null) {
                buf.append(strJSON);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buf.toString();
    }

    /**
     * Helper method to parse the JSON data to the List<Category
     *
     * @param context      Activity/Application context
     * @param jsonFileName the name name of text file relative to the inner folder path in the assets
     * @return categories , the List<Category
     */
    public static List<Category> getCategoryList(Context context, String jsonFileName) {

        List<Category> categories = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(getJSONData(context, jsonFileName));
            JSONArray jsonArray = jsonObject.getJSONArray("category");
            for (int i = 0; i < jsonArray.length(); i++) {
                Category user = new Category(jsonArray.getJSONObject(i));
                categories.add(user);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return categories;
    }
}
