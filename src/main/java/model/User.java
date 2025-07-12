package model;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class User {
    @Builder.Default
    private String email = "default@example.com";  // Добавлено значение по умолчанию

    @Builder.Default
    private String password = "password123";

    @Builder.Default
    private String name = "Test User";
}

