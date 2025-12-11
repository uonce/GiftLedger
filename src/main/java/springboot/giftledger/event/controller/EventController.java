package springboot.giftledger.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.giftledger.event.dto.EventRequestDto;
import springboot.giftledger.event.dto.EventResultDto;
import springboot.giftledger.event.service.EventService;
import springboot.giftledger.security.MyUserDetails;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class EventController {

    private final EventService eventService;

    @PostMapping("")
    public ResponseEntity<EventResultDto> insertEvent(
            @AuthenticationPrincipal String email,
            @RequestBody EventRequestDto eventRequestDto) {

        log.info("[EventController - insertEvent] 사용자 정보 email: {}", email);
        log.info("[EventController - insertEvent] 이벤트 등록 요청 eventRequestDto: {}", eventRequestDto);

        try {
            EventResultDto eventResultDto = eventService.insertEvent(email, eventRequestDto);

            log.info("eventResultDto: {}", eventResultDto);
            if ("success".equals(eventResultDto.getResult())) {
                return ResponseEntity.ok(eventResultDto);
            } else {
                log.warn("[EventController - insertEvent] eventResultDto 등록 실패: {}", eventResultDto);
                return ResponseEntity.status(401).body(eventResultDto);
            }
        } catch (Exception e) {
            log.error("[EventController - insertEvent] 서버 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // 500
                    .body(EventResultDto.builder().result("fail").build());
        }

    }
}
