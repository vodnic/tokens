package com.tangent.tokens.service;

import com.tangent.tokens.model.Address;
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
    private final EvmRpcService evmRpcService;

    @Autowired
    public TokenService(TokenRepository tokenRepository, EvmRpcService evmRpcService) {
        this.tokenRepository = tokenRepository;
        this.evmRpcService = evmRpcService;
    }

    public Optional<Token> getTokenById(UUID id) {
        return tokenRepository.findById(id);
    }

    public Token getTokenByAddressAndChain(Address address, int chainId) {
        Optional<Token> stored = tokenRepository.findByAddressAndChainId(address, chainId);
        return stored.orElseGet(() -> fetchAndStoreToken(address, chainId));
    }

    private Token fetchAndStoreToken(Address address, int chainId) {
        Token token = new Token();
        token.setAddress(address);
        token.setChainId(chainId);
        token.setName(evmRpcService.getTokenName(address, chainId));
        token.setSymbol(evmRpcService.getTokenSymbol(address, chainId));
        token.setDecimals(evmRpcService.getTokenDecimals(address, chainId));
        tokenRepository.save(token);

        return token;
    }

    public List<Token> listTokens() {
        return tokenRepository.findAll();
    }

}
