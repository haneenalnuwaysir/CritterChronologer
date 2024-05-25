package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.Entity.Customer;
import com.udacity.jdnd.course3.critter.Repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.Repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PetRepository petRepository;

    public Customer getById(long customerId) {
        return customerRepository.findById(customerId).
                orElseThrow(() -> new UnsupportedOperationException());
    }
    public Customer findOwnerById(Long id){
        Optional<Customer> optionalOwner = this.customerRepository.findById(id);
        if(optionalOwner.isPresent()){
            Customer owner = optionalOwner.get();
            return owner;
        }
        return optionalOwner.orElseThrow(UnsupportedOperationException::new);
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

}
