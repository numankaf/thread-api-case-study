package com.threadserver.thread;

import com.threadserver.dto.thread.ThreadCreateDto;
import com.threadserver.dto.thread.ThreadUpdateDto;
import com.threadserver.entity.ThreadEntity;
import com.threadserver.enums.ThreadType;
import com.threadserver.repository.ThreadRepository;
import com.threadserver.service.ThreadService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ThreadE2ERestTests {

    @Autowired
    private ThreadRepository threadRepository;

    @Autowired
    private ThreadService threadService;

    @LocalServerPort
    private int port;


    private List<ThreadEntity> savedThreads;

    @BeforeEach
    void setUp() {
        ThreadCreateDto threadCreateDto = new ThreadCreateDto();
        threadCreateDto.setThreadNumber(2);
        threadCreateDto.setThreadType(ThreadType.SENDER);
        savedThreads = threadService.createThreads(threadCreateDto);
    }

    @AfterEach
    public void tearDown() throws Exception {
        threadRepository.deleteAll();
    }

    @Test
    public void shouldReturnAllThreads() {
        when()
                .get(String.format("http://localhost:%s/thread", port))
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test
    public void shouldCreateThread() {
        var threadCreateDto = new ThreadCreateDto();
        threadCreateDto.setThreadNumber(1);
        threadCreateDto.setThreadType(ThreadType.SENDER);

        given()
                .contentType("application/json")
                .body(threadCreateDto)
                .when()
                .post(String.format("http://localhost:%s/thread", port))
                .then()
                .statusCode(201)
                .body("message", containsString("Threads are created successfully"));
    }

    @Test
    public void shouldUpdateThread() {
        var threadUpdateDto = new ThreadUpdateDto();
        threadUpdateDto.setIsActive(false);
        threadUpdateDto.setPriority(3);

        given()
                .contentType("application/json")
                .body(threadUpdateDto)
                .when()
                .patch(String.format("http://localhost:%s/thread/%d", port, savedThreads.get(0).getId()))
                .then()
                .statusCode(200)
                .body("message", containsString("Thread is updated successfully"));

        var updatedThread = threadRepository.findById(savedThreads.get(0).getId());
        assertThat(updatedThread).isNotEmpty();
        assertThat(updatedThread.get().getIsActive()).isEqualTo(false);
        assertThat(updatedThread.get().getPriority()).isEqualTo(3);

    }

    @Test
    public void shouldDeleteThread() {
        given()
                .when()
                .delete(String.format("http://localhost:%s/thread/%d", port, savedThreads.get(0).getId()))
                .then()
                .statusCode(204);
        var maybeThread =  threadRepository.findById(savedThreads.get(0).getId());
        assertThat(maybeThread).isEqualTo(Optional.empty());
    }

    @Test
    public void shouldFailonDeleteReturnNotFoundForNonExistentThread() {
        given()
                .when()
                .delete(String.format("http://localhost:%s/thread/%d", port, 9999))
                .then()
                .statusCode(404)
                .body("message", containsString("Thread not found"));
    }
}
