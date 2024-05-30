package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.Entity.Employee;
import com.udacity.jdnd.course3.critter.Entity.Pet;
import com.udacity.jdnd.course3.critter.Entity.Schedule;
import com.udacity.jdnd.course3.critter.Exception.ResourceNotFoundException;
import com.udacity.jdnd.course3.critter.Repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.Repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.Repository.PetRepository;
import com.udacity.jdnd.course3.critter.Repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
@Service
public class ScheduleService {
    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    PetRepository petRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    public Schedule createSchedule(Schedule schedule){
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getAllSchedules(){
        return scheduleRepository.findAll();
    }

    public List<Schedule> getScheduleForPet(long petId){
        Pet pet = petRepository.findById(petId)
                .orElseThrow(ResourceNotFoundException::new);
        return scheduleRepository.getSchedulesByPetsContains(pet);
    }

    public List<Schedule> getScheduleForEmployee(long employeeId){
        Employee employee= employeeRepository.findById(employeeId)
                .orElseThrow(ResourceNotFoundException::new);
        return scheduleRepository.getSchedulesByEmployeesContains(employee);
    }

    public List<Schedule> getScheduleForCustomer(long petId){
        return scheduleRepository.findByPets_Id(petId);
    }

}
