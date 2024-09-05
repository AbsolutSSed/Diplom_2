package user.manager;

import user.generator.UserDataGenerator;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import util.RequestSetup;
import user.User;

import static io.restassured.RestAssured.*;

public class UserManager extends RequestSetup {
    private UserDataGenerator userDataGenerator = new UserDataGenerator();
    private static final String API_PATH_CREATE_USER = "/auth/register";
    private static final String API_PATH_LOGIN_USER = "/auth/login";
    private static final String API_PATH_DELETE_USER = "/auth/user";
    private static final String API_PATH_UPDATE_USER_DATA = "/auth/user";
    @Step("Генерация данных пользователя")
    public User createUserData() {
        User user = userDataGenerator.generateUserData();
        return user;
    }
    @Step("Регистрация пользователя")
    public Response createNewUser(User user) {
        Response createResponse = given()
                .spec(RequestSetup.requestSpec())
                .body(user)
                .when()
                .post(API_PATH_CREATE_USER);
        return createResponse;
    }
    @Step("Вход под данными пользователя и извлечение access токена")
    public String userLoginAndExtractToken(User user) {
        Response loginResponse = given()
                .spec(RequestSetup.requestSpec())
                .body(user)
                .when()
                .post(API_PATH_LOGIN_USER);
        String accessToken = loginResponse.jsonPath().getString("accessToken");
        return accessToken;
    }
    @Step("Вход под данными пользователя")
    public Response userLogin(User user) {
        Response loginResponse = given()
                .spec(RequestSetup.requestSpec())
                .body(user)
                .when()
                .post(API_PATH_LOGIN_USER);

        return loginResponse;
    }
    @Step("Удаление созданного пользователя")
    public void deleteUser(String accessToken) {
        Response deleteResponse = given()
                .spec(RequestSetup.requestSpec())
                .header("Authorization", accessToken)
                .delete(API_PATH_DELETE_USER);
    }
    @Step("Обновление данных пользователя с передачей авторизации")
    public Response updateLoggedUserData(User editedUser, String accessToken) {
        Response updateResponse = given()
                .spec(RequestSetup.requestSpec())
                .header("Authorization", accessToken)
                .body(editedUser)
                .patch(API_PATH_UPDATE_USER_DATA);
        return updateResponse;
    }
    @Step("Обновление данных пользователя с передачей авторизации")
    public Response updateNotLoggedUserData(User editedUser, String accessToken) {
        Response updateResponse = given()
                .spec(RequestSetup.requestSpec())
                .header("Authorization", accessToken)
                .body(editedUser)
                .patch(API_PATH_UPDATE_USER_DATA);
        return updateResponse;
    }
}
