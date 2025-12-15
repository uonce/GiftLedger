package springboot.giftledger.auth.service;

import springboot.giftledger.auth.dto.StatisticsDto;

public interface StatisticsService {
    StatisticsDto getEventStatistics(String ages, String eventType, String actionType);
}
