package com.udacity.jdnd.course3.critter.Controller;

import com.udacity.jdnd.course3.critter.DTO.ScheduleDTO;
import com.udacity.jdnd.course3.critter.Entity.Employee;
import com.udacity.jdnd.course3.critter.Entity.Pet;
import com.udacity.jdnd.course3.critter.Entity.Schedule;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PetService petService;

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        List<Employee> employees = employeeService.findByIdIn(scheduleDTO.getEmployeeIds());
        List<Pet> pets = petService.findAllPetsById(scheduleDTO.getPetIds());

        Schedule schedule = new Schedule();

        BeanUtils.copyProperties(scheduleDTO, schedule);
        schedule.setEmployees(employees);
        schedule.setPets(pets);

        Schedule createSchedule = scheduleService.saveSchedule(schedule);

        employees.stream().forEach(employee -> {
            if(employee.getSchedules() == null)
                employee.setSchedules(new ArrayList<>());

            employee.getSchedules().add(createSchedule);
        });

        pets.stream().forEach(pet -> {
            if(pet.getSchedules() == null)
                pet.setSchedules(new ArrayList<>());

            pet.getSchedules().add(createSchedule);
        });


        return scheduleDTO;
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules = this.scheduleService.findAll();
        List<ScheduleDTO> dto = new ArrayList<>();
        for(Schedule s:schedules){
            dto.add(convertToDTO(s));
        }
        return dto;
//        return convertToDto(scheduleService.findAll());

    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
         List<Schedule> schedules = this.scheduleService.getSchedulesByPetId(petId);
         List<ScheduleDTO> dto = new ArrayList<>();
             for(Schedule s:schedules){
                 dto.add(convertToDTO(s));
             }
         return dto;
    }
    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<Schedule> schedules = this.scheduleService.getScheduleByEmployee(employeeId);
        List<ScheduleDTO> dto = new ArrayList<>();
        for(Schedule s:schedules){
            dto.add(convertToDTO(s));
        }
        return dto;
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {

        List<Schedule> schedules = this.scheduleService.getScheduleByOwner(customerId);
        List<ScheduleDTO> dto = new ArrayList<>();
        for(Schedule s:schedules){
            dto.add(convertToDTO(s));
        }
        return dto;
    }

//    private List<ScheduleDTO> convertToDto(List<Schedule> schedules){
//        return schedules.stream().map(schedule -> {
//            ScheduleDTO scheduleDTO = new ScheduleDTO();
//            BeanUtils.copyProperties(schedule, scheduleDTO);
//
//            scheduleDTO.setEmployeeIds(schedule.getEmployees().stream().map(Employee::getId).collect(Collectors.toList()));
//            scheduleDTO.setPetIds(schedule.getPets().stream().map(Pet::getId).collect(Collectors.toList()));
//
//            return scheduleDTO;
//
//        }).collect(Collectors.toList());
//    }
    private ScheduleDTO convertToDTO(Schedule schedule) {

        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);

        // setting the rest (pets, employees) of properties for schedulePTO
        scheduleDTO.setActivities(schedule.getActivities());

        List<Pet> pets = schedule.getPets();
        List<Long> petId = new ArrayList<>();
        for (Pet pet : pets) {
            petId.add(pet.getId());
        }
        scheduleDTO.setPetIds(petId);
        List<Employee> employees = schedule.getEmployees();
        List<Long> employeeId = new ArrayList<>();
        for (Employee employee : employees) {
            employeeId.add(employee.getId());
        }
        scheduleDTO.setEmployeeIds(employeeId);
        return scheduleDTO;

    }

    private Schedule convertFromDTO(ScheduleDTO scheduleDTO){
        ModelMapper modelMapper = new ModelMapper();
        Schedule schedule = modelMapper.map(scheduleDTO, Schedule.class);

        // setting the rest (pets, employees) of properties for schedule
        schedule.setActivities(scheduleDTO.getActivities());
        HashMap<Long, Employee> employeeMap = new HashMap<>();
        for (Long employeeId : scheduleDTO.getEmployeeIds()) {
            Optional<Employee> optionalEmployee = Optional.ofNullable(employeeService.findById(employeeId));
            if (optionalEmployee.isPresent()) {
                employeeMap.put(employeeId, optionalEmployee.get());
            } else {
                throw new UnsupportedOperationException();
            }
        }
        schedule.setEmployees(new ArrayList<Employee>(employeeMap.values()));
        HashMap<Long, Pet> petMap = new HashMap<>();
        for (Long petId : scheduleDTO.getPetIds()) {
            Optional<Pet> optionalPet = Optional.ofNullable(petService.getById(petId));
            if (optionalPet.isPresent()) {
                petMap.put(petId, optionalPet.get());
            } else {
                throw new UnsupportedOperationException();
            }
        }
        schedule.setPets(new ArrayList<Pet>(petMap.values()));
        return schedule;
    }
}
