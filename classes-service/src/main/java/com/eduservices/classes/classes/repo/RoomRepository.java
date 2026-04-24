package com.eduservices.classes.repo;

import com.eduservices.classes.entity.RoomEntity;
import com.eduservices.repo.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends GenericRepository<RoomEntity, Long> {

    List<RoomEntity> findByName(String name);
    
}
