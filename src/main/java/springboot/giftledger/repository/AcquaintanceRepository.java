package springboot.giftledger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springboot.giftledger.entity.Acquaintance;

public interface AcquaintanceRepository extends JpaRepository<Acquaintance,Long> {
    boolean existsByPhone(String phone);

    Acquaintance findByPhone(String phone);
}
