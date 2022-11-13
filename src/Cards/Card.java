package Cards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import fileio.CardInput;

import java.util.ArrayList;
@JsonPropertyOrder({"mana", "description", "colors", "name"})
@JsonIgnoreProperties({ "frozen", "used", "frozenCount" })
public class Card {
    public static boolean isTank(String name) {
        return name.equals("Warden ") || name.equals("Goliath ");
    }
    public static boolean checkEnvironment(String name) {
        return name.equals("Heart Hound") || name.equals("Winterfell") || name.equals("Firestorm");
    }
    public static boolean checkMinion(String name) {
        return  name.equals("Sentinel") || name.equals("Berserker") || name.equals("Goliath") ||
                name.equals("Warden") || name.equals("The Ripper") || name.equals("Miraj") ||
                name.equals("The Cursed One") || name.equals("Disciple");
    }
    public static boolean frontMinion(String name) {
        return name.equals("The Ripper") || name.equals("Miraj") || name.equals("Goliath") || name.equals("Warden");
    }
    public static boolean backMinion(String name) {
        return name.equals("Sentinel") || name.equals("Berserker") || name.equals("Disciple") || name.equals("The Cursed One");
    }
    private boolean frozen;
    private int frozenCount;

    public int getFrozenCount() {
        return frozenCount;
    }

    public void setFrozenCount(int frozenCount) {
        this.frozenCount = frozenCount;
    }

    private boolean used;

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public void freeze() {
        frozen = true;
        frozenCount = 0;
    }
    public void unfreeze(){
        frozen = false;
    }
    public boolean getFrozen() {
        return frozen;
    }
    private String description;
    private int mana;
    private ArrayList<String> colors;
    private String name;
    public Card(int mana, String description, String name, ArrayList<String> colors){
        this.mana = mana;
        this.description = description;
        this.name = name;
        this.colors = colors;
    }
    public Card() {

    }
    public Card(CardInput card) {
        mana = card.getMana();
        description = new String(card.getDescription());
        colors = new ArrayList<String>(card.getColors());
        name = new String(card.getName());
    }
    public void ability() {}
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
