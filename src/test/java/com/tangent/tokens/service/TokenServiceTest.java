package com.tangent.tokens.service;

import com.tangent.tokens.model.Address;
import com.tangent.tokens.model.Token;
import com.tangent.tokens.repository.TokenRepository;
import com.tangent.tokens.service.evmrpc.EvmRpcService;
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

    @Mock
    private EvmRpcService evmRpcService;

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
    public void testGetTokenBySymbolAndChain_dbEntryExists_returnStored() {
        Address address = Address.fromString("0x7d90d74b3501440f95fb8ee55af2ed2933c18fdd");
        int chainId = 1;
        Token token = new Token();
        when(tokenRepository.findBySymbolAndChainId("DAI", chainId)).thenReturn(Collections.singletonList(token));

        List<Token> result = tokenService.getTokenBySymbolAndChain("DAI", chainId);

        assertEquals(token, result.get(0));
    }

    @Test
    public void testGetTokenByAddressAndChain_dbEntryExists_returnStored() {
        Address address = Address.fromString("0x7d90d74b3501440f95fb8ee55af2ed2933c18fdd");
        int chainId = 1;
        Token token = new Token();
        when(tokenRepository.findByAddressAndChainId(address, chainId)).thenReturn(Optional.of(token));

        Token result = tokenService.getTokenByAddressAndChain(address, chainId);

        assertEquals(token, result);
    }

    @Test
    public void testGetTokenByAddressAndChain_noDbEntry_fetchFromRPC() {
        Address address = Address.fromString("0x7d90d74b3501440f95fb8ee55af2ed2933c18fdd");
        int chainId = 1;
        when(tokenRepository.findByAddressAndChainId(address, chainId)).thenReturn(Optional.empty());
        when(evmRpcService.getTokenName(address, chainId)).thenReturn("Test Token");
        when(evmRpcService.getTokenSymbol(address, chainId)).thenReturn("TEST");
        when(evmRpcService.getTokenDecimals(address, chainId)).thenReturn(18);

        Token result = tokenService.getTokenByAddressAndChain(address, chainId);

        Token expectedToken = new Token();
        expectedToken.setAddress(address);
        expectedToken.setChainId(1);
        expectedToken.setName("Test Token");
        expectedToken.setSymbol("TEST");
        expectedToken.setDecimals(18);
        assertEquals(expectedToken, result);
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
