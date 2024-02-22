package org.example.carpooling.models;

import java.util.Optional;

public class TravelFilterOptions {

    private final Optional<String> title;
    private final Optional<String> startPoint;
    private final Optional<String> endPoint;
    private final Optional<String> createdBy;
    private final Optional<String> sortBy;
    private final Optional<String> orderBy;

    public TravelFilterOptions(String title,
                               String startPoint,
                               String endPoint,
                               String createdBy,
                               String sortBy,
                               String orderBy) {
        this.title = Optional.ofNullable(title);
        this.startPoint = Optional.ofNullable(startPoint);
        this.endPoint = Optional.ofNullable(endPoint);
        this.createdBy = Optional.ofNullable(createdBy);
        this.sortBy = Optional.ofNullable(sortBy);
        this.orderBy = Optional.ofNullable(orderBy);
    }

    public Optional<String> getTitle() {
        return title;
    }

    public Optional<String> getStartPoint() {
        return startPoint;
    }

    public Optional<String> getEndPoint() {
        return endPoint;
    }

    public Optional<String> getCreatedBy() {
        return createdBy;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getOrderBy() {
        return orderBy;
    }
}
