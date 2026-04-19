package com.eduservices.address.repo;

import com.eduservices.address.entity.AddressDetails;
import com.eduservices.common.repo.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends GenericRepository<AddressDetails, Long> {

    List<AddressDetails> findByAddressLine1ContainingIgnoreCase(String addressLine1);

    List<AddressDetails> findByAddressLine2ContainingIgnoreCase(String addressLine2);

    List<AddressDetails> findByLandmarkContainingIgnoreCase(String landmark);

    List<AddressDetails> findByCity(String city);

    List<AddressDetails> findByDistrict(String district);

    List<AddressDetails> findByState(String state);

    List<AddressDetails> findByPostalCode(String postalCode);

   List<AddressDetails> findByRegion(String region);

    List<AddressDetails> findByAddressType(String addressType);

    List<AddressDetails> findByStatus(String status);

}
