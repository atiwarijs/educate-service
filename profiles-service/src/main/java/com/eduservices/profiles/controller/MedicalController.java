package com.eduservices.profiles.controller;

import com.eduservices.profiles.entity.MedicalDetails;
import com.eduservices.profiles.service.MedicalService;
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

@Tag(name = "Medical Details", description = "This is medial details apis")
@RestController
@RequestMapping("/medical")
public class MedicalController {

    private final MedicalService medicalService;

    public MedicalController(MedicalService medicalService) {
        this.medicalService = medicalService;
    }

    @Operation(summary = "Fetch All", description = "Get all medical details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }, description = "Data retrieve successfully!"),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }, description = "Data not found!"),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }, description = "Internal server error!")
    })
    @GetMapping
    public ResponseEntity<List<MedicalDetails>> getAll() {
        List<MedicalDetails> medicals = medicalService.findAll();
        return ResponseEntity.ok(medicals);
    }

    @Operation(summary = "Fetch One", description = "Get medical details by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }, description = "Data retrieve successfully!"),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }, description = "Data not found!"),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }, description = "Internal server error!")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MedicalDetails> getById(@PathVariable Long id) {
        MedicalDetails medical = medicalService.findById(id);
        if (medical != null) {
            return ResponseEntity.ok(medical);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create", description = "Create new medical details")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema()) }, description = "Medical created successfully!"),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }, description = "Bad request!"),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }, description = "Internal server error!")
    })
    @PostMapping
    public ResponseEntity<MedicalDetails> create(@Valid @RequestBody MedicalDetails medical) {
        MedicalDetails created = medicalService.save(medical);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Update", description = "Update existing medical details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }, description = "Medical updated successfully!"),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }, description = "Bad request!"),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }, description = "Data not found!"),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }, description = "Internal server error!")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MedicalDetails> update(@PathVariable Long id, @Valid @RequestBody MedicalDetails medical) {
        MedicalDetails updated = medicalService.update(id, medical);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete", description = "Delete medical details by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }, description = "Medical deleted successfully!"),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }, description = "Data not found!"),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }, description = "Internal server error!")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (medicalService.existsById(id)) {
            medicalService.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
