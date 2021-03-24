import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class PlayImpl {

	private static final int BOARD_MAX_ROW = 8;
	private static final int BOARD_MAX_COLUMN = 8;
	private static double timeRemainingInMillis;

	protected void setTime(Input input) {
		timeRemainingInMillis = input.getTimeRemainingInSeconds() * 1000;
	}

	public static ArrayList<Cell> getAllPlayerPositionByColor(Checker board, String color) {
		// TODO Auto-generated method stub
		ArrayList<Cell> playerPositions = new ArrayList<>();
		for (int i = 0; i < BOARD_MAX_ROW; i++) {
			for (int j = 0; j < BOARD_MAX_COLUMN; j++) {
				if (board.getBoard()[i][j].getPlayerType().color.equalsIgnoreCase(color)) {
					playerPositions.add(board.getBoard()[i][j]);
				}
			}
		}
		return playerPositions;
	}

	public static ArrayList<Move> getAllPossibleMove(Checker board, ArrayList<Cell> allPlayers) {
		// TODO Auto-generated method stub
		MoveToPlay nextMove = null;
		boolean jumpMovesAvailable = false;

		ArrayList<ArrayList<Move>> allPlayersMoves = new ArrayList<>();
		for (Cell cell : allPlayers) {
			allPlayersMoves.add(getAvailableMoves(board, cell));
		}

		ArrayList<Move> possibleMovesList = new ArrayList<>();
		for (ArrayList<Move> moves : allPlayersMoves) {
			if (!moves.isEmpty() && (moves.get(0).getMoveType() == MoveType.JUMP)) {
				jumpMovesAvailable = true;
				break;
			}
		}
		for (ArrayList<Move> moves : allPlayersMoves) {
			possibleMovesList.addAll(moves);
		}
		if (jumpMovesAvailable) {
			Iterator<Move> itr = possibleMovesList.iterator();
			while (itr.hasNext()) {
				Move tempMove = itr.next();
				if (tempMove.getMoveType() == MoveType.EMPTY)
					itr.remove();
			}
		}
		return possibleMovesList;
	}

	public static ArrayList<Move> getAvailableMoves(Checker board, Cell cell) {
		ArrayList<Move> availableMoves = new ArrayList<>();
		if (!isNotNull(cell)) {
			return availableMoves;
		}
		final ArrayList<Move> allJumpMoves = getJumpMoves(board, cell);
		if (!allJumpMoves.isEmpty()) {
			availableMoves.addAll(allJumpMoves);
			return availableMoves;
		}
		final ArrayList<Move> allSingleMoves = getSingleMoves(board, cell);
		availableMoves.addAll(allSingleMoves);
		return availableMoves;
	}

	public static Move makeMove(Checker board, int depth, PlayerType player) {
		MoveToPlay bestMove = new MoveToPlay();
		bestMove.setHeuristic(Integer.MIN_VALUE);
		MoveToPlay move = new MoveToPlay();
		Move bestMovetoMake = null;
		ArrayList<Cell> playerPositionByColor = getAllPlayerPositionByColor(board, player.color);
		ArrayList<Move> allMoves = getAllPossibleMove(board, playerPositionByColor);
		try {
			for (int i = 1; i <= depth; i++) {
				move = minimaxStart(board, i, player.color, true, allMoves);
				if (move.getHeuristic() >= bestMove.getHeuristic()) {
					bestMove = move;
					bestMovetoMake = bestMove.getMove();
				}
			}
		} catch (AgentTimeoutException e) {
			return bestMovetoMake;
		}
		return bestMovetoMake;
	}

	private static final String WHITE = "WHITE";
	private static final String BLACK = "BLACK";

	public static MoveToPlay minimaxStart(Checker board, int depth, String playerColor, boolean maximizingPlayer,
			ArrayList<Move> moves) throws AgentTimeoutException {
		double alpha = Double.NEGATIVE_INFINITY;
		double beta = Double.POSITIVE_INFINITY;
		MoveToPlay bestMove = new MoveToPlay();
		int randomMoveNumber = 0;
		ArrayList<Double> heuristics = new ArrayList<>();
		if (moves.isEmpty())
			return null;
		Checker tempBoard = null;
		for (int i = 0; i < moves.size(); i++) {
			if (System.currentTimeMillis() - homework.START_TIME >= timeRemainingInMillis) {
				throw new AgentTimeoutException();
			}
			tempBoard = board.clone(board);
			tempBoard.makeMove(moves.get(i));
			heuristics.add(minimax(tempBoard, depth - 1, flipSide(playerColor), !maximizingPlayer, alpha, beta));
		}

		double maxHeuristics = Double.NEGATIVE_INFINITY;

		Random rand = new Random();
		for (int i = heuristics.size() - 1; i >= 0; i--) {
			if (heuristics.get(i) >= maxHeuristics) {
				maxHeuristics = heuristics.get(i);
			}
		}

		for (int i = 0; i < heuristics.size(); i++) {
			if (heuristics.get(i) < maxHeuristics) {
				heuristics.remove(i);
				moves.remove(i);
				i--;
			}
		}
		randomMoveNumber = rand.nextInt(moves.size());
		bestMove.setHeuristic(heuristics.get(randomMoveNumber));
		bestMove.setMove(moves.get(randomMoveNumber));

		return bestMove;
	}

	public static double minimax(Checker board, int depth, String playerColor, boolean maximizingPlayer, double alpha,
			double beta) throws AgentTimeoutException {
		if (System.currentTimeMillis() - homework.START_TIME >= timeRemainingInMillis) {
			throw new AgentTimeoutException();
		}
		if (depth == 0) {
			return getHeuristic(board, playerColor);
		}

		ArrayList<Cell> playerPositionByColor = getAllPlayerPositionByColor(board, playerColor);
		ArrayList<Move> possibleMoves = getAllPossibleMove(board, playerPositionByColor);

		double initial = 0;
		Checker tempBoard = null;
		if (maximizingPlayer) {
			initial = Double.NEGATIVE_INFINITY;
			for (int i = 0; i < possibleMoves.size(); i++) {
				tempBoard = board.clone(board);
				tempBoard.makeMove(possibleMoves.get(i));

				double result = minimax(tempBoard, depth - 1, flipSide(playerColor), !maximizingPlayer, alpha, beta);

				initial = Math.max(result, initial);
				alpha = Math.max(alpha, initial);

				if (alpha >= beta)
					break;
			}
		}
		// minimizing
		else {
			initial = Double.POSITIVE_INFINITY;
			for (int i = 0; i < possibleMoves.size(); i++) {
				tempBoard = board.clone(board);
				tempBoard.makeMove(possibleMoves.get(i));
				double result = minimax(tempBoard, depth - 1, flipSide(playerColor), !maximizingPlayer, alpha, beta);

				initial = Math.min(result, initial);
				alpha = Math.min(alpha, initial);

				if (alpha >= beta)
					break;
			}
		}

		return initial;
	}

	public static double getHeuristic(Checker board, String PlayerColor) {
		double kingWeight = 0.5;
		double result = 0;
		if (PlayerColor.equalsIgnoreCase(WHITE))
			result = board.getWhiteKingCount() * kingWeight + board.getWhiteNormalPieceCount()
					- board.getBlackKingCount() * kingWeight - board.getBlackNormalPieceCount();
		else
			result = board.getBlackKingCount() * kingWeight + board.getBlackNormalPieceCount()
					- board.getWhiteKingCount() * kingWeight - board.getWhiteNormalPieceCount();
		return result;

	}

	public static String flipSide(String color) {
		if (color.equalsIgnoreCase(WHITE)) {
			return BLACK;
		} else {
			return WHITE;
		}

	}

	public static boolean isNotNull(Object obj) {
		return null != obj;
	}

	public static ArrayList<Move> getJumpMoves(Checker board, Cell cell) {
		int piecesJumped = 0;
		int kingsJumped = 0;
		int longestJump = 0;
		ArrayList<Jump> jumps = new ArrayList<>();
		ArrayList<Cell> visitedCells = new ArrayList<>();
		ArrayList<Cell> skippedCells = new ArrayList<>();
		visitedCells.add(cell);
		getJumps(board, null, cell, jumps, visitedCells, skippedCells);
		if (skippedCells != null) {
			for (Cell sCell : skippedCells) {
				piecesJumped++;
				if (sCell.getPlayerType().isKing) {
					kingsJumped++;
				}
			}
		}
		ArrayList<Move> jumpMoves = new ArrayList<>();
		if (jumps.size() != 0) {
			ArrayList<ArrayList<Cell>> jumpList = getJumpingPaths(getParentInfo(jumps), cell, board);
			for (ArrayList<Cell> path : jumpList) {
				if (path.size() > longestJump) {
					longestJump = path.size();
				}
			}
			for (ArrayList<Cell> path : jumpList) {
				if (path.size() == longestJump) {
					jumpMoves.add(getMove(cell.getPlayerType(), MoveType.JUMP, path, path.get(0),
							path.get(path.size() - 1), piecesJumped, kingsJumped, skippedCells));
				}
			}
		}
		return jumpMoves;
	}

	public static void getJumps(Checker board, Cell parent, Cell cell, ArrayList<Jump> jumps,
			ArrayList<Cell> visitedCells, ArrayList<Cell> skippedCells) {
		Cell topLeftLeft = null;
		Cell topRightRight = null;
		Cell bottomLeftLeft = null;
		Cell bottomRightRight = null;
		Cell topLeft = board.getTopLeft(board.getBoard(), cell.getRow(), cell.getCol());
		if (isNotNull(topLeft)) {
			topLeftLeft = board.getTopLeft(board.getBoard(), topLeft.getRow(), topLeft.getCol());
		}
		Cell topRight = board.getTopRight(board.getBoard(), cell.getRow(), cell.getCol());
		if (isNotNull(topRight)) {
			topRightRight = board.getTopRight(board.getBoard(), topRight.getRow(), topRight.getCol());
		}
		Cell bottomLeft = board.getBottomLeft(board.getBoard(), cell.getRow(), cell.getCol());
		if (isNotNull(bottomLeft)) {
			bottomLeftLeft = board.getBottomLeft(board.getBoard(), bottomLeft.getRow(), bottomLeft.getCol());
		}
		Cell bottomRight = board.getBottomRight(board.getBoard(), cell.getRow(), cell.getCol());
		if (isNotNull(bottomRight)) {
			bottomRightRight = board.getBottomRight(board.getBoard(), bottomRight.getRow(), bottomRight.getCol());
		}

		if (visitedCells.get(0).getPlayerType() == PlayerType.WHITE) {
			if (isNotNull(topLeft) && isNotNull(topLeftLeft)
					&& isJumpValid(parent, topLeft, topLeftLeft, visitedCells, PlayerType.WHITE)) {
				skippedCells.add(topLeft);
				visitedCells.add(topLeftLeft);
				if (!(cell.getRow() == 0)) {
					recursiveGetJumps(board, cell, topLeftLeft, jumps, visitedCells, skippedCells);
				}
			}
			if (isNotNull(topRight) && isNotNull(topRightRight)
					&& isJumpValid(parent, topRight, topRightRight, visitedCells, PlayerType.WHITE)) {
				skippedCells.add(topRight);
				visitedCells.add(topRightRight);
				if (!(cell.getRow() == 0))
					recursiveGetJumps(board, cell, topRightRight, jumps, visitedCells, skippedCells);
			}
		}
		if (visitedCells.get(0).getPlayerType() == PlayerType.WHITEKING
				|| visitedCells.get(0).getPlayerType() == PlayerType.BLACKKING) {
			if (isNotNull(bottomLeft) && isNotNull(bottomLeftLeft) && isJumpValid(parent, bottomLeft, bottomLeftLeft,
					visitedCells, visitedCells.get(0).getPlayerType())) {
				skippedCells.add(bottomLeft);
				visitedCells.add(bottomLeftLeft);
				recursiveGetJumps(board, cell, bottomLeftLeft, jumps, visitedCells, skippedCells);
			}
			if (isNotNull(bottomRight) && isNotNull(bottomRight) && isJumpValid(parent, bottomRight, bottomRightRight,
					visitedCells, visitedCells.get(0).getPlayerType())) {
				skippedCells.add(bottomRight);
				visitedCells.add(bottomRightRight);
				recursiveGetJumps(board, cell, bottomRightRight, jumps, visitedCells, skippedCells);
			}
			if (isNotNull(topLeft) && isNotNull(topLeftLeft)
					&& isJumpValid(parent, topLeft, topLeftLeft, visitedCells, visitedCells.get(0).getPlayerType())) {
				skippedCells.add(topLeft);
				visitedCells.add(topLeftLeft);
				recursiveGetJumps(board, cell, topLeftLeft, jumps, visitedCells, skippedCells);
			}
			if (isNotNull(topRight) && isNotNull(topRightRight) && isJumpValid(parent, topRight, topRightRight,
					visitedCells, visitedCells.get(0).getPlayerType())) {
				skippedCells.add(topRight);
				visitedCells.add(topRightRight);
				recursiveGetJumps(board, cell, topRightRight, jumps, visitedCells, skippedCells);
			}
		}
		if (visitedCells.get(0).getPlayerType() == PlayerType.BLACK) {
			if (isNotNull(bottomLeft) && isNotNull(bottomLeftLeft)
					&& isJumpValid(parent, bottomLeft, bottomLeftLeft, visitedCells, PlayerType.BLACK)) {
				skippedCells.add(bottomLeft);
				visitedCells.add(bottomLeftLeft);
				if (!(cell.getRow() == 7)) {
					recursiveGetJumps(board, cell, bottomLeftLeft, jumps, visitedCells, skippedCells);
				}
			}
			if (isNotNull(bottomRight) && isNotNull(bottomRightRight)
					&& isJumpValid(parent, bottomRight, bottomRightRight, visitedCells, PlayerType.BLACK)) {
				skippedCells.add(bottomRight);
				visitedCells.add(bottomRightRight);
				if (!(cell.getRow() == 7)) {
					recursiveGetJumps(board, cell, bottomRightRight, jumps, visitedCells, skippedCells);
				}
			}
		}

	}

	public static boolean isJumpValid(final Cell parentCell, final Cell inBetweenCell, final Cell destinationCell,
			ArrayList<Cell> visitedCells, PlayerType color) {
		if (destinationCell != null) {
			if (color == PlayerType.WHITE || color == PlayerType.WHITEKING) {
				return (inBetweenCell.getPlayerType() == PlayerType.BLACK
						|| inBetweenCell.getPlayerType() == PlayerType.BLACKKING)
						&& destinationCell.getPlayerType() == PlayerType.NONE && isNotSame(parentCell, destinationCell)
						&& !visitedCells.contains(destinationCell);
			}
			if ((color == PlayerType.BLACK || color == PlayerType.BLACKKING)) {
				return (inBetweenCell.getPlayerType() == PlayerType.WHITE
						|| inBetweenCell.getPlayerType() == PlayerType.WHITEKING)
						&& destinationCell.getPlayerType() == PlayerType.NONE && isNotSame(parentCell, destinationCell)
						&& !visitedCells.contains(destinationCell);
			}
		}
		return false;

	}

	public static boolean isNotSame(Cell cell1, Cell cell2) {
		if (null == cell1) {
			return true;
		}
		return cell1.getRow() != cell2.getRow() || cell1.getCol() != cell2.getCol();
	}

	public static void recursiveGetJumps(Checker board, Cell startingCell, Cell destinationCell, ArrayList<Jump> jumps,
			ArrayList<Cell> visitedCells, ArrayList<Cell> skippedCells) {
		jumps.add(new Jump(startingCell, destinationCell));
		getJumps(board, startingCell, destinationCell, jumps, visitedCells, skippedCells);
	}

	public static Cell[][] getParentInfo(ArrayList<Jump> jumps) {
		Cell[][] parentInfo = new Cell[8][8];
		for (Jump jump : jumps) {
			Cell current = jump.getCurrent();
			parentInfo[current.getRow()][current.getCol()] = jump.getParent();
		}
		return parentInfo;
	}

	public static ArrayList<ArrayList<Cell>> getJumpingPaths(final Cell[][] parentInfo, final Cell startingCell,
			Checker board) {
		ArrayList<ArrayList<Cell>> jumpingPaths = new ArrayList<>();
		for (int i = 0; i < parentInfo.length; i++) {
			for (int j = 0; j < parentInfo[i].length; j++) {
				if (null != parentInfo[i][j]) {
					jumpingPaths.add(retracePath(parentInfo, startingCell, board.getBoard()[i][j], parentInfo[i][j]));
				}
			}
		}
		return jumpingPaths;
	}

	public static ArrayList<Cell> retracePath(final Cell[][] parentInfo, final Cell startingCell,
			final Cell currentCell, final Cell target) {
		final ArrayList<Cell> path = new ArrayList<>();
		path.add(currentCell);
		if (currentCell.getRow() == startingCell.getRow() && currentCell.getCol() == startingCell.getCol()) {
			path.add(startingCell);
			return path;
		}
		Cell cell = target;
		path.add(cell);
		while (cell.getRow() != startingCell.getRow() || cell.getCol() != startingCell.getCol()) {
			if (cell.getRow() < 0 || cell.getRow() >= 8 || cell.getCol() < 0 || cell.getCol() >= 8) {
				return null;
			}
			cell = parentInfo[cell.getRow()][cell.getCol()];
			path.add(cell);
		}
		Collections.reverse(path);
		return path;
	}

	public static Move getMove(PlayerType playerType, MoveType moveType, List<Cell> path, Cell startingCell,
			Cell destinationCell, int piecesSkipped, int kingsSkipped, List<Cell> skippedCell) {
		return new Move(playerType, moveType, path, startingCell, destinationCell, piecesSkipped, kingsSkipped,
				skippedCell);
	}

	public static ArrayList<Move> getSingleMoves(Checker board, Cell cell) {
		Cell topLeft = board.getTopLeft(board.getBoard(), cell.getRow(), cell.getCol());
		Cell topRight = board.getTopRight(board.getBoard(), cell.getRow(), cell.getCol());
		Cell bottomLeft = board.getBottomLeft(board.getBoard(), cell.getRow(), cell.getCol());
		Cell bottomRight = board.getBottomRight(board.getBoard(), cell.getRow(), cell.getCol());

		ArrayList<Move> availableMoves = new ArrayList<>();
		if (cell.getPlayerType() == PlayerType.WHITE) {
			if (isValidSingleMove(cell, topLeft)) {
				addSingleMove(MoveType.EMPTY, cell, topLeft, availableMoves);
			}
			if (isValidSingleMove(cell, topRight)) {
				addSingleMove(MoveType.EMPTY, cell, topRight, availableMoves);
			}
		}

		if (cell.getPlayerType() == PlayerType.WHITEKING || cell.getPlayerType() == PlayerType.BLACKKING) {
			if (isValidSingleMove(cell, topLeft)) {
				addSingleMove(MoveType.EMPTY, cell, topLeft, availableMoves);
			}
			if (isValidSingleMove(cell, topRight)) {
				addSingleMove(MoveType.EMPTY, cell, topRight, availableMoves);
			}
			if (isValidSingleMove(cell, bottomLeft)) {
				addSingleMove(MoveType.EMPTY, cell, bottomLeft, availableMoves);
			}
			if (isValidSingleMove(cell, bottomRight)) {
				addSingleMove(MoveType.EMPTY, cell, bottomRight, availableMoves);
			}
		}
		if (cell.getPlayerType() == PlayerType.BLACK) {
			if (isValidSingleMove(cell, bottomLeft)) {
				addSingleMove(MoveType.EMPTY, cell, bottomLeft, availableMoves);
			}
			if (isValidSingleMove(cell, bottomRight)) {
				addSingleMove(MoveType.EMPTY, cell, bottomRight, availableMoves);
			}
		}
		return availableMoves;
	}

	public static boolean isValidSingleMove(final Cell startingCell, final Cell destinationCell) {
		return isNotNull(destinationCell) && destinationCell.getPlayerType() == PlayerType.NONE
				&& !(destinationCell.getPlayerType() == startingCell.getPlayerType());
	}

	public static void addSingleMove(MoveType moveType, Cell startingCell, Cell destinationCell,
			ArrayList<Move> availableMoves) {
		availableMoves.add(getMove(startingCell.getPlayerType(), moveType, Arrays.asList(startingCell, destinationCell),
				startingCell, destinationCell, 0, 0, null));
	}

}
