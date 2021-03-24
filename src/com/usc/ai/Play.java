package com.usc.ai;

import java.util.ArrayList;

public interface Play {

	ArrayList<Cell> getAllPlayerPositionByColor(Checker board, String color);

	ArrayList<Move> getAllPossibleMove(Checker board, ArrayList<Cell> allPlayers);

}
