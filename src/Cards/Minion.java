package Cards;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import fileio.CardInput;

import java.util.ArrayList;

@JsonPropertyOrder({"mana", "attackDamage", "health", "description", "colors", "name"})
public class Minion extends Card{
    private int attackDamage;
    private int health;

    public Minion(CardInput card) {
        super(card);
        attackDamage = card.getAttackDamage();
        health = card.getHealth();
    }
    public Minion(int mana, int attackDamage, int health, String description, ArrayList<String> colors, String name) {
        super();
        this.setMana(mana);
        this.attackDamage = attackDamage;
        this.health = health;
        this.setDescription(description);
        this.setColors(colors);
        this.setName(name);
    }
    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    @Override
    public String toString() {
        return "{"
                +  "mana="
                + getMana()
                +  ", attackDamage="
                + attackDamage
                + ", health="
                + health
                +  ", description='"
                + getDescription()
                + '\''
                + ", colors="
                + getColors()
                + ", name='"
                +  ""
                + getName()
                + '\''
                + '}';
    }
}
