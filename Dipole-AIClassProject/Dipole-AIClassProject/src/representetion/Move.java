package representetion;

/*
 * Classe rappresentante l'entità mossa.
 */
public class Move {
	
	private Direction dir;
	private int destinationIndex;
	private int i,j;
	
	private String source;
	private int sourceIndex;
	
	private int pawnsNumber;
	
	private MoveType type;
	
	private static int SIDE_LENGTH = 8;
	
	public Move(String source, Direction dir, int pawnsNumber) {
		this.source=source;
		this.dir=dir;
		this.pawnsNumber=pawnsNumber;
		setSourceIndex();
		setDestinationIndex();
		this.type = MoveType.UNDEFINED;
	}
	
	public Move(String source, Direction dir, int pawnsNumber, MoveType type) {
		this.source=source;
		this.dir=dir;
		this.pawnsNumber=pawnsNumber;
		setSourceIndex();
		setDestinationIndex();
		this.type = type;
	}
	
	/*
	 * Il metodo setSourceIndex() serve a impostare l'indice iniziale della mossa. In particolare questo è fornito dal server, 
	 * dato informazioni sulla cella di partenza. Tale cella è identificata da una stringa unica che riporta come primo
	 * elemento la riga (un carattere da A ad H) e come secondo elemento la colonna (un intero da 1 a 8). Per quanto 
	 * riguarda il carattere questo viene poi trasformato in un indice di riga da un metodo apposito, mentre per quanto
	 * riguarda i
	 * l numero questo viene prelevato dalla stringa, castizzato ad intero e decrementato di 1 perchè il
	 * range delle colonne nel vettore è [0,7].
	 */
	private void setSourceIndex() {
		char c = this.source.charAt(0);
		this.j = (Character.getNumericValue(this.source.charAt(1)))-1;
		this.i = computeRow(c);
		this.sourceIndex = this.i*SIDE_LENGTH+this.j;
		setIJ();
	}
	
	/*
	 * In base al valore del carattere c ricevuto come parametro determina il numero di riga corrispondente.
	 */
	private int computeRow(char c) {
		switch(c) {
			case 'H': return 0;
			case 'G': return 1;
			case 'F': return 2;
			case 'E': return 3;
			case 'D': return 4;
			case 'C': return 5;
			case 'B': return 6;
			case 'A': return 7;
			default: return -1;
		}
	}
	
	/*
	 * Il metodo imposta l'indice di destinazione della mossa.
	 */
	private void setDestinationIndex() {
		int tmp=dir.getMovementNumber(dir);
		this.destinationIndex=this.sourceIndex+(tmp*this.pawnsNumber);
	}
	
	public int getSourceIndex() {
		return this.sourceIndex;
	}
	
	public int getDestinationIndex() {
		return this.destinationIndex;
	}
	
	public int getPawnsNumber() {
		return this.pawnsNumber;
	}
	
	public MoveType getType() {
		return this.type;
	}
	
	public void setType(MoveType type) {
		this.type = type;
	}
	
	/*
	 * Serve per verificare se la mossa è una di tipo OUT_OF_BOARD, quando questo non è stato prima specificato.
	 */
	public boolean isOutOfBoard() {
		if (type==MoveType.OUT_OF_BOARD)
			return true;
		//setIJ();
		if (this.i>7 || this.j>7 || this.i<0 || this.j<0) {
			this.type = MoveType.OUT_OF_BOARD;
			return true;
		}
		return false;
	}
	
	/*
	 * Metodo privato invocato all'interno di isOutOfBoard. Serve a calcolare gli indici i, j di una generica matrice
	 * rappresentante la scacchiera. i e j sono gli indici di destinazione della mossa di oggetto.
	 */
	private void setIJ() {
		switch (this.dir) {
			case N: this.i+=this.pawnsNumber; break;
			case S: this.i-=this.pawnsNumber; break;
			case E: this.j+=this.pawnsNumber; break;
			case W: this.j-=this.pawnsNumber; break;
			case NE: this.i+=this.pawnsNumber; this.j+=this.pawnsNumber; break;
			case SE: this.i-=this.pawnsNumber; this.j+=this.pawnsNumber; break;
			case SW: this.i-=this.pawnsNumber; this.j-=this.pawnsNumber; break;
			default: this.i+=this.pawnsNumber; this.j-=this.pawnsNumber; break;		
		}
	}
	
	/*
	 * Metodo che restituisce la stringa da inviare al server, nel formato MOVE <start>,<dir>,<num>.
	 */
	public String moveToString() {
		String result = "MOVE"+" "+source+","+dir.toString()+","+pawnsNumber;
		return result;
	}
}
