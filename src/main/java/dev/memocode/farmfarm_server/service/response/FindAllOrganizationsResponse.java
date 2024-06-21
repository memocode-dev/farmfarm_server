package dev.memocode.farmfarm_server.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindAllOrganizationsResponse {
    private List<FindAllOrganizationsResponse__Organization> organizations;
}
