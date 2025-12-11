package springboot.giftledger.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatternDto {
    private Map<String, Long> monthlyData;
    private Map<String, Long> weekdayData;
}
