import groovy.util.logging.Slf4j;
import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;


import io.restassured.response.ValidatableResponse;

import org.junit.Test;


import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;


@Epic("API Tests")
@Feature("User Registration")
@Slf4j
public class UserRegistrationApiTest extends BaseTest {

    @Test
    @Story("Создание пользователя")
    @Description("Проверка успешной регистрации")
    @Step("Тест регистрации нового пользователя")
    public void testSuccessfulRegistration() {
        String email = generateTestEmail();
        String password = generateRandomPassword();
        String name = generateRandomName();

        ValidatableResponse response = usersSteps.registerUser(email, password, name);
        response.statusCode(SC_OK)
                .body("success", equalTo(true));
    }



    @Test
    @DisplayName("Регистрация с существующим email")
    @Story("Дублирование пользователя")
    @Description("Проверка ошибки при регистрации существующего пользователя")
    public void testDuplicateRegistration() {

        ValidatableResponse response = usersSteps.registerUser(
                testUserEmail,
                testUserPassword,
                "Test User"
        );
        response.statusCode(SC_FORBIDDEN)
                .body("message", equalTo("User already exists"));
    }
    @Test
    @DisplayName("Регистрация без обязательных полей")
    @Story("Валидация регистрации")
    @Description("Проверка ошибки при регистрации без обязательных полей")
    public void testRegistrationWithMissingFields() {

        usersSteps.registerUser("", "password123", "Name")
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Email, password and name are required fields"));


        usersSteps.registerUser("test@example.com", "", "Name")
                .statusCode(SC_FORBIDDEN);
    }
}
