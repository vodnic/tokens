package com.tangent.tokens.service;

import com.tangent.tokens.model.Token;
import com.tangent.tokens.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    @Autowired
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Optional<Token> getTokenById(UUID id) {
        return tokenRepository.findById(id);
    }

    public List<Token> listTokens() {
        return tokenRepository.findAll();
    }

}
