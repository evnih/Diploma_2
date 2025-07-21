import groovy.util.logging.Slf4j;
import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;


import io.restassured.response.ValidatableResponse;

import model.User;
import org.junit.Test;


import static org.apache.http.HttpStatus.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;


@Epic("API Tests")
@Feature("User Registration")
@Slf4j
public class UserRegistrationApiTest extends BaseTest {

    @Test
    @Story("Создание пользователя")
    @Description("Проверка успешной регистрации")
    @Severity(SeverityLevel.BLOCKER)
    @Step("Тест регистрации нового пользователя")
    public void testSuccessfulRegistration() {
        User newUser = User.builder()
                .email(generateTestEmail())
                .password(generateRandomPassword())
                .name(generateRandomName())
                .build();

        ValidatableResponse response = usersSteps.registerUser(newUser);
        verifySuccessfulRegistration(response);
    }
    @Step("Проверка успешной регистрации")
    private void verifySuccessfulRegistration(ValidatableResponse response) {
        response.statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());
    }

    @Test
    @DisplayName("Регистрация с существующим email")
    @Story("Дублирование пользователя")
    @Description("Проверка ошибки при регистрации существующего пользователя")
    @Severity(SeverityLevel.CRITICAL)
    public void testDuplicateRegistration() {
        User duplicateUser = User.builder()
                .email(testUser.getEmail())  // Используем email уже зарегистрированного пользователя
                .password(generateRandomPassword())
                .name(generateRandomName())
                .build();

        ValidatableResponse response = usersSteps.registerUser(duplicateUser);

        response.assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Регистрация без email")
    @Story("Валидация регистрации")
    @Description("Проверка ошибки при регистрации без email")
    @Severity(SeverityLevel.CRITICAL)
    public void testRegistrationWithoutEmail() {
        User userWithoutEmail = User.builder()
                .email("")
                .password(generateRandomPassword())
                .name(generateRandomName())
                .build();

        ValidatableResponse response = usersSteps.registerUser(userWithoutEmail);

        response.assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
    @Test
    @DisplayName("Регистрация без пароля")
    @Story("Валидация регистрации")
    @Description("Проверка ошибки при регистрации без пароля")
    @Severity(SeverityLevel.CRITICAL)
    public void testRegistrationWithoutPassword() {
        User userWithoutPassword = User.builder()
                .email(generateTestEmail())
                .password("")
                .name(generateRandomName())
                .build();

        ValidatableResponse response = usersSteps.registerUser(userWithoutPassword);

        response.assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Регистрация без имени")
    @Story("Валидация регистрации")
    @Description("Проверка ошибки при регистрации без имени")
    @Severity(SeverityLevel.NORMAL)
    public void testRegistrationWithoutName() {
        User userWithoutName = User.builder()
                .email(generateTestEmail())
                .password(generateRandomPassword())
                .name("")
                .build();

        ValidatableResponse response = usersSteps.registerUser(userWithoutName);

        response.assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

}
