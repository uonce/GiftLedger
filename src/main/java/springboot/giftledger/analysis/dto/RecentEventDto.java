package springboot.giftledger.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.giftledger.entity.GiftLog;

import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecentEventDto {
    private Long giftId;
    private Long amount;
    private String actionType;
    private String payMethod;
    private String memo;

    private String acquaintanceName;
    private String eventType;
    private String eventName;
    private String eventDate;
    private String location;

    public static RecentEventDto from(GiftLog giftLog) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return RecentEventDto.builder()
                .giftId(giftLog.getGiftId())
                .amount(giftLog.getAmount())
                .actionType(giftLog.getActionType().name())
                .payMethod(giftLog.getPayMethod().name())
                .memo(giftLog.getMemo())
                .acquaintanceName(giftLog.getEvent().getAcquaintance().getName())
                .eventType(giftLog.getEvent().getEventType().name())
                .eventName(giftLog.getEvent().getEventName())
                .eventDate(giftLog.getEvent().getEventDate().format(formatter))
                .location(giftLog.getEvent().getLocation())
                .build();
    }
}