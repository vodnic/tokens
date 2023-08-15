package com.tangent.tokens.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EvmRpcService {

    private Map<Integer, String> rpcMapping = new HashMap<>();

    @Value("${rpc.mapping}")
    public void setRpcMapping(String rpcMappingJson) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        rpcMapping = mapper.readValue(rpcMappingJson, new TypeReference<>() {
        });
    }

    public String getRpcUrl(Integer chainId) {
        return rpcMapping.get(chainId);
    }

}
