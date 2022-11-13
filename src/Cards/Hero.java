package Cards;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import fileio.CardInput;

import java.util.ArrayList;
@JsonPropertyOrder({"mana", "description", "colors", "name"})
@JsonIgnoreProperties({ "used" })
public class Hero {
    public void useAbility(int playerId, ArrayList<ArrayList<Card>> board, int row) {
        switch (this.getName()) {
            case "Lord Royce": {
                int maxAttack = ((Minion)board.get(row).get(0)).getAttackDamage();
                int poz = 0;
                for (int i = 1; i < board.get(row).size(); i++)
                    if (((Minion) board.get(row).get(i)).getAttackDamage() > maxAttack) {
                        maxAttack = ((Minion) board.get(row).get(i)).getAttackDamage();
                        poz = i;
                    }
                board.get(row).get(poz).freeze();
                this.setUsed();
                break;
            }
            case "Empress Thorina": {
                int maxHealth = ((Minion)board.get(row).get(0)).getHealth();
                int poz = 0;
                for (int i = 1; i < board.get(row).size(); i++)
                    if (((Minion) board.get(row).get(i)).getHealth() > maxHealth) {
                        maxHealth = ((Minion) board.get(row).get(i)).getHealth();
                        poz = i;
                    }
                board.get(row).remove(poz);
                this.setUsed();
                break;
            }
            case "King Mudface": {
                for (Card card : board.get(row)){
                    Minion minion = ((Minion)card);
                    minion.setHealth(minion.getHealth() + 1);
                }
                this.setUsed();
                break;
            }
            case "General Kocioraw": {
                for (Card card : board.get(row)){
                    Minion minion = ((Minion)card);
                    minion.setAttackDamage(minion.getAttackDamage() + 1);
                }
                this.setUsed();
                break;
            }
        }
    }
    public void unUse() {
        use = false;
    }
    public boolean isUsed() {
        return use;
    }
    public void setUsed() {
        use = true;
    }
    private boolean use;
    private int mana;
    private String description;
    private ArrayList<String> colors;
    private String name;
    private int health = 30;

    public String getName() {
        return name;
    }
    public void setHealth(int health) {
        this.health = health;
    }
    public int getHealth() {
        return health;
    }

    public Hero(int mana, String description, ArrayList<String> colors, String name, int health) {
        this.mana = mana;
        this.description = description;
        this.colors = colors;
        this.name = name;
        this.health = health;
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
