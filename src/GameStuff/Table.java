package GameStuff;

import Cards.Card;

import java.util.ArrayList;

public class Table {
    ArrayList<ArrayList<Card>> table;

    ArrayList<ArrayList<Card>> getTable() {
        if (table == null)
            table = new ArrayList<>();
        return table;
    }
}
