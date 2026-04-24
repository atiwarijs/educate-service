package com.eduservices.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import com.eduservices.repo.GenericRepository;
import com.eduservices.service.GenericService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

public abstract class GenericController<D, E, ID extends Serializable> {

    private final GenericService<D, E, ID> genericService;

    protected GenericController(
            GenericRepository<E, ID> genericRepository,
            ModelMapper mapper,
            Class<D> dtoClass,
            Class<E> entityClass) {
        this.genericService = new GenericService<>(genericRepository, mapper, dtoClass, entityClass);
    }


    @Operation(summary = "Fetch All", description = "Get all data list")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = { @Content(schema = @Schema())}, description = "Data retrieve successfully!" ),
            @ApiResponse(responseCode = "404",content = {@Content(schema = @Schema())}, description = "Data not found!" ),
            @ApiResponse(responseCode = "500",content = {@Content(schema = @Schema())}, description = "Internal server error!" )
    })
    @GetMapping("/list")
    public ResponseEntity<?> getAllList(){
            return new ResponseEntity<>(genericService.findAll(), HttpStatus.OK);
    }

    @Operation(summary = "Fetch All", description = "Get all data by pagination.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = { @Content(schema = @Schema())}, description = "Data retrieve successfully!" ),
            @ApiResponse(responseCode = "404",content = {@Content(schema = @Schema())}, description = "Data not found!" ),
            @ApiResponse(responseCode = "500",content = {@Content(schema = @Schema())}, description = "Internal server error!" )
    })
    @GetMapping
    public ResponseEntity<?> getAllByPage(
            @RequestParam(name="page",defaultValue = "1", required = true) Integer page,
            @RequestParam(name="size",defaultValue = "10", required = true) Integer size,
            @RequestParam(name="sort",defaultValue = "id,asc", required = true) String[] sort){

            return ResponseEntity.ok(genericService.findAll(setPageRequest(page, size,sort)));
    }

    @Operation(summary = "Fetch One", description = "Get data by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema())}, description = "Data retrieve successfully!" ),
            @ApiResponse(responseCode = "404",content = {@Content(schema = @Schema())}, description = "Data not found!" ),
            @ApiResponse(responseCode = "500",content = {@Content(schema = @Schema())}, description = "Internal server error!" )
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getOneById(@PathVariable ID id){
        return new ResponseEntity<D>(genericService.findById(id), HttpStatus.OK);
    }

    @Operation(summary = "Create", description = "Create new data")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema())}, description = "Data created successfully!" ),
            @ApiResponse(responseCode = "400",content = {@Content(schema = @Schema())}, description = "Bad request!" ),
            @ApiResponse(responseCode = "500",content = {@Content(schema = @Schema())}, description = "Internal server error!" )
    })
    @PostMapping
    public ResponseEntity<D> save(@RequestBody @Valid D requestDto){
        return new ResponseEntity<>(genericService.save(requestDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Update", description = "Update existing data")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema())}, description = "Data updated successfully!" ),
            @ApiResponse(responseCode = "400",content = {@Content(schema = @Schema())}, description = "Bad request!" ),
            @ApiResponse(responseCode = "404",content = {@Content(schema = @Schema())}, description = "Data not found!" ),
            @ApiResponse(responseCode = "500",content = {@Content(schema = @Schema())}, description = "Internal server error!" )
    })
    @PutMapping
    public ResponseEntity<D> update(@RequestBody @Valid D requestDto){
        return new ResponseEntity<>(genericService.save(requestDto), HttpStatus.OK);
    }

    @Operation(summary = "Delete One", description = "Delete by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema())}, description = "Data delete successfully!" ),
            @ApiResponse(responseCode = "400",content = {@Content(schema = @Schema())}, description = "Bad request!" ),
            @ApiResponse(responseCode = "404",content = {@Content(schema = @Schema())}, description = "Data not found!" ),
            @ApiResponse(responseCode = "500",content = {@Content(schema = @Schema())}, description = "Internal server error!" )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable ID id){
        genericService.deleteBuId(id);
        return ResponseEntity.ok("Data deleted successfully!");
    }

    private Pageable setPageRequest(Integer page, Integer size, String[] sort){
        if(page != null && size != null){
            String sortField = sort[0] == null ? "id" : sort[0];
            String sortDirection = sort[1] == null ? "asc" : sort[1];
            Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Sort.Order order = new Sort.Order(direction, sortField);
            return PageRequest.of(page-1, size, Sort.by(order));
        }
        throw new RuntimeException("Page number or page size not found in request query!");
    }

}
