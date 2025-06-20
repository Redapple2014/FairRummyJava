package com.fairrummy.dto.constants.template;

public enum TemplateStatus {
    ACTIVE,
    DRAFTED;

    public static boolean isActive(TemplateStatus templateStatus) {
        return templateStatus == ACTIVE;
    }

    public static boolean isDrafted(TemplateStatus templateStatus) {
        return templateStatus == DRAFTED;
    }
}
