package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.Enum.EmployeeSkill;
import com.udacity.jdnd.course3.critter.Entity.Employee;
import com.udacity.jdnd.course3.critter.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee findById(long employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(() -> new UnsupportedOperationException());
    }

    public List<Employee> findByIdIn(List<Long> employeeIds) {
        return employeeRepository.findAllById(employeeIds);
    }

    public List<Employee> findAvailableEmployees(Set<EmployeeSkill> skills, DayOfWeek dayOfWeek) {

        List<Employee> employees = employeeRepository.findAllBySkillsInAndEmployeeAvailability(skills, dayOfWeek);

        List<Employee> result = new ArrayList<>();
        employees.stream().forEach(employee -> {
            if(employee.getSkills().containsAll(skills)){
                result.add(employee);
            }
        });
        return result;
    }
}
