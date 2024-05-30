package com.udacity.jdnd.course3.critter.Controller;

import com.udacity.jdnd.course3.critter.DTO.CustomerDTO;
import com.udacity.jdnd.course3.critter.DTO.EmployeeDTO;
import com.udacity.jdnd.course3.critter.DTO.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critter.Entity.Customer;
import com.udacity.jdnd.course3.critter.Entity.Employee;
import com.udacity.jdnd.course3.critter.Entity.Pet;
import com.udacity.jdnd.course3.critter.Repository.PetRepository;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private CustomerService customerService;
    private EmployeeService employeeService;
    private PetService petService;
    private PetRepository petRepository;

    public UserController(CustomerService customerService, EmployeeService employeeService, PetService petService, PetRepository petRepository) {
        this.customerService = customerService;
        this.employeeService = employeeService;
        this.petService = petService;
        this.petRepository = petRepository;
    }

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        return convertCustomerEntityToDTO(
                customerService.saveCustomer(
                        convertDTOtoCustomerEntity(customerDTO)));
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        return customerService.getAllCustomers()
                .stream()
                .map(this::convertCustomerEntityToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        Pet pet = petService.getPet(petId);
        return convertCustomerEntityToDTO(customerService.getCustomerById(pet.getOwner().getId()));
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        return convertEmployeeEntityToDTO(employeeService.saveEmployee(convertDTOToEmployeeEntity(employeeDTO)));
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        return convertEmployeeEntityToDTO(employeeService.getEmployeeById(employeeId));
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        employeeService.setAvailability(daysAvailable,employeeId);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        return employeeService.findEmployeesForService(employeeDTO.getSkills(),employeeDTO.getDate().getDayOfWeek())
                .stream()
                .map(this::convertEmployeeEntityToDTO)
                .collect(Collectors.toList());
    }

    private Customer convertDTOtoCustomerEntity(CustomerDTO customerDTO){
        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customer.setNotes(customerDTO.getNotes());

        if (customerDTO.getPetIds() != null) {
            List<Pet> pets = customerDTO.getPetIds().stream()
                    .map(petId -> petRepository.findById(petId)
                            .orElseThrow(() -> new EntityNotFoundException("Pet not found")))
                    .collect(Collectors.toList());
            customer.setPets(pets);
        }

        return customer;
    }

    private CustomerDTO convertCustomerEntityToDTO(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();

        BeanUtils.copyProperties(customer,customerDTO);
        List<Long> petIds = customer.getPets() != null ?
                customer.getPets().stream().map(Pet::getId).collect(Collectors.toList()) :
                new ArrayList<>();

        customerDTO.setPetIds(petIds);

        return customerDTO;
    }

    private Employee convertDTOToEmployeeEntity(EmployeeDTO employeeDTO){
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);

        employee.setSkills(employeeDTO.getSkills());
        employee.setDaysAvailable(employeeDTO.getDaysAvailable());
        return employee;
    }
    private EmployeeDTO convertEmployeeEntityToDTO(Employee employee){
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee,employeeDTO);

        employeeDTO.setSkills(employee.getSkills());
        employeeDTO.setDaysAvailable(employee.getDaysAvailable());
        return employeeDTO;
    }
}