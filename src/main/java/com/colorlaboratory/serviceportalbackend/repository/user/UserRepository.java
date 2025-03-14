package com.colorlaboratory.serviceportalbackend.repository.user;

import com.colorlaboratory.serviceportalbackend.model.entity.user.Role;
import com.colorlaboratory.serviceportalbackend.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByRoleIn(List<Role> roles);

    List<User> findByRole(Role role);

    @Query("SELECT u FROM User u LEFT JOIN u.issues i " +
            "WHERE u.role = :role " +
            "AND (:minIssues IS NULL OR SIZE(u.issues) >= :minIssues) " +
            "AND (:maxIssues IS NULL OR SIZE(u.issues) <= :maxIssues) " +
            "AND (:company IS NULL OR u.companyName LIKE %:company%) " +
            "AND (:name IS NULL OR u.firstName LIKE %:name% OR u.lastName LIKE %:name%) " +
            "ORDER BY " +
            "CASE WHEN :sortBy = 'name' AND :order = 'asc' THEN u.firstName END ASC, " +
            "CASE WHEN :sortBy = 'name' AND :order = 'desc' THEN u.firstName END DESC")
    List<User> findUsersWithFilters(@Param("role") Role role,
                                    @Param("sortBy") String sortBy,
                                    @Param("order") String order,
                                    @Param("minIssues") Integer minIssues,
                                    @Param("maxIssues") Integer maxIssues,
                                    @Param("company") String company,
                                    @Param("name") String name);
}
