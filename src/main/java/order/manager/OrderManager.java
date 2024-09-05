package order.manager;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import order.Order;
import util.RequestSetup;

import static io.restassured.RestAssured.given;

public class OrderManager extends RequestSetup {
    private static final String API_PATH_ORDER = "/orders";

    @Step("Создание заказа")
    public Response createOrder(Order order, String accessToken) {
        Response createOrderResponse = given()
                .spec(RequestSetup.requestSpec())
                .header("Authorization", accessToken)
                .body(order)
                .post(API_PATH_ORDER);
        return createOrderResponse;
    }

    @Step("Получение списка заказов")
    public Response takeOrderList(String accessToken) {
        Response takeOrderListResponse = given()
                .spec(RequestSetup.requestSpec())
                .header("Authorization", accessToken)
                .get(API_PATH_ORDER);
        return takeOrderListResponse;
    }
}
