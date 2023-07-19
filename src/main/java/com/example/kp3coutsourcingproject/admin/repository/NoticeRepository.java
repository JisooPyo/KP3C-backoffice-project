package com.example.kp3coutsourcingproject.admin.repository;


import com.example.kp3coutsourcingproject.admin.entity.Notice;
import com.example.kp3coutsourcingproject.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice,Long> {

}
