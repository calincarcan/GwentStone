package player;

import cards.Card;
import cards.Environment;
import cards.Hero;
import cards.Minion;
import fileio.Coordinates;
import fileio.DecksInput;

import java.util.ArrayList;

public class Player {
    static final int STARTING_MANA = 1;
    static final int ZERO = 0;
    static final int MAX_MANA = 10;
    static final int PLAYER_ONE_ID = 1;
    static final int FRONT_ROW_1 = 2;
    static final int FRONT_ROW_2 = 1;
    static final int BACK_ROW_1 = 3;
    static final int BACK_ROW_2 = 0;
    private int mana;
    private ArrayList<ArrayList<Card>> decks;
    private final ArrayList<Card> hand;
    private int nrCardsInDeck;
    private int nrDecks;
    private int indexDeck;
    private Hero hero;

    /**
     * @param playerId active player
     * @param board    the board
     * @return The method checks if there are tanks to block the attack to the hero.
     */
    public static boolean checkTanksHero(final int playerId,
                                         final ArrayList<ArrayList<Card>> board) {
        boolean areTanks = false;
        if (playerId == PLAYER_ONE_ID) {
            for (Card var : board.get(BACK_ROW_2)) {
                if (Card.isTank(var.getName())) {
                    areTanks = true;
                    break;
                }
            }
            for (Card var : board.get(FRONT_ROW_2)) {
                if (Card.isTank(var.getName())) {
                    areTanks = true;
                    break;
                }
            }
        } else {
            for (Card var : board.get(FRONT_ROW_1)) {
                if (Card.isTank(var.getName())) {
                    areTanks = true;
                    break;
                }
            }
            for (Card var : board.get(BACK_ROW_1)) {
                if (Card.isTank(var.getName())) {
                    areTanks = true;
                    break;
                }
            }
        }
        return !areTanks;
    }

    /**
     * @param playerId active player
     * @param board    the board
     * @param attacked the coordinates of the attacked minion
     * @return The method checks if there are tanks to block the attack to the minion.
     */
    public static boolean checkTanks(final int playerId, final ArrayList<ArrayList<Card>> board, final Coordinates attacked) {
        boolean isTank = false;
        boolean areTanks = false;
        Card card = board.get(attacked.getX()).get(attacked.getY());
        if (Card.isTank(card.getName())) {
            isTank = true;
        }
        if (playerId == PLAYER_ONE_ID) {
            for (Card var : board.get(BACK_ROW_2)) {
                if (Card.isTank(var.getName())) {
                    areTanks = true;
                    break;
                }
            }
            for (Card var : board.get(FRONT_ROW_2)) {
                if (Card.isTank(var.getName())) {
                    areTanks = true;
                    break;
                }
            }
        } else {
            for (Card var : board.get(FRONT_ROW_1)) {
                if (Card.isTank(var.getName())) {
                    areTanks = true;
                    break;
                }
            }
            for (Card var : board.get(BACK_ROW_1)) {
                if (Card.isTank(var.getName())) {
                    areTanks = true;
                    break;
                }
            }
        }
        if (isTank) {
            return true;
        }
        return !areTanks;
    }

    /**
     * @param activePlayerId active player
     * @param row            affected row
     * @return The method checks if the attack is directed towards an enemy.
     */
    public static boolean checkAttack(final int activePlayerId, final int row) {
        if (activePlayerId == PLAYER_ONE_ID && (row == BACK_ROW_2 || row == FRONT_ROW_2)) {
            return true;
        }
        return activePlayerId != PLAYER_ONE_ID && (row == FRONT_ROW_1 || row == BACK_ROW_1);
    }

    /**
     * @return Returns the mana the player has.
     */
    public int getMana() {
        return mana;
    }

    /**
     * @param mana Sets the mana to a given value.
     */
    public void setMana(final int mana) {
        this.mana = mana;
    }

    /**
     * @param mana Subtracts a given amount of mana from the player's pool;
     */
    public void useMana(final int mana) {
        this.mana -= mana;
    }

    /**
     * Adds mana to the pool/
     */
    public void growMana(final int round) {
        if (round < MAX_MANA) {
            this.mana += round;
        } else {
            this.mana += MAX_MANA;
        }
    }

    /**
     * Draws a card from the deck, adding it to the hand and removing it from the deck.
     */
    public void drawCard() {
        if (decks.get(indexDeck).size() > ZERO) {
            hand.add(decks.get(indexDeck).get(ZERO));
            decks.get(indexDeck).remove(ZERO);
        }
    }

    /**
     * @return Returns the hand of the player.
     */
    public ArrayList<Card> getHand() {
        return hand;
    }

    public Player(final DecksInput deckInput, final int indexDeck, final Hero hero) {
        decks = new ArrayList<>();
        for (int i = 0; i < deckInput.getDecks().size(); i++) {
            decks.add(new ArrayList<>());
            for (int j = 0; j < deckInput.getDecks().get(i).size(); j++) {
                if (Card.checkMinion(deckInput.getDecks().get(i).get(j).getName())) {
                    decks.get(i).add(new Minion(deckInput.getDecks().get(i).get(j)));
                } else if (Card.checkEnvironment(deckInput.getDecks().get(i).get(j).getName())) {
                    decks.get(i).add(new Environment(deckInput.getDecks().get(i).get(j)));
                }
            }
        }
        this.hand = new ArrayList<>();
        this.nrCardsInDeck = deckInput.getNrCardsInDeck();
        this.indexDeck = indexDeck;
        this.nrDecks = deckInput.getNrDecks();
        this.hero = hero;
        this.mana = STARTING_MANA;
    }

    /**
     * @return Returns the decks of the player.
     */
    public ArrayList<ArrayList<Card>> getDecks() {
        return decks;
    }

    /**
     * @param decks the decks of the player
     *              Sets the decks of the player.
     */
    public void setDecks(final ArrayList<ArrayList<Card>> decks) {
        this.decks = decks;
    }

    /**
     * @return Returns the nr of cards in deck.
     */
    public int getNrCardsInDeck() {
        return nrCardsInDeck;
    }

    /**
     * @param nrCardsInDeck Set nr of cards in deck.
     */
    public void setNrCardsInDeck(final int nrCardsInDeck) {
        this.nrCardsInDeck = nrCardsInDeck;
    }

    /**
     * @return Returns the nr of decks.
     */
    public int getNrDecks() {
        return nrDecks;
    }

    /**
     * @param nrDecks Sets the nr of decks.
     */
    public void setNrDecks(final int nrDecks) {
        this.nrDecks = nrDecks;
    }

    /**
     * @return Returns the index of the deck used.
     */
    public int getIndexDeck() {
        return indexDeck;
    }

    /**
     * @return Returns the hero of the player.
     */
    public Hero getHero() {
        return hero;
    }

    /**
     * @param hero Sets the hero of the player.
     */
    public void setHero(final Hero hero) {
        this.hero = hero;
    }
}
