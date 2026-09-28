package com.mams.dto;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
public record AssignmentRequest(@NotNull Long baseId,@NotNull Long equipmentTypeId,@NotBlank String personnelName,@NotNull @Min(1) Integer quantity,@NotNull LocalDate assignedDate) {}
