package com.prescription.management.entities;

import com.prescription.commons.constant.Unit;
import lombok.Data;

import javax.persistence.*;

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

    @Column(name = "quantity", nullable = false)
    private long quantity;

    @Column(name = "unit")
    @Enumerated(EnumType.STRING)
    private Unit unit;
}
