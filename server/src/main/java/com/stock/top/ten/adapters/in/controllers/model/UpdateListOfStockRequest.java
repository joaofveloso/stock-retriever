package com.stock.top.ten.adapters.in.controllers.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;
import java.util.List;

public record UpdateListOfStockRequest(
        @NotBlank(message = "End date is mandatory")
        @JsonFormat(pattern = "MM-dd-yyyy") LocalDate endDate,
        List<String> stocks) {
}