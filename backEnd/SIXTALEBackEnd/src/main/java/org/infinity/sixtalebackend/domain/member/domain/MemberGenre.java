package org.infinity.sixtalebackend.domain.member.domain;

import jakarta.persistence.*;
import lombok.*;
import org.infinity.sixtalebackend.domain.model.Genre;

@Entity
@Getter
@Builder
@Table(name = "member_genre")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberGenre {
    @EmbeddedId
    private MemberGenreID id;

    @ManyToOne
    @MapsId("memberID")
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @MapsId("genreID")
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;
}
