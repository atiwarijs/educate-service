package com.eduservices.student.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.eduservices.student.dto.ClassInfoDTO;
import com.eduservices.student.entity.ClassInfo;
import com.eduservices.student.entity.Sections;
import com.eduservices.student.exception.NoContentException;
import com.eduservices.student.mapper.ClassMapper;
import com.eduservices.student.repo.ClassInfoRepository;
import com.eduservices.student.service.ClassesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Class Details", description = "This is class information details apis")
@RestController
@RequestMapping("/classes")
public class ClassInfoController {

	private final ClassInfoRepository classInfoRepository;
	private final ClassesService classesService;
	private final ModelMapper modelMapper;

	private static final Logger logger = LoggerFactory.getLogger(ClassInfoController.class);

	public ClassInfoController(ClassInfoRepository classInfoRepository, ClassesService classesService, ModelMapper modelMapper) {
		this.classInfoRepository = classInfoRepository;
		this.classesService = classesService;
		this.modelMapper = modelMapper;
	}

	@Operation(summary = "Fetch All", description = "Get all data list")
	@ApiResponses({
			@ApiResponse(responseCode = "200",content = { @Content(schema = @Schema())}, description = "Data retrieve successfully!" ),
			@ApiResponse(responseCode = "404",content = {@Content(schema = @Schema())}, description = "Data not found!" ),
			@ApiResponse(responseCode = "500",content = {@Content(schema = @Schema())}, description = "Internal server error!" )
	})
	@GetMapping("/list")
	public ResponseEntity<List<ClassInfoDTO>> getAllList(){
		List<ClassInfo> classInfos = classInfoRepository.findAll();
		List<ClassInfoDTO> classInfoDTOs = classInfos.stream()
				.map(classInfo -> modelMapper.map(classInfo, ClassInfoDTO.class))
				.collect(Collectors.toList());
		return new ResponseEntity<>(classInfoDTOs, HttpStatus.OK);
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
		List<ClassInfo> classInfos = classInfoRepository.findAll(pageable).getContent();
		List<ClassInfoDTO> classInfoDTOs = classInfos.stream()
				.map(classInfo -> modelMapper.map(classInfo, ClassInfoDTO.class))
				.collect(Collectors.toList());
		return ResponseEntity.ok(classInfoDTOs);
	}

	@Operation(summary = "Fetch One", description = "Get data by id")
	@ApiResponses({
			@ApiResponse(responseCode = "200",content = { @Content(schema = @Schema())}, description = "Data retrieve successfully!" ),
			@ApiResponse(responseCode = "404",content = {@Content(schema = @Schema())}, description = "Data not found!" ),
			@ApiResponse(responseCode = "500",content = {@Content(schema = @Schema())}, description = "Internal server error!" )
	})
	@GetMapping("/{id}")
	public ResponseEntity<ClassInfoDTO> getOneById(@PathVariable Long id){
		ClassInfo classInfo = classInfoRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("ClassInfo not found with id: " + id));
		ClassInfoDTO classInfoDTO = modelMapper.map(classInfo, ClassInfoDTO.class);
		return new ResponseEntity<>(classInfoDTO, HttpStatus.OK);
	}

	@Operation(summary = "Create", description = "Create new data")
	@ApiResponses({
			@ApiResponse(responseCode = "201",content = { @Content(schema = @Schema())}, description = "Data created successfully!" ),
			@ApiResponse(responseCode = "400",content = {@Content(schema = @Schema())}, description = "Bad request!" ),
			@ApiResponse(responseCode = "500",content = {@Content(schema = @Schema())}, description = "Internal server error!" )
	})
	@PostMapping
	public ResponseEntity<ClassInfoDTO> save(@RequestBody @Valid ClassInfoDTO requestDto){
		ClassInfo classInfo = modelMapper.map(requestDto, ClassInfo.class);
		ClassInfo savedClassInfo = classInfoRepository.save(classInfo);
		ClassInfoDTO responseDto = modelMapper.map(savedClassInfo, ClassInfoDTO.class);
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
	public ResponseEntity<ClassInfoDTO> update(@RequestBody @Valid ClassInfoDTO requestDto){
		ClassInfo existingClassInfo = classInfoRepository.findById(requestDto.getId())
				.orElseThrow(() -> new RuntimeException("ClassInfo not found with id: " + requestDto.getId()));
		
		modelMapper.map(requestDto, existingClassInfo);
		ClassInfo updatedClassInfo = classInfoRepository.save(existingClassInfo);
		ClassInfoDTO responseDto = modelMapper.map(updatedClassInfo, ClassInfoDTO.class);
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
		classInfoRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("ClassInfo not found with id: " + id));
		classInfoRepository.deleteById(id);
		return ResponseEntity.ok("Data deleted successfully!");
	}

	@PostMapping("/create")
	public ResponseEntity<?> createClassWithSections(@RequestBody ClassInfoDTO classInfo) {
		logger.info("Received request to save class with name {}", classInfo.getClassName());
		ClassInfoDTO saved = classesService.createClassWithSections(classInfo);
		return ResponseEntity.ok(saved);
	}

	@PutMapping("/update")
	public ResponseEntity<?> updateClassWithSections(@RequestBody ClassInfoDTO classInfo) {
		logger.info("Received request to update class with name {}", classInfo.getClassName());
		ClassInfoDTO updated = classesService.updateClassWithSections(classInfo);
		return ResponseEntity.ok(updated);
	}

	@GetMapping("/list")
	public ResponseEntity<?> getAll() {
		List<ClassInfo> list = classInfoRepository.findAll();
		if (ObjectUtils.isEmpty(list)) {
			throw new NoContentException("No content found!");
		}
		Map<Long, ClassInfo> mergedMap = new HashMap<>();
		list.forEach(c -> {
			mergedMap.merge(c.getId(), c, (existing, incoming) -> {
				// Merge sections - avoid duplicates
				List<Sections> mergedSections = new ArrayList<>(existing.getSections());
				incoming.getSections().forEach(sec -> {
					boolean exists = mergedSections.stream().anyMatch(s -> s.getId().equals(sec.getId()));
					if (!exists) {
						mergedSections.add(sec);
					}
				});
				existing.setSections(mergedSections);
				return existing;
			});
		});
		List<ClassInfo> mergedList = new ArrayList<>(mergedMap.values());
		return ResponseEntity.ok(mergedList);
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
