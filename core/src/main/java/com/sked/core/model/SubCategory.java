package com.sked.core.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * ERC, All rights Reserved
 * Created by Sanjeet on 01-Jan-16.
 */
public class SubCategory {
    private int id;
    private String name;

    /**
     *Used to parse the JSONObjects of SubCategory
     * @param subCategory
     */
    public  SubCategory(JSONObject subCategory){
        try {
            this.id = subCategory.getInt("id");
            this.name = subCategory.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
