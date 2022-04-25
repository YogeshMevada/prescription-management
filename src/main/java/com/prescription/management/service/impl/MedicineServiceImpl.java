package com.prescription.management.service.impl;

import com.prescription.management.constant.ResponseStatus;
import com.prescription.management.constant.Unit;
import com.prescription.management.dto.request.AddMedicineRequest;
import com.prescription.management.dto.response.ApiResponse;
import com.prescription.management.dto.response.MedicineResponse;
import com.prescription.management.entities.Medicine;
import com.prescription.management.repository.MedicineRepository;
import com.prescription.management.service.MedicineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;

    @Autowired
    public MedicineServiceImpl(final MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    @Override
    public MedicineResponse getMedicine(final String medicineReferenceNumber) throws Exception {
        log.info("Medicine service - get by medicine reference number");
        final Medicine medicine = medicineRepository.findByMedicineReferenceNumber(medicineReferenceNumber);
        if (Objects.isNull(medicine)) {
            log.error("No medicine found");
            throw new Exception("No medicine found");
        }
        return map(medicine);
    }

    @Override
    public List<MedicineResponse> getMedicines() throws Exception {
        log.info("Medicine service - find all");
        final List<Medicine> medicines = medicineRepository.findAll();
        if (medicines.isEmpty()) {
            log.error("No medicines found");
            throw new Exception("No medicines found");
        }
        return medicines.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public Medicine findMedicine(final long medicineId, final String medicineReferenceNumber) {
        log.info("Medicine service - find by medicine id & reference number");
        return medicineRepository.findByIdAndMedicineReferenceNumber(medicineId, medicineReferenceNumber);
    }

    @Override
    public ApiResponse create(final AddMedicineRequest addMedicineRequest) throws Exception {
        log.info("Medicine service - create medicine");

        final Medicine medicineDuplicate = medicineRepository.findDuplicate(addMedicineRequest.getActiveIngredientName(), addMedicineRequest.getBrandName());
        if (Objects.nonNull(medicineDuplicate)) {
            log.error("Medicine is already present");
            throw new Exception("Medicine is already present");
        }

        final Medicine medicine = map(addMedicineRequest);
        medicineRepository.save(medicine);

        log.info("Medicine created successfully");
        return ApiResponse.builder()
                .message("Medicine created successfully")
                .status(ResponseStatus.SUCCESS)
                .build();
    }

    private Medicine map(final AddMedicineRequest addMedicineRequest) {
        final Medicine medicine = new Medicine();
        medicine.setMedicineReferenceNumber(UUID.randomUUID().toString());
        medicine.setBrandName(addMedicineRequest.getBrandName());
        medicine.setActiveIngredientName(addMedicineRequest.getActiveIngredientName());
        medicine.setQuantity(addMedicineRequest.getQuantity());
        medicine.setUnit(Unit.getUnitBySymbol(addMedicineRequest.getUnit()));
        return medicine;
    }

    private MedicineResponse map(final Medicine medicine) {
        return MedicineResponse.builder()
                .medicineId(medicine.getId())
                .medicineReferenceNumber(medicine.getMedicineReferenceNumber())
                .brandName(medicine.getBrandName())
                .activeIngredientName(medicine.getActiveIngredientName())
                .quantity(medicine.getQuantity())
                .unit(medicine.getUnit().getSymbol())
                .build();
    }
}
