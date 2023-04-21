package representetion;


public class Chessboard {
	
	private int[] chessboard;
	public static final int SIDE_LENGTH = 8;
		
	//int per indicare il numero di pedine bianche e nere correntemente in gioco
	private int whitePawnsNumber;
	private int blackPawnsNumber;
	
	public Chessboard () {
		setBoard();
	}
	
	public Chessboard(int[] board) {
		chessboard = new int[64];
		System.arraycopy(board, 0, chessboard, 0, chessboard.length);
	}
	
	private void setBoard() {
		//inizialmente ogni giocatore ha 12 pedine
		whitePawnsNumber=12;
		blackPawnsNumber=12;
		
		//impostazione della scacchiera iniziale. 
		//Le celle nere hanno un valore 20, le bianche, sulle quali non si può posizionare la pedina, rimangono a 0.
		chessboard = new int[SIDE_LENGTH*SIDE_LENGTH];
		int i,j, index;
		for (i=0; i<SIDE_LENGTH; i++) {
			for (j=0; j<SIDE_LENGTH; j++) {
				if ((i%2==0 && j%2==0) || (i%2!=0 && j%2!=0)) {
					index = i*SIDE_LENGTH+j;
					chessboard[index] = 20;
				}
			}
		}
		
		//le pedine bianche hanno SEMPRE valore positivo. Inizialmente sono posizionate in H5
		//le pedine nere hanno SEMPRE valore negativo. Inizialmente sono posizionate in A4
		chessboard[4]=12;
		chessboard[59]=-12;
	}
	
	public int[] getChessboard() {
		return this.chessboard;
	}
	
	public void setChessboard(int[] chessboard) {
		this.chessboard=chessboard;
	}
	
	public int getWhitePawnsNumber() {
		return this.whitePawnsNumber;
	}
	
	public void setWhitePawnsNumber(int whitePawnsNumber) {
		this.whitePawnsNumber=whitePawnsNumber;
	}
	
	public int getBlackPawnsNumber() {
		return this.blackPawnsNumber;
	}
	
	public void setBlackPawnsNumber(int blackPawnsNumber) {
		this.blackPawnsNumber=blackPawnsNumber;
	}
	
	public Chessboard boardCopy() {
		Chessboard board = new Chessboard();
		System.arraycopy(this.chessboard, 0, board.chessboard, 0, board.chessboard.length);
		board.setBlackPawnsNumber(this.blackPawnsNumber);
		board.setWhitePawnsNumber(this.whitePawnsNumber);
		return board;
	}
	
}




