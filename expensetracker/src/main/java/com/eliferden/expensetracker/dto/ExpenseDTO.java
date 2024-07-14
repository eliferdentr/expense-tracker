package com.eliferden.expensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDTO {
    private Long id;
    private Double amount;
    private Long categoryId;
    private String categoryName;
    private String description;
    private Date date;
    private Long userId;
    private String userName;
}
