package com.example.kp3coutsourcingproject.common.file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Post_Image, Long>{
    List<Post_Image> findByPostId(Long postId);
}

