package com.prescription.management.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "prescription")
public class Prescription extends AuditModel {

    @Column(name = "appointment_number")
    private String appointmentNumber;

    @Column(name = "prescription_reference_number")
    private String prescriptionReferenceNumber;

    @ManyToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    private Patient patient;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "prescription_id")
    private Set<PrescriptionItem> prescriptionItems = new HashSet<>();
}
