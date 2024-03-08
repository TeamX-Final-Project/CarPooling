package org.example.carpooling.models;

import java.util.Optional;

public class TravelFilterOptions {
    private final int page;
    private final int size;
    private final String keyword;
    private final String sortBy;
    private final String orderBy;

    public TravelFilterOptions(
            Integer page,
            Integer size,
            String keyword,
            String sortBy,
            String orderBy) {
        this.page = page;
        this.size = size;
        this.keyword = keyword;
        this.sortBy = sortBy;
        this.orderBy = orderBy;

    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getSortBy() {
        return sortBy;
    }

    public String getOrderBy() {
        return orderBy;
    }
}
