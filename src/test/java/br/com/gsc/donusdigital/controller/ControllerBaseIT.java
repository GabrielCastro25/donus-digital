package br.com.gsc.donusdigital.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ControllerBaseIT {

    @BeforeAll
    public static void setUpAll() {
        RestAssured.port = 8080;
        RestAssured.baseURI = "http://localhost";
    }
}
