

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;


import model.User;
import org.junit.After;
import org.junit.Before;
import steps.OrdersSteps;
import steps.UsersSteps;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


public class BaseTest {
    protected UsersSteps usersSteps;
    protected OrdersSteps ordersSteps;
    protected String testUserEmail;
    protected String testUserPassword = "password123"; // Добавляем поле с паролем
    protected String testUserName = "Test User";
    protected String accessToken;
    protected User testUser;

    @Before
    @Step("Подготовка тестовых данных")
    public void setUp() {

        usersSteps = new UsersSteps();
        ordersSteps = new OrdersSteps();
        testUser = User.builder()
                .email(generateTestEmail())
                .password(generateRandomPassword())
                .name(generateRandomName())
                .build();

        // Регистрация тестового пользователя
        ValidatableResponse response = usersSteps.registerUser(testUser);

        accessToken = response.extract().path("accessToken");
    }



    @After
    @Step("Очистка тестовых данных")
    public void tearDown() {
        if (accessToken != null) {
            usersSteps.deleteUser(accessToken);
        }
    }


    @Step("Генерация уникального email")
    protected String generateTestEmail() {
        return "user_" + System.currentTimeMillis() + "_" +
                Thread.currentThread().getId() + "@example.com";
    }

    @Step("Генерация случайного пароля")
    protected String generateRandomPassword() {
        return "Pass_" + UUID.randomUUID().toString().substring(0, 8);
    }
    @Step("Генерация имени")
    protected String generateRandomName() {
        return "User_" + UUID.randomUUID().toString().substring(0, 8);
    }

    @Step("Генерация ингредиентов")
    protected List<String> generateIngredients(int count) {
        return List.of(
                "61c0c5a71d1f82001bdaaa6" + (count + 1),
                "61c0c5a71d1f82001bdaaa6" + (count + 2)
        );
    }
}

