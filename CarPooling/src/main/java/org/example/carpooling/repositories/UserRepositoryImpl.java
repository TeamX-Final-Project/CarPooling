//package org.example.carpooling.repositories;
//
//
//import org.example.carpooling.exceptions.EntityNotFoundException;
//import org.example.carpooling.models.User;
//import org.example.carpooling.models.UserFilterOptions;
//import org.example.carpooling.repositories.contracts.UserRepositoryOld;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.query.Query;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Deprecated
//@Repository
//public class UserRepositoryImpl implements UserRepositoryOld {
//
//    private static final String USER_CONSTANT = "User not found";
//    private final SessionFactory sessionFactory;
//
//
//    @Autowired
//    public UserRepositoryImpl(SessionFactory sessionFactory) {
//        this.sessionFactory = sessionFactory;
//    }
//
//    @Override
//    public List<User> getAllUsers(UserFilterOptions filterOptions) {
//        try (Session session = sessionFactory.openSession()) {
//            List<String> filters = new ArrayList<>();
//            Map<String, Object> params = new HashMap<>();
//            StringBuilder queryString = new StringBuilder(" from User ");
//
//            filterOptions.getPhoneNumber().ifPresent(value -> {
//                filters.add(" phoneNumber like :phoneNumber ");
//                params.put("firstName", String.format("%%%s%%", value));
//            });
//            filterOptions.getEmail().ifPresent(value -> {
//                filters.add(" email like :email ");
//                params.put("email", String.format("%%%s%%", value));
//            });
//            filterOptions.getUsername().ifPresent(value -> {
//                filters.add(" username like :username ");
//                params.put("username", String.format("%%%s%%", value));
//            });
//            if (!filters.isEmpty()) {
//                queryString.append("where").append(String.join(" and ", filters));
//            }
//            queryString.append(generateOrderBy(filterOptions));
//            Query<User> query = session.createQuery(queryString.toString(), User.class);
//            query.setProperties(params);
//            List<User> result = query.list();
//            if (result.isEmpty()) {
//                throw new EntityNotFoundException(USER_CONSTANT);
//            }
//            return result;
//        }
//    }
//
//    @Override
//    public User getByUserId(long id) {
//        try (Session session = sessionFactory.openSession()) {
//            User user = session.get(User.class, id);
//            if (user == null) {
//                throw new EntityNotFoundException("User", id);
//            }
//            return user;
//        }
//    }
//
//    @Override
//    public User getByPhoneNumber(String phoneNumber) {
//        try (Session session = sessionFactory.openSession()) {
//            Query<User> query = session.createQuery("from User where phoneNumber = :phoneNumber", User.class);
//            query.setParameter("phoneNumber", phoneNumber);
//            List<User> result = query.list();
//            if (result.isEmpty()) {
//                throw new EntityNotFoundException("Phone number", "phone number", phoneNumber);
//            }
//            return result.get(0);
//        }
//    }
//
//    @Override
//    public User getByEmail(String email) {
//        try (Session session = sessionFactory.openSession()) {
//            Query<User> query = session.createQuery("from User where email = :email", User.class);
//            query.setParameter("email", email);
//            List<User> result = query.list();
//            if (result.isEmpty()) {
//                throw new EntityNotFoundException("User", "email", email);
//            }
//            return result.get(0);
//        }
//    }
//
//    @Override
//    public User getByUsername(String username) {
//        try (Session session = sessionFactory.openSession()) {
//            Query<User> query = session.createQuery("from User where username = :username", User.class);
//            query.setParameter("username", username);
//            List<User> result = query.list();
//            if (result.isEmpty()) {
//                throw new EntityNotFoundException("User", "username", username);
//            }
//            return result.get(0);
//        }
//    }
//
//
//    @Override
//    public User create(User user) {
//        try (Session session = sessionFactory.openSession()) {
//            session.beginTransaction();
//            session.persist(user);
//            session.getTransaction().commit();
//        }
//        return user;
//    }
//
//    @Override
//    public User update(User user) {
//        User updatedUser;
//        try (Session session = sessionFactory.openSession()) {
//            session.beginTransaction();
//            updatedUser = session.merge(user);
//            session.getTransaction().commit();
//        }
//        return updatedUser;
//    }
//
//    @Override
//    public long getUserCount() {
//        try (Session session = sessionFactory.openSession()) {
//            String hql = "SELECT COUNT(*) FROM User where userStatus=1";
//
//            Query<Long> query = session.createQuery(hql, Long.class);
//
//            List<Long> resultList = query.list();
//
//            return resultList.get(0);
//        }
//    }
//
//    private String generateOrderBy(UserFilterOptions filterOptions) {
//        if (filterOptions.getSortBy().isEmpty()) {
//            return "";
//        }
//        String orderBy = "";
//        switch (filterOptions.getSortBy().get()) {
//            case "phoneNumber":
//                orderBy = "phoneNumber";
//                break;
//            case "email":
//                orderBy = "email";
//                break;
//            case "username":
//                orderBy = "username";
//                break;
//            default:
//                return "";
//        }
//
//        orderBy = String.format(" order by %s", orderBy);
//
//        if (filterOptions.getOrderBy().isPresent()
//                && filterOptions.getOrderBy().get().equalsIgnoreCase("desc")) {
//            orderBy = String.format("%s desc", orderBy);
//        }
//        return orderBy;
//    }
//}
