public class Cheat implements Strategy {
    @Override
    public String getMove(String playerMove) {
        String computerMove;
        switch (playerMove) {
            case "R":
                computerMove = "P"; // Paper covers Rock
                break;
            case "P":
                computerMove = "S"; // Scissors cuts Paper
                break;
            case "S":
                computerMove = "R"; // Rock breaks Scissors
                break;
            default:
                computerMove = "R"; // Default safety fallback
                break;
        }
        return computerMove;
    }
}