package com.prescription.management.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "patient")
public class Patient extends AuditModel {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users user;

    @Column(name = "patient_reference_number", nullable = false)
    private String patientReferenceNumber;
}
