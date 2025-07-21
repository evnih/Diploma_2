import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.User;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;



@Epic("API Tests")
    @Feature("User Login")
    public class UserLoginTest extends BaseTest {

        @Test
        @Story("Логин пользователя")
        @Description("Успешный вход под существующим пользователем")
        @Severity(SeverityLevel.BLOCKER)
        public void testSuccessfulLogin() {
            User validUser = User.builder()
                    .email(testUserEmail)
                    .password(testUserPassword)
                    .build();
            ValidatableResponse response = usersSteps.loginUser(validUser);

            usersSteps.checkLoginSuccess(response);
        }

    @Test
    @Story("Логин пользователя")
    @DisplayName("Вход с неверными учетными данными")
    @Description("Попытка авторизации с неверными учетными данными")
    @Severity(SeverityLevel.BLOCKER)
    public void testLoginWithInvalidCredentials() {
        User invalidUser = User.builder()
                .email("wrong@example.com")
                .password("wrongpass")
                .build();
        ValidatableResponse response = usersSteps.loginUser(invalidUser);

        usersSteps.checkLoginFailure(response);
    }

    @Test
    @Story("Логин пользователя")
    @DisplayName("Вход без пароля")
    @Description("Попытка авторизации с пустым паролем")
    @Severity(SeverityLevel.CRITICAL)
    public void testLoginWithoutPassword() {
        ValidatableResponse response = usersSteps.loginUser(testUserEmail, "");

        usersSteps.checkLoginFailure(response);
    }

    @Test
    @Story("Логин пользователя")
    @DisplayName("Вход без email")
    @Description("Попытка авторизации с пустым email")
    @Severity(SeverityLevel.CRITICAL)
    public void testLoginWithoutEmail() {
        ValidatableResponse response = usersSteps.loginUser("", testUserPassword);

        response.statusCode(SC_UNAUTHORIZED)
                .body("message", equalTo("email or password are incorrect"));
    }
}

