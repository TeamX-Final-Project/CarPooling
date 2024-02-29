package org.example.carpooling.models;

import java.util.Optional;

public class UserFilterOptions {

    private final Optional<String> phoneNumber;
    private final Optional<String> email;
    private final Optional<String> username;
    private final Optional<String> sortBy;
    private final Optional<String> orderBy;

    public UserFilterOptions() {
        this.phoneNumber = Optional.empty();
        this.email = Optional.empty();
        this.username = Optional.empty();
        this.sortBy = Optional.empty();
        this.orderBy = Optional.empty();
    }

    public UserFilterOptions(String phoneNumber, String email,
                             String username, String sortBy, String orderBy) {
        this.phoneNumber = Optional.ofNullable(phoneNumber);
        this.email = Optional.ofNullable(email);
        this.username = Optional.ofNullable(username);
//        this.postId = Optional.ofNullable(postId);
        this.sortBy = Optional.ofNullable(sortBy);
        this.orderBy = Optional.ofNullable(orderBy);
    }



    public Optional<String> getPhoneNumber() {
        return phoneNumber;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public Optional<String> getUsername() {
        return username;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getOrderBy() {
        return orderBy;
    }

}
