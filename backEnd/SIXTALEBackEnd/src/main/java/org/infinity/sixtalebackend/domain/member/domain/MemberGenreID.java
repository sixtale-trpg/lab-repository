package org.infinity.sixtalebackend.domain.member.domain;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

// 복합키 설정을 위한 EmbeddedId 설정

@Embeddable
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class MemberGenreID implements Serializable {
    private Long memberID;
    private Long genreID;

}
