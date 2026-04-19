package com.eduservices.profiles.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.eduservices.profiles.entity.PersonalDetails;
import com.eduservices.profiles.repo.PersonalRepository;
import com.eduservices.profiles.util.PersonalDetailsMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eduservices.profile.dto.PersonalDetailsDto;

@Tag(name = "Personal details", description = "This is personal details apis")
@RestController
@RequestMapping("/personal")
public class PersonalController {

	@Autowired
	private PersonalRepository personalRepo;
	
	@Autowired
	private PersonalDetailsMapper mapper;
	
	private final ModelMapper modelMapper;

	public PersonalController(PersonalRepository personalRepo, ModelMapper modelMapper) {
		this.personalRepo = personalRepo;
		this.modelMapper = modelMapper;
	}

	@Operation(summary = "Fetch All", description = "Get all data list")
	@ApiResponses({
			@ApiResponse(responseCode = "200",content = { @Content(schema = @Schema())}, description = "Data retrieve successfully!" ),
			@ApiResponse(responseCode = "404",content = {@Content(schema = @Schema())}, description = "Data not found!" ),
			@ApiResponse(responseCode = "500",content = {@Content(schema = @Schema())}, description = "Internal server error!" )
	})
	@GetMapping("/list")
	public ResponseEntity<List<PersonalDetailsDto>> getAllList(){
		List<PersonalDetails> personalDetails = personalRepo.findAll();
		List<PersonalDetailsDto> personalDetailsDTOs = personalDetails.stream()
				.map(personal -> mapper.toDto(personal))
				.collect(Collectors.toList());
		return new ResponseEntity<>(personalDetailsDTOs, HttpStatus.OK);
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
		List<PersonalDetails> personalDetails = personalRepo.findAll(pageable).getContent();
		List<PersonalDetailsDto> personalDetailsDTOs = personalDetails.stream()
				.map(personal -> mapper.toDto(personal))
				.collect(Collectors.toList());
		return ResponseEntity.ok(personalDetailsDTOs);
	}

	@Operation(summary = "Fetch One", description = "Get data by id")
	@ApiResponses({
			@ApiResponse(responseCode = "200",content = { @Content(schema = @Schema())}, description = "Data retrieve successfully!" ),
			@ApiResponse(responseCode = "404",content = {@Content(schema = @Schema())}, description = "Data not found!" ),
			@ApiResponse(responseCode = "500",content = {@Content(schema = @Schema())}, description = "Internal server error!" )
	})
	@GetMapping("/{id}")
	public ResponseEntity<PersonalDetailsDto> getOneById(@PathVariable Long id){
		PersonalDetails personal = personalRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("PersonalDetails not found with id: " + id));
		PersonalDetailsDto personalDTO = mapper.toDto(personal);
		return new ResponseEntity<>(personalDTO, HttpStatus.OK);
	}

	@Operation(summary = "Create", description = "Create new data")
	@ApiResponses({
			@ApiResponse(responseCode = "201",content = { @Content(schema = @Schema())}, description = "Data created successfully!" ),
			@ApiResponse(responseCode = "400",content = {@Content(schema = @Schema())}, description = "Bad request!" ),
			@ApiResponse(responseCode = "500",content = {@Content(schema = @Schema())}, description = "Internal server error!" )
	})
	@PostMapping
	public ResponseEntity<PersonalDetailsDto> save(@RequestBody @Valid PersonalDetailsDto requestDto){
		PersonalDetails personal = mapper.toEntity(requestDto);
		PersonalDetails savedPersonal = personalRepo.save(personal);
		PersonalDetailsDto responseDto = mapper.toDto(savedPersonal);
		return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
	}

	@Operation(summary = "Update", description = "Update existing data")
	@ApiResponses({
			@ApiResponse(responseCode = "200",content = { @Content(schema = @Schema())}, description = "Data updated successfully!" ),
			@ApiResponse(responseCode = "400",content = {@Content(schema = @Schema())}, description = "Bad request!" ),
			@ApiResponse(responseCode = "404",content = {@Content(schema = @Schema())}, description = "Data not found!" ),
			@ApiResponse(responseCode = "500",content = {@Content(schema = @Schema())}, description = "Internal server error!" )
	})
	@PutMapping
	public ResponseEntity<PersonalDetailsDto> update(@RequestBody @Valid PersonalDetailsDto requestDto){
		PersonalDetails existingPersonal = personalRepo.findById(requestDto.getId())
				.orElseThrow(() -> new RuntimeException("PersonalDetails not found with id: " + requestDto.getId()));
		
		mapper.updateEntityFromDto(requestDto, existingPersonal);
		PersonalDetails updatedPersonal = personalRepo.save(existingPersonal);
		PersonalDetailsDto responseDto = mapper.toDto(updatedPersonal);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
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
		personalRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("PersonalDetails not found with id: " + id));
		personalRepo.deleteById(id);
		return ResponseEntity.ok("Data deleted successfully!");
	}

	/**
	 * Finds personal details by userId - Custom endpoint preserved
	 * 
	 * @param userId The UUID of the user to find profiles for
	 * @return List of personal details for the specified user
	 */
	@GetMapping("/findByUserId/{userId}")
	public ResponseEntity<?> findByUserId(@PathVariable String userId) {
		Optional<PersonalDetails> personalDetails = personalRepo.findByUserId(userId);
		if (personalDetails.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		 PersonalDetailsDto dto = mapper.toDto(personalDetails.get());
		return ResponseEntity.ok(dto);
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
