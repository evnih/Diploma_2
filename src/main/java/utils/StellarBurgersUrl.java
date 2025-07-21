package utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StellarBurgersUrl {
    public static final String BASE = "https://stellarburgers.nomoreparties.site/api";
    public static final String REGISTER = BASE + "/auth/register";
    public static final String ORDERS = BASE + "/orders";
    public static final String LOGIN = BASE + "/auth/login";
    public static final String INGREDIENTS = BASE + "/ingredients";
    public static final String USER = BASE + "/auth/user";
}
