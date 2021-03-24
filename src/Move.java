import java.util.List;

/**
 * @author UtkarshBhardwaj
 */

public class Move {
	private PlayerType playerType;
	private MoveType moveType;
	private List<Cell> path;
	private Cell startingCell;
	private Cell destinationCell;
	private int piecesJumped;
	private int kingsJumped;
	private List<Cell> skippedCell;

	public Move(PlayerType playerType, MoveType moveType, List<Cell> path, Cell startingCell, Cell destinationCell,
			int piecesJumped, int kingsJumped, List<Cell> skippedCell) {
		this.playerType = playerType;
		this.moveType = moveType;
		this.path = path;
		this.startingCell = startingCell;
		this.destinationCell = destinationCell;
		this.piecesJumped = piecesJumped;
		this.kingsJumped = kingsJumped;
		this.skippedCell = skippedCell;
	}

	public PlayerType getPlayerType() {
		return this.playerType;
	}

	public MoveType getMoveType() {
		return this.moveType;
	}

	public List<Cell> getPath() {
		return this.path;
	}

	public Cell getStartingCell() {
		return this.startingCell;
	}

	public Cell getDestinationCell() {
		return this.destinationCell;
	}

	public int getPiecesJumped() {
		return piecesJumped;
	}

	public void setPiecesJumped(int piecesJumped) {
		this.piecesJumped = piecesJumped;
	}

	public int getKingsJumped() {
		return kingsJumped;
	}

	public List<Cell> getSkippedCell() {
		return skippedCell;
	}

	public void setSkippedCell(List<Cell> skippedCell) {
		this.skippedCell = skippedCell;
	}

	public void setKingsJumped(int kingsJumped) {
		this.kingsJumped = kingsJumped;
	}

}
