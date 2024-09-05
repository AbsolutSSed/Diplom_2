package ingridients.manager;

import ingridients.IngredientResponse;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import util.RequestSetup;

public class IngredientManager extends RequestSetup {
    private static final String API_PATH_INGREDIENTS = "/ingredients";

    @Step("Получение ингридиентов")
    public IngredientResponse takeIngredientList() {
        Response takeIngredientListResponse = RestAssured.given()
                .spec(RequestSetup.requestSpec())
                .get(API_PATH_INGREDIENTS);
        return takeIngredientListResponse.body().as(IngredientResponse.class);
    }
}
