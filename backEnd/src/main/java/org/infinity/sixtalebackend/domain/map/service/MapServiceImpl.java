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
import org.infinity.sixtalebackend.domain.room.domain.Room;
import org.infinity.sixtalebackend.domain.room.repository.RoomRepository;
import org.infinity.sixtalebackend.domain.scenario.domain.Scenario;
import org.infinity.sixtalebackend.domain.scenario.repository.ScenarioRepository;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class MapServiceImpl implements Mapservice {

    private final RoomRepository roomRepository;
    private final ScenarioRepository scenarioRepository;
    private final MapRepository mapRepository;
    private final PlaceEventRepository placeEventRepository;
    private final NPCEventRepository npcEventRepository;

    @Override
    public MapListResponse getMapList(Long roomID) {

        Room room = roomRepository.findById(roomID).get();
        log.info("room = {}", room.getId());
        Scenario scenario = scenarioRepository.findByRoom(room);
        log.info("scenario = {}", scenario.getId());
        List<Map> mapList = mapRepository.findByScenario(scenario);
        log.info("mapList OK");

        List<MapResponse> maps = mapList.stream()
                .map(m -> MapResponse.builder()
                        .id(m.getId())
                        .name(m.getName())
                        .scenarioID(m.getScenario().getId())
                        .isNpc(m.isNpc())
                        .isPlace(m.isPlace())
                        .imageURL(m.getImageURL())
                        .bgmURL(m.getBgmURL())
                        .build())
                .collect(Collectors.toList());

        return MapListResponse.builder().mapList(maps).build();
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
                .scenarioID(map.getScenario().getId())
                .description(map.getDescription())
                .isNpc(map.isNpc())
                .isPlace(map.isPlace())
                .imageURL(map.getImageURL())
                .bgmURL(map.getBgmURL())
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

    @Override
    public PlaceEventResponse readPlaceEvent(Long roomID, Long mapID, Long placeEventID) {
        PlaceEvent placeEvent = placeEventRepository.findById(placeEventID).get();

        PlaceEventResponse response = PlaceEventResponse.builder()
                .id(placeEvent.getId())
                .map(placeEvent.getMap())
                .row(placeEvent.getRow())
                .col(placeEvent.getCol())
                .description(placeEvent.getDescription())
                .nextMap(placeEvent.getNextMap())
                .build();

        return response;
    }

    @Override
    public NPCEventResponse readNPCEvent(Long roomID, Long mapID, Long npcEventID) {
        NPCEvent npcEvent = npcEventRepository.findById(npcEventID).get();

        NPCEventResponse response = NPCEventResponse.builder()
                .id(npcEvent.getId())
                .map(npcEvent.getMap())
                .description(npcEvent.getDescription())
                .scenarioNPC(npcEvent.getScenarioNPC())
                .currentHp(npcEvent.getCurrentHp())
                .build();

        return response;
    }
}
