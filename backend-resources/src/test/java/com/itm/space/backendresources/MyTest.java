package com.itm.space.backendresources;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;


import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;


import java.util.UUID;




@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MyTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @WithMockUser(username = "moderator", roles = "MODERATOR")
    public void testGetUserById()throws Exception{
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/users/"+UUID.randomUUID(), String.class);
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
    }

}




























