package org.infinity.sixtalebackend.domain.scenario.service;

import org.infinity.sixtalebackend.domain.member.dto.MemberResponseDto;
import org.infinity.sixtalebackend.domain.scenario.dto.ScenarioListResponseDto;
import org.infinity.sixtalebackend.domain.scenario.dto.ScenarioResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ScenarioService {

    ScenarioListResponseDto getScenarioList(Long memberID, Long genreID, String title, Pageable diaryPageable);

    ScenarioResponseDto getScenarioInfo(Long scenarioID, Long memberID);
}