package org.infinity.sixtalebackend.domain.map.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.infinity.sixtalebackend.domain.map.domain.Map;
import org.infinity.sixtalebackend.domain.map.domain.NPCEvent;
import org.infinity.sixtalebackend.domain.map.domain.PlaceEvent;
import org.infinity.sixtalebackend.domain.map.dto.*;
import org.infinity.sixtalebackend.domain.map.repository.MapRepository;
import org.infinity.sixtalebackend.domain.map.repository.NPCEventRepository;
import org.infinity.sixtalebackend.domain.map.repository.PlaceEventRepository;
import org.infinity.sixtalebackend.domain.scenario.domain.Scenario;
import org.infinity.sixtalebackend.domain.scenario.repository.ScenarioRepository;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class MapServiceImpl implements Mapservice {

    private final ScenarioRepository scenarioRepository;
    private final MapRepository mapRepository;
    private final PlaceEventRepository placeEventRepository;
    private final NPCEventRepository npcEventRepository;

    @Override
    public MapListResponse getMapList(Long roomID) {

        Scenario scenario = scenarioRepository.findById(roomID).get();
        List<Map> maps = mapRepository.findByScenario(scenario);

        MapListResponse response = new MapListResponse();
        response.setMapList(maps);

        return response;
    }

    @Override
    public MapResponse readMap(Long roomID, Long mapID) {

        System.out.println("roomID = "+ roomID+" mapID="+ mapID);

        Map map = mapRepository.findById(mapID).get();

        System.out.println("***************map************");
        System.out.println("id="+map.getId());
        System.out.println("scenario="+map.getScenario());
        System.out.println("description="+map.getDescription());

        MapResponse response = MapResponse.builder()
                .id(map.getId())
                .scenario(map.getScenario())
                .description(map.getDescription())
                .isNpc(map.isNpc())
                .isPlace(map.isPlace())
                .imageURL(map.getImageURL())
                .bgmURL(map.getBgmURL())
                .createdAt(map.getCreatedAt())
                .build();

        return response;
    }

    @Override
    public PlaceEventListResponse getPlaceEventList(Long roomID, Long mapID) {
        Map map = mapRepository.findById(mapID).get();
        List<PlaceEvent> placeEvents = placeEventRepository.findByMap(map);

        PlaceEventListResponse response = new PlaceEventListResponse();
        response.setPlaceEvents(placeEvents);

        return response;
    }

    @Override
    public NPCEventListResponse getNPCEventList(Long roomID, Long mapID) {
        Map map = mapRepository.findById(mapID).get();
        List<NPCEvent> npcEvents = npcEventRepository.findByMap(map);

        NPCEventListResponse response = new NPCEventListResponse();
        response.setNpcEvents(npcEvents);

        return response;
    }
}
