package dev.memocode.farmfarm_server.domain;

import dev.memocode.farmfarm_server.domain.base_entity.IdentifiableSoftDeletableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "houses")
@EqualsAndHashCode(callSuper = true)
public class LocalHouse extends IdentifiableSoftDeletableEntity {
    @Column(name = "name", unique = true)
    private String name;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "house_id")
    private House house;
}
