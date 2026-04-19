package com.eduservices.address.service;

import com.eduservices.address.entity.AddressDetails;
import com.eduservices.address.repo.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressRepository addressRepository;

    @Override
    public List<AddressDetails> searchAddress(String key, String value) {
        return switch (key.toLowerCase()) {
            case "line1" -> addressRepository.findByAddressLine1ContainingIgnoreCase(value);
            case "line2" -> addressRepository.findByAddressLine2ContainingIgnoreCase(value);
            case "landmark" -> addressRepository.findByLandmarkContainingIgnoreCase(value);
            case "city" -> addressRepository.findByCity(value);
            case "district" -> addressRepository.findByDistrict(value);
            case "state" -> addressRepository.findByState(value);
            case "postal" -> addressRepository.findByPostalCode(value);
            case "region" -> addressRepository.findByRegion(value);
            case "type" -> addressRepository.findByAddressType(value);
            case "status" -> addressRepository.findByStatus(value);
            default -> List.of();
        };
    }
}
