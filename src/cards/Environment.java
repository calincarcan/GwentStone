package cards;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import fileio.CardInput;
import java.util.ArrayList;


@JsonPropertyOrder({"mana", "description", "colors", "name"})
public class Environment extends Card {
    static final int FRONT_ROW_1 = 2;
    static final int FRONT_ROW_2 = 1;
    static final int BACK_ROW_1 = 3;
    static final int BACK_ROW_2 = 0;
    /**
     *
     * @param affectedRow The row of the board where the environment affects the minion.
     * @param board The board where the minions are placed and the game takes place.
     * The method activates the ability of an environment card. Winterfell
     * freezes all the cards on the affected row, making them unable to attack next turn.
     * Firestorm damages all the minions on the affected row with 1 dmg.
     * Heart Hound removes the minion with the highest hp from the row adding it to the row of
     *              the player using the card.
     */
    public void useAbility(final int affectedRow, final ArrayList<ArrayList<Card>> board) {
        switch (this.getName()) {
            case "Winterfell" -> {
                for (Card card : board.get(affectedRow)) {
                    card.freeze();
                }
            }
            case "Firestorm" -> {
                for (Card card : board.get(affectedRow)) {
                    ((Minion) card).setHealth(((Minion) card).getHealth() - 1);
                }
                for (int i = 0; i < board.get(affectedRow).size(); i++) {
                    if (((Minion) board.get(affectedRow).get(i)).getHealth() <= 0) {
                        board.get(affectedRow).remove(i);
                        i--;
                    }
                }
            }
            case "Heart Hound" -> {
                int stolen = 0;
                int health = ((Minion) board.get(affectedRow).get(0)).getHealth();
                for (int i = 1; i < board.get(affectedRow).size(); i++) {
                    if (((Minion) board.get(affectedRow).get(i)).getHealth() > health) {
                        stolen = i;
                        health = ((Minion) board.get(affectedRow).get(i)).getHealth();
                    }
                }
                switch (affectedRow) {
                    case BACK_ROW_2 -> {
                        board.get(BACK_ROW_1).add(board.get(affectedRow).get(stolen));
                        board.get(BACK_ROW_2).remove(board.get(BACK_ROW_2).get(stolen));
                    }
                    case FRONT_ROW_2 -> {
                        board.get(FRONT_ROW_1).add(board.get(affectedRow).get(stolen));
                        board.get(FRONT_ROW_2).remove(board.get(BACK_ROW_2).get(stolen));
                    }
                    case FRONT_ROW_1 -> {
                        board.get(FRONT_ROW_2).add(board.get(affectedRow).get(stolen));
                        board.get(FRONT_ROW_1).remove(board.get(BACK_ROW_2).get(stolen));
                    }
                    case BACK_ROW_1 -> {
                        board.get(BACK_ROW_2).add(board.get(affectedRow).get(stolen));
                        board.get(BACK_ROW_1).remove(board.get(BACK_ROW_2).get(stolen));
                    }
                    default -> {
                    }
                }
            }
            default -> {

            }
        }
    }

    public Environment(final CardInput card) {
        super(card);
    }
    public Environment(final int mana, final String description, final String name,
                       final ArrayList<String> colors) {
        super(mana, description, name, colors);
    }

}

