package steps;

import Utils.ApiClient;
import Utils.StellarBurgersUrl;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import model.User;


import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.*;


public class UsersSteps {
    private final RequestSpecification requestSpec;

    public UsersSteps() {
        this.requestSpec = ApiClient.baseSpec();
    }

    public UsersSteps(String token) {
        this.requestSpec = ApiClient.authSpec(token);
    }

    @Step("Логин пользователя")
    public ValidatableResponse loginUser(String email, String password) {
        String body = String.format("{\"email\": \"%s\", \"password\": \"%s\"}", email, password);
        return given(requestSpec)
                .body(body)
                .when()
                .post(StellarBurgersUrl.LOGIN_ENDPOINT)
                .then();
    }
    @Step("Проверка успешного логина")
    public void checkLoginSuccess(ValidatableResponse response) {
        response.statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", startsWith("Bearer "))
                .body("user.email", notNullValue());
    }

    @Step("Проверка ошибки логина")
    public void checkLoginFailure(ValidatableResponse response) {
        response.statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }



    @Step("Регистрация пользователя")
    public ValidatableResponse registerUser(String email, String password, String name) {
        return given(requestSpec)
                .body(new User(email, password, name))
                .post(StellarBurgersUrl.REGISTER)
                .then();

    }
    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String token) {
        return given()
                .spec(ApiClient.authSpec(token))
                .delete(StellarBurgersUrl.USER)
                .then();
    }
}