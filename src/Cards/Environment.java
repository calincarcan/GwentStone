package Cards;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import fileio.CardInput;

import java.util.ArrayList;

@JsonPropertyOrder({"mana", "description", "colors", "name"})
public class Environment extends Card{
    public void useAbility(int affectedRow, ArrayList<ArrayList<Card>> board) {
            switch (this.getName()) {
                case "Winterfell": {
                    for (Card card : board.get(affectedRow))
                        card.freeze();
                    break;
                }
                case "Firestorm": {
                    for (Card card : board.get(affectedRow))
                        ((Minion)card).setHealth(((Minion)card).getHealth() - 1);
                    int maxSize = board.get(affectedRow).size();
                    for (int i = 0; i < board.get(affectedRow).size(); i++)
                        if (((Minion)board.get(affectedRow).get(i)).getHealth() <= 0) {
                            board.get(affectedRow).remove(i);
                            i--;
                            maxSize--;
                        }
                    break;
                }
                case "Heart Hound": {
                    int stolen = 0;
                    int health = ((Minion)board.get(affectedRow).get(0)).getHealth();
                    for (int i = 1; i < board.get(affectedRow).size(); i++) {
                        if (((Minion)board.get(affectedRow).get(i)).getHealth() > health) {
                            stolen = i;
                            health = ((Minion)board.get(affectedRow).get(i)).getHealth();
                        }
                    }
                    switch (affectedRow) {
                        case 0: {
                            board.get(3).add(board.get(affectedRow).get(stolen));
                            board.get(0).remove(board.get(0).get(stolen));
                        break;
                        }
                        case 1: {
                            board.get(2).add(board.get(affectedRow).get(stolen));
                            board.get(1).remove(board.get(0).get(stolen));
                            break;
                        }
                        case 2: {
                            board.get(1).add(board.get(affectedRow).get(stolen));
                            board.get(2).remove(board.get(0).get(stolen));
                            break;
                        }
                        case 3: {
                            board.get(0).add(board.get(affectedRow).get(stolen));
                            board.get(3).remove(board.get(0).get(stolen));
                            break;
                        }
                    }
                    break;
                }
            }
    }
    public Environment(CardInput card) {
        super(card);
    }
    public Environment(int mana, String description, String name, ArrayList<String> colors) {
        super(mana, description, name, colors);
    }

}

