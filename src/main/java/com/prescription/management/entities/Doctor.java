package com.prescription.management.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "doctor")
public class Doctor extends AuditModel {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users user;

    @Column(name = "doctor_reference_number", nullable = false)
    private String doctorReferenceNumber;

    @Column(name = "degree")
    private String degree;
}
