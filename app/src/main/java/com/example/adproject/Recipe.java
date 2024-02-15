package com.example.adproject;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable {
    private Integer id;
    private String name;
    private String description;
    private String image;

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getNumberOfRating() {
        return numberOfRating;
    }

    public void setNumberOfRating(Integer numberOfRating) {
        this.numberOfRating = numberOfRating;
    }

    public Integer getNumberOfSaved() {
        return numberOfSaved;
    }

    public void setNumberOfSaved(Integer numberOfSaved) {
        this.numberOfSaved = numberOfSaved;
    }

    public LocalDate getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(LocalDate submittedDate) {
        this.submittedDate = submittedDate;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public Integer getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(Integer preparationTime) {
        this.preparationTime = preparationTime;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    private Double rating;
    private Integer numberOfRating;
    private Integer numberOfSaved;
    private LocalDate submittedDate;
    private List<String> tags;
    private Integer servings;
    private Integer preparationTime;
    private List<String> steps;

    // 通用构造函数
    public Recipe(int id, String name, String description, String image,
                  Double rating,
                  Integer numberOfRating,
                  Integer numberOfSaved,
                  LocalDate submittedDate,
                  List<String> tags,
                  Integer servings,
                  Integer preparationTime,
                  List<String> steps) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image=image;
        this.rating=rating;
        this.numberOfRating=numberOfRating;
        this.numberOfSaved=numberOfSaved;
        this.submittedDate=submittedDate;
        this.tags=tags;
        this.servings=servings;
        this.preparationTime=preparationTime;
        this.steps=steps;


    }

    public Recipe() {

    }


    // 从JSONObject创建Recipe实例的静态方法
    public static Recipe fromJson(JSONObject jsonObject) throws JSONException {
        int id = jsonObject.getInt("id");
        String name = jsonObject.getString("name");
        String description = jsonObject.optString("description", "");
        String image = jsonObject.optString("image", "");
        double rating = jsonObject.optDouble("rating", 0);
        int numberOfRating = jsonObject.optInt("numberOfRating", 0);
        int numberOfSaved = jsonObject.optInt("numberOfSaved", 0);
        String dateStr = jsonObject.optString("submittedDate", null);
        LocalDate submittedDate = null;
        if (dateStr != null) {
            submittedDate = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
        }
        List<String> tags = new ArrayList<>();
        if (jsonObject.has("tags")) {
            JSONArray tagsArray = jsonObject.getJSONArray("tags");
            for (int i = 0; i < tagsArray.length(); i++) {
                tags.add(tagsArray.getString(i));
            }
        }
        int servings = jsonObject.optInt("servings", 0);
        int preparationTime = jsonObject.optInt("preparationTime", 0);
        List<String> steps = new ArrayList<>();
        if (jsonObject.has("steps")) {
            JSONArray stepsArray = jsonObject.getJSONArray("steps");
            for (int i = 0; i < stepsArray.length(); i++) {
                steps.add(stepsArray.getString(i));
            }
        }

        return new Recipe(id, name, description, image, rating, numberOfRating, numberOfSaved,
                submittedDate, tags, servings, preparationTime, steps);
    }

    // Parcelable接口的方法实现
    protected Recipe(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        image = in.readString();
        rating = in.readDouble();
        numberOfRating = in.readInt();
        numberOfSaved = in.readInt();
        String dateStr = in.readString();
        submittedDate = dateStr != null ? LocalDate.parse(dateStr) : null;
        tags = in.createStringArrayList();
        servings = in.readInt();
        preparationTime = in.readInt();
        steps = in.createStringArrayList();
    }


    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(image);
        dest.writeDouble(rating);
        dest.writeInt(numberOfRating);
        dest.writeInt(numberOfSaved);
        dest.writeString(submittedDate != null ? submittedDate.toString() : null);
        dest.writeStringList(tags);
        dest.writeInt(servings);
        dest.writeInt(preparationTime);
        dest.writeStringList(steps);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    // Getter方法
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
