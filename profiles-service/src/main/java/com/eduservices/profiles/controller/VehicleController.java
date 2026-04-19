package com.eduservices.profiles.controller;

import com.eduservices.profiles.entity.VehicleDetails;
import com.eduservices.profiles.service.VehicleService;
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

@Tag(name = "Vehicle Details", description = "This is vehicle details apis")
@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @Operation(summary = "Fetch All", description = "Get all vehicle details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }, description = "Data retrieve successfully!"),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }, description = "Data not found!"),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }, description = "Internal server error!")
    })
    @GetMapping
    public ResponseEntity<List<VehicleDetails>> getAll() {
        List<VehicleDetails> vehicles = vehicleService.findAll();
        return ResponseEntity.ok(vehicles);
    }

    @Operation(summary = "Fetch One", description = "Get vehicle details by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }, description = "Data retrieve successfully!"),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }, description = "Data not found!"),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }, description = "Internal server error!")
    })
    @GetMapping("/{id}")
    public ResponseEntity<VehicleDetails> getById(@PathVariable Long id) {
        VehicleDetails vehicle = vehicleService.findById(id);
        if (vehicle != null) {
            return ResponseEntity.ok(vehicle);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create", description = "Create new vehicle details")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema()) }, description = "Vehicle created successfully!"),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }, description = "Bad request!"),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }, description = "Internal server error!")
    })
    @PostMapping
    public ResponseEntity<VehicleDetails> create(@Valid @RequestBody VehicleDetails vehicle) {
        VehicleDetails created = vehicleService.save(vehicle);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Update", description = "Update existing vehicle details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }, description = "Vehicle updated successfully!"),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }, description = "Bad request!"),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }, description = "Data not found!"),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }, description = "Internal server error!")
    })
    @PutMapping("/{id}")
    public ResponseEntity<VehicleDetails> update(@PathVariable Long id, @Valid @RequestBody VehicleDetails vehicle) {
        VehicleDetails updated = vehicleService.update(id, vehicle);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete", description = "Delete vehicle details by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }, description = "Vehicle deleted successfully!"),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }, description = "Data not found!"),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }, description = "Internal server error!")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (vehicleService.existsById(id)) {
            vehicleService.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
