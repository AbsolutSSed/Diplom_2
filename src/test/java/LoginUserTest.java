import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import user.User;
import user.manager.UserManager;
import user.UserResponses;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LoginUserTest {
    private UserManager userManager;
    private User user;

    @Before
    public void setUp(){
        userManager = new UserManager();
        user = userManager.createUserData();
    }
    @Test
    @DisplayName("Вход в учетную запись пользователя")
    public void loginUserAccount(){
        userManager.createNewUser(user);
        Response loginResponse = userManager.userLogin(user);
        UserResponses userResponses = loginResponse.getBody().as(UserResponses.class);
        assertEquals(true,userResponses.isSuccess());
    }
    @Test
    @DisplayName("Невозможно войти в учетную запись пользователя с неверным паролем")
    public void loginInvalidPasswordUserAccount(){
        userManager.createNewUser(user);
        user.setPassword("invalidpass");
        Response loginResponse = userManager.userLogin(user);
        UserResponses userResponses = loginResponse.getBody().as(UserResponses.class);
        assertEquals(false,userResponses.isSuccess());
    }
    @Test
    @DisplayName("Невозможно войти в учетную запись пользователя с неверным email")
    public void loginInvalidEmailUserAccount(){
        userManager.createNewUser(user);
        user.setEmail("invalidemail");
        Response loginResponse = userManager.userLogin(user);
        UserResponses userResponses = loginResponse.getBody().as(UserResponses.class);
        assertEquals(false,userResponses.isSuccess());
    }
}
