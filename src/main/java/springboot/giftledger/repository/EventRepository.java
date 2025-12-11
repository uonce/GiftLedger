package springboot.giftledger.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import springboot.giftledger.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long>{

}