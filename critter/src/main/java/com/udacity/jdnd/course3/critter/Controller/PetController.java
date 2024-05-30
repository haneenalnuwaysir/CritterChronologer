package com.udacity.jdnd.course3.critter.Controller;

import com.udacity.jdnd.course3.critter.DTO.PetDTO;
import com.udacity.jdnd.course3.critter.Entity.Pet;
import com.udacity.jdnd.course3.critter.Enum.PetType;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    private PetService petService;
    private CustomerService customerService;

    public PetController(PetService petService, CustomerService customerService) {
        this.petService = petService;
        this.customerService = customerService;}

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet pet= convertDTOToEntity(petDTO);
        return convertEntityToDTO(petService.savePet(pet));
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        return convertEntityToDTO(petService.getPet(petId));
    }

    @GetMapping
    public List<PetDTO> getPets(){
        return petService.getPets()
                .stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {

        return petService.getPetsByOwner(ownerId)
                .stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }
    private PetDTO convertEntityToDTO(Pet pet){
        PetDTO petDTO = new PetDTO();
        petDTO.setId(pet.getId());
        petDTO.setName(pet.getName());
        petDTO.setType(pet.getPetType());
        petDTO.setNotes(pet.getNotes());
        petDTO.setOwnerId(pet.getOwner().getId());
        petDTO.setBirthDate(pet.getBirthDate());
        return petDTO;

    }
    private Pet convertDTOToEntity(PetDTO petDTO){
        Pet pet = new Pet();
        pet.setName(petDTO.getName());
        pet.setPetType(PetType.valueOf(petDTO.getType().name()));
        pet.setNotes(petDTO.getNotes());
        pet.setOwner(customerService.getCustomerById(petDTO.getOwnerId()));
        pet.setBirthDate(petDTO.getBirthDate());
        return pet;
    }
}