package com.taskify.taskifyApi.infrastructure.output.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notification")
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000)
    private String message;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE", nullable = false)
    private Boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity user;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}
