package com.prescription.management.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "medicine")
public class Medicine extends AuditModel {

    @Column(name = "medicine_reference_number", nullable = false)
    private String medicineReferenceNumber;

    @Column(name = "brand_name", nullable = false)
    private String brandName;

    @Column(name = "active_ingredient_name", nullable = false)
    private String activeIngredientName;

    @Column(name = "unit")
    private String unit;
}
