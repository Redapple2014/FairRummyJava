package org.fcesur.gcs.dto.constants.template;

public enum ServiceFeeType {
    ABSOLUTE,
    PERCENTAGE;

    public static boolean isAbsolute(ServiceFeeType serviceFeeType) {
        return serviceFeeType == ABSOLUTE;
    }

    public static boolean isPercentage(ServiceFeeType serviceFeeType) {
        return serviceFeeType == PERCENTAGE;
    }
}
