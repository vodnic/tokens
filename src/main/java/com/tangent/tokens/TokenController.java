package com.tangent.tokens;

import models.Token;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/token")
public class TokenController {

    @GetMapping
    public Set<Token> getToken() {
        Set<Token> tokens = new HashSet<>();
        tokens.add(new Token(UUID.randomUUID(), "0x1234", "Ethereum", "ETH", "Ether", 18));
        tokens.add(new Token(UUID.randomUUID(), "0x4567", "Ethereum", "WBTC", "Wrapped Bitcoin", 8));
        return tokens;
    }

    @GetMapping("/{id}")
    public Token getTokenById(@PathVariable("id") String id) {
        return new Token(UUID.randomUUID(), "0x1234", "Ethereum", "ETH", "Ether", 18);
    }
    
}
