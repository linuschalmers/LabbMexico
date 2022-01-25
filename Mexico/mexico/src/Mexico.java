
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
    final int startAmount = 3;   // Money for a player. Select any
    final int mexico = 1000;     // A value greater than any other

    void program() {
        //test();            // <----------------- UNCOMMENT to test


        int pot = 0;         // What the winner will get
        Player[] players;    // The players (array of Player objects)
        Player current;      // Current player for round
        Player leader;       // Player starting the round

        players = getPlayers();
        current = getRandomPlayer(players);
        leader = current;

        out.println("Mexico Game Started");
        statusMsg(players);

       //while (players.length > 1) {   // Game over when only one player left

            // ----- In ----------
            String cmd = getPlayerChoice(current);
            if ("r".equals(cmd)) {

                    // --- Process ------
                if(current.nRolls < maxRolls){
                    current.fstDice=rollDice(current);
                    current.secDice=rollDice(current);

                    setScore(current);
                }
                else{
                    current = next(players, current);
                }


                    // ---- Out --------
                    roundMsg(current);

            } else if ("n".equals(cmd)) {

                   current = next(players, current);
            } else {
                out.println("?");
            }

            //if ( round finished) {
                // --- Process -----

                // ----- Out --------------------
                //out.println("Round done " + getLoser(players) + " lost!");
                out.println("Next to roll is " + current.name);

                statusMsg(players);
            //}
        //}
        out.println("Game Over, winner is " + players[0].name + ". Will get " + pot + " from pot");
    }


    // ---- Game logic methods --------------

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


    Player getLoser(Player[] players, Player current){
        Player Loser = current;
        int min = current.score;
        for(int i = 0; i < players.length; i++){
            if (players[i].score < min){
               min = players[i].score ;
               Loser = players[i] ;
            }
        }
        return Loser;

    }

    void clearRoundResults(){

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



    // ---------- IO methods (nothing to do here) -----------------------

    Player[] getPlayers() {
        // Ugly for now. If using a constructor this may
        // be cleaned up.
        Player[] players = new Player[3];
        Player p1 = new Player();
        p1.name = "Loffe";
        p1.amount = startAmount;
        Player p2 = new Player();
        p2.name = "Boken";
        p2.amount = startAmount;
        Player p3 = new Player();
        p3.name = "Linux";
        p3.amount = startAmount;
        players[0] = p1;
        players[1] = p2;
        players[2] = p3;
        return players;
    }

    void statusMsg(Player[] players) {
        out.print("Status: ");
        for (int i = 0; i < players.length; i++) {
            out.print(players[i].name + " " + players[i].amount + " ");
        }
        out.println();
    }

    void roundMsg(Player current) {
        out.println(current.name + " got " +
                current.fstDice + " and " + current.secDice);
    }

    String getPlayerChoice(Player player) {
        out.print("Player is " + player.name + " > ");
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
        Player[] ps = {new Player(), new Player(), new Player()};
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


