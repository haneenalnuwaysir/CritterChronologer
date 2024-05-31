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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

        return convertToDto(scheduleService.findAll());

    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        return convertToDto(petService.getById(petId).getSchedules());
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        Employee employee = employeeService.findById(employeeId);
        if(employee.getSchedules() == null)
            return null;

        List<Schedule> schedules = employee.getSchedules();
        return convertToDto(schedules);
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {

        List<Pet> pets =customerService.getById(customerId).getPets();
        HashMap<Long, Schedule> map = new HashMap<>();

        pets.stream().forEach(pet -> {
            pet.getSchedules().stream().forEach(schedule -> {
                map.put(schedule.getId(), schedule);
            });
        });
        return convertToDto(new ArrayList<>(map.values()));
    }

    private List<ScheduleDTO> convertToDto(List<Schedule> schedules){
        return schedules.stream().map(schedule -> {
            ScheduleDTO scheduleDTO = new ScheduleDTO();
            BeanUtils.copyProperties(schedule, scheduleDTO);

            scheduleDTO.setEmployeeIds(schedule.getEmployees().stream().map(Employee::getId).collect(Collectors.toList()));
            scheduleDTO.setPetIds(schedule.getPets().stream().map(Pet::getId).collect(Collectors.toList()));

            return scheduleDTO;

        }).collect(Collectors.toList());
    }
}
