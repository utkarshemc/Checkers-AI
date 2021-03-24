/**
 * @author UtkarshBhardwaj
 */
public class Jump {
    public final Cell parent;
    public final Cell current;
    int piecesJumped;
    int kingsjumped;


    public Jump(final Cell parent, final Cell current) {
        this.parent = parent;
        this.current = current;
    }


    public Cell getParent() {
        return this.parent;
    }


    public Cell getCurrent() {
        return this.current;
    }


	public int getPiecesJumped() {
		return piecesJumped;
	}


	public void setPiecesJumped(int piecesJumped) {
		this.piecesJumped = piecesJumped;
	}


	public int getKingsjumped() {
		return kingsjumped;
	}


	public void setKingsjumped(int kingsjumped) {
		this.kingsjumped = kingsjumped;
	}
    
}
