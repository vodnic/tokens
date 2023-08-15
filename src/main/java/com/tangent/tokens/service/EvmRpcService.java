package com.tangent.tokens.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tangent.tokens.exception.ObjectNotFoundException;
import com.tangent.tokens.model.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EvmRpcService {

    private final Map<Integer, Web3j> chainIdToWeb3j = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(EvmRpcService.class);

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

    public String getTokenName(Address contractAddress, int chainId) {
        logger.debug("Getting token name for token {} on chain {}", contractAddress, chainId);
        return callContractFunction(contractAddress, chainId, "name");
    }

    public String getTokenSymbol(Address contractAddress, int chainId) {
        logger.debug("Getting token symbol for token {} on chain {}", contractAddress, chainId);
        return callContractFunction(contractAddress, chainId, "symbol");
    }

    public int getTokenDecimals(Address contractAddress, int chainId) {
        logger.debug("Getting token decimals for token {} on chain {}", contractAddress, chainId);
        return Integer.parseInt(callContractFunction(contractAddress, chainId, "decimals"));
    }

    private String  callContractFunction(Address contractAddress, int chainId, String functionName) {
        Function function = functionName.equals("decimals")
                ? new Function(functionName, Collections.emptyList(), List.of(new org.web3j.abi.TypeReference<Uint8>() {}))
                : defineSimpleFunction(functionName);

        EthCall response = executeEthCall(contractAddress, chainId, function);
        List<Type> result = FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());
        if (result.isEmpty()) {
            throw new ObjectNotFoundException("No result returned from contract call");
        }
        return result.get(0).getValue().toString();
    }

    private EthCall executeEthCall(Address contractAddress, int chainId, Function function) {
        try {
            logger.trace("Calling contract function {} on token {} on chain {}", function.getName(), contractAddress, chainId);
            return Optional.ofNullable(chainIdToWeb3j.get(chainId))
                    .orElseThrow(() -> new RuntimeException("No Web3j instance for chainId: " + chainId))
                    .ethCall(Transaction.createEthCallTransaction(null, contractAddress.toString(), FunctionEncoder.encode(function)), DefaultBlockParameterName.LATEST)
                    .send();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Function defineSimpleFunction(String functionName) {
        return new Function(
                functionName,
                Collections.emptyList(),
                List.of(new org.web3j.abi.TypeReference<Utf8String>() {})
        );
    }

}