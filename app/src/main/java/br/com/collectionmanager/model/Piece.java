package br.com.collectionmanager.model;

public class Piece {
    private long id;
    private String name;
    private String description;
    private String entryDate;
    private String deliveryDeadline;
    private String observations;
    private String status; // "Pending", "In Production", "Completed"
    private long collectionId;

    public Piece(long id, String name, String description, String entryDate, String deliveryDeadline, String observations, String status, long collectionId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.entryDate = entryDate;
        this.deliveryDeadline = deliveryDeadline;
        this.observations = observations;
        this.status = status;
        this.collectionId = collectionId;
    }

    public Piece(String name, String description, String entryDate, String deliveryDeadline, String observations, String status, long collectionId) {
        this.name = name;
        this.description = description;
        this.entryDate = entryDate;
        this.deliveryDeadline = deliveryDeadline;
        this.observations = observations;
        this.status = status;
        this.collectionId = collectionId;
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

    public String getEntryDate() {
        return entryDate;
    }

    public String getDeliveryDeadline() {
        return deliveryDeadline;
    }

    public String getObservations() {
        return observations;
    }

    public String getStatus() {
        return status;
    }

    public long getCollectionId() {
        return collectionId;
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

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public void setDeliveryDeadline(String deliveryDeadline) {
        this.deliveryDeadline = deliveryDeadline;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCollectionId(long collectionId) {
        this.collectionId = collectionId;
    }
}