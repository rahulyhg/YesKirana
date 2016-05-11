package com.sked.core.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * ERC, All rights Reserved
 * Created by Sanjeet on 01-Jan-16.
 */
public class Category {
    private int id;
    private String name;
    private List<SubCategory> subCategories;

    /**
     * Creates the instance of Category from the JSONObject instance
     *
     * @param category
     */
    public Category(JSONObject category) {
        try {
            this.id = category.getInt("id");
            this.name = category.getString("name");
            this.subCategories = new ArrayList<>();
            JSONArray subCategoryArray = category.getJSONArray("subCategory");
            for (int i = 0; i < subCategoryArray.length(); i++) {
                subCategories.add(new SubCategory(subCategoryArray.getJSONObject(i)));
            }
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

    public List<SubCategory> getSubCategories() {
        return subCategories;
    }

}
