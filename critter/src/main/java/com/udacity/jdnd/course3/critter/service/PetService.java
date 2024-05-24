package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.Entity.Pet;
import com.udacity.jdnd.course3.critter.Repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PetService {

    @Autowired
    private PetRepository petRepository;

    public Pet findById(long id) {
        return petRepository.findById(id).orElseThrow(() -> new UnsupportedOperationException());
    }

    public Pet save(Pet pet) {
        return petRepository.save(pet);
    }

    public List<Pet> findAllById(List<Long> petIds) {
        return petRepository.findAllById(petIds);
    }

    public List<Pet> findAll() {
        return petRepository.findAll();
    }
}
