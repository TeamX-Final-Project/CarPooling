package org.example.carpooling.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@EqualsAndHashCode
@Table(name ="feedbacks")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "rating")
    private int rating;

    @Column(name = "comment")
    private String comment;

    @OneToOne
    @JoinColumn(name = "travel_id")
    private Travel travel;

    @OneToOne
    @JoinColumn(name = "from_user_id")
    private User fromUser;

    @ManyToOne
    @JoinColumn(name = "to_user_id")
    private User toUser;

    public Feedback() {
    }


}

