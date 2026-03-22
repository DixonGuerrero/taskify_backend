package com.taskify.taskifyApi.infrastructure.output.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name ="users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    private String password;
    private String phone;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isEnabled = true;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean accountNonExpired = true;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean accountNonLocked = true;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean credentialsNonExpired = true;

    @ManyToOne(targetEntity = ImageEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ImageEntity image;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn (name = "role_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private RoleEntity role;


}
