package com.udacity.jdnd.course3.critter.Controller;

import com.udacity.jdnd.course3.critter.DTO.PetDTO;
import com.udacity.jdnd.course3.critter.Entity.Customer;
import com.udacity.jdnd.course3.critter.Entity.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.PetService;
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

        Customer owner = customerService.findById(petDTO.getOwnerId());

        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet);
        pet.setOwner(owner);

        Pet savedPet = petService.save(pet);

        if(owner.getPets() == null)
            owner.setPets(new ArrayList<>());

        owner.getPets().add(savedPet);

        BeanUtils.copyProperties(savedPet ,petDTO);

        return petDTO;
    }

    @PostMapping("/{ownerId}")
    public PetDTO savePet(@RequestBody PetDTO petDTO, @PathVariable("ownerId") Long ownerId) {
        petDTO.setOwnerId(ownerId);
        return savePet(petDTO);
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {

        PetDTO petDTO = new PetDTO();
        Pet pet = petService.findById(petId);
        BeanUtils.copyProperties(pet, petDTO);
        petDTO.setOwnerId(pet.getOwner().getId());

        return petDTO;
    }

    @GetMapping
    public List<PetDTO> getPets(){
        return petService.findAll().stream().map(pet -> {
            PetDTO petDTO = new PetDTO();
            BeanUtils.copyProperties(pet, petDTO);
            petDTO.setOwnerId(pet.getOwner().getId());
            return petDTO;
        }).collect(Collectors.toList());
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {

        return customerService.findById(ownerId).getPets().stream().map(pet -> {
            PetDTO petDTO = new PetDTO();
            BeanUtils.copyProperties(pet, petDTO);
            petDTO.setOwnerId(ownerId);
            return petDTO;
        }).collect(Collectors.toList());

    }
}
