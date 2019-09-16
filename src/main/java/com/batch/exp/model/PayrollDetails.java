package com.batch.exp.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class PayrollDetails implements Serializable {

    private static final long serialVersionUID = 7196276656830859421L;
    private int numericLookUpCode;
    private int numericAssociateId;
    private double amount;
    private long paymentStartDate;
    private int paymentType;
    private long recurringPaymentDate;
}
