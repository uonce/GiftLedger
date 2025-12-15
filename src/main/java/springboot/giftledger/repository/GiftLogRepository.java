package springboot.giftledger.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import springboot.giftledger.entity.GiftLog;

@Repository
public interface GiftLogRepository extends JpaRepository<GiftLog, Long> {
//    @Query("""
//        SELECT AVG(g.amount)
//        FROM GiftLog g
//        JOIN g.event e
//        JOIN e.acquaintance a
//        JOIN a.member m
//        WHERE m.ages = :ages
//        AND e.eventType = :eventType
//        AND g.actionType = :actionType
//    """)
//    Double findAverageAmountByAgesAndEventTypeAndActionType(
//            @Param("ages") String ages,
//            @Param("eventType") EventType eventType,
//            @Param("actionType") ActionType actionType
//    );
//
//    @Query("""
//        SELECT COUNT(g)
//        FROM GiftLog g
//        JOIN g.event e
//        JOIN e.acquaintance a
//        JOIN a.member m
//        WHERE m.ages = :ages
//        AND e.eventType = :eventType
//        AND g.actionType = :actionType
//    """)
//    Long countByAgesAndEventTypeAndActionType(
//            @Param("ages") String ages,
//            @Param("eventType") EventType eventType,
//            @Param("actionType") ActionType actionType
//    );
//    
//    
//    
//    GiftLog findFirstByEventOrderByGiftIdDesc(Event event);
	
	
	
    @Query("SELECT COALESCE(SUM(g.amount), 0) " +
            "FROM GiftLog g " +
            "WHERE g.eventAcquaintance.event.eventId = :eventId")
    Long sumAmountByEventId(@Param("eventId") Long eventId);
    
    
    @Query("""
    	    select g
    	    from GiftLog g
    	    join g.eventAcquaintance ea
    	    where ea.event.eventId = :eventId
    	    order by g.giftId asc
    	""")
    	Optional<GiftLog> findFirstByEventId(@Param("eventId") Long eventId);
}
