package com.example.chatgptbasedcookingingredients;

import com.example.chatgptbasedcookingingredients.service.EmailAlreadyRegisteredException;
import com.example.chatgptbasedcookingingredients.service.User;
import com.example.chatgptbasedcookingingredients.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "   "})
    void register_blankOrNullEmail_returnsBadRequest(String email) {
        UserController controller = new UserController(userService);

        ResponseEntity<String> response = controller.register(new RegisterRequest(email, "secret123"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verifyNoInteractions(userService);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "   "})
    void register_blankOrNullPassword_returnsBadRequest(String password) {
        UserController controller = new UserController(userService);

        ResponseEntity<String> response = controller.register(new RegisterRequest("user@example.com", password));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verifyNoInteractions(userService);
    }

    @Test
    void register_validRequest_returnsCreatedWithoutPassword() {
        when(userService.register("user@example.com", "secret123"))
                .thenReturn(new User("user@example.com", "hashed-secret123"));
        UserController controller = new UserController(userService);

        ResponseEntity<String> response = controller.register(new RegisterRequest("user@example.com", "secret123"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo("user@example.com");
        assertThat(response.getBody()).doesNotContain("secret123", "hashed-secret123");
        verify(userService).register("user@example.com", "secret123");
    }

    @Test
    void register_duplicateEmail_returnsConflict() {
        doThrow(new EmailAlreadyRegisteredException("user@example.com"))
                .when(userService).register("user@example.com", "secret123");
        UserController controller = new UserController(userService);

        ResponseEntity<String> response = controller.register(new RegisterRequest("user@example.com", "secret123"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }
}
