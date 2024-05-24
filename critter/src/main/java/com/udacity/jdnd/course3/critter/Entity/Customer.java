package com.udacity.jdnd.course3.critter.Entity;

import lombok.Data;
import org.hibernate.annotations.Nationalized;
import org.springframework.boot.test.autoconfigure.data.cassandra.DataCassandraTest;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nationalized
    @Column(length = 500)
    private String name;
    private String phoneNumber;

    @Column(length = 1000)
    private String notes;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<Pet> pets;

}
