package com.prescription.management.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "pharmacist")
public class Pharmacist extends AuditModel {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users users;

    @Column(name = "pharmacy_reference_number", nullable = false)
    private String pharmacyReferenceNumber;

    @Column(name = "license_number")
    private String licenseNumber;
}
