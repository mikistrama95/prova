package representetion;

public class Player {
	
	private int pawnsNumber;
	private Color color;
	
	public Player(Color c) {
		this.color = c;
		this.pawnsNumber = 12;
	}
	public int getPawnsNumber() {
		return this.pawnsNumber;
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public void setPawnsNumber(int x) {
		this.pawnsNumber=x;
	}
	
	public void setColor(Color c) {
		this.color=c;
	}
	
	public Color getOppositeColor() {
		Color result = Color.WHITE;
		if (this.getColor()==Color.WHITE)
			result = Color.BLACK;
		return result;
	}
	
}
