package com.mams.dto;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
public record PurchaseRequest(@NotNull Long baseId,@NotNull Long equipmentTypeId,@NotNull @Min(1) Integer quantity,@NotNull LocalDate purchaseDate,String referenceNumber) {}
