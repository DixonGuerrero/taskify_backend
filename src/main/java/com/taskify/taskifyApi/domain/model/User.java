package com.taskify.taskifyApi.domain.model;

import lombok.*;
import java.util.List;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private String phone;

    private Boolean isEnabled;
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;

    private Image image;
    private Role role;

    private List<File> ownedFiles;

    public long getTotalStorageUsed() {
        if (ownedFiles == null) return 0L;
        return ownedFiles.stream()
                .mapToLong(File::getFileSize)
                .sum();
    }
}