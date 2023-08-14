package com.tangent.tokens;

import com.tangent.tokens.exception.ObjectNotFoundException;
import com.tangent.tokens.model.Address;
import com.tangent.tokens.model.Token;
import com.tangent.tokens.exception.InvalidRequestException;
import com.tangent.tokens.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/token")
public class TokenController {

    private final TokenService tokenService;
    private static final Logger logger = LoggerFactory.getLogger(TokenController.class);

    @Autowired
    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping
    public List<Token> getToken() {
        logger.info("Getting all tokens");
        return tokenService.listTokens();
    }

    @GetMapping("/{address}")
    public ResponseEntity<?> getTokenById(
            @PathVariable("address") String addressParam,
            @RequestParam(name="chainId", required = false, defaultValue = "1") int chainId)
    {

        Address address;
        try {
            address = Address.fromString(addressParam);
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("Invalid address");
        }

        Optional<Token> tokenOptional = tokenService.getTokenByAddressAndChain(address, chainId);
        if (tokenOptional.isEmpty()) {
            throw new ObjectNotFoundException("Token not found");
        }
        return ResponseEntity.ok(tokenOptional.get());
    }


}
