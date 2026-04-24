package com.eduservices.service;

import com.eduservices.exception.InvalidIdException;
import com.eduservices.exception.NoContentException;
import com.eduservices.repo.GenericRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public record GenericService<D, E, ID extends Serializable>(
        GenericRepository<E, ID> genericRepository,
        ModelMapper mapper,
        Class<D> dtoClass,
        Class<E> entityClass){

    public Page<D> findAll(Pageable pageable){
        Page<E> pages = genericRepository.findAll(pageable);
        if(pages.hasContent()){
            return pages.map(entity -> mapper.map(entity,dtoClass));
        }
        throw new NoContentException("Content not available!");
    }

    public List<D> findAll(){
        List<E> list = genericRepository.findAll();
        if(!list.isEmpty()){
            return list.stream().map(entity -> mapper.map(entity, dtoClass)).toList();
        }
        throw new NoContentException("Content not available!");
    }

    public D findById(ID id) {
        Optional<E> operationalEntity = genericRepository.findById(id);
        if(operationalEntity.isPresent()){
            return mapper.map(operationalEntity, dtoClass);
        }
        throw new InvalidIdException("Invalid Id!");
    }

    @Transactional
    public D save(D dto) {
        E entity = mapper.map(dto, entityClass);
        E savedEntity = genericRepository.save(entity);
        return mapper.map(savedEntity, dtoClass);
    }

    @Transactional
    public void deleteBuId(ID id) {
        E entity = mapper.map(findById(id), entityClass);
        genericRepository.delete(entity);
    }

    public void deleteAll(){
        genericRepository.deleteAll();
    }
}
