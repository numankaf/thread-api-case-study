package com.threadserver.thread;

import com.threadserver.dto.thread.ThreadCreateDto;
import com.threadserver.dto.thread.ThreadUpdateDto;
import com.threadserver.entity.ThreadEntity;
import com.threadserver.enums.ThreadType;
import com.threadserver.repository.ThreadRepository;
import com.threadserver.service.ThreadMapProviderService;
import com.threadserver.service.ThreadService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@SpringBootTest()
public class ThreadServiceTests {
    @Autowired
    private ThreadRepository threadRepository;

    @Autowired
    private ThreadService threadService;

    @Autowired
    private ThreadMapProviderService threadMapProviderService;

    @AfterEach
    public void tearDown() throws Exception {
        threadRepository.deleteAll();
    }
    @Test
    public void shouldCreateThreadS() {
        var threadCreateDto = new ThreadCreateDto();
        threadCreateDto.setThreadNumber(2);
        threadCreateDto.setThreadType(ThreadType.SENDER);

        var entities = threadService.createThreads(threadCreateDto);
        assertThat(entities).isNotNull();
        assertThat(entities.size()).isEqualTo(2);
        entities.forEach((entity -> {
            assertThat(entity.getIsActive()).isEqualTo(true);
            assertThat(entity.getPriority()).isEqualTo(1);
            assertThat(entity.getType()).isEqualTo(ThreadType.SENDER);
            assertThat(threadMapProviderService.getThread(entity.getId())).isNotNull();
        }));

    }

    @Test
    public void shouldDeleteThreadById() {
        var threadCreateDto = new ThreadCreateDto();
        threadCreateDto.setThreadNumber(1);
        threadCreateDto.setThreadType(ThreadType.SENDER);

        var entities = threadService.createThreads(threadCreateDto);

        assertThat(entities).isNotNull();
        assertThat(entities.size()).isEqualTo(1);

        threadService.deleteThread(entities.get(0).getId());
        var maybeThread =  threadRepository.findById(entities.get(0).getId());
        assertThat(maybeThread).isEqualTo(Optional.empty());

    }

    @Test
    public void shouldReturnAllThreads() {
        var threadCreateDto = new ThreadCreateDto();
        threadCreateDto.setThreadNumber(2);
        threadCreateDto.setThreadType(ThreadType.RECEIVER);
        threadService.createThreads(threadCreateDto);

        var entities = threadService.findAllThreads();
        assertThat(entities).isNotNull();
        assertThat(entities.size()).isEqualTo(2);

    }

    @Test
    public void shouldStartThread() {
        var threadEntity = new ThreadEntity();
        threadEntity.setIsActive(true);
        threadEntity.setPriority(5);
        threadEntity.setType(ThreadType.RECEIVER);
        var savedEntity = threadRepository.save(threadEntity);

        threadService.startThread(threadEntity);

        var thread = threadMapProviderService.getThread(savedEntity.getId());
        assertThat(thread.isAlive()).isEqualTo(true);
        assertThat(thread.getName()).isEqualTo("Thread "+ savedEntity.getId());
        assertThat(thread.getPriority()).isEqualTo(savedEntity.getPriority());
    }


    @Test
    public void shouldUpdateThreadById() {
        var threadEntity = new ThreadEntity();
        threadEntity.setIsActive(true);
        threadEntity.setPriority(5);
        threadEntity.setType(ThreadType.RECEIVER);
        var savedEntity = threadRepository.save(threadEntity);
        threadService.startThread(threadEntity);

        ThreadUpdateDto threadUpdateDto = new ThreadUpdateDto();
        threadUpdateDto.setIsActive(false);
        threadUpdateDto.setPriority(2);

        threadService.updateThread(savedEntity.getId(),threadUpdateDto);

        var updatedEntity = threadRepository.findById(savedEntity.getId());

        assertThat(updatedEntity).isNotEmpty();
        assertThat(updatedEntity.get().getIsActive()).isEqualTo(false);
        assertThat(updatedEntity.get().getPriority()).isEqualTo(2);

        var thread = threadMapProviderService.getThread(savedEntity.getId());
        assertThat(thread.isAlive()).isEqualTo(false);
        assertThat(thread.getPriority()).isEqualTo(2);

    }


}
