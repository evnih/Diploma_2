package model;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import net.datafaker.Faker;


@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class User {
    private static final Faker faker = new Faker();

    @Builder.Default
    private String email = faker.internet().emailAddress();;

    @Builder.Default
    private String password = faker.internet().password(8, 16, true, true, true);

    @Builder.Default
    private String name = faker.name().fullName();;
}

