package com.eduservices.enrollment.controller;

import com.eduservices.enrollment.repository.EnrollmentRepository;
import com.eduservices.enrollment.dto.EnrollmentDTO;
import com.eduservices.enrollment.entity.EnrollmentEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "enrollment Details", description = "This is enrollment details api to manage students and teachers association")
@RestController
@RequestMapping("/enrollment")
public class EnrollmentController {

    private final EnrollmentRepository enrollmentRepository;
    private final ModelMapper modelMapper;

    public EnrollmentController(EnrollmentRepository enrollmentRepository, ModelMapper modelMapper) {
        this.enrollmentRepository = enrollmentRepository;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Fetch All", description = "Get all data list")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = { @Content(schema = @Schema())}, description = "Data retrieve successfully!" ),
            @ApiResponse(responseCode = "404",content = {@Content(schema = @Schema())}, description = "Data not found!" ),
            @ApiResponse(responseCode = "500",content = {@Content(schema = @Schema())}, description = "Internal server error!" )
    })
    @GetMapping("/list")
    public ResponseEntity<List<EnrollmentDTO>> getAllList(){
        List<EnrollmentEntity> enrollments = enrollmentRepository.findAll();
        List<EnrollmentDTO> enrollmentDTOs = enrollments.stream()
                .map(enrollment -> modelMapper.map(enrollment, EnrollmentDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(enrollmentDTOs, HttpStatus.OK);
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

        Pageable pageable = setPageRequest(page, size, sort);
        List<EnrollmentEntity> enrollments = enrollmentRepository.findAll(pageable).getContent();
        List<EnrollmentDTO> enrollmentDTOs = enrollments.stream()
                .map(enrollment -> modelMapper.map(enrollment, EnrollmentDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(enrollmentDTOs);
    }

    @Operation(summary = "Fetch One", description = "Get data by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema())}, description = "Data retrieve successfully!" ),
            @ApiResponse(responseCode = "404",content = {@Content(schema = @Schema())}, description = "Data not found!" ),
            @ApiResponse(responseCode = "500",content = {@Content(schema = @Schema())}, description = "Internal server error!" )
    })
    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentDTO> getOneById(@PathVariable Long id){
        EnrollmentEntity enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment not found with id: " + id));
        EnrollmentDTO enrollmentDTO = modelMapper.map(enrollment, EnrollmentDTO.class);
        return new ResponseEntity<>(enrollmentDTO, HttpStatus.OK);
    }

    @Operation(summary = "Create", description = "Create new data")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema())}, description = "Data created successfully!" ),
            @ApiResponse(responseCode = "400",content = {@Content(schema = @Schema())}, description = "Bad request!" ),
            @ApiResponse(responseCode = "500",content = {@Content(schema = @Schema())}, description = "Internal server error!" )
    })
    @PostMapping
    public ResponseEntity<EnrollmentDTO> save(@RequestBody @Valid EnrollmentDTO requestDto){
        EnrollmentEntity enrollment = modelMapper.map(requestDto, EnrollmentEntity.class);
        EnrollmentEntity savedEnrollment = enrollmentRepository.save(enrollment);
        EnrollmentDTO responseDto = modelMapper.map(savedEnrollment, EnrollmentDTO.class);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @Operation(summary = "Update", description = "Update existing data")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema())}, description = "Data updated successfully!" ),
            @ApiResponse(responseCode = "400",content = {@Content(schema = @Schema())}, description = "Bad request!" ),
            @ApiResponse(responseCode = "404",content = {@Content(schema = @Schema())}, description = "Data not found!" ),
            @ApiResponse(responseCode = "500",content = {@Content(schema = @Schema())}, description = "Internal server error!" )
    })
    @PutMapping
    public ResponseEntity<EnrollmentDTO> update(@RequestBody @Valid EnrollmentDTO requestDto){
        EnrollmentEntity existingEnrollment = enrollmentRepository.findById(requestDto.getId())
                .orElseThrow(() -> new RuntimeException("Enrollment not found with id: " + requestDto.getId()));
        
        modelMapper.map(requestDto, existingEnrollment);
        EnrollmentEntity updatedEnrollment = enrollmentRepository.save(existingEnrollment);
        EnrollmentDTO responseDto = modelMapper.map(updatedEnrollment, EnrollmentDTO.class);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "Delete One", description = "Delete by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema())}, description = "Data delete successfully!" ),
            @ApiResponse(responseCode = "400",content = {@Content(schema = @Schema())}, description = "Bad request!" ),
            @ApiResponse(responseCode = "404",content = {@Content(schema = @Schema())}, description = "Data not found!" ),
            @ApiResponse(responseCode = "500",content = {@Content(schema = @Schema())}, description = "Internal server error!" )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment not found with id: " + id));
        enrollmentRepository.deleteById(id);
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
