package com.tangent.tokens;

import com.tangent.tokens.exception.ObjectNotFoundException;
import com.tangent.tokens.model.Address;
import com.tangent.tokens.model.Token;
import com.tangent.tokens.service.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TokenController.class)
public class TokenControllerTest {

    @MockBean
    private TokenService tokenService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetTokens_returnsTokenList() throws Exception {
        Token mockToken = new Token();
        when(tokenService.listTokens()).thenReturn(Collections.singletonList(mockToken));

        mockMvc.perform(get("/token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());

        verify(tokenService, times(1)).listTokens();
    }

    @Test
    public void testGetTokenById_returnsToken() throws Exception {
        String addressParam = "0x23f4569002a5A07f0Ecf688142eEB6bcD883eeF8";
        Address mockAddress = Address.fromString(addressParam);
        Token mockToken = new Token();

        when(tokenService.getTokenByAddressAndChain(eq(mockAddress), anyInt())).thenReturn(mockToken);

        mockMvc.perform(get("/token/" + addressParam))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());

        verify(tokenService, times(1)).getTokenByAddressAndChain(eq(mockAddress), anyInt());
    }

    @Test
    public void testGetTokenById_notFound() throws Exception {
        String addressParam = "0x23f4569002a5A07f0Ecf688142eEB6bcD883eeF8";
        Address mockAddress = Address.fromString(addressParam);

        when(tokenService.getTokenByAddressAndChain(eq(mockAddress), anyInt())).thenThrow(new ObjectNotFoundException(""));

        mockMvc.perform(get("/token/" + addressParam))
                .andExpect(status().isNotFound());

        verify(tokenService, times(1)).getTokenByAddressAndChain(eq(mockAddress), anyInt());
    }

    @Test
    public void testGetTokenById_invalidAddress() throws Exception {
        mockMvc.perform(get("/token/invalidAddress"))
                .andExpect(status().isBadRequest());

        verify(tokenService, never()).getTokenByAddressAndChain(any(Address.class), anyInt());
    }

    @Test
    public void testGetTokenById_500OnServerError() throws Exception {
        when(tokenService.getTokenByAddressAndChain(any(Address.class), anyInt())).thenThrow(new RuntimeException());

        mockMvc.perform(get("/token/0x23f4569002a5A07f0Ecf688142eEB6bcD883eeF8"))
                .andExpect(status().isInternalServerError());
    }

}
