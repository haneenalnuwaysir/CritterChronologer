package com.udacity.jdnd.course3.critter.Repository;

import com.udacity.jdnd.course3.critter.Entity.Employee;
import com.udacity.jdnd.course3.critter.Entity.Pet;
import com.udacity.jdnd.course3.critter.Entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
//    List<Schedule> findScheduleByPetsId(Long id);
//    List<Schedule> findScheduleByEmployeesId(Long employeeId);
//    List<Schedule> findScheduleByPetsCustomerId(Long customerId);

    List<Schedule> findByPets_Id(Long petId);
    List<Schedule> getSchedulesByEmployeesContains(Employee employee);
    List<Schedule> getSchedulesByPetsContains(Pet pet);

}
