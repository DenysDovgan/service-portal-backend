package com.colorlaboratory.serviceportalbackend.repository.media;

import com.colorlaboratory.serviceportalbackend.model.entity.media.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
    List<Media> findAllByIssue_Id(Long issueId);
}
