package org.example.carpooling.repositories;

import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.TravelFilterOptions;
import org.example.carpooling.repositories.contracts.TravelRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TravelRepositoryImpl implements TravelRepository {

    private final SessionFactory sessionFactory;

    public TravelRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Travel> getAllTravels(TravelFilterOptions travelFilterOptions) {
        try (Session session = sessionFactory.openSession()) {
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();
            StringBuilder queryString = new StringBuilder(" from Travel ");

            travelFilterOptions.getTitle().ifPresent(value -> {
                filters.add(" title like :title");
                params.put("title", String.format("%%%s%%", value));
            });
            travelFilterOptions.getStartPoint().ifPresent(value -> {
                filters.add(" startPoint like :startPoint");
                params.put("startPoint", String.format("%%%s%%", value));
            });
            travelFilterOptions.getEndPoint().ifPresent(value -> {
                filters.add(" endPoint like :endPoint");
                params.put("endPoint", String.format("%%%s%%", value));
            });
            travelFilterOptions.getCreatedBy().ifPresent(value -> {
                filters.add(" createdBy.username like :createdBy");
                params.put("createdBy", String.format("%%%s%%", value));
            });

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

    @Override
    public Travel getById(int id) {
        return null;
    }

    @Override
    public Travel create() {
        return null;
    }

    @Override
    public Travel updated() {
        return null;
    }

    @Override
    public Travel delete() {
        return null;
    }

    @Override
    public long getTravelsCount() {
        return 0;
    }

    private String generateOrderBy(TravelFilterOptions travelFilterOptions) {
        if (travelFilterOptions.getSortBy().isEmpty()) {
            return "";
        }

        String orderBy;
        switch (travelFilterOptions.getSortBy().get()) {
            case "title":
                orderBy = "title";
                break;
            case "startPoint":
                orderBy = "startPoint";
                break;
            case "endPoint":
                orderBy = "endPoint";
                break;
            case "username":
                orderBy = "username";
                break;
            default:
                return "";
        }

        orderBy = String.format(" order by %s", orderBy);

        if (travelFilterOptions.getOrderBy().isPresent()
                && travelFilterOptions.getOrderBy().get().equalsIgnoreCase("desc")) {
            orderBy = String.format("%s desc", orderBy);
        }
        return orderBy;
    }
}
