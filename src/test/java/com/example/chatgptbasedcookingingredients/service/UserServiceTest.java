package com.example.chatgptbasedcookingingredients.service;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Test
    void register_newUser_storesHashedPasswordNotPlainText() {
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        when(passwordEncoder.encode("secret123")).thenReturn("hashed-secret123");
        UserService userService = new UserService(passwordEncoder);

        User user = userService.register("user@example.com", "secret123");

        assertThat(user.email()).isEqualTo("user@example.com");
        assertThat(user.hashedPassword()).isEqualTo("hashed-secret123");
        assertThat(user.hashedPassword()).isNotEqualTo("secret123");
    }

    @Test
    void register_duplicateEmail_throwsException() {
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        when(passwordEncoder.encode("secret123")).thenReturn("hashed-secret123");
        UserService userService = new UserService(passwordEncoder);
        userService.register("user@example.com", "secret123");

        assertThatThrownBy(() -> userService.register("user@example.com", "otherPassword"))
                .isInstanceOf(EmailAlreadyRegisteredException.class);
    }

    @Test
    void register_concurrentSameEmail_onlyOneSucceeds() throws Exception {
        PasswordEncoder passwordEncoder = new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return "hashed-" + rawPassword;
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return encode(rawPassword).equals(encodedPassword);
            }
        };
        UserService userService = new UserService(passwordEncoder);

        int threadCount = 20;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch ready = new CountDownLatch(threadCount);
        CountDownLatch start = new CountDownLatch(1);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger conflictCount = new AtomicInteger();

        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            futures.add(executor.submit(() -> {
                ready.countDown();
                try {
                    start.await();
                    userService.register("race@example.com", "secret123");
                    successCount.incrementAndGet();
                } catch (EmailAlreadyRegisteredException e) {
                    conflictCount.incrementAndGet();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }));
        }

        ready.await();
        start.countDown();
        for (Future<?> future : futures) {
            future.get();
        }
        executor.shutdown();

        assertThat(successCount.get()).isEqualTo(1);
        assertThat(conflictCount.get()).isEqualTo(threadCount - 1);
    }
}
