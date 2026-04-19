package com.eduservices.address.controller;

import com.eduservices.address.entity.AddressDetails;
import com.eduservices.address.service.AddressService;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.eduservices.common.controller.GenericController;
import com.eduservices.common.repo.GenericRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Address", description = "Address management api")
@RestController
@RequestMapping("/address")
public class AddressController extends GenericController<AddressDetails, AddressDetails, Long> {

    @Autowired
    AddressService addressService;

    public AddressController(GenericRepository<AddressDetails, Long> genericRepository, 
                           org.modelmapper.ModelMapper modelMapper) {
        super(genericRepository, modelMapper, AddressDetails.class, AddressDetails.class);
    }

    @GetMapping("/search")
    public ResponseEntity<?> findAddresses(
            @RequestParam("key") String addressKey,
            @RequestParam("value") String addressValue) {

        if (StringUtils.hasText(addressKey) && StringUtils.hasText(addressValue)) {
            return new ResponseEntity<>(addressService.searchAddress(addressKey, addressValue), HttpStatus.OK);
        }
        return new ResponseEntity<>("Search key or value may be empty or null!",HttpStatus.BAD_REQUEST);
    }
}
