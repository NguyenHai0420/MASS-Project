package com.group_project.MASS.repository;

import com.group_project.MASS.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    // Find all notifications for a specific user, ordered by creation date in descending order
    Page<Notification> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable page);

    // Find all unread notifications for a specific user, ordered by creation date in descending order
    List<Notification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(Long userId);

    // Count the number of unread notifications for a specific user
    Long countByUserIdAndIsReadFalse(Long userId);

    // Find a notification by its ID and user ID
    Optional<Notification> findByIdAndUserId(Long notificationId, Long userId);
}
