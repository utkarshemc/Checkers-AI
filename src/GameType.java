/**
 * @author UtkarshBhardwaj
 */
public enum GameType {
	SINGLE("SINGLE"), GAME("GAME");

	private final String typeOfGame;

	GameType(final String typeOfGame) {
		this.typeOfGame = typeOfGame;
	}

	public static GameType getGameType(String type) {
		for (GameType gameType : GameType.values()) {
			if (gameType.typeOfGame.equals(type)) {
				return gameType;
			}
		}
		return null;
	}
}
