package org.example.carpooling.models;

import jakarta.persistence.*;

@Entity
@Table(name = "travel_preferences")
public class TravelPreferences {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "text")
    private String text;
    @OneToOne
    @JoinColumn(name = "travel_id")
    private Travel travelId;

    public TravelPreferences() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Travel getTravelId() {
        return travelId;
    }

    public void setTravelId(Travel travelId) {
        this.travelId = travelId;
    }
}
