package com.usc.ai;

/**
 * @author UtkarshBhardwaj
 */
public class MoveToPlay {
	public double heuristic;
	public Move move;

	public MoveToPlay() {
		this.heuristic = 0;
	}

	public MoveToPlay(Move move, double heuristic) {
		this.move = move;
		this.heuristic = heuristic;
	}

	public void setMove(Move move) {
		this.move = move;
	}

	public double getHeuristic() {
		return this.heuristic;
	}

	public Move getMove() {
		return this.move;
	}

	public void setHeuristic(double heuristic) {
		this.heuristic = heuristic;
	}

}
