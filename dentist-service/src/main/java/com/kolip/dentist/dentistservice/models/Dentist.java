package com.kolip.dentist.dentistservice.models;

import com.kolip.dentist.dentistservice.constants.DentalSpecialization;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Dentist {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DentistSeq")
    private long id;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private Date dateBirth;
    @Column
    private String phoneNumber;
    @Column
    private String email;
    @Column
    private DentalSpecialization dentalSpecialization;
    @Column
    private String graduatedUniversity;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(Date dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public DentalSpecialization getDentalSpecialization() {
        return dentalSpecialization;
    }

    public void setDentalSpecialization(DentalSpecialization dentalSpecialization) {
        this.dentalSpecialization = dentalSpecialization;
    }

    public String getGraduatedUniversity() {
        return graduatedUniversity;
    }

    public void setGraduatedUniversity(String graduatedUniversity) {
        this.graduatedUniversity = graduatedUniversity;
    }

    @Override
    public String toString() {
        return "Dentist{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateBirth=" + dateBirth +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", dentalSpecialization=" + dentalSpecialization +
                ", graduatedUniversity='" + graduatedUniversity + '\'' +
                '}';
    }
}
