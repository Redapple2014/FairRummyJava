package com.fairrummy.mapper;


import com.fairrummy.model.entity.Template;
import com.fairrummy.request.dto.TemplateCreateRequestDTO;
import com.fairrummy.response.dto.LobbyCashGameResponseDTO;
import com.fairrummy.response.dto.TemplateResponseDTO;

import java.util.Map;

public class TemplateMapper {

    public static Template mapToTemplate(TemplateCreateRequestDTO templateCreateRequestDTO) {
        return Template.builder()
              .minBuyin(templateCreateRequestDTO.getMinBuyin())
              .maxBuyin(templateCreateRequestDTO.getMaxBuyin())
              .status(templateCreateRequestDTO.getStatus())
              .gid(templateCreateRequestDTO.getGid())
              .name(templateCreateRequestDTO.getName())
              .minPlayer(templateCreateRequestDTO.getMinPlayer())
              .maxPlayer(templateCreateRequestDTO.getMaxPlayer())
              .noOfCards(templateCreateRequestDTO.getNoOfCards())
              .gameStartTime(templateCreateRequestDTO.getGameStartTime())
              .pointValue(templateCreateRequestDTO.getPointValue())
              .noOfDeck(templateCreateRequestDTO.getNoOfDeck())
              .cardsPerPlayer(templateCreateRequestDTO.getCardsPerPlayer())
              .playerTurnTime(templateCreateRequestDTO.getPlayerTurnTime())
              .serviceFee(templateCreateRequestDTO.getServiceFee())
              .graceTime(templateCreateRequestDTO.getGraceTime())
              .dealsPerGame(templateCreateRequestDTO.getDealsPerGame())
              .variantType(templateCreateRequestDTO.getVariantType())
              .build();
    }

    public static TemplateResponseDTO mapToResponseDTO(Template template) {
        return TemplateResponseDTO.builder()
              .id(template.getId())
              .minBuyin(template.getMinBuyin())
              .maxBuyin(template.getMaxBuyin())
              .status(template.getStatus())
              .gid(template.getGid())
              .name(template.getName())
              .minPlayer(template.getMinPlayer())
              .maxPlayer(template.getMaxPlayer())
              .noOfCards(template.getNoOfCards())
              .gameStartTime(template.getGameStartTime())
              .pointValue(template.getPointValue())
              .noOfDeck(template.getNoOfDeck())
              .cardsPerPlayer(template.getCardsPerPlayer())
              .playerTurnTime(template.getPlayerTurnTime())
              .serviceFee(template.getServiceFee())
              .graceTime(template.getGraceTime())
              .dealsPerGame(template.getDealsPerGame())
              .variantType(template.getVariantType())
              .build();
    }

    public static LobbyCashGameResponseDTO.CashGameTemplate mapToLobbyTemplateResponseDTO(TemplateResponseDTO template) {
        return LobbyCashGameResponseDTO.CashGameTemplate.builder()
              .id(template.getId())
              .minBuyin(template.getMinBuyin())
              .maxBuyin(template.getMaxBuyin())
              .status(template.getStatus())
              .gid(template.getGid())
              .name(template.getName())
              .minPlayer(template.getMinPlayer())
              .maxPlayer(template.getMaxPlayer())
              .noOfCards(template.getNoOfCards())
              .gameStartTime(template.getGameStartTime())
              .pointValue(template.getPointValue())
              .noOfDeck(template.getNoOfDeck())
              .cardsPerPlayer(template.getCardsPerPlayer())
              .playerTurnTime(template.getPlayerTurnTime())
              .serviceFee(template.getServiceFee())
              .graceTime(template.getGraceTime())
              .dealsPerGame(template.getDealsPerGame())
              .variantType(template.getVariantType())
              .build();
    }


}
