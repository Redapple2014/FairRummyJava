package com.fairrummy.dto.constants.template;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TemplateInfo {
    private int templateId;

    public TemplateInfo(int templateId) {
        this.templateId = templateId;
    }
}
