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
        String email = generateTestEmail();
        String password = generateRandomPassword();
        String name = generateRandomName();

        ValidatableResponse response = usersSteps.registerUser(email, password, name);

        response.assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());
    }
    @Step("Проверка успешной регистрации")
    private void verifySuccessfulRegistration(ValidatableResponse response) {
        response.statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());
    }

    @Test
    @DisplayName("Регистрация с существующим email")
    @Story("Дублирование пользователя")
    @Description("Проверка ошибки при регистрации существующего пользователя")
    @Severity(SeverityLevel.CRITICAL)
    public void testDuplicateRegistration() {
        ValidatableResponse response = usersSteps.registerUser(testUserEmail, testUserPassword, testUserName);

        response.assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Регистрация без обязательных полей")
    @Story("Валидация регистрации")
    @Description("Проверка ошибки при регистрации без обязательных полей")
    @Severity(SeverityLevel.NORMAL)
    public void testRegistrationWithMissingFields() {

        ValidatableResponse response = usersSteps.registerUser("", "", "");

        response.assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
