package com.example.adproject;
import org.json.JSONException;
import org.json.JSONObject;
public class RecipeModel {
    private int id;
    private String name;
    private String description;
    // 其他字段...
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    public RecipeModel(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getInt("id");
            this.name = jsonObject.getString("name");
            this.description = jsonObject.optString("description", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
