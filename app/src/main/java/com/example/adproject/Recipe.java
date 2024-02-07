
package com.example.adproject;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.Serializable;

        import java.io.Serializable;

public class Recipe implements Serializable {
    private int id;
    private String name;
    private String description;

    // 通用构造函数
    public Recipe(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // 从JSONObject创建Recipe实例的静态方法
    public static Recipe fromJson(JSONObject jsonObject) throws JSONException {
        int id = jsonObject.getInt("id");
        String name = jsonObject.getString("name");
        String description = jsonObject.optString("description", "");
        return new Recipe(id, name, description);
    }

    // Getter方法
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
}
