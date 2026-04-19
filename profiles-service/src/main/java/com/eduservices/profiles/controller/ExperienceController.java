package com.eduservices.profiles.controller;

import com.eduservices.profiles.entity.ExperienceDetails;
import com.eduservices.profiles.service.ExperienceService;
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

@Tag(name = "Experience Details", description = "This is experience details apis")
@RestController
@RequestMapping("/experience")
public class ExperienceController {

    private final ExperienceService experienceService;

    public ExperienceController(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }

    @Operation(summary = "Fetch All", description = "Get all experience details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }, description = "Data retrieve successfully!"),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }, description = "Data not found!"),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }, description = "Internal server error!")
    })
    @GetMapping
    public ResponseEntity<List<ExperienceDetails>> getAll() {
        List<ExperienceDetails> experiences = experienceService.findAll();
        return ResponseEntity.ok(experiences);
    }

    @Operation(summary = "Fetch One", description = "Get experience details by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }, description = "Data retrieve successfully!"),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }, description = "Data not found!"),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }, description = "Internal server error!")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ExperienceDetails> getById(@PathVariable Long id) {
        ExperienceDetails experience = experienceService.findById(id);
        if (experience != null) {
            return ResponseEntity.ok(experience);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create", description = "Create new experience details")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema()) }, description = "Experience created successfully!"),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }, description = "Bad request!"),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }, description = "Internal server error!")
    })
    @PostMapping
    public ResponseEntity<ExperienceDetails> create(@Valid @RequestBody ExperienceDetails experience) {
        ExperienceDetails created = experienceService.save(experience);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Update", description = "Update existing experience details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }, description = "Experience updated successfully!"),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }, description = "Bad request!"),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }, description = "Data not found!"),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }, description = "Internal server error!")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ExperienceDetails> update(@PathVariable Long id, @Valid @RequestBody ExperienceDetails experience) {
        ExperienceDetails updated = experienceService.update(id, experience);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete", description = "Delete experience details by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }, description = "Experience deleted successfully!"),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }, description = "Data not found!"),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }, description = "Internal server error!")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (experienceService.existsById(id)) {
            experienceService.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
