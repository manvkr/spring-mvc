package com.example.mvc.repository;

import com.example.mvc.model.Severity;
import com.example.mvc.model.Bug;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class BugRepository {

    @PersistenceContext
    private EntityManager em;

    public List<Bug> findAll() {
        return em.createQuery("SELECT b FROM Bug b ORDER BY b.createdAt DESC", Bug.class).getResultList();
    }

    public List<Bug> findBySeverity(Severity severity) {
        return em.createQuery("SELECT b FROM Bug b WHERE b.severity = :severity ORDER BY b.createdAt DESC", Bug.class)
                .setParameter("severity", severity)
                .getResultList();
    }

    public Bug save(Bug bug) {
        if (bug.getId() == null) {
            em.persist(bug);
            return bug;
        } else {
            return em.merge(bug);
        }
    }
}