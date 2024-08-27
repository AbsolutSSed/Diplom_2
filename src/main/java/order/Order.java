package order;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Order {
    private List<String> ingredients;

    @Override
    public String toString() {
        return "Order{" +
                "ingredients=" + ingredients +
                '}';
    }
}
