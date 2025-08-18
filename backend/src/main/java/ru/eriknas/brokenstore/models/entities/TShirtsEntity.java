package ru.eriknas.brokenstore.models.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_shirt", uniqueConstraints = @UniqueConstraint(columnNames = "article"))
public class TShirtsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "article", unique = true)
    private String article;

    @Column(name = "name")
    private String name;

    @Column(name = "size")
    private String size;

    @Column(name = "color")
    private String color;

    @Column(name = "image")
    private String image;

    @Column(name = "material")
    private String material;

    @Column(name = "country_of_production")
    private String countryOfProduction;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @JsonProperty("isActive")
    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "archived_at")
    private OffsetDateTime archivedAt;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;


    @PrePersist
    public void onCreate() {
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

    @JsonProperty("archived_At")
    public OffsetDateTime getArchivedAt() {
        return archivedAt;
    }

    @JsonProperty("created_At")
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("updated_At")
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}