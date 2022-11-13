package main;

import Cards.*;
import GameStuff.Player;
import checker.Checker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import checker.CheckerConstants;
import fileio.ActionsInput;
import fileio.CardInput;
import fileio.DecksInput;
import fileio.Input;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import static java.util.Collections.*;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }

        Checker.calculateScore();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Input inputData = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH + filePath1),
                Input.class);

        ArrayNode output = objectMapper.createArrayNode();
        // TODO: add here the entry point to your implementation
        Hero hero1 = new Hero(inputData.getGames().get(0).getStartGame().getPlayerOneHero());
        Hero hero2 = new Hero(inputData.getGames().get(0).getStartGame().getPlayerTwoHero());
        int index1 = inputData.getGames().get(0).getStartGame().getPlayerOneDeckIdx();
        int index2 = inputData.getGames().get(0).getStartGame().getPlayerTwoDeckIdx();
        DecksInput decks1 = inputData.getPlayerOneDecks();
        DecksInput decks2 = inputData.getPlayerTwoDecks();
        boolean order = inputData.getGames().get(0).getStartGame().getStartingPlayer() == 1;

        ArrayList<ArrayList<Card>> board = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            board.add(new ArrayList<>());
        Player player1 = new Player(decks1, index1, hero1);
        Player player2 = new Player(decks2, index2, hero2);
        shuffle(player1.getDecks().get(index1),
                new Random(inputData.getGames().get(0).getStartGame().getShuffleSeed()));
        shuffle(player2.getDecks().get(index2),
                new Random(inputData.getGames().get(0).getStartGame().getShuffleSeed()));

        player1.drawCard();
        player2.drawCard();

        int round = 2;
        int turn = 1;
        int activePlayerId = inputData.getGames().get(0).getStartGame().getStartingPlayer();
        Player activePlayer = activePlayerId == 1 ? player1:player2;
        for (ActionsInput action : inputData.getGames().get(0).getActions()) {
            Player printPlayer = action.getPlayerIdx() == 1 ? player1:player2;
            int printPlayerId = action.getPlayerIdx() == 1 ? 1:2;
            String command = action.getCommand();
            switch (command) {
                case "getPlayerDeck": {
                    output.addObject().put("command", command).put("playerIdx", printPlayerId)
                            .putPOJO("output", printPlayer.getDecks().get(printPlayer.getIndexDeck()));
                    break;
                }
                case "getPlayerHero": {
                    output.addObject().put("command", command).put("playerIdx", printPlayerId)
                            .putPOJO("output", printPlayer.getHero());
                    break;
                }
                case "getPlayerTurn": {
                    output.addObject().put("command", command).put("output", activePlayerId);
                    break;
                }
                case "endPlayerTurn": {
                    activePlayerId = activePlayerId == 1 ? 2:1;
                    if (activePlayerId == 1) {
                        for (int i = 0; i < board.get(2).size(); i++) {
                            if (board.get(2).get(i).getFrozenCount() == 1) {
                                board.get(2).get(i).unfreeze();
                                board.get(2).get(i).setFrozenCount(0);
                            }
                            else
                                board.get(2).get(i).setFrozenCount(1);
                            board.get(2).get(i).setUsed(false);
                        }
                        for (int i = 0; i < board.get(3).size(); i++) {
                            if (board.get(3).get(i).getFrozenCount() == 1) {
                                board.get(3).get(i).unfreeze();
                                board.get(3).get(i).setFrozenCount(0);
                            }
                            else
                                board.get(3).get(i).setFrozenCount(1);
                            board.get(3).get(i).setUsed(false);
                        }
                    } else {
                        for (int i = 0; i < board.get(0).size(); i++) {
                            if (board.get(0).get(i).getFrozenCount() == 1) {
                                board.get(0).get(i).unfreeze();
                                board.get(0).get(i).setFrozenCount(0);
                            }
                            else
                                board.get(0).get(i).setFrozenCount(1);
                            board.get(0).get(i).setUsed(false);
                        }
                        for (int i = 0; i < board.get(1).size(); i++) {
                            if (board.get(1).get(i).getFrozenCount() == 1) {
                                board.get(1).get(i).unfreeze();
                                board.get(1).get(i).setFrozenCount(0);
                            }
                            else
                                board.get(1).get(i).setFrozenCount(1);
                            board.get(1).get(i).setUsed(false);
                        }
                    }
                    activePlayer = activePlayerId == 1 ? player1:player2;
                    if (turn == 1)
                        turn++;
                    else {
                        player1.growMana(round);
                        player2.growMana(round);
                        player1.drawCard();
                        player2.drawCard();
                        turn = 1;
                        round++;
                    }
                    break;
                }
                case "getCardsInHand": {
                    ArrayList<Card> copyHand = new ArrayList<Card>();
                        for (Card card : printPlayer.getHand()) {
                            if (Card.checkEnvironment(card.getName())) {
                                copyHand.add(new Environment(card.getMana(), card.getDescription(), card.getName(), card.getColors()));
                            }
                            if (Card.checkMinion(card.getName())) {
                                copyHand.add(new Minion(card.getMana(), ((Minion) card).getAttackDamage(), ((Minion) card).getHealth(), card.getDescription(), card.getColors(), card.getName()));
                            }
                        }
                        output.addObject().put("command", command).put("playerIdx", printPlayerId)
                                .putPOJO("output", copyHand);
                    break;
                }
                case "placeCard": {
                    if (activePlayerId == 1) {
                        String name = player1.getHand().get(action.getHandIdx()).getName();
                        if (Card.checkEnvironment(name)) {
                            output.addObject().put("command", command).put("handIdx", action.getHandIdx()).
                                    put("error", "Cannot place environment card on table.");
                            break;
                        }
                        if (player1.getMana() < player1.getHand().get(action.getHandIdx()).getMana()) {
                            output.addObject().put("command", command).put("handIdx", action.getHandIdx()).
                                    put("error", "Not enough mana to place card on table.");
                            break;
                        }
                        if (Card.frontMinion(name)) {
                            if (board.get(2).size() == 5) {
                                output.addObject().put("command", command).put("handIdx", action.getHandIdx()).
                                        put("error", "Cannot place card on table since row is full.");
                                break;
                            }
                            board.get(2).add(player1.getHand().get(action.getHandIdx()));
                            player1.useMana(player1.getHand().get(action.getHandIdx()).getMana());
                            player1.getHand().remove(action.getHandIdx());
                        }
                        else if (Card.backMinion(name)) {
                            if (board.get(3).size() == 5) {
                                output.addObject().put("command", command).put("handIdx", action.getHandIdx()).
                                        put("error", "Cannot place card on table since row is full.");
                                break;
                            }
                            board.get(3).add(player1.getHand().get(action.getHandIdx()));
                            player1.useMana(player1.getHand().get(action.getHandIdx()).getMana());
                            player1.getHand().remove(action.getHandIdx());
                        }

                    }
                    else {
                        String name = player2.getHand().get(action.getHandIdx()).getName();
                        if (Card.checkEnvironment(name)) {
                            output.addObject().put("command", command).put("handIdx", action.getHandIdx()).
                                    put("error", "Cannot place environment card on table.");
                            break;
                        }
                        if (player2.getMana() < player2.getHand().get(action.getHandIdx()).getMana()) {
                            output.addObject().put("command", command).put("handIdx", action.getHandIdx()).
                                    put("error", "Not enough mana to place card on table.");
                            break;
                        }
                        if (Card.frontMinion(name)) {
                            if (board.get(1).size() == 5) {
                                output.addObject().put("command", command).put("handIdx", action.getHandIdx()).
                                        put("error", "Cannot place card on table since row is full.");
                                break;
                            }
                            board.get(1).add(player2.getHand().get(action.getHandIdx()));
                            player2.useMana(player2.getHand().get(action.getHandIdx()).getMana());
                            player2.getHand().remove(action.getHandIdx());
                        }
                        if (Card.backMinion(name)) {
                            if (board.get(0).size() == 5) {
                                output.addObject().put("command", command).put("handIdx", action.getHandIdx()).
                                        put("error", "Cannot place card on table since row is full.");
                                break;
                            }
                            board.get(0).add(player2.getHand().get(action.getHandIdx()));
                            player2.useMana(player2.getHand().get(action.getHandIdx()).getMana());
                            player2.getHand().remove(action.getHandIdx());
                        }
                    }
                    break;
                }
                case "getPlayerMana": {
                    output.addObject().put("command", command).put("playerIdx", printPlayerId).
                            put("output", printPlayer.getMana());
                    break;
                }
                case "getCardsOnTable": {
                    output.addObject().put("command", command).putPOJO("output", board);
                    break;
                }
                case "getEnvironmentCardsInHand": {
                    ArrayList<Card> copyEnvironments = new ArrayList<Card>();
                    for (Card card : printPlayer.getHand()) {
                        if (Card.checkEnvironment(card.getName())) {
                            copyEnvironments.add(new Environment(card.getMana(), card.getDescription(), card.getName(), card.getColors()));
                        }
                    }
                    output.addObject().put("command", action.getCommand()).put("playerIdx", printPlayerId)
                            .putPOJO("output", copyEnvironments);
                    break;
                }
                case "useEnvironmentCard": {
                    if (!Card.checkEnvironment(activePlayer.getHand().get(action.getHandIdx()).getName())) {
                        output.addObject().put("command", command).put("handIdx", action.getHandIdx()).
                                put("affectedRow", action.getAffectedRow()).put("error", "Chosen card is not of type environment.");
                        break;
                    }
                    if (activePlayer.getMana() < activePlayer.getHand().get(action.getHandIdx()).getMana()) {
                        output.addObject().put("command", command).put("handIdx", action.getHandIdx()).
                                put("affectedRow", action.getAffectedRow()).put("error", "Not enough mana to use environment card.");
                        break;
                    }
                    if ((action.getAffectedRow() == 2 || action.getAffectedRow() == 3) && activePlayerId == 1) {
                        output.addObject().put("command", command).put("handIdx", action.getHandIdx()).
                                put("affectedRow", action.getAffectedRow()).put("error", "Chosen row does not belong to the enemy.");
                        break;
                    }
                    if ((action.getAffectedRow() == 0 || action.getAffectedRow() == 1) && activePlayerId == 2) {
                        output.addObject().put("command", command).put("handIdx", action.getHandIdx()).
                                put("affectedRow", action.getAffectedRow()).put("error", "Chosen row does not belong to the enemy.");
                        break;
                    }
                    if (activePlayer.getHand().get(action.getHandIdx()).getName().equals("Heart Hound") &&
                            ((action.getAffectedRow() == 3 && board.get(0).size() == 5) ||
                             (action.getAffectedRow() == 2 && board.get(1).size() == 5) ||
                             (action.getAffectedRow() == 1 && board.get(2).size() == 5) ||
                             (action.getAffectedRow() == 0 && board.get(3).size() == 5))) {
                        output.addObject().put("command", command).put("handIdx", action.getHandIdx()).
                                put("affectedRow", action.getAffectedRow()).put("error", "Cannot steal enemy card since the player's row is full.");
                        break;
                    }
                    ((Environment) activePlayer.getHand().get(action.getHandIdx())).useAbility(action.getAffectedRow(), board);
                    activePlayer.useMana(activePlayer.getHand().get(action.getHandIdx()).getMana());
                    activePlayer.getHand().remove(action.getHandIdx());
                    break;
                }
                case "getCardAtPosition": {
                    Card copyMinion = new Minion(
                            board.get(action.getX()).get(action.getY()).getMana(),
                            ((Minion)board.get(action.getX()).get(action.getY())).getAttackDamage(),
                            ((Minion)board.get(action.getX()).get(action.getY())).getHealth(),
                            board.get(action.getX()).get(action.getY()).getDescription(),
                            board.get(action.getX()).get(action.getY()).getColors(),
                            board.get(action.getX()).get(action.getY()).getName());
                    output.addObject().put("command", command).putPOJO("output", copyMinion);
                    break;
                }
                case "getFrozenCardsOnTable": {
                    ArrayList<Card> frozenCards = new ArrayList<Card>();
                    for (int i = 0; i < 4; i++)
                        for (int j = 0; j < board.get(i).size(); j++)
                            if (board.get(i).get(j).getFrozen())
                                frozenCards.add(new Minion(
                                        board.get(i).get(j).getMana(),
                                        ((Minion)board.get(i).get(j)).getAttackDamage(),
                                        ((Minion)board.get(i).get(j)).getHealth(),
                                        board.get(i).get(j).getDescription(),
                                        board.get(i).get(j).getColors(),
                                        board.get(i).get(j).getName()));
                    output.addObject().put("command", command).putPOJO("output", frozenCards);
                    break;
                }
                case "cardUsesAttack": {
                    int attackedX = action.getCardAttacked().getX();
                    int attackerX = action.getCardAttacker().getX();
                    int attackedY = action.getCardAttacked().getY();
                    int attackerY = action.getCardAttacker().getY();
                    Card attacker = board.get(attackerX).get(attackerY);
                    Card attacked = board.get(attackedX).get(attackedY);
                    if (attacker.getFrozen()) {
                        output.addObject().put("command", command).putPOJO("cardAttacker", action.getCardAttacker()).
                                putPOJO("cardAttacked", action.getCardAttacked()).put("error", "Attacker card is frozen.");
                        break;
                    }
                    if (attacker.isUsed()) {
                        output.addObject().put("command", command).putPOJO("cardAttacker", action.getCardAttacker()).
                                putPOJO("cardAttacked", action.getCardAttacked()).put("error", "Attacker card has already attacked this turn.");
                        break;
                    }
                    if (!Player.checkAttack(activePlayerId, attackedX)) {
                        output.addObject().put("command", command).putPOJO("cardAttacker", action.getCardAttacker()).
                                putPOJO("cardAttacked", action.getCardAttacked()).put("error", "Attacked card does not belong to the enemy.");
                        break;
                    }
                    if (!Player.checkTanks(activePlayerId, board, action.getCardAttacked())) {
                        output.addObject().put("command", command).putPOJO("cardAttacker", action.getCardAttacker()).
                                putPOJO("cardAttacked", action.getCardAttacked()).put("error", "Attacked card is not of type 'Tank'.");
                        break;
                    }
                    ((Minion)attacked).setHealth(((Minion)attacked).getHealth() - ((Minion)attacker).getAttackDamage());
                    if (((Minion)attacked).getHealth() < 1)
                        board.get(attackedX).remove(attacked);
                    attacker.setUsed(true);
                    break;
                }
                default:
                    break;
            }
        }
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }
}
