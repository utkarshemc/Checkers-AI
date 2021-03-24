/**
 * @author UtkarshBhardwaj
 */

public class Checker {
	private Cell[][] board;
	private final int SIZE = 8;

	private int whiteNormalPieceCount;
	private int whiteKingCount;
	private int blackNormalPieceCount;
	private int blackKingCount;

	public Checker(Cell[][] board) {
		this.board = board;

		whiteNormalPieceCount = 0;
		whiteKingCount = 0;
		blackNormalPieceCount = 0;
		blackKingCount = 0;

		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				PlayerType piece = getPiece(i, j).getPlayerType();
				if (piece == PlayerType.BLACK)
					blackNormalPieceCount++;
				else if (piece == PlayerType.BLACKKING)
					blackKingCount++;
				else if (piece == PlayerType.WHITE)
					whiteNormalPieceCount++;
				else if (piece == PlayerType.WHITEKING)
					whiteKingCount++;
			}
		}
	}

	public int getWhiteNormalPieceCount() {
		return whiteNormalPieceCount;
	}

	public int getWhiteKingCount() {
		return whiteKingCount;
	}

	public int getBlackNormalPieceCount() {
		return blackNormalPieceCount;
	}

	public int getBlackKingCount() {
		return blackKingCount;
	}

	public int getSIZE() {
		return SIZE;
	}

	public Cell[][] getBoard() {
		return board;
	}

	public void setBoard(Cell[][] board) {
		this.board = board;
	}

	private Cell getPiece(int row, int col) {
		return board[row][col];
	}

	public Cell getTopLeft(Cell[][] board, int row, int col) {
		if (row > 0 && col > 0) {
			row -= 1;
			col -= 1;
			return board[row][col];
		}
		return null;
	}

	public Cell getTopRight(Cell[][] board, int row, int col) {
		if (row > 0 && col < 7) {
			row -= 1;
			col += 1;
			return board[row][col];
		}
		return null;
	}

	public Cell getBottomLeft(Cell[][] board, int row, int col) {
		if (row < 7 && col > 0) {
			row += 1;
			col -= 1;
			return board[row][col];
		}
		return null;
	}

	public Cell getBottomRight(Cell[][] board, int row, int col) {
		if (row < 7 && col < 7) {
			row += 1;
			col += 1;
			return board[row][col];
		}
		return null;
	}

	public Checker clone(Checker board) {
		Cell[][] newBoard = new Cell[8][8];
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				Cell temp = new Cell(board.getBoard()[j][i].getRow(), board.getBoard()[j][i].getCol(),
						board.getBoard()[j][i].getPlayerType());
				newBoard[j][i] = temp;
			}
		}
		Checker b = new Checker(newBoard);
		return b;
	}

	void makeMove(Move move) {
		int sRow = move.getStartingCell().getRow();
		int sCol = move.getStartingCell().getCol();
		int dRow = move.getDestinationCell().getRow();
		int dCol = move.getDestinationCell().getCol();
		if (board[sRow][sCol].getPlayerType().equals(move.getPlayerType())
				&& board[dRow][dCol].getPlayerType().equals(PlayerType.NONE)) {
			board[sRow][sCol].setPlayerType(PlayerType.NONE);
			if (dRow == 0 && move.getPlayerType() == PlayerType.WHITE) {
				board[dRow][dCol].setPlayerType(PlayerType.WHITEKING);
				whiteNormalPieceCount--;
				whiteKingCount++;
			} else if (dRow == 7 && move.getPlayerType() == PlayerType.BLACK) {
				board[dRow][dCol].setPlayerType(PlayerType.BLACKKING);
				blackKingCount++;
				blackNormalPieceCount--;
			} else {
				board[dRow][dCol].setPlayerType(move.getPlayerType());
			}
			if (move.getMoveType() == MoveType.JUMP) {
				for (Cell cell : move.getSkippedCell()) {
					if (cell.getPlayerType() == PlayerType.WHITE) {
						whiteNormalPieceCount--;
					}
					if (cell.getPlayerType() == PlayerType.BLACK) {
						blackNormalPieceCount--;
					}
					if (cell.getPlayerType() == PlayerType.WHITEKING) {
						whiteKingCount--;
					}
					if (cell.getPlayerType() == PlayerType.BLACKKING) {
						blackKingCount--;
					}
					board[cell.getRow()][cell.getCol()].setPlayerType(PlayerType.NONE);
				}
			}
		}
	}
}
