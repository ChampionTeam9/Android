
package com.example.adproject;
        import org.json.JSONException;
        import org.json.JSONObject;

public class Recipe {
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

    public Recipe(JSONObject jsonObject) {
        this.id = jsonObject.optInt("id");
        this.name = jsonObject.optString("name");
        this.description = jsonObject.optString("description", "");

    }


    public void setName(String name) {
        this.name=name;
    }

    public void setDescription(String description) {
        this.description=description;
    }
}
