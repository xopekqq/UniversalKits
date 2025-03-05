package ru.xopek.universalkits.database;

import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.bukkit.Bukkit;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class DatabaseMethods {
    /**
     * DatabaseWorker
     */
    public static UserEntity getUserEntityByName (Session sn , String queryName) {
        try {
            CriteriaBuilder criteria = sn.getCriteriaBuilder();
            CriteriaQuery<UserEntity> query = criteria.createQuery(UserEntity.class);

            Root<UserEntity> root = query.from(UserEntity.class);
            query.select(root);

            query.where(
                    criteria.equal(root.get("userName"), queryName)
            );
            Query<UserEntity> _query = sn.createQuery(query);

            return _query.getSingleResult();
        } catch (Exception cause) {
            /**
             * Skip since trash Hibernate makes catches :skull:
             */
            if(cause instanceof NoResultException || cause instanceof NonUniqueResultException)
                return null;

            Bukkit.getLogger().info("Database getUserEntity error");
            Bukkit.getLogger().info("Player: " + queryName);

            cause.printStackTrace();
            return null;
        }
    }
}
