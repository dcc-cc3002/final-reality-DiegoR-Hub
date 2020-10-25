package com.github.cc3002.finalreality.model.character;

import com.github.cc3002.finalreality.model.character.player.*;
import com.github.cc3002.finalreality.model.weapon.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerCharacterTest {
    private Enemy testEnemy;
    private static final String ENEMY_NAME = "Goblin";
    private static final int ENEMY_WEIGHT = 11;
    private static final int ATTACK_POINTS = 12;
    protected BlockingQueue<ICharacter> turns;
    private Engineer testEngineer;
    private WhiteMage testWhiteMage;
    private BlackMage testBlackMage;
    private Thief testThief;
    private Knight testKnight;
    private List<PlayerCharacter> listaPlayerCharacter;
    private Axe testAxe;
    private Sword testSword;
    private Staff testStaff;
    private Knife testKnife;
    private Bow testBow;
    private Enemy testEnemyDead;
    private Enemy testEnemyAboutToDie;
    private static final int DEFENSE_POINTS = 7;
    private static  final int HEALTH_POINTS = 17;
    private static final int MANA = 15;


    @BeforeEach
    void setUp() {
        testEnemyDead = new Enemy(turns, ENEMY_NAME, ENEMY_WEIGHT, DEFENSE_POINTS, 0, ATTACK_POINTS);
        testEnemy=new Enemy(turns, ENEMY_NAME, ENEMY_WEIGHT, DEFENSE_POINTS, HEALTH_POINTS, ATTACK_POINTS);
        turns = new LinkedBlockingQueue<>();
        listaPlayerCharacter = new ArrayList<PlayerCharacter>();
        testBlackMage = new BlackMage(turns, "cosa", DEFENSE_POINTS, HEALTH_POINTS, MANA);
        testEngineer = new Engineer(turns, "casa", DEFENSE_POINTS, HEALTH_POINTS);
        testKnight = new Knight(turns, "peso", DEFENSE_POINTS, HEALTH_POINTS);
        testThief = new Thief(turns, "ladron", DEFENSE_POINTS, HEALTH_POINTS);
        testWhiteMage = new WhiteMage(turns, "hola hola", DEFENSE_POINTS, HEALTH_POINTS, MANA);
        listaPlayerCharacter.add(testBlackMage);
        listaPlayerCharacter.add(testWhiteMage);
        listaPlayerCharacter.add(testThief);
        listaPlayerCharacter.add(testKnight);
        listaPlayerCharacter.add(testEngineer);
        testAxe = new Axe("prueba", 15, 10);
        testBow = new Bow("caco", 20, 13);
        testKnife = new Knife("hola", 20, 12);
        testSword = new Sword("ota", 14, 18);
        testStaff = new Staff("jiji", 15, 16, 34);
        testWhiteMage.equipWeapon(testStaff);
        testThief.equipWeapon(testBow);
        testKnight.equipWeapon(testAxe);
        testEngineer.equipWeapon(testAxe);
        testBlackMage.equipWeapon(testKnife);
        testEnemyAboutToDie = new Enemy(turns, ENEMY_NAME, ENEMY_WEIGHT, DEFENSE_POINTS, testEngineer.getEquippedWeapon().getDamage() - DEFENSE_POINTS - 1, ATTACK_POINTS);
    }
    /***
     * Chequea que un enemigo no pueda atacar a otr enemigo
     */
    @Test
    void attackTest() {
        testEngineer.attack(testEnemy);
        assertTrue(testEnemy.getHealthPoints() == HEALTH_POINTS - (testEngineer.getEquippedWeapon().getDamage()-testEnemy.getDefensePoints()));

        testEngineer.attack(testThief);
        assertTrue(testThief.getHealthPoints() == HEALTH_POINTS );

        assertTrue(testEnemyDead.isDead());
        testEngineer.attack(testEnemyDead);
        assertTrue(testEnemyDead.isDead());
        assertTrue(testEnemyDead.getHealthPoints() == 0);

        assertFalse(testEnemyAboutToDie.isDead());
        testEngineer.attack(testEnemyAboutToDie);
        assertTrue(testEnemyAboutToDie.isDead());
        assertTrue(testEnemyAboutToDie.getHealthPoints() == 0);
    }
    @Test
    void waitTurnTest() {
        Assertions.assertTrue(turns.isEmpty());
        listaPlayerCharacter.get(0).waitTurn();
        try {
            // Thread.sleep is not accurate so this values may be changed to adjust the
            // acceptable error margin.
            // We're testing that the character waits approximately 1 second.
            Thread.sleep(900);
            Assertions.assertEquals(0, turns.size());
            Thread.sleep(200);
            Assertions.assertEquals(1, turns.size());
            Assertions.assertEquals(listaPlayerCharacter.get(0), turns.peek());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
