package com.taskify.taskifyApi.infrastructure.output.adapters;

import com.taskify.taskifyApi.application.ports.output.FilePersistencePort;
import com.taskify.taskifyApi.domain.model.File;
import com.taskify.taskifyApi.infrastructure.output.mapper.FilePersistenceMapper;
import com.taskify.taskifyApi.infrastructure.output.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FilePersistenceAdapter implements FilePersistencePort {

    private final FileRepository repository;
    private final FilePersistenceMapper mapper;

    @Override
    public Optional<File> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toFile);
    }

    @Override
    public File save(File file) {
        return mapper.toFile(
                repository.save(
                        mapper.toFileEntity(file)
                )
        );
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}