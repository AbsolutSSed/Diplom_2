import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import user.User;
import user.manager.UserManager;
import user.UserResponses;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
public class UpdateUserDataTest {
    private UserManager userManager;
    private User user;
    private String accessToken;

    Faker faker = new Faker();

    @Before
    public void setUp(){
        userManager = new UserManager();
        user = userManager.createUserData();
        userManager.createNewUser(user);
    }
    @Test
    @DisplayName("Изменение пользовательского email авторизованного пользователя")
    public void editUserEmailLoggedUser() {
        accessToken = userManager.userLoginAndExtractToken(user);
        user.setEmail(faker.internet().emailAddress());
        Response updateResponse = userManager.updateLoggedUserData(user,accessToken);
        UserResponses userResponses = updateResponse.getBody().as(UserResponses.class);
        assertEquals(true,userResponses.isSuccess());
    }
    @Test
    @DisplayName("Изменение пользовательского имени авторизованного пользователя")
    public void editUserNameLoggedUser() {
        accessToken = userManager.userLoginAndExtractToken(user);
        user.setName(faker.funnyName().name());
        Response updateResponse = userManager.updateLoggedUserData(user,accessToken);
        UserResponses userResponses = updateResponse.getBody().as(UserResponses.class);
        assertEquals(true,userResponses.isSuccess());
    }
    @Test
    @DisplayName("Невозможно изменить email неавторизованным пользователем")
    public void editUserEmailNotLoggedUser() {
        user.setEmail(faker.internet().emailAddress());
        Response updateResponse = userManager.updateLoggedUserData(user,"null");
        UserResponses userResponses = updateResponse.getBody().as(UserResponses.class);
        assertEquals(false,userResponses.isSuccess());
    }
    @Test
    @DisplayName("Невозможно изменить имя неавторизованным пользователем")
    public void editUserNameNotLoggedUser() {
        user.setName(faker.funnyName().name());
        Response updateResponse = userManager.updateLoggedUserData(user,"null");
        UserResponses userResponses = updateResponse.getBody().as(UserResponses.class);
        assertEquals(false,userResponses.isSuccess());
    }
}
