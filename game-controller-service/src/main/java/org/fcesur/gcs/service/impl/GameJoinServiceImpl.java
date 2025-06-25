package org.fcesur.gcs.service.impl;

import org.fcesur.gcs.http.impl.GameEngineRestClient;
import org.fcesur.gcs.request.dto.FMGRequest;
import org.fcesur.gcs.response.dto.FMGResponse;
import org.fcesur.gcs.response.dto.TemplateResponseDTO;
import org.fcesur.gcs.service.TemplateService;
import org.fcesur.gcs.service.message.GameJoinService;
import org.fcesur.gcs.utility.GCSTableStatus;
import org.fcesur.gcs.utility.TableInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameJoinServiceImpl implements GameJoinService {

    @Autowired
    private GameEngineRestClient geRestClient;

    @Autowired
    private TemplateService templateService;

    @Override
    public FMGResponse joinTable(FMGRequest request) {
        int templateId = request.getTemplateId();

        TemplateResponseDTO templateResponseDTO = templateService.getTemplate(templateId);
        FMGResponse response = null;
        TableInfo tableInfo = GCSTableStatus.getBestTable(templateId);
        if (tableInfo != null) {
            response = new FMGResponse();
            response.setTableId(tableInfo.getTableId());
            response.setEngineIP(tableInfo.getEngineIp());

            return response;
        }

        response = geRestClient.tableJoin(templateId);

        if (response != null) {
            GCSTableStatus.updateTableInfo(response, templateId, templateResponseDTO.getMaxPlayer());
        } else {
            response = new FMGResponse();
            response.setErrorMessage("Unable to find the table");
        }
        return response;
    }
}
