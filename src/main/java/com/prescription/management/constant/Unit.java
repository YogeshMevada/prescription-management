package com.prescription.management.constant;

import org.apache.commons.lang3.StringUtils;

public enum Unit {

    KG("kilogram", "Kg"),
    GM("gram", "g"),
    MG("milligram", "mg"),
    MCG("microgram", "mcg"),
    LTR("litre", "L"),
    ML("millilitre", "ml"),
    CC("cubic centimetre", "cc"),
    MOL("mole", "mol"),
    MMOL("millimole", "mmol");

    private String text;
    private String symbol;

    public static Unit getUnitByText(final String text) {
        if (StringUtils.isEmpty(text)) {
            throw new IllegalArgumentException("Unit text can not be empty");
        }
        for (final Unit unit : values()) {
            if (text.equalsIgnoreCase(unit.getText())) {
                return unit;
            }
        }
        throw new IllegalArgumentException("Unit test not found");
    }

    public static Unit getUnitBySymbol(final String symbol) {
        if (StringUtils.isEmpty(symbol)) {
            throw new IllegalArgumentException("Unit symbol can not be empty");
        }
        for (final Unit unit : values()) {
            if (symbol.equalsIgnoreCase(unit.getSymbol())) {
                return unit;
            }
        }
        throw new IllegalArgumentException("Unit symbol not found");
    }

    Unit(final String text, final String symbol) {
        this.text = text;
        this.symbol = symbol;
    }

    public String getText() {
        return text;
    }

    public String getSymbol() {
        return symbol;
    }
}
