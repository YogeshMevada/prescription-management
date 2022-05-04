package com.prescription.management.entities;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;

@Data
@Entity
@Table(name = "prescription_item")
public class PrescriptionItem {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "medicine_id")
    private Medicine medicine;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "instructions", nullable = false)
    private String instructions;
}
