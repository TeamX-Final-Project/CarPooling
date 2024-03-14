package org.example.carpooling.repositories;


import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.models.UserSecurityCode;
import org.example.carpooling.repositories.contracts.UserSecurityCodeRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class UserSecurityCodeRepositoryImpl implements UserSecurityCodeRepository {

    private final SessionFactory sessionFactory;


    @Autowired
    public UserSecurityCodeRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public UserSecurityCode save(UserSecurityCode securityCode) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(securityCode);
            session.getTransaction().commit();
        }
        return securityCode;
    }

    @Override
    public UserSecurityCode getCodeByUserId(long userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<UserSecurityCode> query = session.createQuery
                    ("from UserSecurityCode where userId = :userId", UserSecurityCode.class);
            query.setParameter("userId", userId);
            List<UserSecurityCode> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("User security code", "user id", userId);
            }
            return result.get(0);
        }
    }

    @Override
    public void delete(UserSecurityCode userSecurityCode) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(userSecurityCode);
            session.getTransaction().commit();
        }
    }
}
