package com.usc.ai;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.OffsetTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Controller extends PlayImpl {

	private static final String INPUT_FILE = "input.txt";
	private static final String OUTPUT_FILE = "output.txt";

	private static final String BLANK_SPACE = " ";
	private static final String NEW_LINE = "\n";
	private static final String NO_OUTPUT = "";

	protected void start() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE));
			Input input = validateAndExtractInput(br);
			setTime(input);
			Checker board = new Checker(input.getChecker().getBoard());
			Move bestMove = makeMove(board, input.getMaxDepth(), input.getPlayerType());

			if (null != bestMove) {
				generateOutput(generateOutputMoves(bestMove));
			} else {
				generateOutput(null);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Input validateAndExtractInput(final BufferedReader br) {
		try {
			Input input = new Input();
			input.setGameType(br.readLine());
			input.setPlayerType(br.readLine());
			input.setTimeRemainingInSeconds(br.readLine());
			input.setChecker(getBoardConfig(br));
			int depthForGame = input.getTimeRemainingInSeconds() > 100 ? 3
					: input.getTimeRemainingInSeconds() > 50 ? 2 : 1;
			input.setMaxDepth(input.getGameType() == GameType.SINGLE ? 3 : depthForGame);
			return input;
		} catch (IOException e) {
			return null;
		}
	}

	private Cell[][] getBoardConfig(final BufferedReader br) throws IOException {
		Cell[][] tempBoard = new Cell[8][8];
		String line;
		int y = 0;
		while ((line = br.readLine()) != null) {
			if (y < 8) {
				final ArrayList<String> row = new ArrayList<>();
				final String[] input = line.split("");
				int x = 0;
				for (String z : input) {
					if (z != null && z.length() != 0 && !z.equals(BLANK_SPACE) && x < 8) {
						PlayerType player = PlayerType.getPlayerByValue(z.trim());
						Cell newCell = new Cell(y, x, player);
						tempBoard[y][x] = newCell;
						x++;
					}
				}
				y++;
			}
		}
		return tempBoard;
	}

	private ArrayList<Output> generateOutputMoves(final Move optimalMove) {
		if (null == optimalMove) {
			return null;
		}
		ArrayList<Output> outputMoves = new ArrayList<>();
		if (optimalMove.getMoveType().equals(MoveType.JUMP)) {
			for (int i = 1; i < optimalMove.getPath().size(); i++) {
				List<Cell> path = Arrays.asList(optimalMove.getPath().get(i - 1), optimalMove.getPath().get(i));
				outputMoves.add(new Output(optimalMove.getMoveType(), path.get(0), path.get(1)));
			}
		} else {
			outputMoves.add(new Output(optimalMove.getMoveType(), optimalMove.getStartingCell(),
					optimalMove.getDestinationCell()));
		}
		return outputMoves;
	}

	private void generateOutput(final ArrayList<Output> optimalMoves) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(OUTPUT_FILE, false);
			if (null != optimalMoves && optimalMoves.size() != 0) {
				StringBuilder output = new StringBuilder();
				for (Output move : optimalMoves) {
					if (move.isOutputNotNull()) {
						output.append(move.getMoveType().getMoveCode()).append(BLANK_SPACE)
								.append(getMove(move.getStartingCell()).append(BLANK_SPACE)
										.append(getMove(move.getDestinationCell())).append(NEW_LINE));
					}
				}
				System.out.println(output.toString());
				fw.write(output.substring(0, output.toString().length() - 1));
			} else {
				fw.write(NO_OUTPUT);
			}
		} catch (IOException e) {
			try {
				System.out.println("IOException occurred");
				fw.write(NO_OUTPUT);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} finally {
			try {
				fw.close();
			} catch (Exception ex) {
				System.out.println("Exception in closing fw");
			}
		}
	}

	private StringBuilder getMove(Cell cell) {
		HashMap<Integer, String> mapColumns = new HashMap<Integer, String>();
		HashMap<Integer, Integer> mapRows = new HashMap<Integer, Integer>();
		mapColumns.put(0, "a");
		mapColumns.put(1, "b");
		mapColumns.put(2, "c");
		mapColumns.put(3, "d");
		mapColumns.put(4, "e");
		mapColumns.put(5, "f");
		mapColumns.put(6, "g");
		mapColumns.put(7, "h");
		mapRows.put(0, 8);
		mapRows.put(1, 7);
		mapRows.put(2, 6);
		mapRows.put(3, 5);
		mapRows.put(4, 4);
		mapRows.put(5, 3);
		mapRows.put(6, 2);
		mapRows.put(7, 1);
		String formatCell = null;
		StringBuilder outputText = new StringBuilder();
		formatCell = mapColumns.get(cell.getCol()) + mapRows.get(cell.getRow());
		outputText.append(formatCell);
		return outputText;
	}

}
