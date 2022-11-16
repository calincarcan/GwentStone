package cards;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import fileio.CardInput;

import java.util.ArrayList;

@JsonPropertyOrder({"mana", "attackDamage", "health", "description", "colors", "name"})
public class Minion extends Card {
    static final int FIRST_PLAYER_ID = 1;
    static final int FRONT_ROW_1 = 2;
    static final int FRONT_ROW_2 = 1;
    static final int BACK_ROW_1 = 3;
    static final int BACK_ROW_2 = 0;
    static final int MINIMUM_DMG = 0;
    static final int RIPPER_DEBUFF = 2;
    static final int MINIMUM_HP = 0;
    static final int DISCIPLE_BUFF = 2;
    /**
     *
     * @param playerId active player
     * @param row the affected row
     * @return The method checks if Disciple is allowed to use its ability on that row.
     */
    public boolean checkDisciple(final int playerId, final  int row) {
        if (playerId == FIRST_PLAYER_ID) {
            return row == FRONT_ROW_1 || row == BACK_ROW_1;
        }
        return row == BACK_ROW_2 || row == FRONT_ROW_2;
    }

    /**
     *
     * @param board the board
     * @param x Attacked card row
     * @param y Attacked card column
     * The method checks what Minion is using the ability based on its name then affects the
     *          attacked card accordingly.
     */
    public void useAbility(final ArrayList<ArrayList<Card>> board, final int x, final int y) {
        Minion card = ((Minion) board.get(x).get(y));
        switch (getName()) {
            case "The Ripper" -> {
                card.setAttackDamage(card.getAttackDamage() - RIPPER_DEBUFF);
                if (card.getAttackDamage() < MINIMUM_DMG) {
                    card.setAttackDamage(MINIMUM_DMG);
                }
                this.setUsed();
            }
            case "Miraj" -> {
                int health = card.getHealth();
                card.setHealth(this.getHealth());
                this.setHealth(health);
                this.setUsed();
            }
            case "The Cursed One" -> {
                int health = card.getHealth();
                int attack = card.getAttackDamage();
                card.setAttackDamage(health);
                card.setHealth(attack);

                if (card.getHealth() <= MINIMUM_HP) {
                    board.get(x).remove(y);
                }
                this.setUsed();
            }
            case "Disciple" -> {
                card.setHealth(card.getHealth() + DISCIPLE_BUFF);
                this.setUsed();
            }
            default -> {

            }
        }
    }
    private int attackDamage;
    private int health;

    public Minion(final CardInput card) {
        super(card);
        attackDamage = card.getAttackDamage();
        health = card.getHealth();
    }
    public Minion(final int mana, final int attackDamage, final int health,
                  final String description, final ArrayList<String> colors, final String name) {
        super();
        this.setMana(mana);
        this.attackDamage = attackDamage;
        this.health = health;
        this.setDescription(description);
        this.setColors(colors);
        this.setName(name);
    }

    /**
     *
     * @return Returns the attack of a minion.
     */
    public int getAttackDamage() {
        return attackDamage;
    }

    /**
     * Sets the attack of a minion to the given value.
     */
    public void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    /**
     *
     * @return Returns the health of the minion.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Sets the health of the minion to a given value.
     */
    public void setHealth(final int health) {
        this.health = health;
    }
    /**
     *
     * @return The method returns the information of a card as a String.
     */
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
