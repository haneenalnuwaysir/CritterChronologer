//package com.udacity.jdnd.course3.critter;
//
//import com.google.common.collect.Sets;
//import com.udacity.jdnd.course3.critter.Controller.*;
//import com.udacity.jdnd.course3.critter.DTO.*;
//import com.udacity.jdnd.course3.critter.Entity.Customer;
//import com.udacity.jdnd.course3.critter.Entity.Employee;
//import com.udacity.jdnd.course3.critter.Enum.EmployeeSkill;
//import com.udacity.jdnd.course3.critter.Enum.PetType;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.DayOfWeek;
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//
//
///**
// * This is a set of functional tests to validate the basic capabilities desired for this application.
// * Students will need to configure the application to run these tests by adding application.properties file
// * to the test/resources directory that specifies the datasource. It can run using an in-memory H2 instance
// * and should not try to re-use the same datasource used by the rest of the app.
// *
// * These tests should all pass once the project is complete.
// */
//@Transactional
//@SpringBootTest(classes = CritterApplication.class)
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class CritterFunctionalTest {
//
//    @Autowired
//    private UserController userController;
//
//    @Autowired
//    private PetController petController;
//
//    @Autowired
//    private ScheduleController scheduleController;
//
//    @Test
//    @Order(1)
//    public void testCreateCustomer(){
//        CustomerDTO customerDTO = createCustomerDTO();
//        CustomerDTO newCustomer = userController.saveCustomer(customerDTO);
//        CustomerDTO retrievedCustomer = userController.getAllCustomers().get(0);
//        Assertions.assertEquals(newCustomer.getName(), customerDTO.getName());
//        Assertions.assertEquals(newCustomer.getId(), retrievedCustomer.getId());
//        Assertions.assertTrue(retrievedCustomer.getId() > 0);
//    }
//
//    @Test
//    @Order(2)
//    public void testCreateEmployee(){
//        EmployeeDTO employeeDTO = createEmployeeDTO();
//        EmployeeDTO newEmployee = userController.saveEmployee(employeeDTO);
//        EmployeeDTO retrievedEmployee = userController.getEmployee(newEmployee.getId());
//        Assertions.assertEquals(employeeDTO.getSkills(), newEmployee.getSkills());
//        Assertions.assertEquals(newEmployee.getId(), retrievedEmployee.getId());
//        Assertions.assertTrue(retrievedEmployee.getId() > 0);
//    }
//
//    @Test
//    @Order(3)
//    public void testAddPetsToCustomer() {
//        CustomerDTO customerDTO = createCustomerDTO();
//        CustomerDTO newCustomer = userController.saveCustomer(customerDTO);
//
//        PetDTO petDTO = createPetDTO();
//        petDTO.setOwnerId(newCustomer.getId());
//        PetDTO newPet = petController.savePet(petDTO);
//
//        //make sure pet contains customer id
//        PetDTO retrievedPet = petController.getPet(newPet.getId());
//        Assertions.assertEquals(retrievedPet.getId(), newPet.getId());
//        Assertions.assertEquals(retrievedPet.getOwnerId(), newCustomer.getId());
//
//        //make sure you can retrieve pets by owner
//        List<PetDTO> pets = petController.getPetsByOwner(newCustomer.getId());
//        Assertions.assertEquals(newPet.getId(), pets.get(0).getId());
//        Assertions.assertEquals(newPet.getName(), pets.get(0).getName());
//
//        //check to make sure customer now also contains pet
//        CustomerDTO retrievedCustomer = userController.getAllCustomers().get(0);
//        Assertions.assertTrue(retrievedCustomer.getPetIds() != null && retrievedCustomer.getPetIds().size() > 0);
//        Assertions.assertEquals(retrievedCustomer.getPetIds().get(0), retrievedPet.getId());
//    }
//
//    @Test
//    @Order(4)
//    public void testFindPetsByOwner() {
//        CustomerDTO customerDTO = createCustomerDTO();
//        CustomerDTO newCustomer = userController.saveCustomer(customerDTO);
//
//        PetDTO petDTO = createPetDTO();
//        petDTO.setOwnerId(newCustomer.getId());
//        PetDTO newPet = petController.savePet(petDTO);
//        petDTO.setType(PetType.DOG);
//        petDTO.setName("DogName");
//        PetDTO newPet2 = petController.savePet(petDTO);
//
//        List<PetDTO> pets = petController.getPetsByOwner(newCustomer.getId());
//        Assertions.assertEquals(pets.size(), 2);
//        Assertions.assertEquals(pets.get(0).getOwnerId(), newCustomer.getId());
//        Assertions.assertEquals(pets.get(0).getId(), newPet.getId());
//    }
//
//    @Test
//    @Order(5)
//    public void testFindOwnerByPet() {
//        CustomerDTO customerDTO = createCustomerDTO();
//        CustomerDTO newCustomer = userController.saveCustomer(customerDTO);
//
//        PetDTO petDTO = createPetDTO();
//        petDTO.setOwnerId(newCustomer.getId());
//        PetDTO newPet = petController.savePet(petDTO);
//
//        CustomerDTO owner = userController.getOwnerByPet(newPet.getId());
//        Assertions.assertEquals(owner.getId(), newCustomer.getId());
//        Assertions.assertEquals(owner.getPetIds().get(0), newPet.getId());
//    }
//
//    @Test
//    @Order(6)
//    public void testChangeEmployeeAvailability() {
//        EmployeeDTO employeeDTO = createEmployeeDTO();
//        EmployeeDTO emp1 = userController.saveEmployee(employeeDTO);
//        Assertions.assertNull(emp1.getEmployeeAvailability());
//
//        Set<DayOfWeek> availability = Sets.newHashSet(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY);
//        userController.setAvailability(availability, emp1.getId());
//
//        EmployeeDTO emp2 = userController.getEmployee(emp1.getId());
//        Assertions.assertEquals(availability, emp2.getEmployeeAvailability());
//    }
//
//    @Test
//    @Order(7)
//    public Employee testFindEmployeesByServiceAndTime() {
//        EmployeeDTO emp1 = createEmployeeDTO();
//        EmployeeDTO emp2 = createEmployeeDTO();
//        EmployeeDTO emp3 = createEmployeeDTO();
//
//        emp1.setEmployeeAvailability(Sets.newHashSet(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY));
//        emp2.setEmployeeAvailability(Sets.newHashSet(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY));
//        emp3.setEmployeeAvailability(Sets.newHashSet(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY));
//
//        emp1.setSkills(Sets.newHashSet(EmployeeSkill.FEEDING, EmployeeSkill.PETTING));
//        emp2.setSkills(Sets.newHashSet(EmployeeSkill.PETTING, EmployeeSkill.WALKING));
//        emp3.setSkills(Sets.newHashSet(EmployeeSkill.WALKING, EmployeeSkill.SHAVING));
//        Employee employee = new Employee();
//        employee.setName("TestEmployee");
//        return employee;
//    }
//
//    private static Customer createCustomer() {
//        Customer owner = new Customer();
//        owner.setName("TestEmployee");
//        owner.setPhoneNumber("123-456-789");
//        return owner;
//    }
//
//    private static EmployeeDTO createEmployeeDTO() {
//        EmployeeDTO employeeDTO = new EmployeeDTO();
//        employeeDTO.setName("TestEmployee");
//        employeeDTO.setSkills(Sets.newHashSet(EmployeeSkill.FEEDING, EmployeeSkill.PETTING));
//        return employeeDTO;
//    }
//    private static CustomerDTO createCustomerDTO() {
//        CustomerDTO customerDTO = new CustomerDTO();
//        customerDTO.setName("TestEmployee");
//        customerDTO.setPhoneNumber("123-456-789");
//        return customerDTO;
//    }
//
//    private static PetDTO createPetDTO() {
//        PetDTO petDTO = new PetDTO();
//        petDTO.setName("TestPet");
//        petDTO.setType(PetType.CAT);
//        return petDTO;
//    }
//
//    private static EmployeeRequestDTO createEmployeeRequestDTO() {
//        EmployeeRequestDTO employeeRequestDTO = new EmployeeRequestDTO();
//        employeeRequestDTO.setDate(LocalDate.of(2019, 12, 25));
//        employeeRequestDTO.setSkills(Sets.newHashSet(EmployeeSkill.FEEDING, EmployeeSkill.WALKING));
//        return employeeRequestDTO;
//    }
//
//    private static ScheduleDTO createScheduleDTO(List<Long> petIds, List<Long> employeeIds, LocalDate date, Set<EmployeeSkill> activities) {
//        ScheduleDTO scheduleDTO = new ScheduleDTO();
//        scheduleDTO.setPetIds(petIds);
//        scheduleDTO.setEmployeeIds(employeeIds);
//        scheduleDTO.setDate(date);
//        scheduleDTO.setActivities(activities);
//        return scheduleDTO;
//    }
//
//    private ScheduleDTO populateSchedule(int numEmployees, int numPets, LocalDate date, Set<EmployeeSkill> activities) {
//        List<Long> employeeIds = IntStream.range(0, numEmployees)
//                .mapToObj(i -> createEmployeeDTO())
//                .map(e -> {
//                    e.setSkills(activities);
//                    e.setEmployeeAvailability(Sets.newHashSet(date.getDayOfWeek()));
//                    return userController.saveEmployee(e).getId();
//                }).collect(Collectors.toList());
//        CustomerDTO cust = userController.saveCustomer(createCustomerDTO());
//        List<Long> petIds = IntStream.range(0, numPets)
//                .mapToObj(i -> createPetDTO())
//                .map(p -> {
//                    p.setOwnerId(cust.getId());
//                    return petController.savePet(p).getId();
//                }).collect(Collectors.toList());
//        return scheduleController.createSchedule(createScheduleDTO(petIds, employeeIds, date, activities));
//    }
//
//    private static void compareSchedules(ScheduleDTO sched1, ScheduleDTO sched2) {
//        Assertions.assertEquals(sched1.getPetIds(), sched2.getPetIds());
//        Assertions.assertEquals(sched1.getActivities(), sched2.getActivities());
//        Assertions.assertEquals(sched1.getEmployeeIds(), sched2.getEmployeeIds());
//        Assertions.assertEquals(sched1.getDate(), sched2.getDate());
//    }
//}
