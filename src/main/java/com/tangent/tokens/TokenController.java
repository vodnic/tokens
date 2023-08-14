package com.tangent.tokens;

import com.tangent.tokens.exception.ObjectNotFoundException;
import com.tangent.tokens.model.Token;
import com.tangent.tokens.exception.InvalidRequestException;
import com.tangent.tokens.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @GetMapping("/{id}")
    public ResponseEntity<?> getTokenById(@PathVariable("id") String id) {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("Invalid UUID string '" + id + "'.");
        }
        Optional<Token> tokenOptional = tokenService.getTokenById(uuid);
        if (tokenOptional.isEmpty()) {
            throw new ObjectNotFoundException("Token with ID " + id + " not found.");
        }
        return ResponseEntity.ok(tokenOptional.get());
    }


}
