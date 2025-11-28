package net.myplayplanet.bwinfbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/** Paginated historical assessment events list. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentEventPageDTO {
    private List<AssessmentEventDTO> items;
    private int page;
    private int pageSize;
    private long totalItems;
}
