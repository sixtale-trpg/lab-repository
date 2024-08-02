package org.infinity.sixtalebackend.domain.map.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.infinity.sixtalebackend.domain.map.domain.Map;
import org.infinity.sixtalebackend.domain.map.domain.PlaceEvent;

import java.util.List;

@Getter
@Setter
public class PlaceEventListResponse {

    List<PlaceEvent> placeEvents;

}
