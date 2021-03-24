/**
 * @author UtkarshBhardwaj
 */
public enum PlayerType {
    BLACK("b", "BLACK", false),
    WHITE("w", "WHITE", false),
    NONE(".", "NONE", false),
	BLACKKING("B", "BLACK", true),
	WHITEKING("W","WHITE", true);

    public final String player;
    public final String color;
    public final Boolean isKing;

    PlayerType(String player, String color,Boolean isKing) {
        this.player = player;
        this.color = color;
        this.isKing = isKing;
    }

    public static PlayerType getPlayerByValue(final String player) {
        for (PlayerType pt : PlayerType.values()) {
            if (pt.player.equals(player)) {
                return pt;
            }
        }
        return null;
    }

    public static PlayerType getPlayerByColor(final String color) {
        for (PlayerType pt : PlayerType.values()) {
            if (pt.color.equals(color)) {
                return pt;
            }
        }
        return NONE;
    }
}
