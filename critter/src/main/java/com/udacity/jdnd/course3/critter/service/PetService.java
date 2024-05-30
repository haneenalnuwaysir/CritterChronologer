package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.Entity.Customer;
import com.udacity.jdnd.course3.critter.Entity.Pet;
import com.udacity.jdnd.course3.critter.Entity.Schedule;
import com.udacity.jdnd.course3.critter.Exception.ResourceNotFoundException;
import com.udacity.jdnd.course3.critter.Repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.Repository.PetRepository;
import com.udacity.jdnd.course3.critter.Repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;


@Service
public class PetService {
    @Autowired
    PetRepository petRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerService customerService;

    public Pet savePet(Pet pet){
        Pet saved = petRepository.save(pet);

        Customer customer = saved.getOwner();
        List<Pet> petsOfCustomer = customer.getPets();

        if(petsOfCustomer == null){
            petsOfCustomer = new ArrayList<>();
        }
        customer.getPets().add(saved);
        petsOfCustomer.add(saved);
        customer.setPets(petsOfCustomer);
        customerRepository.save(customer);

        return saved;
    }
    public Pet getPet(Long petId){
        return petRepository.findById(petId)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public List<Pet> getPets(){
        return petRepository.findAll();
    }

    public List<Pet> getPetsByOwner(Long ownerId){
        return petRepository.getPetsByOwner_Id(ownerId);
    }

    public List<Pet> getPetsBySchedule(Long scheduleId){
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(ResourceNotFoundException::new);

        return schedule.getPets();
    }


}