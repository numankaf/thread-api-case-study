package com.threadserver.controller;

import com.threadserver.common.HttpResponse;
import com.threadserver.dto.thread.ThreadCreateDto;
import com.threadserver.dto.thread.ThreadUpdateDto;
import com.threadserver.entity.ThreadEntity;
import com.threadserver.service.ThreadService;
import com.threadserver.util.HttpResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/thread")
@RequiredArgsConstructor
public class ThreadController {
    private final ThreadService threadService;

    @Operation(
            summary = "Retrieve all threads",
            description = "Fetches a list of all threads currently available in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved all threads",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ThreadEntity.class))
                            )
                    }
            )
    })
    @GetMapping()
    public ResponseEntity<List<ThreadEntity>> findAllThreads() {
        return ResponseEntity.ok(threadService.findAllThreads());
    }


    @Operation(
            summary = "Create new threads",
            description = "This endpoint creates new threads with the specified `threadNumber` and `threadType`. It returns a response confirming the creation of threads."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Threads are created successfully",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = HttpResponse.class
                                    ),
                                    examples = @ExampleObject(value = "{ \"message\": \"Threads are created successfully\", \"status\": \"CREATED\", \"statusCode\": \"201\", \"timestamp\": \"2024-12-08 16:19:29.166\" }")
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request, invalid input data",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = HttpResponse.class),
                                    examples = @ExampleObject(value = "{ \"message\": \"Invalid data provided\", \"status\": \"BAD_REQUEST\", \"statusCode\": \"400\", \"timestamp\": \"2024-12-08 16:19:29.166\" }")
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request. Validation failed for the input data",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = HttpResponse.class),
                                    examples = @ExampleObject(value = "{ \"message\": \"Validation failed for the input data\", \"status\": \"BAD_REQUEST\", \"statusCode\": \"400\", \"timestamp\": \"2024-12-08 16:19:29.166\" }")
                            )
                    }
            )
    })
    @PostMapping()
    public ResponseEntity<HttpResponse> createThreads(@Valid
                                                      @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                              description = "Provide the thread details to create new threads.",
                                                              required = true,
                                                              content = @Content(
                                                                      mediaType = "application/json",
                                                                      schema = @Schema(implementation = ThreadCreateDto.class),
                                                                      examples = @ExampleObject(value = "{ \"threadNumber\": \"5\", \"threadType\": \"SENDER\" }")
                                                              )
                                                      )
                                                      @RequestBody ThreadCreateDto threadCreateDto) {
        threadService.createThreads(threadCreateDto);
        return HttpResponseUtil.createHttpResponse(HttpStatus.CREATED, "Threads are created successfully");
    }


    @Operation(
            summary = "Update a thread by ID",
            description = "Updates the properties of a thread, such as its priority and active status. The request body should contain the updated values. If the thread is successfully updated, a 200 (OK) response is returned. If the thread is not found, a 404 (Not Found) response is returned."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Thread updated successfully",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = HttpResponse.class),
                                    examples = @ExampleObject(value = "{ \"message\": \"Thread is updated successfully\", \"status\": \"OK\", \"statusCode\": \"200\", \"timestamp\": \"2024-12-08 16:19:29.166\" }")
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Thread not found with the specified ID",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = HttpResponse.class),
                                    examples = @ExampleObject(value = "{ \"message\": \"Thread not found\", \"status\": \"NOT_FOUND\", \"statusCode\": \"404\", \"timestamp\": \"2024-12-08 16:19:29.166\" }")
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request. Validation failed for the input data",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = HttpResponse.class),
                                    examples = @ExampleObject(value = "{ \"message\": \"Validation failed for the input data\", \"status\": \"BAD_REQUEST\", \"statusCode\": \"400\", \"timestamp\": \"2024-12-08 16:19:29.166\" }")
                            )
                    }
            )
    })
    @PatchMapping("/{id}")
    public ResponseEntity<HttpResponse> updateThread(@PathVariable Long id,
                                                     @Valid
                                                     @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                             description = "Provide the thread details to update the thread.",
                                                             required = true,
                                                             content = @Content(
                                                                     mediaType = "application/json",
                                                                     schema = @Schema(implementation = ThreadUpdateDto.class),
                                                                     examples = @ExampleObject(value = "{ \"priority\": \"5\", \"isActive\": false }")
                                                             )
                                                     )
                                                     @RequestBody ThreadUpdateDto threadUpdateDto) {
        threadService.updateThread(id, threadUpdateDto);
        return HttpResponseUtil.createHttpResponse(HttpStatus.OK, "Thread is updated successfully");
    }

    @Operation(
            summary = "Delete a thread by ID",
            description = "Deletes the thread with the specified `id`. If the thread is found and successfully deleted, a 204 (No Content) response is returned. If the thread is not found, a 404 (Not Found) response is returned."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Thread deleted successfully. No content is returned.",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Thread not found with the specified ID",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = HttpResponse.class),
                                    examples = @ExampleObject(value = "{ \"message\": \"Thread not found with given id: 1\", \"status\": \"NOT_FOUND\", \"statusCode\": \"404\", \"timestamp\": \"2024-12-08 16:19:29.166\" }")
                            )
                    }
            ),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteThread(@PathVariable Long id) {
        threadService.deleteThread(id);
        return ResponseEntity.noContent().build();
    }


}
