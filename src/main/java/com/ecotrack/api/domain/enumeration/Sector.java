package com.ecotrack.api.domain.enumeration;

/**
 * The Sector enumeration.
 */
public enum Sector {
    IRON_AND_STEEL("Iron and Steel"),
    ALUMINUM("Aluminum"),
    CEMENT("Cement"),
    FERTILIZERS("Fertilizers"),
    ELECTRICITY("Electricity"),
    HYDROGEN("Hydrogen"),
    GLASS("Glass"),
    CHEMICALS("Chemicals"),
    MANUFACTURING("Manufacturing");

    private final String label;

    Sector(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
