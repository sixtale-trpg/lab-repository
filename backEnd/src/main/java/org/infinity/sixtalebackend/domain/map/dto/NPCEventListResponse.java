package org.infinity.sixtalebackend.domain.map.dto;

import lombok.Getter;
import lombok.Setter;
import org.infinity.sixtalebackend.domain.map.domain.NPCEvent;

import java.util.List;

@Getter
@Setter
public class NPCEventListResponse {

    List<NPCEvent> npcEvents;

}
