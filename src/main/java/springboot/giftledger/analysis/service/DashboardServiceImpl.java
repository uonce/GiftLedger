package springboot.giftledger.analysis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springboot.giftledger.analysis.dto.DashboardDto;
import springboot.giftledger.analysis.repository.GiftLogRepository;
import springboot.giftledger.entity.GiftLog;
import springboot.giftledger.entity.Member;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService{

    private final GiftLogRepository giftLogRepository;

    @Override
    public DashboardDto getDashboard(Member member) {
        Long memberId = member.getMemberId();

        Long totalGive = giftLogRepository.getTotalGiveByMemberId(memberId);

        Long totalTake = giftLogRepository.getTotalTakeByMemberId(memberId);

        Long balance = totalGive - totalTake;

        Double recoveryRate = totalGive > 0 ? (totalTake.doubleValue() / totalGive.doubleValue()) * 100 : 0.0;

        int currentYear = LocalDate.now().getYear();
        int lastYear = currentYear - 1;


        return null;
    }

    @Override
    public List<GiftLog> getRecentEvents(Member member) {
        return List.of();
    }
}
