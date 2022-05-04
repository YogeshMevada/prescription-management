package com.prescription.management.entities;

import com.prescription.commons.constant.Status;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "address")
public class Address extends AuditModel {

    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "building_name", nullable = false)
    private String buildingName;

    @Column(name = "street")
    private String street;

    @Column(name = "zip", nullable = false)
    private String zip;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
}
