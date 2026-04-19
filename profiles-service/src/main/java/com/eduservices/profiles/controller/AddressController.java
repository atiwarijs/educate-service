package com.eduservices.profiles.controller;

import com.eduservices.profiles.entity.AddressDetails;
import com.eduservices.profiles.repo.AddressRepository;
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

@Tag(name = "Address Details", description = "This is address details apis")
@RestController
@RequestMapping("/address")
public class AddressController {

    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;

    public AddressController(AddressRepository addressRepository, ModelMapper modelMapper) {
        this.addressRepository = addressRepository;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Fetch All", description = "Get all data list")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = { @Content(schema = @Schema())}, description = "Data retrieve successfully!" ),
            @ApiResponse(responseCode = "404",content = {@Content(schema = @Schema())}, description = "Data not found!" ),
            @ApiResponse(responseCode = "500",content = {@Content(schema = @Schema())}, description = "Internal server error!" )
    })
    @GetMapping("/list")
    public ResponseEntity<List<AddressDetails>> getAllList(){
        List<AddressDetails> addresses = addressRepository.findAll();
        return new ResponseEntity<>(addresses, HttpStatus.OK);
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
        List<AddressDetails> addresses = addressRepository.findAll(pageable).getContent();
        return ResponseEntity.ok(addresses);
    }

    @Operation(summary = "Fetch One", description = "Get data by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = { @Content(schema = @Schema())}, description = "Data retrieve successfully!" ),
            @ApiResponse(responseCode = "404",content = {@Content(schema = @Schema())}, description = "Data not found!" ),
            @ApiResponse(responseCode = "500",content = {@Content(schema = @Schema())}, description = "Internal server error!" )
    })
    @GetMapping("/{id}")
    public ResponseEntity<AddressDetails> getOneById(@PathVariable Long id){
        AddressDetails address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AddressDetails not found with id: " + id));
        return new ResponseEntity<>(address, HttpStatus.OK);
    }

    @Operation(summary = "Create", description = "Create new data")
    @ApiResponses({
            @ApiResponse(responseCode = "201",content = { @Content(schema = @Schema())}, description = "Data created successfully!" ),
            @ApiResponse(responseCode = "400",content = {@Content(schema = @Schema())}, description = "Bad request!" ),
            @ApiResponse(responseCode = "500",content = {@Content(schema = @Schema())}, description = "Internal server error!" )
    })
    @PostMapping
    public ResponseEntity<AddressDetails> save(@RequestBody @Valid AddressDetails requestDto){
        AddressDetails address = modelMapper.map(requestDto, AddressDetails.class);
        AddressDetails savedAddress = addressRepository.save(address);
        return new ResponseEntity<>(savedAddress, HttpStatus.CREATED);
    }

    @Operation(summary = "Update", description = "Update existing data")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = { @Content(schema = @Schema())}, description = "Data updated successfully!" ),
            @ApiResponse(responseCode = "400",content = {@Content(schema = @Schema())}, description = "Bad request!" ),
            @ApiResponse(responseCode = "404",content = {@Content(schema = @Schema())}, description = "Data not found!" ),
            @ApiResponse(responseCode = "500",content = {@Content(schema = @Schema())}, description = "Internal server error!" )
    })
    @PutMapping
    public ResponseEntity<AddressDetails> update(@RequestBody @Valid AddressDetails requestDto){
        AddressDetails existingAddress = addressRepository.findById(requestDto.getId())
                .orElseThrow(() -> new RuntimeException("AddressDetails not found with id: " + requestDto.getId()));
        
        modelMapper.map(requestDto, existingAddress);
        AddressDetails updatedAddress = addressRepository.save(existingAddress);
        return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }

    @Operation(summary = "Delete One", description = "Delete by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = { @Content(schema = @Schema())}, description = "Data delete successfully!" ),
            @ApiResponse(responseCode = "400",content = {@Content(schema = @Schema())}, description = "Bad request!" ),
            @ApiResponse(responseCode = "404",content = {@Content(schema = @Schema())}, description = "Data not found!" ),
            @ApiResponse(responseCode = "500",content = {@Content(schema = @Schema())}, description = "Internal server error!" )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AddressDetails not found with id: " + id));
        addressRepository.deleteById(id);
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
