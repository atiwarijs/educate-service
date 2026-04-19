package com.eduservices.campusmgmt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eduservices.campusmgmt.dto.StateDTO;
import com.eduservices.campusmgmt.service.LocationService;

@RestController
public class LocationController {

	@Autowired
	private LocationService locationService;

	@GetMapping("/states")
	public List<StateDTO> getStates() {
		return locationService.fetchAllStates();
	}

//	@GetMapping("/states/{stateId}/cities")
//	public List<City> getCities(@PathVariable Long stateId) {
//		return locationService.getCitiesByState(stateId);
//	}
}
