package org.example.carpooling.models.dto;

public class TravelFilterDto {

    private int page;
    private int size;
    private String keyword;
    private String startPoint;
    private String endPoint;
    private String sortBy;
    private String orderBy;

    public TravelFilterDto() {
    }

    public TravelFilterDto(int page, int size, String keyword, String startPoint, String endPoint, String sortBy, String orderBy) {
        this.page = page;
        this.size = size;
        this.keyword = keyword;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
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

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
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
