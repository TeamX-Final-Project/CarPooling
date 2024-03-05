package org.example.carpooling.repositories;

import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.TravelFilterOptions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TravelRepositoryImpl {

    private final SessionFactory sessionFactory;

    public TravelRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    //TODO fix this method to work for travels

    public List<Travel> findAll(TravelFilterOptions travelFilterOptions) {
        try (Session session = sessionFactory.openSession()) {
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();
            StringBuilder queryString = new StringBuilder(" from Travel ");

//            travelFilterOptions.getStartPoint().ifPresent(value -> {
//                filters.add(" startPoint like :startPoint");
//                params.put("startPoint", String.format("%%%s%%", value));
//            });
//            travelFilterOptions.getEndPoint().ifPresent(value -> {
//                filters.add(" endPoint like :endPoint");
//                params.put("endPoint", String.format("%%%s%%", value));
//            });

            if (!filters.isEmpty()) {
                queryString.append("where").append(String.join(" and ", filters));
            }
            queryString.append(generateOrderBy(travelFilterOptions));

            Query<Travel> query = session.createQuery(queryString.toString(), Travel.class);
            query.setProperties(params);
            return query.list();
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Travel not found");
        }
    }


    public Travel getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Travel travel = session.get(Travel.class, id);
            if (travel == null) {
                throw new EntityNotFoundException("Travel", id);
            }
            return travel;
        }

    }


    public Travel create(Travel travel) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(travel);
            session.getTransaction().commit();
        }
        return travel;
    }


    public Travel update(Travel travelToUpdate) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(travelToUpdate);
            session.getTransaction().commit();
        }
        return travelToUpdate;
    }


    public Travel deleteTravelById(Travel travelToDelete) {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(travelToDelete);
            session.getTransaction().commit();
        }
        return travelToDelete;
    }


    public Travel cancel(Travel travelToCancel) {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(travelToCancel);
            session.getTransaction().commit();
        }
        return travelToCancel;
    }


    public long getTravelsCount() {
        return 0;
    }

    private String generateOrderBy(TravelFilterOptions travelFilterOptions) {
        if (travelFilterOptions.getSortBy().isEmpty()) {
            return "";
        }

        String orderBy;
        switch (travelFilterOptions.getSortBy()) {
            case "title":
                orderBy = "title";
                break;
            case "startPoint":
                orderBy = "startPoint";
                break;
            case "endPoint":
                orderBy = "endPoint";
                break;
            default:
                return "";
        }

        orderBy = String.format(" order by %s", orderBy);

        if (travelFilterOptions.getOrderBy().isEmpty()
                && travelFilterOptions.getOrderBy().equalsIgnoreCase("desc")) {
            orderBy = String.format("%s desc", orderBy);
        }
        return orderBy;
    }


}
