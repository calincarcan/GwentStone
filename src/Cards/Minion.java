package Cards;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import fileio.CardInput;

import java.util.ArrayList;

@JsonPropertyOrder({"mana", "attackDamage", "health", "description", "colors", "name"})
public class Minion extends Card{
    public boolean checkDisciple(int playerId, int row) {
        if (playerId == 1)
            return row == 2 || row == 3;
        return row == 0 || row == 1;
    }
    public void useAbility(ArrayList<ArrayList<Card>> board, int x, int y) {
        Minion card = ((Minion)board.get(x).get(y));
        switch (getName()) {
            case "The Ripper": {
                card.setAttackDamage(card.getAttackDamage() - 2);
                if (card.getAttackDamage() < 0)
                    card.setAttackDamage(0);
                this.setUsed();
                break;
            }
            case "Miraj": {
                int health = card.getHealth();
                card.setHealth(this.getHealth());
                this.setHealth(health);
                this.setUsed();
                break;
            }
            case "The Cursed One": {
                int health = card.getHealth();
                int attack = card.getAttackDamage();
                card.setAttackDamage(health);
                card.setHealth(attack);

                if (card.getHealth() < 1) {
                    board.get(x).remove(y);
                }
                this.setUsed();
                break;
            }
            case "Disciple": {
                card.setHealth(card.getHealth() + 2);
                this.setUsed();
                break;
            }
        }
    }
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
