package com.taskify.taskifyApi.infrastructure.output.entity;

import com.taskify.taskifyApi.domain.enums.TaskPriority;
import com.taskify.taskifyApi.domain.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "task")
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Column(columnDefinition = "DATE")
    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity assigned;

    @ManyToOne(targetEntity = ProjectEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProjectEntity project;


}
