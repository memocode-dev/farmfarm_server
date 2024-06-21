package dev.memocode.farmfarm_server.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "houses")
@EqualsAndHashCode(callSuper = true)
public class House extends IdentifiableTimeManagementEntity {
    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;
}
