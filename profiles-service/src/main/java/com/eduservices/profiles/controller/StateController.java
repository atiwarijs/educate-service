package com.eduservices.profiles.controller;

import com.eduservices.profiles.entity.State;
import com.eduservices.profiles.service.StateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "State and city Details", description = "This is location details apis")
@RestController
@RequestMapping("/states")
public class StateController {

    private final StateService stateService;

    public StateController(StateService stateService) {
        this.stateService = stateService;
    }

    @Operation(summary = "Fetch All", description = "Get all states")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }, description = "Data retrieve successfully!"),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }, description = "Data not found!"),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }, description = "Internal server error!")
    })
    @GetMapping
    public ResponseEntity<List<State>> getAll() {
        List<State> states = stateService.findAll();
        return ResponseEntity.ok(states);
    }

    @Operation(summary = "Fetch One", description = "Get state by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }, description = "Data retrieve successfully!"),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }, description = "Data not found!"),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }, description = "Internal server error!")
    })
    @GetMapping("/{id}")
    public ResponseEntity<State> getById(@PathVariable Long id) {
        State state = stateService.findById(id);
        if (state != null) {
            return ResponseEntity.ok(state);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create", description = "Create new state")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema()) }, description = "State created successfully!"),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }, description = "Bad request!"),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }, description = "Internal server error!")
    })
    @PostMapping
    public ResponseEntity<State> create(@Valid @RequestBody State state) {
        State created = stateService.save(state);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Update", description = "Update existing state")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }, description = "State updated successfully!"),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }, description = "Bad request!"),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }, description = "Data not found!"),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }, description = "Internal server error!")
    })
    @PutMapping("/{id}")
    public ResponseEntity<State> update(@PathVariable Long id, @Valid @RequestBody State state) {
        State updated = stateService.update(id, state);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete", description = "Delete state by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }, description = "State deleted successfully!"),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }, description = "Data not found!"),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }, description = "Internal server error!")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (stateService.existsById(id)) {
            stateService.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
