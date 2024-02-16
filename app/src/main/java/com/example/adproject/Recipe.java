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
    private Double rating;
    private Integer numberOfRating;
    private Integer numberOfSaved;
    private LocalDate submittedDate;
    private List<String> tags;
    private Integer servings;
    private Integer preparationTime;
    private List<String> steps;
    private Integer healthScore;
    private Double calories;
    private Double protein;
    private Double carbohydrate;
    private Double sugar;
    private Double sodium;
    private Double fat;
    private Double saturatedFat;
    private String username;

    // 通用构造函数
    public Recipe(Integer id, String name, String description, String image, Double rating, Integer numberOfRating,
                     Integer numberOfSaved, LocalDate submittedDate, List<String> tags, Integer servings,
                     Integer preparationTime, List<String> steps, Integer healthScore, Double calories, Double protein,
                     Double carbohydrate, Double sugar, Double sodium, Double fat, Double saturatedFat,String username) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.rating = rating;
        this.numberOfRating = numberOfRating;
        this.numberOfSaved = numberOfSaved;
        this.submittedDate = submittedDate;
        this.tags = tags;
        this.servings = servings;
        this.preparationTime = preparationTime;
        this.steps = steps;
        this.healthScore=healthScore;
        this.calories=calories;
        this.protein = protein;
        this.carbohydrate = carbohydrate;
        this.sugar = sugar;
        this.sodium = sodium;
        this.fat = fat;
        this.saturatedFat = saturatedFat;
        this.username=username;
    }

    public Recipe() {

    }


    // 从JSONObject创建Recipe实例的静态方法
    public static Recipe fromJson(JSONObject jsonObject) throws JSONException {
        int id = jsonObject.getInt("id");
        String name = jsonObject.getString("name");
        String username = jsonObject.optString("username", "");
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
        Integer healthScore = jsonObject.optInt("healthScore", 0);
        Double calories = jsonObject.optDouble("calories", 0.0);
        Double protein = jsonObject.optDouble("protein", 0.0);
        Double carbohydrate = jsonObject.optDouble("carbohydrate", 0.0);
        Double sugar = jsonObject.optDouble("sugar", 0.0);
        Double sodium = jsonObject.optDouble("sodium", 0.0);
        Double fat = jsonObject.optDouble("fat", 0.0);
        Double saturatedFat = jsonObject.optDouble("saturatedFat", 0.0);

        return new Recipe(id, name, description, image, rating, numberOfRating, numberOfSaved,
                submittedDate, tags, servings, preparationTime, steps, healthScore, calories, protein,
                carbohydrate, sugar, sodium, fat, saturatedFat, username);

    }

    // Parcelable接口
    protected Recipe(Parcel in) {
        id = in.readInt();
        name = in.readString();
        username = in.readString();
        description = in.readString();
        image = in.readString();
        if (in.readByte() == 0) {
            rating = null;
        } else {
            rating = in.readDouble();
        }
        if (in.readByte() == 0) {
            numberOfRating = null;
        } else {
            numberOfRating = in.readInt();
        }
        if (in.readByte() == 0) {
            numberOfSaved = null;
        } else {
            numberOfSaved = in.readInt();
        }
        submittedDate = (LocalDate) in.readSerializable();
        tags = in.createStringArrayList();
        if (in.readByte() == 0) {
            servings = null;
        } else {
            servings = in.readInt();
        }
        if (in.readByte() == 0) {
            preparationTime = null;
        } else {
            preparationTime = in.readInt();
        }
        steps = in.createStringArrayList();
        if (in.readByte() == 0) {
            healthScore = null;
        } else {
            healthScore = in.readInt();
        }
        if (in.readByte() == 0) {
            calories = null;
        } else {
            calories = in.readDouble();
        }
        if (in.readByte() == 0) {
            protein = null;
        } else {
            protein = in.readDouble();
        }
        if (in.readByte() == 0) {
            carbohydrate = null;
        } else {
            carbohydrate = in.readDouble();
        }
        if (in.readByte() == 0) {
            sugar = null;
        } else {
            sugar = in.readDouble();
        }
        if (in.readByte() == 0) {
            sodium = null;
        } else {
            sodium = in.readDouble();
        }
        if (in.readByte() == 0) {
            fat = null;
        } else {
            fat = in.readDouble();
        }
        if (in.readByte() == 0) {
            saturatedFat = null;
        } else {
            saturatedFat = in.readDouble();
        }
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
        dest.writeInt(id == null ? 0 : id);
        dest.writeString(name);
        dest.writeString(username);
        dest.writeString(description);
        dest.writeString(image);
        dest.writeByte((byte) (rating == null ? 0 : 1));
        if (rating != null) {
            dest.writeDouble(rating);
        }
        dest.writeByte((byte) (numberOfRating == null ? 0 : 1));
        if (numberOfRating != null) {
            dest.writeInt(numberOfRating);
        }
        dest.writeByte((byte) (numberOfSaved == null ? 0 : 1));
        if (numberOfSaved != null) {
            dest.writeInt(numberOfSaved);
        }
        dest.writeSerializable(submittedDate);
        dest.writeStringList(tags);
        dest.writeByte((byte) (servings == null ? 0 : 1));
        if (servings != null) {
            dest.writeInt(servings);
        }
        dest.writeByte((byte) (preparationTime == null ? 0 : 1));
        if (preparationTime != null) {
            dest.writeInt(preparationTime);
        }
        dest.writeStringList(steps);
        dest.writeByte((byte) (healthScore == null ? 0 : 1));
        if (healthScore != null) {
            dest.writeInt(healthScore);
        }
        dest.writeByte((byte) (calories == null ? 0 : 1));
        if (calories != null) {
            dest.writeDouble(calories);
        }
        dest.writeByte((byte) (protein == null ? 0 : 1));
        if (protein != null) {
            dest.writeDouble(protein);
        }
        dest.writeByte((byte) (carbohydrate == null ? 0 : 1));
        if (carbohydrate != null) {
            dest.writeDouble(carbohydrate);
        }
        dest.writeByte((byte) (sugar == null ? 0 : 1));
        if (sugar != null) {
            dest.writeDouble(sugar);
        }
        dest.writeByte((byte) (sodium == null ? 0 : 1));
        if (sodium != null) {
            dest.writeDouble(sodium);
        }
        dest.writeByte((byte) (fat == null ? 0 : 1));
        if (fat != null) {
            dest.writeDouble(fat);
        }
        dest.writeByte((byte) (saturatedFat == null ? 0 : 1));
        if (saturatedFat != null) {
            dest.writeDouble(saturatedFat);
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

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

    public Integer getHealthScore() {
        return healthScore;
    }

    public void setHealthScore(Integer healthScore) {
        this.healthScore = healthScore;
    }

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public Double getProtein() {
        return protein;
    }

    public void setProtein(Double protein) {
        this.protein = protein;
    }

    public Double getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(Double carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public Double getSugar() {
        return sugar;
    }

    public void setSugar(Double sugar) {
        this.sugar = sugar;
    }

    public Double getSodium() {
        return sodium;
    }

    public void setSodium(Double sodium) {
        this.sodium = sodium;
    }

    public Double getFat() {
        return fat;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }

    public Double getSaturatedFat() {
        return saturatedFat;
    }

    public void setSaturatedFat(Double saturatedFat) {
        this.saturatedFat = saturatedFat;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}