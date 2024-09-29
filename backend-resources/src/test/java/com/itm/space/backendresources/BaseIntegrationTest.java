package com.itm.space.backendresources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

import static org.springframework.http.MediaType.APPLICATION_JSON;

/*
 * BaseIntegrationTest служит базовым классом для всех интеграционных тестов в проекте.
 * Он предоставляет общие настройки и методы, которые могут быть использованы в конкретных тестах.
 * Это позволяет избежать дублирования кода и упрощает написание тестов.*/
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public abstract class BaseIntegrationTest {

    /*Это объект ObjectWriter из библиотеки Jackson, который используется для сериализации объектов в JSON.
    Он настраивается для отключения обертки корневого значения и использования pretty print для форматирования JSON.
     */
    private final ObjectWriter contentWriter = new ObjectMapper()
            .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
            .writer()
            .withDefaultPrettyPrinter();

    //Это объект MockMvc, который используется для выполнения HTTP-запросов к контроллерам в тестах. Он автоматически внедряется с помощью аннотации @Autowired
    @Autowired
    protected MockMvc mvc;

    //Этот метод принимает объект MockHttpServletRequestBuilder и устанавливает тип содержимого запроса как application/json
    protected MockHttpServletRequestBuilder requestToJson(MockHttpServletRequestBuilder requestBuilder) {
        return requestBuilder
                .contentType(APPLICATION_JSON);
    }
//Этот метод принимает объект MockHttpServletRequestBuilder и объект содержимого, который нужно отправить в запросе.
// Он сериализует объект содержимого в JSON и добавляет его в запрос.
    protected MockHttpServletRequestBuilder requestWithContent(MockHttpServletRequestBuilder requestBuilder,
                                                               Object content) throws JsonProcessingException {
        return requestToJson(requestBuilder).content(contentWriter.writeValueAsString(content));
    }

    // Поднимаем окружение с помощью Docker Compose
    @Container
    static DockerComposeContainer<?> environment =
            new DockerComposeContainer<>(new File("src/test/docker-compose-test.yml")) // путь к файлу docker-compose.yml
                    .withExposedService("db", 5432) // Подключаем сервис базы данных (имя сервиса db и порт 5432)
                    .withExposedService("kc", 8080); // Подключаем сервис Keycloak (kc и порт 8080)

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        // Подключение к базе данных
        String jdbcUrl = "jdbc:postgresql://" + environment.getServiceHost("db", 5432)
                + ":" + environment.getServicePort("db", 5432) + "/keycloak_db";
        registry.add("spring.datasource.url", () -> jdbcUrl);
        registry.add("spring.datasource.username", () -> "my_admin");
        registry.add("spring.datasource.password", () -> "my_password");
        registry.add("keycloak.realm", () -> "ITM");
    }
}
