package com.udacity.jdnd.course3.critter.Entity;

import com.udacity.jdnd.course3.critter.Enum.PetType;
import lombok.Data;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "pet")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nationalized
    @Column(length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    private PetType petType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Customer owner;

    private LocalDate birthDate;
    private String notes;

    @ManyToMany(mappedBy = "pets")
    private List<Schedule> schedules;

}
