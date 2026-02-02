package com.stackdarker.platform.media.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MediaObjectRepository extends JpaRepository<MediaObjectEntity, UUID> {

    Optional<MediaObjectEntity> findByIdAndUserId(UUID id, UUID userId);
}
