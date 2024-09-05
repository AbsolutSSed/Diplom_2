import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import user.User;
import user.manager.UserManager;
import user.UserResponses;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CreateUserTest {
    private UserManager userManager;
    private User user;

    @Before
    public void setUp(){
        userManager = new UserManager();
        user = userManager.createUserData();
    }
    @Test
    @DisplayName("Создание пользователя")
    public void createUser() {
        Response createResponse = userManager.createNewUser(user);
        createResponse.then().statusCode(200);
        UserResponses userResponses = createResponse.getBody().as(UserResponses.class);
        assertEquals(true,userResponses.isSuccess());
        String accessToken = userManager.userLoginAndExtractToken(user);
        userManager.deleteUser(accessToken);
    }
    @Test
    @DisplayName("Невозможно создать пользователя который уже зарегистрирован")
    public void createDoubleUser() {
        Response createFirstTime = userManager.createNewUser(user);
        createFirstTime.then().statusCode(200);
        UserResponses firstCreationResult = createFirstTime.getBody().as(UserResponses.class);
        //Проверяем что первый пользователь был успешно создан
        assertEquals(true,firstCreationResult.isSuccess());

        Response tryToCreateSecondTime = userManager.createNewUser(user);
        tryToCreateSecondTime.then().statusCode(403);
        UserResponses secondCreationResult = tryToCreateSecondTime.getBody().as(UserResponses.class);
        //Проверяем что не удалось создать второго такого же пользователя
        assertEquals(false,secondCreationResult.isSuccess());
        String accessToken = userManager.userLoginAndExtractToken(user);
        userManager.deleteUser(accessToken);
    }
    @Test
    @DisplayName("Невозможно создать пользователя без email")
    public void createUserWithoutEmail() {
        user.setEmail(null);
        Response createResponse = userManager.createNewUser(user);
        createResponse.then().statusCode(403);
        UserResponses userResponses = createResponse.getBody().as(UserResponses.class);
        assertEquals(false,userResponses.isSuccess());
    }
    @Test
    @DisplayName("Невозможно создать пользователя без пароля")
    public void createUserWithoutPassword() {
        user.setPassword(null);
        Response createResponse = userManager.createNewUser(user);
        createResponse.then().statusCode(403);
        UserResponses userResponses = createResponse.getBody().as(UserResponses.class);
        assertEquals(false,userResponses.isSuccess());
    }
    @Test
    @DisplayName("Невозможно создать пользователя без имени")
    public void createUserWithoutName() {
        user.setName(null);
        Response createResponse = userManager.createNewUser(user);
        createResponse.then().statusCode(403);
        UserResponses userResponses = createResponse.getBody().as(UserResponses.class);
        assertEquals(false,userResponses.isSuccess());
    }
}
