package steps;

import Utils.ApiClient;
import Utils.StellarBurgersUrl;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import model.Order;

import java.util.List;
import java.util.Map;

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
    public ValidatableResponse createOrder(List<String> ingredients, String token) {
        Map<String, Object> orderData = Map.of("ingredients", ingredients);

        if (token != null) {
            return given()
                    .spec(ApiClient.authSpec(token))
                    .body(orderData)
                    .when()
                    .post(StellarBurgersUrl.ORDERS)
                    .then();
        } else {
            return given()
                    .spec(ApiClient.baseSpec())
                    .body(orderData)
                    .when()
                    .post(StellarBurgersUrl.ORDERS)
                    .then();
        }
    }
}