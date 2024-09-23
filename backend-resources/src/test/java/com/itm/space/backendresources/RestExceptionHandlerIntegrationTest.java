package com.itm.space.backendresources;

import com.itm.space.backendresources.api.request.UserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RestExceptionHandlerIntegrationTest extends BaseIntegrationTest {

    @Test
    public void testHandleBackendResourcesException() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/trigger-backend-exception"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Backend resource error message"));
    }

    @Test
    public void testHandleInvalidArgument() throws Exception {
        UserRequest invalidUser = new UserRequest("", "invalid-email", "", "", "");

        mvc.perform(requestWithContent(MockMvcRequestBuilders.post("/api/trigger-validation-exception"), invalidUser))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.username").value("Username should not be blank"))
                .andExpect(jsonPath("$.email").value("Email should be valid"))
                .andExpect(jsonPath("$.password").value("Password should be greater than 4 characters long"))
                .andExpect(jsonPath("$.firstName").value("must not be blank"))
                .andExpect(jsonPath("$.lastName").value("must not be blank"));
    }
}
