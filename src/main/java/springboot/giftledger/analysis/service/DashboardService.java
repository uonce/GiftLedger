package springboot.giftledger.analysis.service;

import springboot.giftledger.analysis.dto.DashboardDto;
import springboot.giftledger.entity.GiftLog;
import springboot.giftledger.entity.Member;

import java.util.List;

public interface DashboardService {

    DashboardDto getDashboard(Member member);

    List<GiftLog> getRecentEvents(Member member);
}
