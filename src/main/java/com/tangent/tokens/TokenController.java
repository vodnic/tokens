package com.tangent.tokens;

import com.tangent.tokens.model.Token;
import com.tangent.tokens.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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
    public Token getTokenById(@PathVariable("id") String id) {
        logger.info("Getting token by id: {}", id);
        return tokenService.getTokenById(UUID.fromString(id)).orElse(null);
    }
    
}
