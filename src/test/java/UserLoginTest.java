import io.qameta.allure.*;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;


@Epic("API Tests")
    @Feature("User Login")
    public class UserLoginTest extends BaseTest {

        @Test
        @Story("Логин пользователя")
        @Description("Успешный вход под существующим пользователем")
        @Severity(SeverityLevel.BLOCKER)
        public void testSuccessfulLogin() {
            ValidatableResponse response = usersSteps.loginUser(testUserEmail, testUserPassword);
            usersSteps.checkLoginSuccess(response);
        }


        @Test
        @Story("Логин пользователя")
        @Description("Вход без email")
        @Severity(SeverityLevel.CRITICAL)
        public void testLoginWithoutEmail() {
            ValidatableResponse response = usersSteps.loginUser("", "validpassword");

            response.statusCode(SC_UNAUTHORIZED)
                    .body("success", equalTo(false))
                    .body("message", equalTo("email or password are incorrect"));
        }
    @Test
    @Story("Логин пользователя")
    @Description("Вход без пароля")
    @Severity(SeverityLevel.CRITICAL)
    public void testLoginWithoutPassword() {
        ValidatableResponse response = usersSteps.loginUser("valid@email.com", "");

        response.statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
    }

