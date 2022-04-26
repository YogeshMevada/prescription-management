package com.prescription.management.service.impl;

import com.prescription.commons.constant.ResponseStatus;
import com.prescription.commons.constant.Unit;
import com.prescription.commons.dto.request.AddMedicineRequest;
import com.prescription.commons.dto.response.ApiResponse;
import com.prescription.commons.dto.response.MedicineResponse;
import com.prescription.commons.dto.response.PageResponse;
import com.prescription.commons.dto.search.MedicineSearchRequest;
import com.prescription.management.entities.Medicine;
import com.prescription.management.repository.MedicineRepository;
import com.prescription.management.service.MedicineService;
import com.prescription.management.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;

    @Autowired
    public MedicineServiceImpl(final MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    @Override
    public ApiResponse<MedicineResponse> getMedicine(final String medicineReferenceNumber) throws Exception {
        log.info("Medicine service - get by medicine reference number");
        final Medicine medicine = medicineRepository.findByMedicineReferenceNumber(medicineReferenceNumber);
        if (Objects.isNull(medicine)) {
            log.error("No medicine found for reference number-{}", medicineReferenceNumber);
            return ApiResponse.<MedicineResponse>builder()
                    .message(String.format("No medicine found for reference number-%s", medicineReferenceNumber))
                    .status(ResponseStatus.ERROR)
                    .build();
        }
        return ApiResponse.<MedicineResponse>builder()
                .data(map(medicine))
                .status(ResponseStatus.SUCCESS)
                .build();
    }

    @Override
    public PageResponse<MedicineResponse> getMedicines(final MedicineSearchRequest medicineSearchRequest) throws Exception {
        log.info("Medicine service - find all");
        final Pageable pageable = CommonUtil.getPageableInfo(medicineSearchRequest);
        final Page<Medicine> medicines = medicineRepository.findAll(searchSpecification(medicineSearchRequest), pageable);

        return CommonUtil.createPageResponse(medicines, this::map);
    }

    @Override
    public Medicine findMedicine(final long medicineId, final String medicineReferenceNumber) {
        log.info("Medicine service - find by medicine id & reference number");
        return medicineRepository.findByIdAndMedicineReferenceNumber(medicineId, medicineReferenceNumber);
    }

    @Override
    public ApiResponse<MedicineResponse> create(final AddMedicineRequest addMedicineRequest) throws Exception {
        log.info("Medicine service - create medicine");

        final Medicine medicineDuplicate = medicineRepository.findDuplicate(addMedicineRequest.getActiveIngredientName(), addMedicineRequest.getBrandName());
        if (Objects.nonNull(medicineDuplicate)) {
            log.error("Medicine is already present");
            throw new Exception("Medicine is already present");
        }

        final Medicine medicine = map(addMedicineRequest);
        final Medicine savedMedicine = medicineRepository.save(medicine);

        log.info("Medicine created successfully");
        return ApiResponse.<MedicineResponse>builder()
                .data(map(savedMedicine))
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

    private Specification<Medicine> searchSpecification(final MedicineSearchRequest medicineSearchRequest) {
        Specification<Medicine> specification = (medicine, query, cb) -> cb.equal(cb.literal(1), 1);
        if (StringUtils.isNotEmpty(medicineSearchRequest.getMedicineReferenceNumber())) {
            specification = specification.and((medicine, query, cb) -> cb.equal(medicine.get("medicineReferenceNumber"), medicineSearchRequest.getMedicineReferenceNumber()));
        }
        if (StringUtils.isNotEmpty(medicineSearchRequest.getBrandName())) {
            specification = specification.and((medicine, query, cb) -> cb.like(medicine.get("brandName"), "%".concat(medicineSearchRequest.getBrandName()).concat("%")));
        }
        if (StringUtils.isNotEmpty(medicineSearchRequest.getActiveIngredientName())) {
            specification = specification.and((medicine, query, cb) -> cb.like(medicine.get("activeIngredientName"), "%".concat(medicineSearchRequest.getActiveIngredientName()).concat("%")));
        }
        return specification;
    }
}
