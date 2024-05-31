package com.udacity.jdnd.course3.critter.Repository;

import com.udacity.jdnd.course3.critter.Entity.Customer;
import com.udacity.jdnd.course3.critter.Entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByOwnerId(long ownerId);

//    @Query("select pet from Pet pet where pet.customer.id = : customerId")
//    List<Pet> findPetsByOwnerId(long customerId);

//    List<Pet> getPetsByOwner_Id(Long ownerId);
//
//    @Query("select p.owner from Pet p where p.id = :petId")
//    Customer getOwnerForPet(Long petId);
}
