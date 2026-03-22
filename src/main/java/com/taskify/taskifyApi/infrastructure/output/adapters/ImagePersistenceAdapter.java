package com.taskify.taskifyApi.infrastructure.output.adapters;

import com.taskify.taskifyApi.application.ports.output.ImagePersistencePort;
import com.taskify.taskifyApi.domain.enums.ImageType;
import com.taskify.taskifyApi.domain.model.Image;
import com.taskify.taskifyApi.infrastructure.output.mapper.ImagePersistenceMapper;
import com.taskify.taskifyApi.infrastructure.output.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ImagePersistenceAdapter implements ImagePersistencePort {

    private final ImageRepository repository;
    @Qualifier("imagePersistenceMapper")
    private final ImagePersistenceMapper mapper;

    @Override
    public Optional<Image> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toImage);
    }

    @Override
    public List<Image> findAllByType(ImageType type) {
        return mapper.toImageList(repository.findAllByType(type));
    }

    @Override
    public List<Image> findAll() {
        return mapper.toImageList(repository.findAll());
    }

    @Override
    public Image save(Image image) {
        return mapper.toImage(
                repository.save(
                        mapper.toImageEntity(image)
                )
        );
    }

    @Override
    public void deleteByID(Long id) {
        repository.deleteById(id);
    }

}
