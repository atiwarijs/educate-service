package com.eduservices.address.service;

import com.eduservices.address.entity.AddressDetails;

import java.util.List;

public interface AddressService {

    List<AddressDetails> searchAddress(String key, String value);

}
