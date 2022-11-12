package Cards;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import fileio.CardInput;

import java.util.ArrayList;
@JsonPropertyOrder({"mana", "description", "colors", "name"})
//@JsonIgnoreProperties({ "manaIncrement" })
public class Hero {
    private int mana;
    private String description;
    private ArrayList<String> colors;
    private String name;
    private static int health = 30;

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }
    public Hero(CardInput hero) {
        mana = hero.getMana();
        description = hero.getDescription();
        colors = hero.getColors();
        name = hero.getName();
    }

    public void resetHealth() {
        health = 30;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public void setColors(ArrayList<String> colors) {
        this.colors = colors;
    }
}
