package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.Entity.Schedule;
import com.udacity.jdnd.course3.critter.Enum.EmployeeSkill;
import com.udacity.jdnd.course3.critter.Entity.Employee;
import com.udacity.jdnd.course3.critter.Exception.ResourceNotFoundException;
import com.udacity.jdnd.course3.critter.Repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.Repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.util.*;


@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee getEmployeeById(long employeeId) {
        return employeeRepository.getById(employeeId);
    }

    public void setAvailability(Set<DayOfWeek> daysAvailable, long employeeId) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        Employee employee1;
        if (employee.isPresent()) {
            employee1 = employee.get();
            employee1.setDaysAvailable(daysAvailable);
            employeeRepository.save(employee1);
        }


    }

    public List<Employee> findEmployeesForService(Set<EmployeeSkill> skills, DayOfWeek dayAvailable) {
        List<Employee> employees= employeeRepository.findAllBySkillsInAndDaysAvailableContains(skills, dayAvailable);

        List<Employee> employeeList = new ArrayList<>();

        employees.forEach(emp -> {
            if(emp.getSkills().containsAll(skills)){ employeeList.add(emp); }
        });
        return employeeList;
    }

    public List<Employee> getEmployeesBySchedule(Long scheduleId){
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(ResourceNotFoundException::new);
        return schedule.getEmployees();
    }

}