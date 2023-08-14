package com.tangent.tokens;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class TokensApplicationTests {

	@Test
	void contextLoads() {
		TokensApplication.main(new String[] {});
	}

}
