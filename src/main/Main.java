package main;

import Cards.*;
import GameStuff.Player;
import GameStuff.Table;
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
import java.util.Objects;
import java.util.Random;

import static java.util.Collections.shuffle;

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

        Table table;
        Player player1 = new Player(decks1, index1, hero1, order);
        Player player2 = new Player(decks2, index2, hero2, !order);
        shuffle(player1.getDecks().get(index1),
                new Random(inputData.getGames().get(0).getStartGame().getShuffleSeed()));
        shuffle(player2.getDecks().get(index2),
                new Random(inputData.getGames().get(0).getStartGame().getShuffleSeed()));

        player1.drawCard();
        player2.drawCard();

        Player activePlayer = order ? player1:player2;
        int activePlayerId = order ? 1:2;
        int round = 1;
        int turn = 1;
        for (ActionsInput action : inputData.getGames().get(0).getActions()) {
            System.out.println(action);
            String command = action.getCommand();

            Player printPlayer = action.getPlayerIdx() == 1 ? player1:player2;
            int printId = action.getPlayerIdx() == 1 ? 1 : 2;
            int deckId = printPlayer == player1 ? index1:index2;
            switch (command) {
                case "getPlayerDeck":
                    output.addObject().put("command", command).put("playerIdx", printId)
                            .putPOJO("output", printPlayer.getDecks().get(deckId));
                    break;
                case "getPlayerHero":
                    output.addObject().put("command", command).put("playerIdx", printId)
                            .putPOJO("output", printPlayer.getHero());
                    break;
                case "getPlayerTurn":
                    output.addObject().put("command", command).put("output", activePlayerId);
                 break;
                case "endPlayerTurn":
                    // TODO: Probably not working
                    if (turn == 1) {
                        if (activePlayerId == 1) {
                            activePlayerId = 2;
                            activePlayer = player2;
                        }
                        else if (activePlayerId == 2) {
                            activePlayerId = 1;
                            activePlayer = player1;
                        }
                        turn++;
                    }
                    else if (turn == 2) {
                        player1.getHero().addMana(round);
                        player2.getHero().addMana(round);
                        player1.drawCard();
                        player2.drawCard();
                        turn = 1;
                        round++;
                    }
                    break;
                case "getCardsInHand":
                    output.addObject().put("command", command).put("playerIdx", printId)
                            .putPOJO("output", printPlayer.getHand());
                    break;
                case "placeCard":
//                    if (Player.checkEnvironment(printPlayer.getHand().get(action.getHandIdx()).getName())) {
//                        Cannot place environment card on table.
//                    }

                    break;
            }
        }

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }

}
