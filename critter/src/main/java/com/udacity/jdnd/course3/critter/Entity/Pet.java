package com.udacity.jdnd.course3.critter.Entity;

import com.udacity.jdnd.course3.critter.Enum.PetType;
import lombok.Data;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nationalized
    @Column(length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    private PetType type;

    private LocalDate birthDate;

    @Column(length = 500)
    private String notes;

    @ManyToOne
    @JoinColumn(name="owner_Id")
    private Customer owner;

    @ManyToMany(mappedBy = "pets")
    private List<Schedule> schedules;

}
