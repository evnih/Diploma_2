package steps;

import model.Order;
import utils.ApiClient;
import utils.StellarBurgersUrl;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;



public class OrdersSteps extends BaseSteps{



    public OrdersSteps() {
        super();
    }


    public OrdersSteps(String token) {
        super(token);
    }

    @Step("Создание заказа")
    public ValidatableResponse createOrder(Order order) {

            return given()
                    .spec(requestSpec)
                    .body(order)
                    .when()
                    .post(StellarBurgersUrl.ORDERS)
                    .then();
        }
    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrderUnauthorized(Order order) {
        return given()
                .spec(ApiClient.baseSpec())
                .body(order)
                .when()
                .post(StellarBurgersUrl.ORDERS)
                .then();
    }

    @Step("Создание заказа с ингредиентами")
    public ValidatableResponse createOrder(List<String> ingredients) {
        Order order = Order.builder()
                .ingredients(ingredients)
                .build();
        return createOrder(order);
    }

    @Step("Создание заказа с ингредиентами без авторизации")
    public ValidatableResponse createOrderUnauthorized(List<String> ingredients) {
        Order order = Order.builder()
                .ingredients(ingredients)
                .build();
        return createOrderUnauthorized(order);
    }

}