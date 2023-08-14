package com.tangent.tokens.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Data
@Entity
@Table(name = "tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "address", nullable = false, length = 42)
    @JsonSerialize(using = Address.AddressSerializer.class)
    private Address address;

    @Column(name = "chain_id", nullable = false)
    private int chainId;

    @Column(name = "symbol", nullable = false, length = 255)
    private String symbol;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "decimals", nullable = false)
    private int decimals;
}

