/**
 * @author UtkarshBhardwaj
 */
public class Cell {
	private int row;
	private int col;
	private PlayerType playerType;

	public Cell(int row, int col, PlayerType playerType) {
		super();
		this.row = row;
		this.col = col;
		this.playerType = playerType;
	}

	public int getRow() {
		return this.row;
	}

	public int getCol() {
		return this.col;
	}

	public void setPlayerType(final PlayerType playerType) {
		this.playerType = playerType;
	}

	public PlayerType getPlayerType() {
		return this.playerType;
	}

}
