package springboot.giftledger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import springboot.giftledger.entity.GiftLog;
import springboot.giftledger.enums.ActionType;
import springboot.giftledger.enums.EventType;

import java.util.List;

@Repository
public interface GiftLogRepository extends JpaRepository<GiftLog, Long> {
    @Query("""
        SELECT AVG(g.amount)
        FROM GiftLog g
        JOIN g.event e
        JOIN e.acquaintance a
        JOIN a.member m
        WHERE m.ages = :ages
        AND e.eventType = :eventType
        AND g.actionType = :actionType
    """)
    Double findAverageAmountByAgesAndEventTypeAndActionType(
            @Param("ages") String ages,
            @Param("eventType") EventType eventType,
            @Param("actionType") ActionType actionType
    );

    @Query("""
        SELECT COUNT(g)
        FROM GiftLog g
        JOIN g.event e
        JOIN e.acquaintance a
        JOIN a.member m
        WHERE m.ages = :ages
        AND e.eventType = :eventType
        AND g.actionType = :actionType
    """)
    Long countByAgesAndEventTypeAndActionType(
            @Param("ages") String ages,
            @Param("eventType") EventType eventType,
            @Param("actionType") ActionType actionType
    );


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


    // 특정 연도의 월별 지출 (1~12월)
    @Query("SELECT MONTH(e.eventDate) as month, SUM(g.amount) as amount " +
            "FROM GiftLog g " +
            "JOIN g.event e " +
            "JOIN e.acquaintance a " +
            "WHERE a.member.memberId = :memberId " +
            "AND g.actionType = 'GIVE' " +
            "AND YEAR(e.eventDate) = :year " +
            "GROUP BY MONTH(e.eventDate)")
    List<Object[]> getMonthlyPattern(@Param("memberId") Long memberId,
                                     @Param("year") int year);

    // 특정 연도의 요일별 평균 지출 (일~토)
    @Query("SELECT DAYOFWEEK(e.eventDate) as dayOfWeek, AVG(g.amount) as avgAmount " +
            "FROM GiftLog g " +
            "JOIN g.event e " +
            "JOIN e.acquaintance a " +
            "WHERE a.member.memberId = :memberId " +
            "AND g.actionType = 'GIVE' " +
            "AND YEAR(e.eventDate) = :year " +
            "GROUP BY DAYOFWEEK(e.eventDate)")
    List<Object[]> getWeekdayPattern(@Param("memberId") Long memberId,
                                     @Param("year") int year);

    // 특정 연도의 이벤트 타입별 분포 (결혼식, 장례식, 생일, 기타)
    @Query("SELECT e.eventType, COUNT(g), SUM(g.amount) " +
            "FROM GiftLog g " +
            "JOIN g.event e " +
            "JOIN e.acquaintance a " +
            "WHERE a.member.memberId = :memberId " +
            "AND g.actionType = 'GIVE' " +
            "AND YEAR(e.eventDate) = :year " +
            "GROUP BY e.eventType")
    List<Object[]> getEventTypeDistribution(@Param("memberId") Long memberId,
                                            @Param("year") int year);
}