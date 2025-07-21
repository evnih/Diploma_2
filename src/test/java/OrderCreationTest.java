import steps.OrdersSteps;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import io.restassured.response.ValidatableResponse;
import lombok.extern.slf4j.Slf4j;

import model.Order;
import org.junit.Test;


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
    @Description("Успешное создание заказа с авторизацией")
    @Severity(SeverityLevel.BLOCKER)
    public void testCreateOrderWithAuth() {
        Order order = Order.builder()
                .ingredients(validIngredients)
                .build();

        ValidatableResponse response = new OrdersSteps(accessToken)
                .createOrder(order);

        verifyOrderCreated(response);
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
    @Description("Попытка создания заказа без авторизации")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreateOrderWithoutAuth() {
        Order order = Order.builder()
                .ingredients(validIngredients)
                .build();

        ValidatableResponse response = new OrdersSteps()
                .createOrderUnauthorized(order);

        response.assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Tag("negative")
    @Description("Попытка создания заказа с неверным хешем")
    @Severity(SeverityLevel.NORMAL)
    public void testCreateOrderWithInvalidIngredients() {
        Order order = Order.builder()
                .ingredients(invalidIngredients)
                .build();

        ValidatableResponse response = new OrdersSteps(accessToken)
                .createOrder(order);

        response.statusCode(SC_INTERNAL_SERVER_ERROR)
                .log().ifValidationFails();
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Tag("negative")
    @Description("Попытка создания заказа без ингредиентов")
    @Severity(SeverityLevel.NORMAL)
    public void testCreateOrderWithoutIngredients() {
        Order emptyOrder = Order.builder().build();

        ValidatableResponse response = new OrdersSteps(accessToken)
                .createOrder(emptyOrder);

        response.statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Ingredient ids must be provided"))
                .log().ifValidationFails();
    }
}