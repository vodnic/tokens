package com.tangent.tokens.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Value;
import java.util.UUID;

@Data
@Entity
@Table(name = "tokens")
public class Token {
    @Id
    UUID id;
    String address;
    String chain;
    String symbol;
    String name;
    int decimals;

}

