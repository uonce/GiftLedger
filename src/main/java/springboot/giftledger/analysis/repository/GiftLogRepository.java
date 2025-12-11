package springboot.giftledger.analysis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import springboot.giftledger.entity.GiftLog;

import java.util.List;

public interface GiftLogRepository extends JpaRepository<GiftLog, Long> {

    @Query("SELECT COALESCE(SUM(g.amount), 0) FROM GiftLog g " +
            "WHERE g.event.acquaintance.member.memberId = :memberId " +
            "AND g.actionType = 'GIVE'")
    Long getTotalGiveByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT COALESCE(SUM(g.amount), 0) FROM GiftLog g " +
            "WHERE g.event.acquaintance.member.memberId = :memberId " +
            "AND g.actionType = 'TAKE'")
    Long getTotalTakeByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT COALESCE(SUM(g.amount), 0) FROM GiftLog g " +
            "WHERE g.event.acquaintance.member.memberId = :memberId " +
            "AND g.actionType = 'GIVE' " +
            "AND YEAR(g.event.eventDate) = :year")
    Long getYearlyGiveByMemberId(@Param("memberId") Long memberId, @Param("year") int year);

    @Query("SELECT g FROM GiftLog g " +
            "WHERE g.event.acquaintance.member.memberId = :memberId " +
            "ORDER BY g.event.eventDate DESC")
    List<GiftLog> findTop5ByMemberIdOrderByEventDateDesc(@Param("memberId") Long memberId);
}
