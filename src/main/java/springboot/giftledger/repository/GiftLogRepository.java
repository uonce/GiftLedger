package springboot.giftledger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springboot.giftledger.entity.EventAcquaintance;
import springboot.giftledger.entity.GiftLog;

import java.util.List;

@Repository
public interface GiftLogRepository extends JpaRepository<GiftLog, Long> {


//    void deleteByEvent_EventId(Long eventId);

    void deleteByGiftId(Long giftId);

    List<GiftLog> findAllByEventAcquaintance(EventAcquaintance eventAcquaintance);
}
