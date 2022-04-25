package com.prescription.management.service;

import com.prescription.management.entities.Pharmacist;

public interface PharmacistService {

    Pharmacist findByReferenceNumber(String pharmacyReferenceNumber);
}
