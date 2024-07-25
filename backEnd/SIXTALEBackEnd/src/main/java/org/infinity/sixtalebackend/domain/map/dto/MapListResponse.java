package org.infinity.sixtalebackend.domain.map.dto;

import lombok.Getter;
import lombok.Setter;
import org.infinity.sixtalebackend.domain.map.domain.Map;

import java.util.List;

@Getter
@Setter
public class MapListResponse {
    private List<Map> mapList;
}
