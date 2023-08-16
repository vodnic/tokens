package com.tangent.tokens.service.evmrpc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
class ChainConfigManager {

    private final Map<Integer, Web3j> chainIdToWeb3j = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(ChainConfigManager.class);

    @Value("${rpc.mapping}")
    public void setChainIdToRpc(String rpcMappingJson) throws JsonProcessingException {
        initializeWeb3jInstances(rpcMappingJson);
    }

    private void initializeWeb3jInstances(String rpcMappingJson) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<Integer, String> chainIdToRpc = mapper.readValue(rpcMappingJson, new TypeReference<>() {});
        chainIdToRpc.forEach((chainId, rpcUrl) -> {
            chainIdToWeb3j.put(chainId, Web3j.build(new HttpService(rpcUrl)));
            logger.info("Added chainId {} with RPC {}", chainId, rpcUrl);
        });
    }

    public Web3j getWeb3jInstanceForChain(int chainId) {
        return Optional.ofNullable(chainIdToWeb3j.get(chainId))
                .orElseThrow(() -> new RuntimeException("No Web3j instance for chainId: " + chainId));
    }
}
