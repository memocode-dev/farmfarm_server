package dev.memocode.farmfarm_server.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "organizations")
@EqualsAndHashCode(callSuper = true)
public class Organization extends IdentifiableTimeManagementEntity {
    @Column(name = "name")
    private String name;
}
