package org.example.carpooling.models;

import org.springframework.data.jpa.domain.Specification;

public class TravelSpecifications {

    public static Specification<Travel> hasKeyword(String keyword, String point) {
        if (keyword == null || keyword.isEmpty()) {
            return null;
        }
        return (root, query, criteriaBuilder) -> {
            String likePattern = "%" + keyword + "%";
            return criteriaBuilder.like(root.get(point), likePattern);
        };
    }
}

