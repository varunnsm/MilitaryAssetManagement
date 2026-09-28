package com.mams.dto;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
public record TransferRequest(@NotNull Long fromBaseId,@NotNull Long toBaseId,@NotNull Long equipmentTypeId,@NotNull @Min(1) Integer quantity,@NotNull LocalDate transferDate,String remarks) {}
