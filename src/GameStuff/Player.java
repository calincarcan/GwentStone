package GameStuff;

import Cards.Card;
import Cards.Environment;
import Cards.Hero;
import Cards.Minion;
import fileio.DecksInput;

import java.util.ArrayList;

public class Player {
    private ArrayList<ArrayList<Card>> decks;
    private static ArrayList<Card> hand = null;
    private int nrCardsInDeck;
    private int nrDecks;
    private int indexDeck;
    private Hero hero;
    private boolean order;

    public void drawCard() {
        getHand().add(decks.get(indexDeck).get(0));
        decks.get(indexDeck).remove(0);
    }
    public ArrayList<Card> getHand() {
        if (hand == null)
            hand = new ArrayList<>();
        return hand;
    }
    public void removeFromHand(Card card) {
        getHand().remove(card);
    }

    public static boolean checkEnvironment(String name) {
        return name.equals("Heart Hound") || name.equals("Winterfell") || name.equals("Firestorm");
    }
    public static boolean checkMinion(String name) {
        return  name.equals("Sentinel") || name.equals("Berserker") || name.equals("Goliath") ||
                name.equals("Warden") || name.equals("The Ripper") || name.equals("Miraj") ||
                name.equals("The Cursed One") || name.equals("Disciple");
    }
    public Player(DecksInput deckInput, int indexDeck, Hero hero, boolean order) {
        decks = new ArrayList<>();
        for (int i = 0; i < deckInput.getDecks().size(); i++) {
            decks.add(new ArrayList<>());
            for (int j = 0; j < deckInput.getDecks().get(i).size(); j++) {
                if (checkMinion(deckInput.getDecks().get(i).get(j).getName())) {
                    decks.get(i).add(new Minion(deckInput.getDecks().get(i).get(j)));
                }
                else if (checkEnvironment(deckInput.getDecks().get(i).get(j).getName())) {
                    decks.get(i).add(new Environment(deckInput.getDecks().get(i).get(j)));
                }
            }
        }
//        this.decks = deckInput.getDecks();
        this.nrCardsInDeck = deckInput.getNrCardsInDeck();
        this.indexDeck = indexDeck;
        this.nrDecks = deckInput.getNrDecks();
        this.hero = hero;
        this.order = order;
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
