package models;

import lombok.Value;
import java.util.UUID;

@Value
public class Token {
    UUID value;
    String address;
    String chain;
    String symbol;
    String name;
    int decimals;
}
