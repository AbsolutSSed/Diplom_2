import ingridients.Ingredient;
import ingridients.IngredientResponse;
import ingridients.manager.IngredientManager;
import io.qameta.allure.Allure;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import order.Order;
import order.manager.OrderManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import user.User;
import user.manager.UserManager;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class CreateOrderTest {

    private OrderManager orderManager;
    private UserManager userManager;
    private String accessToken;
    private Order order;
    private boolean expectedSuccess;
    private boolean isAuthorized;
    private int expectedResponseCode;
    private static List<String> ingredientIds;

    @Before
    public void setUp() {
        userManager = new UserManager();
        orderManager = new OrderManager();

        if (isAuthorized) {
            User user = userManager.createUserData();
            userManager.createNewUser(user);
            accessToken = userManager.userLoginAndExtractToken(user);
        } else {
            accessToken = ""; // Пустой токен для неавторизованных запросов
        }
    }

    @Parameterized.Parameters(name = "{index}: Авторизация пользователя ={0}, Ингредиенты ={1}, Ожидаемый результат ={2}, Код ответа ={3} ")
    public static Collection<Object[]> data() {
        IngredientManager ingredientManager = new IngredientManager();
        IngredientResponse ingredientResponse = ingredientManager.takeIngredientList();
        ingredientIds = ingredientResponse
                .getData()
                .stream()
                .map(Ingredient::getId)
                .collect(Collectors.toList());

        return Arrays.asList(new Object[][] {
                // Создание заказа с авторизацией и правильными ингредиентами
                { true, ingredientIds.subList(0,10), true, 200 },
                // Создание заказа без авторизации и правильными ингредиентами
                { false, ingredientIds.subList(1,5), true, 200 },
                // Создание заказа с авторизацией, но без ингредиентов
                { true, Arrays.asList(), false, 400 },
                // Создание заказа с авторизацией и неверным хешем ингредиентов
                { true, Arrays.asList("invalidHash"), false, 500 },
        });
    }

    public CreateOrderTest(boolean isAuthorized, List<String> ingredients, boolean expectedSuccess, int expectedResponseCode) {
        this.isAuthorized = isAuthorized;
        this.expectedSuccess = expectedSuccess;
        this.order = new Order(ingredients);
        this.expectedResponseCode = expectedResponseCode;
    }

    @Test
    @DisplayName("Проверка создания заказа с параметрами")
    public void createOrder() {
        Response createResponse = orderManager.createOrder(order, accessToken);
        Allure.addAttachment("Ожидаемый код ответа",String.valueOf(expectedResponseCode));
        int actualStatusCode = createResponse.getStatusCode();
        Allure.addAttachment("Фактический код ответа",String.valueOf(actualStatusCode));
        createResponse.then().statusCode(expectedResponseCode);
        String successString = createResponse.then().extract().path("success").toString();
        boolean isSuccess = Boolean.parseBoolean(successString);

        if (expectedSuccess) {
            assertTrue(isSuccess);

        } else {
            assertFalse(isSuccess);
        }
    }
    @After
    public void deleteCreatedUsers() {
        if (isAuthorized) {
            userManager.deleteUser(accessToken);
        }
    }
}
