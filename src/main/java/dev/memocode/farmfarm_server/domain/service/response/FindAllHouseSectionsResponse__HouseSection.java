package dev.memocode.farmfarm_server.domain.service.response;

import dev.memocode.farmfarm_server.domain.entity.SyncStatus;
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
public class FindAllHouseSectionsResponse__HouseSection {
    private UUID id;
    private Integer sectionNumber;
    private Instant createdAt;
    private Instant updatedAt;
    private SyncStatus syncStatus;
}
