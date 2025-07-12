package steps;

import Utils.ApiClient;
import Utils.StellarBurgersUrl;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import model.Order;

import static io.restassured.RestAssured.given;



public class OrdersSteps {
    private final RequestSpecification requestSpec;

    public OrdersSteps() {
        this.requestSpec = ApiClient.baseSpec();
    }

    public OrdersSteps(String token) {
        this.requestSpec = ApiClient.authSpec(token);
    }

    @Step("Создание заказа")
    public ValidatableResponse createOrder(Order order) {
        return given(requestSpec)
                .body(order)
                .when()
                .post(StellarBurgersUrl.ORDERS)
                .then();
    }
}