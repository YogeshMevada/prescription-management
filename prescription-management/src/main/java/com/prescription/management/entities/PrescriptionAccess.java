package com.prescription.management.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "prescription_access")
public class PrescriptionAccess extends AuditModel {

    @Column(name = "access_reference_number", nullable = false)
    private String accessReferenceNumber;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    private Doctor doctor;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "pharmacist_id", referencedColumnName = "id")
    private Pharmacist pharmacist;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    private Patient patient;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "prescription_id", referencedColumnName = "id")
    private Prescription prescription;

    @Column(name = "is_approved")
    private boolean approved = false;
}
