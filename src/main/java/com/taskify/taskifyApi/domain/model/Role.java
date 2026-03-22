package com.taskify.taskifyApi.domain.model;

import com.taskify.taskifyApi.domain.enums.RoleEnum;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    private Long id;
    private RoleEnum name;

}
