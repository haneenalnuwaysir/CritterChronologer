package com.udacity.jdnd.course3.critter.Controller;

import com.udacity.jdnd.course3.critter.DTO.PetDTO;
import com.udacity.jdnd.course3.critter.Entity.Customer;
import com.udacity.jdnd.course3.critter.Entity.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    private PetService petService;

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Customer owner = null;

        if (petDTO.getOwnerId() != 0) {
            owner = this.customerService.findOwnerById(petDTO.getOwnerId());
        }

        Pet pet = convertPetDTOToPet(petDTO);
        pet.setCustomer(owner);
        Pet savedPet = petService.savePet(pet);

        if (owner != null) {
            owner.addPet(savedPet);
        }
        return convertPetToPetDTO(savedPet);
//
//        Customer customer = customerService.getById(petDTO.getOwnerId());
//
//        Pet pet = new Pet();
//        BeanUtils.copyProperties(petDTO, pet);
//        pet.setCustomer(customer);
//
//        Pet savedPet = petService.savePet(pet);
//
//        if(customer.getPets() == null)
//            customer.setPets(new ArrayList<>());
//
//        customer.getPets().add(savedPet);
//
//        BeanUtils.copyProperties(savedPet ,petDTO);
//
//        return petDTO;
    }

//    @PostMapping("/{ownerId}")
//    public PetDTO savePet(@RequestBody PetDTO petDTO, @PathVariable("ownerId") Long ownerId) {
//        petDTO.setOwnerId(ownerId);
//        return savePet(petDTO);
//    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {

        PetDTO petDTO = new PetDTO();
        Pet pet = petService.getById(petId);
        BeanUtils.copyProperties(pet, petDTO);
        petDTO.setOwnerId(pet.getCustomer().getId());

        return petDTO;
    }

    @GetMapping
    public List<PetDTO> getPets(){
        return petService.findAll().stream().map(pet -> {
            PetDTO petDTO = new PetDTO();
            BeanUtils.copyProperties(pet, petDTO);
            petDTO.setOwnerId(pet.getCustomer().getId());
            return petDTO;
        }).collect(Collectors.toList());
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {

        return customerService.getById(ownerId).getPets().stream().map(pet -> {
            PetDTO petDTO = new PetDTO();
            BeanUtils.copyProperties(pet, petDTO);
            petDTO.setOwnerId(ownerId);
            return petDTO;
        }).collect(Collectors.toList());

    }

    private PetDTO convertPetToPetDTO(Pet pet){
        PetDTO petDTO = new PetDTO();
        // in order for copyProperties to work, properties of the DTO and normal object must match in name
        BeanUtils.copyProperties(pet, petDTO);
        if (pet.getCustomer() != null) {
            petDTO.setOwnerId(pet.getCustomer().getId());
        }
        return petDTO;
    }

    private Pet convertPetDTOToPet(PetDTO petDTO){
        ModelMapper modelMapper = new ModelMapper();
        Pet pet = modelMapper.map(petDTO, Pet.class);
        return pet;
    }
}
