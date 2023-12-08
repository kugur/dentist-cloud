package com.kolip.dentist.authenticationservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
@SequenceGenerator(name = "role_seq", sequenceName = "role_seq", initialValue = 10, allocationSize = 10)
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
