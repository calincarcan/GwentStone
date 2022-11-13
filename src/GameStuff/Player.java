package GameStuff;

import Cards.Card;
import Cards.Environment;
import Cards.Hero;
import Cards.Minion;
import fileio.Coordinates;
import fileio.DecksInput;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Player {
    private int mana;
    private ArrayList<ArrayList<Card>> decks;
    private ArrayList<Card> hand;
    private int nrCardsInDeck;
    private int nrDecks;
    private int indexDeck;
    private Hero hero;
    public static boolean checkTanksHero(int playerId, ArrayList<ArrayList<Card>> board) {
        boolean areTanks = false;
        if (playerId == 1) {
            for (Card var : board.get(0))
                if (Card.isTank(var.getName())) {
                    areTanks = true;
                    break;
                }
            for (Card var : board.get(1))
                if (Card.isTank(var.getName())) {
                    areTanks = true;
                    break;
                }
        } else {
            for (Card var : board.get(2))
                if (Card.isTank(var.getName())) {
                    areTanks = true;
                    break;
                }
            for (Card var : board.get(3))
                if (Card.isTank(var.getName())) {
                    areTanks = true;
                    break;
                }
        }
        return !areTanks;
    }
    public static boolean checkTanks(int playerId, ArrayList<ArrayList<Card>> board, Coordinates attacked) {
        boolean isTank = false;
        boolean areTanks = false;
        Card card = board.get(attacked.getX()).get(attacked.getY());
        if (Card.isTank(card.getName()))
            isTank = true;
        if (playerId == 1) {
            for (Card var : board.get(0))
                if (Card.isTank(var.getName())) {
                    areTanks = true;
                    break;
                }
            for (Card var : board.get(1))
                if (Card.isTank(var.getName())) {
                    areTanks = true;
                    break;
                }
        } else {
            for (Card var : board.get(2))
                if (Card.isTank(var.getName())) {
                    areTanks = true;
                    break;
                }
            for (Card var : board.get(3))
                if (Card.isTank(var.getName())) {
                    areTanks = true;
                    break;
                }
        }
        if (isTank)
            return true;
        return !areTanks;
    }
    public static boolean checkAttack(int activePlayerId, int row) {
        if (activePlayerId == 1 && (row == 0 || row == 1))
            return true;
        return activePlayerId == 2 && (row == 2 || row == 3);
    }
    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }
    public void useMana(int mana) {
        this.mana -= mana;
    }
    public void growMana(int round) {
        if (round < 10)
            this.mana += round;
        else
            this.mana += 10;
    }
    public void drawCard() {
        if (decks.get(indexDeck).size() > 0) {
            hand.add(decks.get(indexDeck).get(0));
            decks.get(indexDeck).remove(0);
        }
    }
    public ArrayList<Card> getHand() {
        return hand;
    }
    public void removeFromHand(Card card) {
        getHand().remove(card);
    }

    public Player(DecksInput deckInput, int indexDeck, Hero hero) {
        decks = new ArrayList<>();
        for (int i = 0; i < deckInput.getDecks().size(); i++) {
            decks.add(new ArrayList<>());
            for (int j = 0; j < deckInput.getDecks().get(i).size(); j++) {
                if (Card.checkMinion(deckInput.getDecks().get(i).get(j).getName())) {
                    decks.get(i).add(new Minion(deckInput.getDecks().get(i).get(j)));
                }
                else if (Card.checkEnvironment(deckInput.getDecks().get(i).get(j).getName())) {
                    decks.get(i).add(new Environment(deckInput.getDecks().get(i).get(j)));
                }
            }
        }
        this.hand = new ArrayList<>();
        this.nrCardsInDeck = deckInput.getNrCardsInDeck();
        this.indexDeck = indexDeck;
        this.nrDecks = deckInput.getNrDecks();
        this.hero = hero;
        this.mana = 1;
    }

    public ArrayList<ArrayList<Card>> getDecks() {
        return decks;
    }

    public void setDecks(ArrayList<ArrayList<Card>> decks) {
        this.decks = decks;
    }

    public int getNrCardsInDeck() {
        return nrCardsInDeck;
    }

    public void setNrCardsInDeck(int nrCardsInDeck) {
        this.nrCardsInDeck = nrCardsInDeck;
    }

    public int getNrDecks() {
        return nrDecks;
    }

    public void setNrDecks(int nrDecks) {
        this.nrDecks = nrDecks;
    }

    public int getIndexDeck() {
        return indexDeck;
    }

    public void setIndexDeck(int indexDeck) {
        this.indexDeck = indexDeck;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }
}
