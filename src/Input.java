import java.util.ArrayList;


/**
 * @author UtkarshBhardwaj
 */
public class Input {
    private GameType gameType;
    private PlayerType playerType;
    private double timeRemainingInSeconds;
    private Checker checker;
    private int maxDepth;

    public void setGameType(final String gameType) {
        this.gameType = GameType.getGameType(gameType);
    }

    public GameType getGameType() {
        return this.gameType;
    }

    public void setPlayerType(final String playerColor) {
        this.playerType = PlayerType.getPlayerByColor(playerColor);
    }

    public PlayerType getPlayerType() {
        return this.playerType;
    }

    public void setTimeRemainingInSeconds(final String time) {
        this.timeRemainingInSeconds = Double.parseDouble(time);
    }

    public double getTimeRemainingInSeconds() {
        return this.timeRemainingInSeconds;
    }

    public void setChecker(Cell[][] board) {
        this.checker = new Checker(board);
    }

    public Checker getChecker() {
        return this.checker;
    }

    public void setMaxDepth(final int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public int getMaxDepth() {
        return this.maxDepth;
    }
}
