package com.tangent.tokens.service.evmrpc;

import com.tangent.tokens.exception.IORuntimeException;
import com.tangent.tokens.exception.ObjectNotFoundException;
import com.tangent.tokens.model.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthCall;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.*;

class EvmRpcServiceTest {

    @InjectMocks
    private EvmRpcService evmRpcService;

    @Mock
    private ChainConfigManager chainConfigManager;

    @Mock
    private Web3j web3j;

    @Mock
    private EthCall mockEthCall;
    @Mock
    private Request ethCallRequest;

    // This is due to problems with mocking generics with wildcards
    private <T> Request<T, EthCall> mockEthCallRequest() {
        return ethCallRequest;
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(chainConfigManager.getWeb3jInstanceForChain(anyInt())).thenReturn(web3j);
    }

    private void mockWeb3Call(String returnValue) {
        when(web3j.ethCall(any(), any())).thenReturn(mockEthCallRequest());
        try {
            when(ethCallRequest.send()).thenReturn(mockEthCall);
        } catch (IOException ignore) {}
        when(mockEthCall.getValue()).thenReturn(returnValue);
    }

    @Test
    void testEthCallSendThrowsIoException_runtimeExceptionThrown() throws IOException {
        Address mockAddress = Address.fromString("0x1234567890abcdef1234567890abcdef12345678");
        when(web3j.ethCall(any(), any())).thenReturn(mockEthCallRequest());
        when(ethCallRequest.send()).thenThrow(new IOException("Test exception"));

        assertThrowsExactly(
                IORuntimeException.class,
                () -> evmRpcService.getTokenName(mockAddress, 1));
    }

    @Test
    void testEthCallNoResultFromContractCall_throwsObjectNotFound() throws IOException {
        Address mockAddress = Address.fromString("0x1234567890abcdef1234567890abcdef12345678");
        mockWeb3Call("");

        assertThrowsExactly(
                ObjectNotFoundException.class,
                () -> evmRpcService.getTokenName(mockAddress, 1));
    }

    @Test
    void testGetTokenName() {
        Address mockAddress = Address.fromString("0x1234567890abcdef1234567890abcdef12345678");
        mockWeb3Call("0x0000000000000000000000000000000000000000000000000000000000000020000000000000000000000000000000000000000000000000000000000000000a5465746865722055534400000000000000000000000000000000000000000000");

        String result = evmRpcService.getTokenName(mockAddress, 1);

        assertEquals("Tether USD", result);
    }

    @Test
    void testGetTokenSymbol() {
        Address mockAddress = Address.fromString("0x1234567890abcdef1234567890abcdef12345678");
        mockWeb3Call("0x000000000000000000000000000000000000000000000000000000000000002000000000000000000000000000000000000000000000000000000000000000045553445400000000000000000000000000000000000000000000000000000000");

        String result = evmRpcService.getTokenSymbol(mockAddress, 1);

        assertEquals("USDT", result);
    }

    @Test
    void testGetTokenDecimals() {
        Address mockAddress = Address.fromString("0x1234567890abcdef1234567890abcdef12345678");
        mockWeb3Call("0x0000000000000000000000000000000000000000000000000000000000000006");

        int result = evmRpcService.getTokenDecimals(mockAddress, 1);

        assertEquals(6, result);
    }

}
