package com.taskify.taskifyApi.infrastructure.output.repository;


import com.taskify.taskifyApi.infrastructure.output.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity,Long> {
}
