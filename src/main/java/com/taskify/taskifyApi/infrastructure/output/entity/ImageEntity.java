package com.taskify.taskifyApi.infrastructure.output.entity;

import com.taskify.taskifyApi.domain.enums.ImageType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "image")
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    @Enumerated(EnumType.STRING)
    @Column(length = 15)
    private ImageType type;


}
