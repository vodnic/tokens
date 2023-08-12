package com.tangent.tokens;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
public class TokenController {

    @GetMapping
    public String getToken() {
        return "This is a token";
    }

    @GetMapping("/{id}")
    public String getTokenById(@PathVariable("id") String id) {
        return "This is a token with id " + id;
    }
    
}
