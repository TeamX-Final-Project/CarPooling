package org.example.carpooling.models;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class TravelSpecifications{

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

