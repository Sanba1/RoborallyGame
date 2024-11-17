package dtu.roborally;

import dtu.roborally.Types_of_cards.BackwardMovement;
import dtu.roborally.Types_of_cards.ClockwiseRotationMovement;
import dtu.roborally.Types_of_cards.CounterClockwiseRotationMovement;
import dtu.roborally.Types_of_cards.ForwardMovementOne;
import dtu.roborally.Types_of_cards.ForwardMovementTwo;
import dtu.roborally.Types_of_cards.ForwardMovementThree;
import dtu.roborally.Types_of_cards.TurnAroundMovement;

import java.util.*;

public abstract class Card {

    private static final String[] deck = {
            "MoveOne", "MoveOne", "MoveOne", "MoveOne",
            "MoveTwo", "MoveTwo", "MoveTwo",
            "MoveThree",
            "Right", "Right", "Right", "Right",
            "Left", "Left", "Left", "Left",
            "UTurn",
            "MoveBack",

    };

    // Eventually, we should make it, so we can adjust the amount of cards
    // using the GUI, idk how but one day, bismillah
    // MoveForwardOneCard = 4
    // MoveForwardTwoCard = 3
    // MoveForwardThreeCard = 1
    // RotateClockwiseCard = 4
    // RotateCounterClockwiseCard = 4
    // UTurnCard = 1
    // MoveBackwardCard = 1
    // RepeatCard = 1

    /**
     * @param positionRobot (Position)
     *
     * @return The robot will attempt to perform the card's action from the current position
     */
    public abstract Position useCard(Position positionRobot);

    /**
     * @param hand (ArrayList<Card>)
     *
     * A hand that is the first 9 cards of a shuffled deck of movement cards
     */
    public static void random(ArrayList<Card> hand) {

        // Shuffle deck
        List<String> stringList = Arrays.asList(deck);
        Collections.shuffle(stringList);
        stringList.toArray(deck);

        // We take only the first 9 cards of the shuffled deck
        for (int i = 0; i < 9;i++) {
            switch (deck[i]) {
                case "MoveOne" -> hand.add(new ForwardMovementOne()); // Move One
                case "MoveTwo" -> hand.add(new ForwardMovementTwo()); // Move Two
                case "MoveThree" -> hand.add(new ForwardMovementThree()); // Move Three
                case "Right" -> hand.add(new ClockwiseRotationMovement()); // Right
                case "Left" -> hand.add(new CounterClockwiseRotationMovement()); // Left
                case "UTurn" -> hand.add(new TurnAroundMovement()); // U Turn
                case "MoveBack" -> hand.add(new BackwardMovement()); // Move Back
                case null, default -> System.out.println("No Card in Deck Index: " + i);
            }
        }
    }
}

