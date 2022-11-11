package Cards;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import fileio.CardInput;
@JsonPropertyOrder({"mana", "description", "colors", "name"})
public class Environment extends Card{
    public Environment(CardInput card) {
        super(card);
    }
}

