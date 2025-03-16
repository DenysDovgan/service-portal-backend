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

    @Query("SELECT u FROM User u LEFT JOIN u.assignedIssues a " +
            "WHERE (:role IS NULL OR u.role = :role) " +
            "AND (:name IS NULL OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:email IS NULL OR u.email LIKE %:email%) " +
            "AND (:phoneNumber IS NULL OR u.phoneNumber LIKE %:phoneNumber%) " +
            "AND (:company IS NULL OR u.companyName LIKE %:company%) " +
            "AND (:city IS NULL OR u.city LIKE %:city%) " +
            "AND (:country IS NULL OR u.country LIKE %:country%) " +
            "AND (:minAssignedIssues IS NULL OR SIZE(u.assignedIssues) >= :minAssignedIssues) " +
            "ORDER BY " +
            "CASE WHEN :sortBy = 'name' AND :order = 'asc' THEN u.firstName END ASC, " +
            "CASE WHEN :sortBy = 'name' AND :order = 'desc' THEN u.firstName END DESC, " +
            "CASE WHEN :sortBy = 'email' AND :order = 'asc' THEN u.email END ASC, " +
            "CASE WHEN :sortBy = 'email' AND :order = 'desc' THEN u.email END DESC, " +
            "CASE WHEN :sortBy = 'assignedIssues' AND :order = 'asc' THEN SIZE(u.assignedIssues) END ASC, " +
            "CASE WHEN :sortBy = 'assignedIssues' AND :order = 'desc' THEN SIZE(u.assignedIssues) END DESC")
    List<User> filterUsers(@Param("role") Role role,
                           @Param("sortBy") String sortBy,
                           @Param("order") String order,
                           @Param("name") String name,
                           @Param("email") String email,
                           @Param("phoneNumber") String phoneNumber,
                           @Param("company") String company,
                           @Param("city") String city,
                           @Param("country") String country,
                           @Param("minAssignedIssues") Integer minAssignedIssues);

}
