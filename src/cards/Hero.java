package cards;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import fileio.CardInput;

import java.util.ArrayList;

@JsonPropertyOrder({"mana", "description", "colors", "name"})
@JsonIgnoreProperties({"used"})
public class Hero {
    static final int ORIGINAL_HEALTH = 30;
    /**
     * @param playerId The id of the player who uses the hero
     * @param board The board where the minions are placed and the game takes place.
     * @param row The row of the board where the environment affects the minion.
     * The method activates the ability of an environment card. Winterfell
     * freezes all the cards on the affected row, making them unable to attack next turn.
     * Firestorm damages all the minions on the affected row with 1 dmg.
     * Heart Hound removes the minion with the highest hp from the row adding it to the row of
     *              the player using the card.
     */
    public void useAbility(final int playerId, final ArrayList<ArrayList<Card>> board,
                           final int row) {
        switch (this.getName()) {
            case "Lord Royce" -> {
                int maxAttack = ((Minion) board.get(row).get(0)).getAttackDamage();
                int poz = 0;
                for (int i = 1; i < board.get(row).size(); i++) {
                    if (((Minion) board.get(row).get(i)).getAttackDamage() > maxAttack) {
                        maxAttack = ((Minion) board.get(row).get(i)).getAttackDamage();
                        poz = i;
                    }
                }
                board.get(row).get(poz).freeze();
                this.setUsed();
            }
            case "Empress Thorina" -> {
                int maxHealth = ((Minion) board.get(row).get(0)).getHealth();
                int poz = 0;
                for (int i = 1; i < board.get(row).size(); i++) {
                    if (((Minion) board.get(row).get(i)).getHealth() > maxHealth) {
                        maxHealth = ((Minion) board.get(row).get(i)).getHealth();
                        poz = i;
                    }
                }
                board.get(row).remove(poz);
                this.setUsed();
            }
            case "King Mudface" -> {
                for (Card card : board.get(row)) {
                    Minion minion = ((Minion) card);
                    minion.setHealth(minion.getHealth() + 1);
                }
                this.setUsed();
            }
            case "General Kocioraw" -> {
                for (Card card : board.get(row)) {
                    Minion minion = ((Minion) card);
                    minion.setAttackDamage(minion.getAttackDamage() + 1);
                }
                this.setUsed();
            }
            default -> {

            }
        }
    }

    /**
     * Resets the hero, so it can attack and use an ability again the next turn.
     */
    public void unUse() {
        use = false;
    }

    /**
     *
     * @return Returns true if the hero has already been used.
     */
    public boolean isUsed() {
        return use;
    }

    /**
     * Sets the hero used, so it cannot attack or use an ability again the same turn.
     */
    public void setUsed() {
        use = true;
    }

    private boolean use;
    private int mana;
    private String description;
    private ArrayList<String> colors;
    private final String name;
    private int health = ORIGINAL_HEALTH;

    /**
     *
     * @return Returns the name of the hero/
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the health of the hero to the given value.
     */
    public void setHealth(final int health) {
        this.health = health;
    }

    /**
     *
     * @return Returns the health of the hero.
     */
    public int getHealth() {
        return health;
    }

    public Hero(final int mana, final String description, final ArrayList<String> colors,
                final String name, final int health) {
        this.mana = mana;
        this.description = description;
        this.colors = colors;
        this.name = name;
        this.health = health;
    }

    public Hero(final CardInput hero) {
        mana = hero.getMana();
        description = hero.getDescription();
        colors = hero.getColors();
        name = hero.getName();
    }

    /**
     *
     * @return Returns the mana required for using the hero ability.
     */
    public int getMana() {
        return mana;
    }

    /**
     * Sets the mana for the hero ability.
     */
    public void setMana(final int mana) {
        this.mana = mana;
    }

    /**
     *
     * @return Returns the description of the hero;
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the hero.
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     *
     * @return Returns the colors of a hero.
     */
    public ArrayList<String> getColors() {
        return colors;
    }

    /**
     *
     * Sets the colors of the hero.
     */
    public void setColors(final ArrayList<String> colors) {
        this.colors = colors;
    }
}
