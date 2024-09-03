package ingridients;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class IngredientResponse {
        private boolean success;
        private List<Ingredient> data;
}
