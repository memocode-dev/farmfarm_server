package dev.memocode.farmfarm_server.domain.service.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SyncHouseSectionRequest {
    private UUID houseId;
    private UUID houseSectionId;
    private Integer sectionNumber;
    private Long houseSectionVersion;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
    private Boolean deleted;
}
