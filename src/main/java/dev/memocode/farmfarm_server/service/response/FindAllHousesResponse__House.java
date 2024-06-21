package dev.memocode.farmfarm_server.service.response;

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
public class FindAllHousesResponse__House {
    private UUID id;
    private String name;
    private Instant createdAt;
    private Instant updatedAt;
}
