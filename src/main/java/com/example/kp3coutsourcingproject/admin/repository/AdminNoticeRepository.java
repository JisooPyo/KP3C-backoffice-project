package com.example.kp3coutsourcingproject.admin.repository;


import com.example.kp3coutsourcingproject.admin.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminNoticeRepository extends JpaRepository<Notice,Long> {

}
