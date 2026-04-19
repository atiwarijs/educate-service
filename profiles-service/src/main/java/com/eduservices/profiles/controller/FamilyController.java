package com.eduservices.profiles.controller;

import com.eduservices.profiles.entity.FamilyMember;
import com.eduservices.profiles.service.FamilyService;
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

@Tag(name = "Family Details", description = "This is family details apis")
@RestController
@RequestMapping("/family")
public class FamilyController {

    private final FamilyService familyService;

    public FamilyController(FamilyService familyService) {
        this.familyService = familyService;
    }

    @Operation(summary = "Fetch All", description = "Get all family members")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }, description = "Data retrieve successfully!"),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }, description = "Data not found!"),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }, description = "Internal server error!")
    })
    @GetMapping
    public ResponseEntity<List<FamilyMember>> getAll() {
        List<FamilyMember> families = familyService.findAll();
        return ResponseEntity.ok(families);
    }

    @Operation(summary = "Fetch One", description = "Get family member by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }, description = "Data retrieve successfully!"),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }, description = "Data not found!"),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }, description = "Internal server error!")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FamilyMember> getById(@PathVariable Long id) {
        FamilyMember family = familyService.findById(id);
        if (family != null) {
            return ResponseEntity.ok(family);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create", description = "Create new family member")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema()) }, description = "Family member created successfully!"),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }, description = "Bad request!"),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }, description = "Internal server error!")
    })
    @PostMapping
    public ResponseEntity<FamilyMember> create(@Valid @RequestBody FamilyMember family) {
        FamilyMember created = familyService.save(family);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Update", description = "Update existing family member")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }, description = "Family member updated successfully!"),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }, description = "Bad request!"),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }, description = "Data not found!"),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }, description = "Internal server error!")
    })
    @PutMapping("/{id}")
    public ResponseEntity<FamilyMember> update(@PathVariable Long id, @Valid @RequestBody FamilyMember family) {
        FamilyMember updated = familyService.update(id, family);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete", description = "Delete family member by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }, description = "Family member deleted successfully!"),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }, description = "Data not found!"),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }, description = "Internal server error!")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (familyService.existsById(id)) {
            familyService.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
