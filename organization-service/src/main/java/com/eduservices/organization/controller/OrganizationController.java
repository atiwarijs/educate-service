package com.eduservices.organization.controller;

import com.eduservices.organization.controller.dto.OrganizationDto;
import com.eduservices.organization.controller.dto.SearchDto;
import com.eduservices.organization.entity.OrganizationEntity;
import com.eduservices.organization.repo.OrganizationRepository;
import com.eduservices.organization.service.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import com.eduservices.organization.exception.AlreadyExistException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Organizations", description = "Organizations management api" )
@RestController
@RequestMapping("/organizations")
public class OrganizationController {

    @Autowired
    OrganizationService organizationService;
    
    private final OrganizationRepository organizationRepository;
    private final ModelMapper modelMapper;

    public OrganizationController(OrganizationRepository organizationRepository, ModelMapper modelMapper) {
        this.organizationRepository = organizationRepository;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Fetch All", description = "Get all data list")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = { @Content(schema = @Schema())}, description = "Data retrieve successfully!" ),
            @ApiResponse(responseCode = "404",content = {@Content(schema = @Schema())}, description = "Data not found!" ),
            @ApiResponse(responseCode = "500",content = {@Content(schema = @Schema())}, description = "Internal server error!" )
    })
    @GetMapping("/list")
    public ResponseEntity<List<OrganizationDto>> getAllList(){
        List<OrganizationEntity> organizations = organizationRepository.findAll();
        List<OrganizationDto> organizationDTOs = organizations.stream()
                .map(organization -> modelMapper.map(organization, OrganizationDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(organizationDTOs, HttpStatus.OK);
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
        List<OrganizationEntity> organizations = organizationRepository.findAll(pageable).getContent();
        List<OrganizationDto> organizationDTOs = organizations.stream()
                .map(organization -> modelMapper.map(organization, OrganizationDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(organizationDTOs);
    }

    @Operation(summary = "Fetch One", description = "Get data by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema())}, description = "Data retrieve successfully!" ),
            @ApiResponse(responseCode = "404",content = {@Content(schema = @Schema())}, description = "Data not found!" ),
            @ApiResponse(responseCode = "500",content = {@Content(schema = @Schema())}, description = "Internal server error!" )
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrganizationDto> getOneById(@PathVariable Long id){
        OrganizationEntity organization = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found with id: " + id));
        OrganizationDto organizationDTO = modelMapper.map(organization, OrganizationDto.class);
        return new ResponseEntity<>(organizationDTO, HttpStatus.OK);
    }

    @Operation(summary = "Create", description = "Create new data")
    @ApiResponses({
            @ApiResponse(responseCode = "201",content = { @Content(schema = @Schema())}, description = "Data created successfully!" ),
            @ApiResponse(responseCode = "400",content = {@Content(schema = @Schema())}, description = "Bad request!" ),
            @ApiResponse(responseCode = "500",content = {@Content(schema = @Schema())}, description = "Internal server error!" )
    })
    @PostMapping
    public ResponseEntity<OrganizationDto> save(@Valid @RequestBody OrganizationDto requestDto) {
        if(!ObjectUtils.isEmpty(organizationService.findByOrgCode(requestDto.getOrgCode()))){
            throw new AlreadyExistException("Organization code already exist!");
        } else if(!ObjectUtils.isEmpty(organizationService.findByOrgEmailId(requestDto.getOrgEmailId()))){
            throw new AlreadyExistException("Organization email id already exist!");
        } else if(!ObjectUtils.isEmpty(organizationService.findByOrgPhoneNo(requestDto.getOrgPhoneNo()))){
            throw new AlreadyExistException("Organization phone already exist!");
        }
        
        OrganizationEntity organization = modelMapper.map(requestDto, OrganizationEntity.class);
        OrganizationEntity savedOrganization = organizationRepository.save(organization);
        OrganizationDto responseDto = modelMapper.map(savedOrganization, OrganizationDto.class);
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
    public ResponseEntity<OrganizationDto> update(@Valid @RequestBody OrganizationDto requestDto){
        OrganizationEntity existingOrganization = organizationRepository.findById(requestDto.getId())
                .orElseThrow(() -> new RuntimeException("Organization not found with id: " + requestDto.getId()));
        
        modelMapper.map(requestDto, existingOrganization);
        OrganizationEntity updatedOrganization = organizationRepository.save(existingOrganization);
        OrganizationDto responseDto = modelMapper.map(updatedOrganization, OrganizationDto.class);
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
        organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found with id: " + id));
        organizationRepository.deleteById(id);
        return ResponseEntity.ok("Data deleted successfully!");
    }

    /* Search organizations by type status or parent id */
    @PostMapping("/search/list")
    public List<OrganizationDto> getOrganizationsList(@RequestBody SearchDto dto){
            return organizationService.getOrgDetailsList(dto);
    }

    /* Search organizations by org code,  email or phone */
    @PostMapping("/search")
    public OrganizationDto getOrganizationDetails(@RequestBody SearchDto dto){
        return organizationService.getOrgDetails(dto);
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
