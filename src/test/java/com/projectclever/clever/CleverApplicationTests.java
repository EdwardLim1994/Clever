package com.projectclever.clever;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CleverApplicationTests {
	@Value("${local.server.port}")
	private int port;

	@Test
	void contextLoads() {
	}

	@Test
	void cleverEndpointRendersTemplateWithEscapedRequestParameter() throws Exception {
		String query = URLEncoder.encode("Ada <script>", StandardCharsets.UTF_8);
		HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/?param=" + query))
				.GET().build();
		HttpResponse<String> response = HttpClient.newHttpClient()
				.send(request, HttpResponse.BodyHandlers.ofString());

		assertEquals(200, response.statusCode());
		assertTrue(response.headers().firstValue("Content-Type").orElse("").startsWith("text/html"));
		assertTrue(response.body().contains("<h1>Hello Ada &lt;script&gt;!</h1>"));
	}

	@Test
	void cleverEndpointUsesDefaultNameWhenRequestParameterIsMissing() throws Exception {
		HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/")).GET().build();
		HttpResponse<String> response = HttpClient.newHttpClient()
				.send(request, HttpResponse.BodyHandlers.ofString());

		assertEquals(200, response.statusCode());
		assertTrue(response.body().contains("<h1>Hello world!</h1>"));
	}

	@Test
	void mainStartsSpringApplication() {
		try (MockedStatic<SpringApplication> spring = Mockito.mockStatic(SpringApplication.class)) {
			CleverApplication.main(new String[0]);
			spring.verify(() -> SpringApplication.run(CleverApplication.class, new String[0]));
		}
	}
}
