package springboot.giftledger.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springboot.giftledger.auth.dto.StatisticsDto;
import springboot.giftledger.enums.ActionType;
import springboot.giftledger.enums.EventType;
import springboot.giftledger.repository.GiftLogRepository;


@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService{

    private final GiftLogRepository giftLogRepository;

    @Override
    public StatisticsDto getEventStatistics(String ages, String eventType, String actionType) {
        EventType eventTypeEnum = EventType.valueOf(eventType);
        ActionType actionTypeEnum = ActionType.valueOf(actionType);

        Double avgAmount = giftLogRepository.findAverageAmountByAgesAndEventTypeAndActionType(ages, eventTypeEnum, actionTypeEnum);

        Long count = giftLogRepository.countByAgesAndEventTypeAndActionType(ages, eventTypeEnum, actionTypeEnum);

        return StatisticsDto.builder()
                .ages(ages)
                .eventType(eventType)
                .averageAmount(avgAmount != null ? avgAmount.longValue() : 0L)
                .totalCount(count)
                .actionType(actionType)
                .build();
    }
}
