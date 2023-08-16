package com.tangent.tokens.service.evmrpc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class ChainConfigManagerTest {

    private final ChainConfigManager chainConfigManager = new ChainConfigManager();

    @Test
    void testLoadConfigForAChain_returnExistingConfig() throws Exception {
        String testJson = "{\"2\":\"http://localhost:8545\"}";

        chainConfigManager.setChainIdToRpc(testJson);

        assertNotNull(chainConfigManager.getWeb3jInstanceForChain(2));
    }

    @Test
    void testLoadConfigForAChain_throwOnNonExistingConfig() throws Exception {
        String testJson = "{\"2\":\"http://localhost:8545\"}";

        chainConfigManager.setChainIdToRpc(testJson);

        assertThrowsExactly(RuntimeException.class, () -> chainConfigManager.getWeb3jInstanceForChain(3));
    }

}
