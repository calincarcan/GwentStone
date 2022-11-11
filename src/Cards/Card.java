package Cards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import fileio.CardInput;

import java.util.ArrayList;
@JsonPropertyOrder({"mana", "description", "colors", "name"})
@JsonIgnoreProperties({ "frozen" })
public class Card {
    private static boolean frozen = true;

    public void freeze() {
        frozen = true;
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
