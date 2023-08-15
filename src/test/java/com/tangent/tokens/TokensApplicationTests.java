package com.tangent.tokens;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;

@ActiveProfiles("test")
@SpringBootTest
class TokensApplicationTests {

	@Test
	void contextLoads() {
		System.setProperty("spring.profiles.active", "test");
		TokensApplication.main(new String[] {});
	}

}
