package main;

import fileio.CardInput;

public class Minion extends Card{
    private int attackDamage;
    private int health;

    public Minion(CardInput card) {
        super(card);
        attackDamage = card.getAttackDamage();
        health = card.getHealth();
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    @Override
    public String toString() {
        return "{"
                +  "mana="
                + getMana()
                +  ", attackDamage="
                + attackDamage
                + ", health="
                + health
                +  ", description='"
                + getDescription()
                + '\''
                + ", colors="
                + getColors()
                + ", name='"
                +  ""
                + getName()
                + '\''
                + '}';
    }
}
