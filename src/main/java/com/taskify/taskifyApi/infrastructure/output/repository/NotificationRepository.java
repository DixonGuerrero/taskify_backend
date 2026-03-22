package com.taskify.taskifyApi.infrastructure.output.repository;

import com.taskify.taskifyApi.infrastructure.output.entity.NotificationEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    @Query("SELECT n FROM NotificationEntity n WHERE n.user.id = :userId AND n.isRead = false")
    List<NotificationEntity> findByUserIdAndIsRead(Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE NotificationEntity n SET n.isRead = true WHERE n.id = :id")
    void markAsReadById(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE NotificationEntity n SET n.isRead = true WHERE n.user.id = :userId")
    void markAllAsReadByUserId(Long userId);
}