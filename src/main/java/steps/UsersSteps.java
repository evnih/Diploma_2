package steps;

import model.User;
import utils.ApiClient;
import utils.StellarBurgersUrl;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;


import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.*;


public class UsersSteps extends BaseSteps{


    public UsersSteps() {
        super();
    }

    public UsersSteps(String token) {
        super(token);
    }

    @Step("Логин пользователя")
    public ValidatableResponse loginUser(User user) {


        return given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post(StellarBurgersUrl.LOGIN)
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
    public ValidatableResponse registerUser(User user) {

        return given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post(StellarBurgersUrl.REGISTER)
                .then();
    }
    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String token) {
        return given()
                .spec(ApiClient.authSpec(token))
                .when()
                .delete(StellarBurgersUrl.USER)
                .then();
    }
    @Step("Логин пользователя по email и паролю")
    public ValidatableResponse loginUser(String email, String password) {
        User user = User.builder()
                .email(email)
                .password(password)
                .build();
        return loginUser(user);
    }
}