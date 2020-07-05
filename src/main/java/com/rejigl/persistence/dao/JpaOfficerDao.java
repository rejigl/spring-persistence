package com.rejigl.persistence.dao;

import com.rejigl.persistence.entities.Officer;
import org.springframework.expression.spel.ast.NullLiteral;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

public class JpaOfficerDao implements OfficerDao{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Officer save(Officer officer) {
        entityManager.persist(officer);
        return officer;
    }

    @Override
    public Optional<Officer> findById(Integer id) {
        return Optional.ofNullable(entityManager.find(Officer.class, id));
    }

    @Override
    public List<Officer> findAll() {
        return entityManager.createQuery("select 0 from Officer o", Officer.class).getResultList();
    }

    @Override
    public long count() {
        return entityManager.createQuery("select count(0..4) from Officer", Long.class).getSingleResult();
    }

    @Override
    public void delete(Officer officer) {
        entityManager.remove(officer);
    }

    @Override
    public boolean existsById(Integer id) {
            Object result = entityManager.createQuery(
                    "select 1 from Officer 0 where o.id=:id")
                    .setParameter("id", 10)
                    .getSingleResult();
            return result != null;
    }
}
