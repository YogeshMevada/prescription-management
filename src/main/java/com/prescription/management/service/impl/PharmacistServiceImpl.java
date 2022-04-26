package com.prescription.management.service.impl;

import com.prescription.management.entities.Pharmacist;
import com.prescription.management.repository.PharmacistRepository;
import com.prescription.management.service.PharmacistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PharmacistServiceImpl implements PharmacistService {

    private final PharmacistRepository pharmacistRepository;

    @Autowired
    public PharmacistServiceImpl(final PharmacistRepository pharmacistRepository) {
        this.pharmacistRepository = pharmacistRepository;
    }

    @Override
    public Pharmacist findByReferenceNumber(final String pharmacyReferenceNumber) {
        log.info("Pharmacist service - find By ReferenceNumber");
        return pharmacistRepository.findBypharmacyReferenceNumber(pharmacyReferenceNumber);
    }

    @Override
    public Pharmacist save(final Pharmacist pharmacist) {
        return pharmacistRepository.save(pharmacist);
    }
}
