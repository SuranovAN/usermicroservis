package suranovan.usermicroservice.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.parsing.Parser;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import suranovan.usermicroservice.repository.UserRepository;

import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class UserControllerTest {

    @LocalServerPort
    private int port;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
        RestAssuredMockMvc.mockMvc(mockMvc);
        RestAssured.registerParser(String.valueOf(ContentType.JSON), Parser.JSON);
    }

    @Test
    public void userAllUserTest() {
        RestAssuredMockMvc
                .when()
                .get("/v1/users")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .assertThat();
    }

    @SneakyThrows
    @Test
    public void userByIdTest() {
        String expectedBody = "{\"id\":1,\"name\":\"testUser\",\"surname\":\"testSurname\",\"patronymic\":\"testPatronymic\",\"creationDate\":\"testDate\"}";
        RestAssuredMockMvc
                .given()
                .queryParam("id", 1)
                .when()
                .get("/v1/user")
                .then()
                .body("id", equalTo(1))
                .body(is(equalTo(expectedBody)))
                .statusCode(200)
                .assertThat();
    }

    @Test
    public void userByIdErrorTest() {
        RestAssuredMockMvc
                .given()
                .when()
                .get("/v1/user")
                .then()
                .statusCode(400)
                .assertThat();
    }

    @Test
    public void produceNewUserToKafkaTest() {
        String testRequestBody = "{\"id\":1,\"name\":\"testUser\",\"surname\":\"testSurname\",\"patronymic\":\"testPatronymic\",\"creationDate\":\"testDate\"}";
        RestAssuredMockMvc
                .given()
                .body(testRequestBody)
                .contentType(ContentType.JSON)
                .when()
                .put("/v1/user/save")
                .then()
                .statusCode(200)
                .assertThat();
    }

    @AfterAll
    public static void clearTestUser() {

    }
}
