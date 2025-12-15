package springboot.giftledger.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import springboot.giftledger.entity.Acquaintance;

@Repository
public interface AcquaintanceRepository extends JpaRepository<Acquaintance, Long> {

	Page<Acquaintance> findByMemberEmail(String email,Pageable pageable);
	Optional<Acquaintance> findByPhone(String phone);

}
