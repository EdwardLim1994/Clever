package com.projectclever.clever;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CleverApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void mainStartsSpringApplication() {
		try (MockedStatic<SpringApplication> spring = Mockito.mockStatic(SpringApplication.class)) {
			CleverApplication.main(new String[0]);
			spring.verify(() -> SpringApplication.run(CleverApplication.class, new String[0]));
		}
	}

}
