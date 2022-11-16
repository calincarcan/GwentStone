package main;

import cards.Card;
import cards.Environment;
import cards.Hero;
import cards.Minion;
import player.Player;
import checker.Checker;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import checker.CheckerConstants;
import fileio.ActionsInput;
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

import static java.util.Collections.shuffle;

/**
 * The entry point to this homework. It runs the checker that tests your implementation.
 */
public final class Main {
    static final int START_ROUND = 1;
    static final int START_TURN = 1;
    static final int ZERO = 0;
    static final int PLAYER_ONE_ID = 1;
    static final int PLAYER_TWO_ID = 2;
    static final int FRONT_ROW1 = 2;
    static final int FRONT_ROW2 = 1;
    static final int BACK_ROW1 = 3;
    static final int BACK_ROW2 = 0;
    static final int MAX_ROW_SIZE = 5;

    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     *
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
        Input inputData = objectMapper
                .readValue(new File(CheckerConstants.TESTS_PATH + filePath1),
                        Input.class);

        ArrayNode output = objectMapper.createArrayNode();

        int gamesOneWon = ZERO;
        int gamesTwoWon = ZERO;
        for (int var = ZERO; var < inputData.getGames().size(); var++) {
            Hero hero1 = new Hero(inputData.getGames().get(var).getStartGame().getPlayerOneHero());
            Hero hero2 = new Hero(inputData.getGames().get(var).getStartGame().getPlayerTwoHero());
            int index1 = inputData.getGames().get(var).getStartGame().getPlayerOneDeckIdx();
            int index2 = inputData.getGames().get(var).getStartGame().getPlayerTwoDeckIdx();
            DecksInput decks1 = inputData.getPlayerOneDecks();
            DecksInput decks2 = inputData.getPlayerTwoDecks();

            ArrayList<ArrayList<Card>> board = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                board.add(new ArrayList<>());
            }

            Player player1 = new Player(decks1, index1, hero1);
            Player player2 = new Player(decks2, index2, hero2);
            shuffle(player1.getDecks().get(index1),
                    new Random(inputData.getGames().get(var).getStartGame().getShuffleSeed()));
            shuffle(player2.getDecks().get(index2),
                    new Random(inputData.getGames().get(var).getStartGame().getShuffleSeed()));
            player1.drawCard();
            player2.drawCard();

            int round = START_ROUND;
            int turn = START_TURN;
            int activePlayerId = inputData.getGames().get(var).getStartGame().getStartingPlayer();
            Player activePlayer = activePlayerId == PLAYER_ONE_ID ? player1 : player2;
            for (ActionsInput action : inputData.getGames().get(var).getActions()) {
                Player printPlayer = action.getPlayerIdx() == PLAYER_ONE_ID ? player1 : player2;
                int printPlayerId =
                        action.getPlayerIdx() == PLAYER_ONE_ID ? PLAYER_ONE_ID : PLAYER_TWO_ID;
                String command = action.getCommand();
                switch (command) {
                    case "getPlayerDeck" -> {
                        output.addObject().put("command", command)
                                .put("playerIdx", printPlayerId)
                                .putPOJO("output", printPlayer.getDecks()
                                        .get(printPlayer.getIndexDeck()));
                    }
                    case "getPlayerHero" -> {
                        Hero hero = printPlayer.getHero();
                        Hero copyHero = new Hero(hero.getMana(), hero.getDescription()
                                , hero.getColors(), hero.getName(), hero.getHealth());
                        output.addObject().put("command", command)
                                .put("playerIdx", printPlayerId)
                                .putPOJO("output", copyHero);
                    }
                    case "getPlayerTurn" -> {
                        output.addObject().put("command", command)
                                .put("output", activePlayerId);
                    }
                    case "endPlayerTurn" -> {
                        if (activePlayerId == PLAYER_ONE_ID) {
                            for (int i = ZERO; i < board.get(FRONT_ROW1).size(); i++) {
                                board.get(FRONT_ROW1).get(i).unfreeze();
                                board.get(FRONT_ROW1).get(i).unUse();
                            }
                            for (int i = ZERO; i < board.get(BACK_ROW1).size(); i++) {
                                board.get(BACK_ROW1).get(i).unfreeze();
                                board.get(BACK_ROW1).get(i).unUse();
                            }
                        } else if (activePlayerId == PLAYER_TWO_ID) {
                            for (int i = ZERO; i < board.get(BACK_ROW2).size(); i++) {
                                board.get(BACK_ROW2).get(i).unfreeze();
                                board.get(BACK_ROW2).get(i).unUse();
                            }
                            for (int i = ZERO; i < board.get(FRONT_ROW2).size(); i++) {
                                board.get(FRONT_ROW2).get(i).unfreeze();
                                board.get(FRONT_ROW2).get(i).unUse();
                            }
                        }
                        activePlayer.getHero().unUse();
                        activePlayer = activePlayerId == PLAYER_ONE_ID ? player2 : player1;
                        activePlayerId =
                                activePlayerId == PLAYER_ONE_ID ? PLAYER_TWO_ID : PLAYER_ONE_ID;
                        if (turn == START_TURN)
                            turn++;
                        else {
                            turn = START_TURN;
                            round++;
                            player1.growMana(round);
                            player2.growMana(round);
                            player1.drawCard();
                            player2.drawCard();
                        }
                    }
                    case "getCardsInHand" -> {
                        ArrayList<Card> copyHand = new ArrayList<>();
                        for (Card card : printPlayer.getHand()) {
                            if (Card.checkEnvironment(card.getName())) {
                                copyHand.add(new Environment(card.getMana()
                                        , card.getDescription(), card.getName(), card.getColors()));
                            }
                            if (Card.checkMinion(card.getName())) {
                                copyHand.add(new Minion(card.getMana()
                                        , ((Minion) card).getAttackDamage()
                                        , ((Minion) card).getHealth()
                                        , card.getDescription(), card.getColors(), card.getName()));
                            }
                        }
                        output.addObject().put("command", command)
                                .put("playerIdx", printPlayerId)
                                .putPOJO("output", copyHand);
                    }
                    case "placeCard" -> {
                        if (activePlayerId == PLAYER_ONE_ID) {
                            String name = player1.getHand().get(action.getHandIdx()).getName();
                            if (Card.checkEnvironment(name)) {
                                final String out = "Cannot place environment card on table.";
                                output.addObject().put("command", command)
                                        .put("handIdx", action.getHandIdx()).
                                        put("error", out);
                                break;
                            }
                            if (player1.getMana()
                                    < player1.getHand().get(action.getHandIdx()).getMana()) {
                                String out = "Not enough mana to place card on table.";
                                output.addObject().put("command", command)
                                        .put("handIdx", action.getHandIdx()).
                                        put("error", out);
                                break;
                            }
                            if (Card.frontMinion(name)) {
                                if (board.get(FRONT_ROW1).size() == MAX_ROW_SIZE) {
                                    String out = "Cannot place card on table since row is full.";
                                    output.addObject().put("command", command)
                                            .put("handIdx", action.getHandIdx()).
                                            put("error", out);
                                    break;
                                }
                                board.get(FRONT_ROW1).add(player1.getHand()
                                        .get(action.getHandIdx()));
                                player1.useMana(player1.getHand()
                                        .get(action.getHandIdx()).getMana());
                                player1.getHand().remove(action.getHandIdx());
                            } else if (Card.backMinion(name)) {
                                if (board.get(BACK_ROW1).size() == MAX_ROW_SIZE) {
                                    String out = "Cannot place card on table since row is full.";
                                    output.addObject().put("command", command)
                                            .put("handIdx", action.getHandIdx()).
                                            put("error", out);
                                    break;
                                }
                                board.get(BACK_ROW1).add(player1.getHand()
                                        .get(action.getHandIdx()));
                                player1.useMana(player1.getHand()
                                        .get(action.getHandIdx()).getMana());
                                player1.getHand().remove(action.getHandIdx());
                            }

                        } else {
                            String name = player2.getHand().get(action.getHandIdx()).getName();
                            if (Card.checkEnvironment(name)) {
                                String out = "Cannot place environment card on table.";
                                output.addObject().put("command", command)
                                        .put("handIdx", action.getHandIdx())
                                        .put("error", out);
                                break;
                            }
                            if (player2.getMana() < player2.getHand()
                                    .get(action.getHandIdx()).getMana()) {
                                String out = "Not enough mana to place card on table.";
                                output.addObject().put("command", command)
                                        .put("handIdx", action.getHandIdx())
                                        .put("error", out);
                                break;
                            }
                            if (Card.frontMinion(name)) {
                                if (board.get(FRONT_ROW2).size() == MAX_ROW_SIZE) {
                                    String out = "Cannot place card on table since row is full.";
                                    output.addObject().put("command", command)
                                            .put("handIdx", action.getHandIdx())
                                            .put("error", out);
                                    break;
                                }
                                board.get(FRONT_ROW2).add(player2.getHand()
                                        .get(action.getHandIdx()));
                                player2.useMana(player2.getHand().get(action.getHandIdx())
                                        .getMana());
                                player2.getHand().remove(action.getHandIdx());
                            }
                            if (Card.backMinion(name)) {
                                if (board.get(BACK_ROW2).size() == MAX_ROW_SIZE) {
                                    String out = "Cannot place card on table since row is full.";
                                    output.addObject().put("command", command)
                                            .put("handIdx", action.getHandIdx())
                                            .put("error", out);
                                    break;
                                }
                                board.get(BACK_ROW2).add(player2.getHand()
                                        .get(action.getHandIdx()));
                                player2.useMana(player2.getHand()
                                        .get(action.getHandIdx()).getMana());
                                player2.getHand().remove(action.getHandIdx());
                            }
                        }
                    }
                    case "getPlayerMana" -> {
                        output.addObject().put("command", command)
                                .put("playerIdx", printPlayerId)
                                .put("output", printPlayer.getMana());
                    }
                    case "getCardsOnTable" -> {
                        output.addObject().put("command", command)
                                .putPOJO("output", board);
                    }
                    case "getEnvironmentCardsInHand" -> {
                        ArrayList<Card> copyEnvironments = new ArrayList<>();
                        for (Card card : printPlayer.getHand()) {
                            if (Card.checkEnvironment(card.getName())) {
                                copyEnvironments.add(new Environment(card.getMana(),
                                        card.getDescription(), card.getName(), card.getColors()));
                            }
                        }
                        output.addObject().put("command", action.getCommand())
                                .put("playerIdx", printPlayerId)
                                .putPOJO("output", copyEnvironments);
                    }
                    case "useEnvironmentCard" -> {
                        if (!Card.checkEnvironment(activePlayer.getHand().get(action.getHandIdx())
                                .getName())) {
                            String out = "Chosen card is not of type environment.";
                            output.addObject().put("command", command)
                                    .put("handIdx", action.getHandIdx())
                                    .put("affectedRow", action.getAffectedRow())
                                    .put("error", out);
                            break;
                        }
                        if (activePlayer.getMana() < activePlayer.getHand()
                                .get(action.getHandIdx()).getMana()) {
                            String out = "Not enough mana to use environment card.";
                            output.addObject().put("command", command)
                                    .put("handIdx", action.getHandIdx())
                                    .put("affectedRow", action.getAffectedRow())
                                    .put("error", out);
                            break;
                        }
                        if ((action.getAffectedRow() == FRONT_ROW1
                                || action.getAffectedRow() == BACK_ROW1)
                                && activePlayerId == PLAYER_ONE_ID) {
                            String out = "Chosen row does not belong to the enemy.";
                            output.addObject().put("command", command)
                                    .put("handIdx", action.getHandIdx())
                                    .put("affectedRow", action.getAffectedRow())
                                    .put("error", out);
                            break;
                        }
                        if ((action.getAffectedRow() == BACK_ROW2
                                || action.getAffectedRow() == FRONT_ROW2)
                                && activePlayerId == PLAYER_TWO_ID) {
                            String out = "Chosen row does not belong to the enemy.";
                            output.addObject().put("command", command)
                                    .put("handIdx", action.getHandIdx())
                                    .put("affectedRow", action.getAffectedRow())
                                    .put("error", out);
                            break;
                        }
                        if (activePlayer.getHand().get(action.getHandIdx()).getName()
                                .equals("Heart Hound")
                                && ((action.getAffectedRow() == 3 && board.get(0).size() == 5)
                                || (action.getAffectedRow() == 2 && board.get(1).size() == 5)
                                || (action.getAffectedRow() == 1 && board.get(2).size() == 5)
                                || (action.getAffectedRow() == 0 && board.get(3).size() == 5))) {
                            String out = "Cannot steal enemy card since the player's row is full.";
                            output.addObject().put("command", command)
                                    .put("handIdx", action.getHandIdx())
                                    .put("affectedRow", action.getAffectedRow())
                                    .put("error", out);
                            break;
                        }
                        ((Environment) activePlayer.getHand().get(action.getHandIdx()))
                                .useAbility(action.getAffectedRow(), board);
                        activePlayer.useMana(activePlayer.getHand()
                                .get(action.getHandIdx()).getMana());
                        activePlayer.getHand().remove(action.getHandIdx());
                    }
                    case "getCardAtPosition" -> {
                        String out = "No card available at that position.";
                        if (board.get(action.getX()).size() <= action.getY()) {
                            output.addObject().put("command", command)
                                    .put("x", action.getX())
                                    .put("y", action.getY())
                                    .put("output", out);
                            break;
                        }
                        Minion card = ((Minion) board.get(action.getX()).get(action.getY()));
                        Card copyMinion = new Minion(card.getMana(), card.getAttackDamage(),
                                card.getHealth(), card.getDescription(), card.getColors(),
                                card.getName());
                        output.addObject().put("command", command)
                                .put("x", action.getX()).put("y", action.getY())
                                .putPOJO("output", copyMinion);
                    }
                    case "getFrozenCardsOnTable" -> {
                        ArrayList<Card> frozenCards = new ArrayList<>();
                        for (int i = 0; i < 4; i++)
                            for (int j = 0; j < board.get(i).size(); j++)
                                if (board.get(i).get(j).getFrozen()) {
                                    frozenCards.add(new Minion(
                                            board.get(i).get(j).getMana(),
                                            ((Minion) board.get(i).get(j)).getAttackDamage(),
                                            ((Minion) board.get(i).get(j)).getHealth(),
                                            board.get(i).get(j).getDescription(),
                                            board.get(i).get(j).getColors(),
                                            board.get(i).get(j).getName()));
                                }
                        output.addObject().put("command", command)
                                .putPOJO("output", frozenCards);
                    }
                    case "cardUsesAttack" -> {
                        int attackedX = action.getCardAttacked().getX();
                        int attackerX = action.getCardAttacker().getX();
                        int attackedY = action.getCardAttacked().getY();
                        int attackerY = action.getCardAttacker().getY();
                        Minion attacker = (Minion) board.get(attackerX).get(attackerY);
                        Minion attacked = (Minion) board.get(attackedX).get(attackedY);
                        if (!Player.checkAttack(activePlayerId, attackedX)) {
                            String out = "Attacked card does not belong to the enemy.";
                            output.addObject().put("command", command)
                                    .putPOJO("cardAttacker", action.getCardAttacker())
                                    .putPOJO("cardAttacked", action.getCardAttacked())
                                    .put("error", out);
                            break;
                        }
                        if (attacker.isUsed()) {
                            String out = "Attacker card has already attacked this turn.";
                            output.addObject().put("command", command)
                                    .putPOJO("cardAttacker", action.getCardAttacker())
                                    .putPOJO("cardAttacked", action.getCardAttacked())
                                    .put("error", out);
                            break;
                        }
                        if (attacker.getFrozen()) {
                            output.addObject().put("command", command)
                                    .putPOJO("cardAttacker", action.getCardAttacker())
                                    .putPOJO("cardAttacked", action.getCardAttacked())
                                    .put("error", "Attacker card is frozen.");
                            break;
                        }
                        if (!Player.checkTanks(activePlayerId, board, action.getCardAttacked())) {
                            String out = "Attacked card is not of type 'Tank'.";
                            output.addObject().put("command", command)
                                    .putPOJO("cardAttacker", action.getCardAttacker())
                                    .putPOJO("cardAttacked", action.getCardAttacked())
                                    .put("error", out);
                            break;
                        }
                        attacked.setHealth(attacked.getHealth() - attacker.getAttackDamage());
                        if (attacked.getHealth() < 1)
                            board.get(attackedX).remove(attacked);
                        attacker.setUsed();
                    }
                    case "cardUsesAbility" -> {
                        int attackedX = action.getCardAttacked().getX();
                        int attackedY = action.getCardAttacked().getY();
                        int attackerX = action.getCardAttacker().getX();
                        int attackerY = action.getCardAttacker().getY();
                        Minion attacker = ((Minion) board.get(attackerX).get(attackerY));
                        Minion attacked = ((Minion) board.get(attackedX).get(attackedY));
                        if (attacker.getFrozen()) {
                            output.addObject().put("command", command)
                                    .putPOJO("cardAttacker", action.getCardAttacker())
                                    .putPOJO("cardAttacked", action.getCardAttacked())
                                    .put("error", "Attacker card is frozen.");
                            break;
                        }
                        if (attacker.isUsed()) {
                            String out = "Attacker card has already attacked this turn.";
                            output.addObject()
                                    .put("command", command)
                                    .putPOJO("cardAttacker", action.getCardAttacker())
                                    .putPOJO("cardAttacked", action.getCardAttacked())
                                    .put("error", out);
                            break;
                        }
                        if (attacker.getName().equals("Disciple")) {
                            if (!attacked.checkDisciple(activePlayerId, attackedX)) {
                                String out = "Attacked card does not belong to the current player.";
                                output.addObject().put("command", command)
                                        .putPOJO("cardAttacker", action.getCardAttacker())
                                        .putPOJO("cardAttacked", action.getCardAttacked())
                                        .put("error", out);
                                break;
                            }
                        }
                        if (attacker.getName().equals("Miraj")
                                || attacker.getName().equals("The Ripper")
                                || attacker.getName().equals("The Cursed One")) {
                            if (!Player.checkAttack(activePlayerId, attackedX)) {
                                String out = "Attacked card does not belong to the enemy.";
                                output.addObject().put("command", command)
                                        .putPOJO("cardAttacker", action.getCardAttacker())
                                        .putPOJO("cardAttacked", action.getCardAttacked())
                                        .put("error", out);
                                break;
                            }
                        }
                        if (!attacker.getName().equals("Disciple")) {
                            if (!Player.checkTanks(activePlayerId, board, action.getCardAttacked())) {
                                String out = "Attacked card is not of type 'Tank'.";
                                output.addObject().put("command", command)
                                        .putPOJO("cardAttacker", action.getCardAttacker())
                                        .putPOJO("cardAttacked", action.getCardAttacked())
                                        .put("error", out);
                                break;
                            }
                        }
                        attacker.useAbility(board, attackedX, attackedY);
                    }
                    case "useAttackHero" -> {
                        int attackerX = action.getCardAttacker().getX();
                        int attackerY = action.getCardAttacker().getY();
                        Minion attacker = ((Minion) board.get(attackerX).get(attackerY));
                        if (attacker.getFrozen()) {
                            output.addObject().put("command", command)
                                    .putPOJO("cardAttacker", action.getCardAttacker())
                                    .put("error", "Attacker card is frozen.");
                            break;
                        }
                        if (attacker.isUsed()) {
                            String out = "Attacker card has already attacked this turn.";
                            output.addObject().put("command", command)
                                    .putPOJO("cardAttacker", action.getCardAttacker())
                                    .put("error", out);
                            break;
                        }
                        if (!Player.checkTanksHero(activePlayerId, board)) {
                            String out = "Attacked card is not of type 'Tank'.";
                            output.addObject().put("command", command)
                                    .putPOJO("cardAttacker", action.getCardAttacker())
                                    .put("error", out);
                            break;
                        }
                        Player player = activePlayerId == 1 ? player2 : player1;
                        Hero hero = player.getHero();
                        hero.setHealth(hero.getHealth() - attacker.getAttackDamage());
                        if (hero.getHealth() <= 0) {
                            if (activePlayerId == 1) {
                                String out = "Player one killed the enemy hero.";
                                output.addObject().put("gameEnded", out);
                                gamesOneWon++;
                            } else {
                                String out = "Player two killed the enemy hero.";
                                output.addObject().put("gameEnded", out);
                                gamesTwoWon++;
                            }
                        }
                        attacker.setUsed();
                    }
                    case "useHeroAbility" -> {
                        Hero hero = activePlayer.getHero();
                        int row = action.getAffectedRow();
                        String name = hero.getName();
                        if (activePlayer.getMana() < hero.getMana()) {
                            String out = "Not enough mana to use hero's ability.";
                            output.addObject().put("command", command)
                                    .put("affectedRow", row)
                                    .putPOJO("error", out);
                            break;
                        }
                        if (hero.isUsed()) {
                            String out = "Hero has already attacked this turn.";
                            output.addObject().put("command", command)
                                    .put("affectedRow", row)
                                    .putPOJO("error", out);
                            break;
                        }
                        if (name.equals("Lord Royce") || name.equals("Empress Thorina")) {
                            if (!Player.checkAttack(activePlayerId, row)) {
                                String out = "Selected row does not belong to the enemy.";
                                output.addObject().put("command", command)
                                        .put("affectedRow", row)
                                        .put("error", out);
                                break;
                            }
                        }
                        if (name.equals("King Mudface") || name.equals("General Kocioraw")) {
                            if (Player.checkAttack(activePlayerId, row)) {
                                String out = "Selected row does not belong to the current player.";
                                output.addObject().put("command", command)
                                        .put("affectedRow", row)
                                        .put("error", out);
                                break;
                            }
                        }
                        hero.useAbility(activePlayerId, board, row);
                        activePlayer.useMana(hero.getMana());
                    }
                    case "getPlayerOneWins" -> {
                        output.addObject().put("command", command)
                                .put("output", gamesOneWon);
                    }
                    case "getPlayerTwoWins" -> {
                        output.addObject().put("command", command)
                                .put("output", gamesTwoWon);
                    }
                    case "getTotalGamesPlayed" -> {
                        output.addObject().put("command", command)
                                .put("output", gamesTwoWon + gamesOneWon);
                    }
                    default -> {
                    }
                }
            }
        }
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }
}
