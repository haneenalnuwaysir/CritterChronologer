package com.udacity.jdnd.course3.critter.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.hibernate.annotations.Nationalized;
import org.springframework.boot.test.autoconfigure.data.cassandra.DataCassandraTest;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Nationalized
    @Column(length = 50)
    private String name;
    private String phoneNumber;
    private String notes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner",
            cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Pet> pets;

    @ManyToMany(mappedBy = "customers")
    private List<Schedule> schedules;

}
