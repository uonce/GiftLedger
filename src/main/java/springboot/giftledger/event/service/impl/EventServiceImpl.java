package springboot.giftledger.event.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import springboot.giftledger.acquaintance.dto.AcquaintanceDto;
import springboot.giftledger.common.dto.ResultDto;
import springboot.giftledger.entity.Acquaintance;
import springboot.giftledger.entity.Event;
import springboot.giftledger.entity.EventAcquaintance;
import springboot.giftledger.entity.GiftLog;
import springboot.giftledger.enums.ActionType;
import springboot.giftledger.enums.EventType;
import springboot.giftledger.enums.PayMethod;
import springboot.giftledger.enums.Relation;
import springboot.giftledger.event.dto.EventDto;
import springboot.giftledger.event.dto.EventListResponse;
import springboot.giftledger.event.dto.EventUpdateRequest;
import springboot.giftledger.event.dto.EventUpdateResponse;
import springboot.giftledger.event.dto.GiftLogDto;
import springboot.giftledger.event.service.EventService;
import springboot.giftledger.repository.AcquaintanceRepository;
import springboot.giftledger.repository.EventAcquaintanceRepository;
import springboot.giftledger.repository.EventRepository;
import springboot.giftledger.repository.GiftLogRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService{
	
	private final EventRepository eventRepository;
	private final AcquaintanceRepository acqRepository;
	private final EventAcquaintanceRepository eventAcqRepository;
	private final GiftLogRepository giftLogRepository;
	
	
	@Override
	@Transactional
	public ResultDto<EventUpdateResponse> updateEvent(long eventId, EventUpdateRequest req, String userName) {
		
		EventDto eventDto= req.getEvent();
		AcquaintanceDto acquaintanceDto= req.getAcquaintance();
		GiftLogDto giftLogDto = req.getGiftLog();
		
		// 이벤트 정보 -> 본인 확인
		Event event = eventRepository.findById(eventId).orElseThrow(
						() -> new IllegalArgumentException("해당 이벤트가 존재하지 않습니다.")
					);

		if(!event.getMember().getEmail().equals(userName)) {
			throw new AccessDeniedException("해당 이벤트를 수정할 권한이 없습니다.");
		}
		
		
		
		// 지인 정보 가져오기
		Acquaintance acq = acqRepository.findById(req.getAcquaintance().getAcquaintanceId()).orElseThrow(
					() -> new IllegalArgumentException("해당 지인 정보가 존재하지 않습니다")
				);	
		
		
		// GiftLog 가져오기
		GiftLog giftLog = giftLogRepository.findById(giftLogDto.getGiftId()).orElseThrow(
						() -> new IllegalArgumentException("해당 경조사비 내역이 존재하지 않습니다.")
					);
		
		if( giftLog.getEventAcquaintance().getAcquaintance().getAcquaintanceId() != acq.getAcquaintanceId()
			|| giftLog.getEventAcquaintance().getEvent().getEventId() != event.getEventId()) {
			throw new IllegalArgumentException("요청이 올바르지 않습니다");
			
		}
		
		// 지인 정보 수정
	    acq.setName(acquaintanceDto.getName());
	    acq.setRelation(Relation.fromDescription(acquaintanceDto.getRelation()));
	    acq.setGroupName(acquaintanceDto.getGroupName());
	    acq.setPhone(acquaintanceDto.getPhone());
		
		// 이벤트 정보 수정
		event.setEventType(EventType.fromDescription(eventDto.getEventType()));
	    event.setEventName(eventDto.getEventName());
	    event.setEventDate(eventDto.getEventDate());
	    event.setLocation(eventDto.getLocation());
	    
	    
	    // 경조사비 정보 수정
	    giftLog.setAmount(giftLogDto.getAmount());
	    giftLog.setActionType(ActionType.fromDescription(giftLogDto.getActionType()));
	    giftLog.setPayMethod(PayMethod.fromDescription(giftLogDto.getPayMethod()));
	    giftLog.setMemo(giftLogDto.getMemo());
	    
		
	    
	    EventUpdateResponse response = EventUpdateResponse.builder()
	    												  .acquaintance(toAcquaintanceDto(acq))
	    												  .event(toEventDto(event))
	    												  .giftLog(toGiftLogDto(giftLog))
	    												  .build();
	    
		
		return ResultDto.of("success", response);
	}


	@Override
	public ResultDto<Page<EventListResponse>> eventList(String email, Pageable pageable) {

	    Page<Event> eventPage =
	            eventRepository.findByMember_Email(email, pageable);

	    Page<EventListResponse> mapped =
	    		eventPage.map(event -> {

	    		    long totalAmount = Optional.ofNullable(
	    		            giftLogRepository.sumAmountByEventId(event.getEventId())
	    		    ).orElse(0L);

	    		    EventDto response = toEventDto(event);
	    		    response.totalAmount(totalAmount);

	    		    // 내가 주최한 이벤트
	    		    if (Boolean.TRUE.equals(event.getIsOwner())) {

	    		        return EventListResponse.builder()
	    		                .event(response)
	    		                .ownerName(event.getMember().getName())
	    		                .relation("본인")
	    		                .memo("")
	    		                .build();
	    		    }

	    		    List<EventAcquaintance> eaList =
	    		            eventAcqRepository.findAllByEventIdWithAcquaintance(event.getEventId());

	    		    if (eaList.isEmpty()) {
	    		        return EventListResponse.builder()
	    		                .event(response)
	    		                .ownerName("미등록")
	    		                .relation("")
	    		                .memo("")
	    		                .build();
	    		    }

	    		    EventAcquaintance ea = eaList.get(0);

	    		    Optional<GiftLog> giftOpt =
	    		            giftLogRepository.findFirstByEventId(event.getEventId());

	    		    return EventListResponse.builder()
	    		            .event(response)
	    		            .ownerName(ea.getAcquaintance().getName())
	    		            .relation(ea.getAcquaintance().getRelation().getDescription())
	    		            .memo(giftOpt.map(GiftLog::getMemo).orElse(""))
	    		            .build();
	    		});

	    return ResultDto.of("success", mapped);
	}
	
	
	
	/* toDto Method*/
	
	private EventDto toEventDto(Event event) {
	    return EventDto.builder()
	            .eventId(event.getEventId())
	            .eventType(event.getEventType().getDescription())
	            .eventName(event.getEventName())
	            .eventDate(event.getEventDate())
	            .location(event.getLocation())
	            .isOwner(event.getIsOwner())
	            .build();
	}
	
	private AcquaintanceDto toAcquaintanceDto(Acquaintance acq) {
	    return AcquaintanceDto.builder()
	            .acquaintanceId(acq.getAcquaintanceId())
	            .memberId(acq.getMember().getMemberId())
	            .name(acq.getName())
	            .relation(acq.getRelation().getDescription())
	            .groupName(acq.getGroupName())
	            .phone(acq.getPhone())
	            .build();
	}
	
	private GiftLogDto toGiftLogDto(GiftLog gl) {
	    return GiftLogDto.builder()
	            .giftId(gl.getGiftId())
	            .amount(gl.getAmount())
	            .actionType(gl.getActionType().getType())
	            .payMethod(gl.getPayMethod().getDescription())
	            .memo(gl.getMemo())
	            .build();
	}



	

	
	
}
