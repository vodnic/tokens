package com.tangent.tokens.service;

import com.tangent.tokens.model.Address;
import com.tangent.tokens.model.Token;
import com.tangent.tokens.repository.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private TokenService tokenService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTokenById() {
        UUID id = UUID.randomUUID();
        Token token = new Token();
        token.setId(id);
        when(tokenRepository.findById(id)).thenReturn(Optional.of(token));

        Optional<Token> result = tokenService.getTokenById(id);

        assertTrue(result.isPresent());
        assertEquals(token, result.get());
    }

    @Test
    public void testGetTokenByAddressAndChain() {
        Address address = Address.fromString("0x7d90d74b3501440f95fb8ee55af2ed2933c18fdd");
        int chainId = 1;
        Token token = new Token();
        when(tokenRepository.findByAddressAndChainId(address, chainId)).thenReturn(Optional.of(token));

        Token result = tokenService.getTokenByAddressAndChain(address, chainId);

        assertEquals(token, result);
    }

    @Test
    public void testListTokens() {
        Token token = new Token();
        when(tokenRepository.findAll()).thenReturn(Collections.singletonList(token));

        List<Token> result = tokenService.listTokens();

        assertEquals(1, result.size());
        assertEquals(token, result.get(0));
    }

}
