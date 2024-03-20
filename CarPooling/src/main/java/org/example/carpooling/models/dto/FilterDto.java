package org.example.carpooling.models.dto;

public class FilterDto {

    private int page;
    private int size;
    private String keyword;
    private String sortBy;
    private String orderBy;

    public FilterDto() {
    }

    public FilterDto(int page, int size, String keyword, String sortBy, String orderBy) {
        this.page = page;
        this.size = size;
        this.keyword = keyword;
        this.sortBy = sortBy;
        this.orderBy = orderBy;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
