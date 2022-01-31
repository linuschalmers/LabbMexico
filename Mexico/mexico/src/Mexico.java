
import java.util.Random;
import java.util.Scanner;

import static java.lang.System.*;


/*
 *  The Mexico dice game
 *  See https://en.wikipedia.org/wiki/Mexico_(game)
 * Albin är inte bra på mexico
 */
public class Mexico {



    public static void main(String[] args) {
        new Mexico().program();
    }

    final Random rand = new Random();
    final Scanner sc = new Scanner(in);
    final int maxRolls = 3;      // No player may exceed this
    final int startAmount = 1;   // Money for a player. Select any
    final int mexico = 1000;     // A value greater than any other

    void program() {
        //test();            // <----------------- UNCOMMENT to test

        int currentMaxRolls = maxRolls;
        int pot = 0;         // What the winner will get
        Player[] players;    // The players (array of Player objects)
        Player current;      // Current player for round
        Player leader;       // Player starting the round

        players = getPlayers();
        current = getRandomPlayer(players);
        leader = current;

        out.println("Mexico Game Started");
        statusMsg(players);

        while (players.length > 1) {   // Game over when only one player left
            out.println("Next to roll is " + current.name);
            String cmd = getPlayerChoice(current);

            if ("r".equals(cmd)) {
                if (current.nRolls < currentMaxRolls) {
                    current.nRolls++;
                    current.fstDice = rollDice(current);
                    current.secDice = rollDice(current);
                    setScore(current);
                    roundMsg(current);
                    out.println("Value of the roll is " + largestCombo(current));
                }
                else {
                    out.println(current.name + " dont have any more rolls available. End of your turn" + "\n" + current.name + "s score is " + current.score + ".");

                    current = next(players, current);
                }
            }
            else if ("n".equals(cmd)) {

                if (allRolled(players) == 1) {
                    currentMaxRolls = current.nRolls;
                }
                out.println( current.name + "s score is " + current.score + "." );
                current = next(players, current);
            }
            else {
                out.println("?");
            }

            if(current.nRolls == currentMaxRolls){

            if (allRolled(players) == players.length) {
                out.println("\n" + "Round done " + getLoser(players, current, pot) + " lost!");
                pot++;
                removeLoser(players, current);
                clearRoundResults(players);
                currentMaxRolls = maxRolls;
                players=amountEquZero(players);
                statusMsg(players);
                current = next(players, current);
                System.out.flush();
            }
            }
        }
        out.println("Game Over, winner is " + players[0].name + ". Will get " + pot + " from pot");
    }

    // TODO implement and test methods (one at the time)

    // rolls the dice
    int rollDice(Player p){
        int diceNumber = rand.nextInt(5) + 1;
        return diceNumber;
    }

    //Compares the two dices and combines them in the largest order
    int largestCombo(Player p){
        int maxDiceCombo;
        if (p.fstDice > p.secDice) {
            maxDiceCombo = (p.fstDice * 10) + p.secDice;
        }else{
            maxDiceCombo = (p.secDice * 10) + p.fstDice;
        }
        return maxDiceCombo;
    }

    void setScore(Player p){
        if(largestCombo(p) == 21){
            p.score = mexico;
        }
        else if (p.fstDice == p.secDice){
            p.score = largestCombo(p) * 10;
        }
        else{
            p.score = largestCombo(p);
        }
    }

    String getLoser(Player[] players, Player current,int pot){
        Player Loser = current;
        int min = current.score;
        for(int i = 0; i < players.length; i++){
            if (players[i].score < min){
                min = players[i].score ;
                Loser = players[i] ;
            }
        }
        Loser.amount--;
        return Loser.name;
    }

    Player[] amountEquZero(Player[] players){
        for (int i = 0; i <= players.length-1 ; i++){
            if(players[i].amount<=0){
                players=removeLoser(players, players[i]);
            }
        }
        return players;
    }

    Player [] removeLoser(Player[] players, Player loser){
        Player[] remainingPlayers = new Player[players.length - 1];
        int räknare = 0;
        for(Player p : players){
            if(p != loser){
                remainingPlayers[räknare++] = p;
            }
        }
        return remainingPlayers;
    }

    void clearRoundResults(Player[] players){
        for (Player p : players) {
            p.score = 0;
            p.nRolls = 0;
            p.fstDice = 0;
            p.secDice = 0;

        }
    }

    int allRolled(Player[] players){
        int antal = 0;
        for (Player p:players) {
            if (p.nRolls>0){
                antal ++;
            }
        }
        return antal;
    }
    // ALLT SOM HAR MED PLAYERS SKRIVS UNDER DEHÄR

    Player next(Player[] players, Player p){
        Player nextP = players [((indexOf(players, p)) + 1) % players.length];
        return nextP;
    }

    int indexOf(Player[] players, Player player) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == player) {
                return i;
            }
        }
        return -1;
    }

    Player getRandomPlayer(Player[] players) {
        return players[rand.nextInt(players.length)];
    }

    // ---------- IO methods (nothing to do here) -----------------------
    void statusMsg(Player[] players) {
        out.print("Status: ");
        for (int i = 0; i < players.length; i++) {
            out.print(players[i].name + " " + players[i].amount + " ");
        }
        out.println();
    }

    void roundMsg(Player current) {
        out.println(current.name + " got " + current.fstDice + " and " + current.secDice);
    }

    String getPlayerChoice(Player player) {
        out.print("\n" + "Does " + player.name + " want to roll? > ");
        return sc.nextLine();
    }

    // Possibly useful utility during development
    String toString(Player p){
        return p.name + ", " + p.amount + ", " + p.fstDice + ", "
                + p.secDice + ", " + p.nRolls;
    }

    // Class for a player
    class Player {
        String name;
        int amount;   // Start amount (money)
        int fstDice;  // Result of first dice
        int secDice;  // Result of second dice
        int nRolls;   // Current number of rolls
        int score;    // The players score

        Player ( String name1, int amount1){
            name = name1;
            amount = amount1;
        }
    }

    Player[] getPlayers(){
        Player[] players = new Player[3];
        Player p1 = new Player("Loffe", startAmount);
        Player p2 = new Player("Boken", startAmount);
        Player p3 = new Player("Linux", startAmount);
        players[0] = p1;
        players[1] = p2;
        players[2] = p3;
        return players;
    }


    /**************************************************
     *  Testing
     *
     *  Test are logical expressions that should
     *  evaluate to true (and then be written out)
     *  No testing of IO methods
     *  Uncomment in program() to run test (only)
     ***************************************************/
    //void test() {
    // A few hard coded player to use for test
    // NOTE: Possible to debug tests from here, very efficient!
    //Player[] ps = {new Player(), new Player(), new Player()};
    //ps[0].fstDice = 2;
    //ps[0].secDice = 6;
    //ps[1].fstDice = 6;
    //ps[1].secDice = 5;
    //ps[2].fstDice = 1;
    //ps[2].secDice = 1;

    //out.println(setScore(ps[0]) == 62);
    //out.println(setScore(ps[1]) == 65);
    //out.println(next(ps, ps[0]) == ps[1]);
    //out.println(getLoser(ps) == ps[0]);


}


