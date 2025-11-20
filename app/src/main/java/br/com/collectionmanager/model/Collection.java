package br.com.collectionmanager.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Collection {
    private long id;
    private String name;
    private String description;
    private String startDate;
    private String estimatedRelease;
    private String createdAt;

    public Collection(long id, String name, String description, String startDate, String estimatedRelease, String createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.estimatedRelease = estimatedRelease;
        this.createdAt = createdAt;
    }

    public Collection(String name, String description, String startDate, String estimatedRelease) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.estimatedRelease = estimatedRelease;
        // Set the creation date when the collection is created
        this.createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    // Getters
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEstimatedRelease() {
        return estimatedRelease;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEstimatedRelease(String estimatedRelease) {
        this.estimatedRelease = estimatedRelease;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}