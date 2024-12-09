package com.threadserver.repository;

import com.threadserver.entity.ThreadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThreadRepository  extends JpaRepository<ThreadEntity, Long> {
    List<ThreadEntity> findAllByOrderByIdDesc();
}
