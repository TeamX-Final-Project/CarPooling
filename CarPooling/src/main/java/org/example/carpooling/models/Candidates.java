package org.example.carpooling.models;

import jakarta.persistence.*;
import org.example.carpooling.models.enums.CandidateStatus;

@Entity
@Table(name = "candidates")
public class Candidates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CandidateStatus status;
    @ManyToOne
    @JoinColumn(name = "travel_id")
    private Travel travel;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public Candidates() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Travel getTravel() {
        return travel;
    }

    public void setTravel(Travel travel) {
        this.travel = travel;
    }

    public CandidateStatus getStatus() {
        return status;
    }

    public void setStatus(CandidateStatus status) {
        this.status = status;
    }
}
