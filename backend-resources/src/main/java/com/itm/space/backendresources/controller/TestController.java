package com.itm.space.backendresources.controller;



import com.itm.space.backendresources.api.request.UserRequest;
import com.itm.space.backendresources.exception.BackendResourcesException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/trigger-backend-exception")
    public void triggerBackendException() {
        throw new BackendResourcesException("Backend resource error message",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
    //для проверки ошибок валидации
    @PostMapping("/trigger-validation-exception")
    public void triggerValidationException(@Valid @RequestBody UserRequest userRequest,
                                           BindingResult bindingResult) throws MethodArgumentNotValidException {
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
    }
}
