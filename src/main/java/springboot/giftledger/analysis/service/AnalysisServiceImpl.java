package springboot.giftledger.analysis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springboot.giftledger.analysis.dto.DashboardDto;
import springboot.giftledger.analysis.dto.PatternDto;
import springboot.giftledger.analysis.dto.RecentEventDto;
import springboot.giftledger.entity.GiftLog;
import springboot.giftledger.entity.Member;
import springboot.giftledger.repository.GiftLogRepository;
import springboot.giftledger.repository.MemberRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalysisServiceImpl implements AnalysisService {

    private final GiftLogRepository giftLogRepository;
    private final MemberRepository memberRepository;

    @Override
    public DashboardDto getDashboard(String email) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        Long memberId = member.getMemberId();


        Long totalGive = giftLogRepository.getTotalGiveByMemberId(memberId);


        Long totalTake = giftLogRepository.getTotalTakeByMemberId(memberId);


        Long balance = totalTake - totalGive;


        Double recoveryRate = totalGive > 0
                ? Math.round((totalTake.doubleValue() / totalGive.doubleValue()) * 1000.0) / 10.0
                : 0.0;


        int currentYear = LocalDate.now().getYear();
        int lastYear = currentYear - 1;

        Long currentYearGive = giftLogRepository.getYearlyGiveByMemberId(memberId, currentYear);
        Long lastYearGive = giftLogRepository.getYearlyGiveByMemberId(memberId, lastYear);

        Long yearChangeAmount = currentYearGive - lastYearGive;
        Double yearChangePercent = lastYearGive > 0
                ? Math.round((yearChangeAmount.doubleValue() / lastYearGive.doubleValue()) * 1000.0) / 10.0
                : 0.0;

        return DashboardDto.builder()
                .totalGive(totalGive)
                .totalTake(totalTake)
                .balance(balance)
                .recoveryRate(recoveryRate)
                .yearChangePercent(yearChangePercent)
                .yearChangeAmount(yearChangeAmount)
                .build();
    }

    @Override
    public List<RecentEventDto> getRecentEvents(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        List<GiftLog> giftLogs = giftLogRepository
                .findTop5ByMemberIdOrderByEventDateDesc(member.getMemberId());

        return giftLogs.stream()
                .limit(5)
                .map(RecentEventDto::from)
                .collect(Collectors.toList());
    }



    @Override
    public PatternDto getPattern(String email, int year) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Long memberId = member.getMemberId();

        Map<Integer, Long> monthlyData = getMonthlyData(memberId, year);

        Map<String, Long> weekdayData = getWeekdayData(memberId, year);

        Map<String, PatternDto.EventTypeData> eventTypeData = getEventTypeData(memberId, year);

        return PatternDto.builder()
                .monthlyData(monthlyData)
                .weekdayData(weekdayData)
                .eventTypeData(eventTypeData)
                .build();
    }

    private Map<Integer, Long> getMonthlyData(Long memberId, int year) {
        List<Object[]> monthlyResults = giftLogRepository.getMonthlyPattern(memberId, year);
        Map<Integer, Long> monthlyData = new HashMap<>();

        for (int i = 1; i <= 12; i++) {
            monthlyData.put(i, 0L);
        }

        for (Object[] row : monthlyResults) {
            Integer month = (Integer) row[0];
            Long amount = ((Number) row[1]).longValue();
            monthlyData.put(month, amount);
        }

        return monthlyData;
    }

    private Map<String, Long> getWeekdayData(Long memberId, int year) {
        List<Object[]> weekdayResults = giftLogRepository.getWeekdayPattern(memberId, year);
        Map<String, Long> weekdayData = new LinkedHashMap<>();

        String[] days = {"일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"};
        for (String day : days) {
            weekdayData.put(day, 0L);
        }

        for (Object[] row : weekdayResults) {
            Integer dayOfWeek = (Integer) row[0];
            Long avgAmount = ((Number) row[1]).longValue();
            weekdayData.put(days[dayOfWeek - 1], avgAmount);
        }

        return weekdayData;
    }

    private Map<String, PatternDto.EventTypeData> getEventTypeData(Long memberId, int year) {
        List<Object[]> eventTypeResults = giftLogRepository.getEventTypeDistribution(memberId, year);
        Map<String, PatternDto.EventTypeData> eventTypeData = new HashMap<>();

        for (Object[] row : eventTypeResults) {
            String eventType = ((Enum<?>) row[0]).name();
            Integer count = ((Number) row[1]).intValue();
            Long amount = ((Number) row[2]).longValue();

            eventTypeData.put(eventType, PatternDto.EventTypeData.builder()
                    .count(count)
                    .amount(amount)
                    .build());
        }

        return eventTypeData;
    }
}
