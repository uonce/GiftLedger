package springboot.giftledger.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsDto {
    private String ages;
    private String eventType;
    private Long averageAmount;
    private Long totalCount;
    private String actionType;
}
