import Utils.ApiClient;
import Utils.StellarBurgersUrl;
import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import io.restassured.response.ValidatableResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import model.Order;
import org.junit.Test;

import static io.restassured.RestAssured.given;

import java.util.List;


import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;


@Epic("API Tests")
@Feature("Order Creation")
@DisplayName("Тесты создания заказов")
@Slf4j
public class OrderCreationTest extends BaseTest {

    private final List<String> validIngredients = List.of(
            "61c0c5a71d1f82001bdaaa6d",
            "61c0c5a71d1f82001bdaaa6f"
    );

    private final List<String> invalidIngredients = List.of("invalid_hash_1", "invalid_hash_2");

    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Step("Тест создания заказа с авторизацией")
    @Severity(SeverityLevel.BLOCKER)
    public void testCreateOrderWithAuth() {
        List<String> ingredients = generateIngredients(2);

        ValidatableResponse response = ordersSteps.createOrder(ingredients, accessToken);

        response.assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }
    @Step("Проверка успешного создания заказа")
    private void verifyOrderCreated(ValidatableResponse response) {
        response.statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Tag("negative")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreateOrderWithoutAuth() {
        List<String> ingredients = generateIngredients(2);

        ValidatableResponse response = ordersSteps.createOrder(ingredients, null);

        response.assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Tag("negative")
    public void testCreateOrderWithInvalidIngredients() {
        val order = Order.builder()
                .ingredients(invalidIngredients)
                .build();

        ValidatableResponse response = given()
                .spec(ApiClient.authSpec(accessToken))
                .body(order)
                .when()
                .post(StellarBurgersUrl.ORDERS)
                .then()
                .log().ifValidationFails();

        response.statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Tag("negative")
    public void testCreateOrderWithoutIngredients() {
        val emptyOrder = Order.builder().build();

        ValidatableResponse response = given()
                .spec(ApiClient.authSpec(accessToken))
                .body(emptyOrder)
                .when()
                .post(StellarBurgersUrl.ORDERS)
                .then()
                .log().ifValidationFails();

        response.statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Ingredient ids must be provided"));
    }
}