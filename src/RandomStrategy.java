import java.util.Random;


public class RandomStrategy implements Strategy {
    private final Random rand = new Random();
    private final String[] moves = {"R", "P", "S"};

    @Override
    public String getMove(String playerMove) {
        // Pick a random index between 0 and 2
        int index = rand.nextInt(moves.length);
        return moves[index];
    }
}