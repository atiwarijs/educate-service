package com.eduservices.campusmgmt.service;

import com.eduservices.campusmgmt.dto.StateDTO;
import java.util.List;

public interface LocationService {
    List<StateDTO> fetchAllStates();
}