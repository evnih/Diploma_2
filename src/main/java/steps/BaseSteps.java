package steps;

import utils.ApiClient;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class BaseSteps {
    protected final RequestSpecification requestSpec;

    public BaseSteps() {
        this.requestSpec = ApiClient.baseSpec();
    }

    public BaseSteps(String token) {
        this.requestSpec = ApiClient.authSpec(token);
    }
}
