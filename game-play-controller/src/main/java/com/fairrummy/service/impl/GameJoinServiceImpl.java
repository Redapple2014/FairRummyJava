package com.fairrummy.service.impl;

import com.fairrummy.http.impl.GameEngineRestClient;
import com.fairrummy.request.dto.FMGRequest;
import com.fairrummy.response.dto.FMGResponse;
import com.fairrummy.response.dto.TemplateResponseDTO;
import com.fairrummy.service.TemplateService;
import com.fairrummy.service.message.GameJoinService;
import com.fairrummy.utility.GCSTableStatus;
import com.fairrummy.utility.TableInfo;
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
        if (tableInfo != null)
        {
            response = new FMGResponse();
            response.setTableId(tableInfo.getTableId());
            response.setEngineIP(tableInfo.getEngineIp());

            return  response;
        }

        response = geRestClient.tableJoin(templateId);

        if( response != null )
        {
            GCSTableStatus.updateTableInfo(response, templateId, templateResponseDTO.getMaxPlayer());
        }
        else
        {
            response = new FMGResponse();
            response.setErrorMessage("Unable to find the table");
        }
        return  response;
    }
}
