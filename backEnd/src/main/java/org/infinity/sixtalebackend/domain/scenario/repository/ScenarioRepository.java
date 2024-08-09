package org.infinity.sixtalebackend.domain.scenario.repository;


import io.lettuce.core.dynamic.annotation.Param;
import org.infinity.sixtalebackend.domain.genre.domain.Genre;
import org.infinity.sixtalebackend.domain.member.domain.Member;
import org.infinity.sixtalebackend.domain.room.domain.Room;
import org.infinity.sixtalebackend.domain.scenario.domain.Scenario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
import java.util.Set;

@Repository
public interface ScenarioRepository extends JpaRepository<Scenario,Long> {

    Scenario findByRoom(Room room);

    /**
     * 시나리오 목록 조회(장르가 있을 경우)
     * @param genreId 장르 필터링
     * @param title 검색할 시나리오 제목
     * @param pageable 좋아요수, 수정일시 순으로 정렬하기 위함, 페이지네이션
     * @return
     */
    @Query("SELECT DISTINCT s FROM Scenario s " +
            "JOIN ScenarioGenre sg ON s.id = sg.scenario.id " +
            "WHERE sg.genre.id = :genreId " +
            "AND (:title IS NULL OR :title = '' OR s.title LIKE %:title%)")
    Page<Scenario> findByGenreIdAndTitleContaining(Long genreId, String title, Pageable pageable);


    /**
     * 시나리오 목록 조회(장르가 없을 경우)
     * @param title 검색할 시나리오 제목
     * @param pageable 좋아요수, 수정일시 순으로 정렬하기 위함, 페이지네이션
     * @return
     */
    @Query("SELECT DISTINCT s FROM Scenario s " +
            "WHERE (:title IS NULL OR :title = '' OR s.title LIKE %:title%)")
    Page<Scenario> findByTitleContaining(String title, Pageable pageable);

    @Query("SELECT s FROM Scenario s JOIN FETCH s.rule WHERE s.id = :id")
    Optional<Scenario> findByIdWithRule(Long id);

    @Query("SELECT s FROM Scenario s WHERE EXISTS (" +
            "SELECT 1 FROM ScenarioLike sl WHERE sl.scenario = s AND sl.member = :member)")
    Page<Scenario> findLikedScenariosByMember(@Param("member") Member member, Pageable scenarioPageable);
}
