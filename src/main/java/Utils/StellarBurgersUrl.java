package Utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StellarBurgersUrl {
    public final String BASE = "https://stellarburgers.nomoreparties.site/api";
    public final String REGISTER = BASE + "/auth/register";
    public final String ORDERS = BASE + "/orders";
    public static final String LOGIN_ENDPOINT = BASE + "/auth/login";
    public final String INGREDIENTS = BASE + "/ingredients";
    public static final String USER = BASE + "/auth/user";
}
