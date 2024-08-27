package user.generator;
import com.github.javafaker.Faker;
import user.User;

public class UserDataGenerator {
    Faker faker = new Faker();
    public User generateUserData() {
        User user = new User(faker.internet().emailAddress(),faker.dune().character(),faker.funnyName().name());
        return user;
    }
}
