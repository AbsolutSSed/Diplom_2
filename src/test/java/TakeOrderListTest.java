import io.qameta.allure.Allure;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import order.Order;
import order.manager.OrderManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import user.User;
import user.manager.UserManager;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class TakeOrderListTest {
    private OrderManager orderManager;
    private UserManager userManager;
    private String accessToken;
    private boolean expectedSuccess;
    private boolean isAuthorized;
    private int expectedResponseCode;
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
    @Parameterized.Parameters(name = "{index}: Авторизация пользователя ={0}, Ожидаемый результат ={1}, Код ответа ={2} ")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { true, true, 200 },
                { false, false, 401 }
        });
        }
    public TakeOrderListTest(boolean isAuthorized, boolean expectedSuccess, int expectedResponseCode) {
        this.isAuthorized = isAuthorized;
        this.expectedSuccess = expectedSuccess;
        this.expectedResponseCode = expectedResponseCode;
    }
    @Test
    @DisplayName("Проверка получения списка заказов с параметрами")
    public void takeOrderList() {
        Response listResponse = orderManager.takeOrderList(accessToken);
        Allure.addAttachment("Ожидаемый код ответа",String.valueOf(expectedResponseCode));
        int actualStatusCode = listResponse.getStatusCode();
        Allure.addAttachment("Фактический код ответа",String.valueOf(actualStatusCode));
        listResponse.then().statusCode(expectedResponseCode);
        String successString = listResponse.then().extract().path("success").toString();
        boolean isSuccess = Boolean.parseBoolean(successString);

        if (expectedSuccess) {
            assertTrue(isSuccess);

        } else {
            assertFalse(isSuccess);
        }
    }
}
