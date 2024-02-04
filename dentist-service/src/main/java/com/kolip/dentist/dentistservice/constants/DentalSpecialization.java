package com.kolip.dentist.dentistservice.constants;

public enum DentalSpecialization {
    GENERAL("general"),
    COSMETIC("cosmetic"),
    ORTHODONTICS("orthodonstics"),
    IMPLANTOLOGY("implantology"),
    PROSTHODONTICS("prosthodontics"),
    PERIODONTICS("periodontics"),
    ENDODONTICS("endodontics"),
    SURGERY("surgey"),
    PEDIATRIC("pediatric");

    private final String value;

    DentalSpecialization(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
