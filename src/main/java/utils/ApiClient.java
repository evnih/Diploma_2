package utils;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import lombok.experimental.UtilityClass;



@UtilityClass
public class ApiClient {

    public static RequestSpecification baseSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(StellarBurgersUrl.BASE)
                .setContentType("application/json")
                .build();
    }

    public static RequestSpecification authSpec(String token) {
        RequestSpecification spec = baseSpec();
        return new RequestSpecBuilder()
                .addRequestSpecification(spec)
                .addHeader("Authorization", token)
                .build();
    }

    public static RequestSpecification specWithBody(Object body) {
        RequestSpecification spec = baseSpec();
        return new RequestSpecBuilder()
                .addRequestSpecification(spec)
                .setBody(body)
                .build();
    }
}
