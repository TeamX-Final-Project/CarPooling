package org.example.carpooling.models.dto;

public class UserFilterDto {

    private String firstName;
    private String username;
    private String email;

    private String phoneNumber;
    private String sortBy;
    private String orderBy;

    public UserFilterDto() {
    }

    public UserFilterDto(String firstName, String username, String email, String phoneNumber, String sortBy, String orderBy) {
        this.firstName = firstName;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.sortBy = sortBy;
        this.orderBy = orderBy;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
