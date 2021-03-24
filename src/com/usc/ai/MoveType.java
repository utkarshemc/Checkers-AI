package com.usc.ai;

/**
 * @author UtkarshBhardwaj
 */
public enum MoveType {
	JUMP("J"), EMPTY("E");

	public final String move;

	MoveType(String move) {
		this.move = move;
	}

	public String getMoveCode() {
		return this.move;
	}
}
