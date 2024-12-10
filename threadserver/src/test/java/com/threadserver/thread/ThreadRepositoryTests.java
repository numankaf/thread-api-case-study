package com.threadserver.thread;


import com.threadserver.entity.ThreadEntity;
import com.threadserver.enums.ThreadType;
import com.threadserver.repository.ThreadRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ThreadRepositoryTests {
    @Autowired
    private ThreadRepository threadRepository;

    private ThreadEntity savedThread;

    @BeforeEach
    void setUp() {
        var thread = new ThreadEntity();
        thread.setType(ThreadType.SENDER);
        thread.setIsActive(true);
        thread.setPriority(5);
        ThreadEntity createdThread = threadRepository.save(thread);
        assertThat(createdThread).isNotNull();
        assertThat(createdThread.getType()).isEqualTo(ThreadType.SENDER);
        assertThat(createdThread.getIsActive()).isEqualTo(true);
        assertThat(createdThread.getPriority()).isEqualTo(5);
        savedThread = createdThread;

    }

    @AfterEach
    public void tearDown()  {
        threadRepository.deleteAll();
    }

    @Test
    public void shouldFindThreadById()  {
        var maybeThread = threadRepository.findById(savedThread.getId());
        assertThat(maybeThread).isNotEmpty();
        assertThat(maybeThread.get()).isEqualTo(savedThread);

    }

    @Test
    public void shouldFetchThreads()  {
        List<ThreadEntity> threadEntities = threadRepository.findAllByOrderByIdDesc();
        assertThat(threadEntities).isNotNull();
        assertThat(threadEntities).isNotEmpty();
    }

    @Test
    public void shouldUpdateThreadById()  {
        savedThread.setPriority(10);
        savedThread.setIsActive(false);
        savedThread.setType(ThreadType.RECEIVER);
        var updatedThread = threadRepository.save(savedThread);
        assertThat(updatedThread).isNotNull();
        assertThat(updatedThread.getType()).isEqualTo(ThreadType.RECEIVER);
        assertThat(updatedThread.getIsActive()).isEqualTo(false);
        assertThat(updatedThread.getPriority()).isEqualTo(10);
    }


}
