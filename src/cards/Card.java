package cards;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import fileio.CardInput;

import java.util.ArrayList;
@JsonPropertyOrder({"mana", "description", "colors", "name"})
@JsonIgnoreProperties({ "frozen", "used" })
public class Card {
    /**
     *
     * @param name name of a minion
     * @return Compare the name given to the names of the tanks. The result is true if the card
     * whose name is given is a tank.
     */
    public static boolean isTank(final String name) {
        return name.equals("Warden") || name.equals("Goliath");
    }

    /**
     *
     * @param name name of a card
     * @return Compare the name given to the names of the environments.
     * The result is true if the card
     * whose name is given is a tank.
     */
    public static boolean checkEnvironment(final String name) {
        return name.equals("Heart Hound") || name.equals("Winterfell") || name.equals("Firestorm");
    }

    /**
     *
     * @param name name of a card
     * @return Compare the name given to the names of the minions. The result is true if the card
     * whose name is given is a minion.
     */
    public static boolean checkMinion(final String name) {
        return     name.equals("Sentinel")
                || name.equals("Berserker")
                || name.equals("Goliath")
                || name.equals("Warden")
                || name.equals("The Ripper")
                || name.equals("Miraj")
                || name.equals("The Cursed One")
                || name.equals("Disciple");
    }

    /**
     *
     * @param name name of a minion
     * @return Compare the name given to the names of the minions that stay in the front row.
     * The result is true if the card
     * whose name is given is a minion.
     */
    public static boolean frontMinion(final String name) {
        return name.equals("The Ripper") || name.equals("Miraj") || name.equals("Goliath")
                || name.equals("Warden");
    }
    /**
     *
     * @param name name of a minion
     * @return Compare the name given to the names of the minions that stay in the back row.
     * The result is true if the card
     * whose name is given is a minion.
     */
    public static boolean backMinion(final String name) {
        return name.equals("Sentinel") || name.equals("Berserker") || name.equals("Disciple")
                || name.equals("The Cursed One");
    }
    private boolean frozen;
    private boolean used;

    /**
     *
     * @return Returns the value of the field "used" of a minion. The result is true if the minion
     * hasn't attacked this turn.
     */
    public boolean isUsed() {
        return used;
    }

    /**
     * The method sets the field "used" of a minion to true.
     */
    public void setUsed() {
        this.used = true;
    }
    /**
     * The method sets the field "used" of a minion to false.
     */
    public void unUse() {
        this.used = false;
    }
    /**
     * The method sets the field "frozen" of a minion to true.
     */
    public void freeze() {
        frozen = true;
    }
    /**
     * The method sets the field "frozen" of a minion to false.
     */
    public void unfreeze() {
        frozen = false;
    }

    /**
     *
     * @return Returns the value of the field "frozen" of a minion. The result is true if the
     * minion is frozen this turn.
     */
    public boolean getFrozen() {
        return frozen;
    }
    private String description;
    private int mana;
    private ArrayList<String> colors;
    private String name;
    public Card(final int mana, final String description, final String name,
                final ArrayList<String> colors) {
        this.mana = mana;
        this.description = description;
        this.name = name;
        this.colors = colors;
    }
    public Card() {

    }
    public Card(final CardInput card) {
        mana = card.getMana();
        description = new String(card.getDescription());
        colors = new ArrayList<String>(card.getColors());
        name = new String(card.getName());
    }

    /**
     * The method is used to activate the ability of a minion.
     * It is overwritten in the Minion class.
     */
    public void ability() {

    }
    /**
     *
     * @return Returns the value of the mana the player has accumulated to the current turn.
     */
    public int getMana() {
        return mana;
    }

    /**
     *
     * @param mana
     * The method sets the mana of a player to the given value;
     */
    public void setMana(final int mana) {
        this.mana = mana;
    }

    /**
     *
     * @return Returns the value of the description of a card.
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The method sets the description of a card.
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     *
     * @return Returns the colors of a card.
     */
    public ArrayList<String> getColors() {
        return colors;
    }

    /**
     *
     * @param colors
     * The method sets the colors of a card.
     */
    public void setColors(final ArrayList<String> colors) {
        this.colors = colors;
    }

    /**
     *
     * @return Returns the name of the card.
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The method sets the name of the card to the given name;
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     *
     * @return The method returns the information of a card as a String.
     */
    @Override
    public String toString() {
        return "{"
                +  "mana="
                + mana
                +  ", description='"
                + description
                + '\''
                + ", colors="
                + colors
                + ", name='"
                +  ""
                + name
                + '\''
                + '}';
    }
}
