package com.colorlaboratory.serviceportalbackend.repository.media;

import com.colorlaboratory.serviceportalbackend.model.entity.media.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
}
