package representetion;
/*
 * Classe rappresentante la direzione della mossa
 */
public enum Direction {
	
	//direzioni possibili
	N, NE, E, SE, S, SW, W, NW;
	
	//la prima riga indica la prima riga in basso della scacchiera
	//probabilmente non serve
	/*
	private enum enumSquare {
		H1, H2, H3, H4, H5, H6, H7, H8,
		G1, G2, G3, G4, G5, G6, G7, G8,
		F1, F2, F3, F4, F5, F6, F7, F8,
		E1, E2, E3, E4, E5, E6, E7, E8,
		D1, D2, D3, D4, D5, D6, D7, D8,
		C1, C2, C3, C4, C5, C6, C7, C8,
		B1, B2, B3, B4, B5, B6, B7, B8,
		A1, A2, A3, A4, A5, A6, A7, A8
	};
	*/
	
	/*
	 * Il metodo trasforma la direzione in un valore numerico per il calcolo degli indici nella chessboard. La traduzione
	 * numerica segue la seguente rosa:
	 * 		NW	         N			NE
		          +7    +8    +9
		              \  |  /
		  	W	  -1 <-  0 -> +1 	E
		              /  |  \
		          -9    -8    -7
		  	SW	         S     		SE
	 */
	public int getMovementNumber(Direction d) {
		switch(d) {
			case N: return 8;
			case NE: return 9;
			case E: return 1;
			case SE: return -7;
			case S: return -8;
			case SW: return -9;
			case W: return -1;
			case NW: return 7;
			default: return 0;
		}
	}
}
