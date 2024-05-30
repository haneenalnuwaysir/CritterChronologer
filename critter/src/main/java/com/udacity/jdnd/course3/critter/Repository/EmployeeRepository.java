package com.udacity.jdnd.course3.critter.Repository;

import com.udacity.jdnd.course3.critter.Entity.Schedule;
import com.udacity.jdnd.course3.critter.Enum.EmployeeSkill;
import com.udacity.jdnd.course3.critter.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

//    List<Employee> findAllByDaysAvailableContaining(DayOfWeek day);

    List<Employee> findAllBySkillsInAndDaysAvailableContains(Set<EmployeeSkill> skills, DayOfWeek daysAvailable);

    @Query("select e.schedules from Employee e where e.id  = :employeeId")
    List<Schedule> getSchedulesForEmployee(Long employeeId);

}
