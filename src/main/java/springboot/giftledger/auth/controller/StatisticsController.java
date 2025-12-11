package springboot.giftledger.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springboot.giftledger.auth.dto.StatisticsDto;
import springboot.giftledger.auth.service.StatisticsService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/event")
    public ResponseEntity<StatisticsDto> getEventStatistics(@RequestParam String ages, @RequestParam String eventType, @RequestParam(defaultValue = "GIVE") String actionType){
        StatisticsDto statisticsDto = statisticsService.getEventStatistics(ages, eventType, actionType);
        return ResponseEntity.ok(statisticsDto);
    }
}
