package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.Entity.Customer;
import com.udacity.jdnd.course3.critter.Entity.Pet;
import com.udacity.jdnd.course3.critter.Entity.Schedule;
import com.udacity.jdnd.course3.critter.Repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.Repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private CustomerRepository customerRepository;

    public Schedule saveSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }
    public List<Schedule> getSchedulesByPetId(Long petId){
        return this.scheduleRepository.getScheduleByPetsId(petId);
    }

    public List<Schedule> getScheduleByOwner(Long ownerId) {
        Optional<Customer> optionalOwner = this.customerRepository.findById(ownerId);

        if (!optionalOwner.isPresent()) {
            throw  new UnsupportedOperationException();
        }
        else{
            Customer customer = optionalOwner.get();
            List<Pet> pets = customer.getPets();
            List<Schedule> schedules = new ArrayList<>();

            for (Pet pet : pets) {
                schedules.addAll(scheduleRepository.getScheduleByPetsId(pet.getId()));
            }
            return schedules;
        }
    }

    public List<Schedule> getScheduleByEmployee(Long employeeId) {
        return scheduleRepository.getScheduleByEmployeesId(employeeId);
    }

}
