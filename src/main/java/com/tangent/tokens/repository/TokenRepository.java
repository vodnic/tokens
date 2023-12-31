package com.tangent.tokens.repository;

import com.tangent.tokens.model.Address;
import com.tangent.tokens.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID> {

    Optional<Token> findByAddressAndChainId(Address address, int chainId);

    List<Token> findBySymbolAndChainId(String symbol, int chainId);
}
