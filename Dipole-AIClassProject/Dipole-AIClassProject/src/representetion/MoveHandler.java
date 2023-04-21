package representetion;

import java.util.ArrayList;
import java.util.HashMap;

public class MoveHandler {

	/*
	 * L'HashMap contenente tutte le possibili mosse in avanti che le pedine possono fare a "scacchiera vuota". In 
	 * particolare le pedine considerate sono le sole pedine bianche. Per il calcolo delle mosse delle pedine nere si 
	 * considera la stessa hashmap. Le mosse al suo interno infatti vengono calcolate per i neri come se idealmente si 
	 * ruotasse di 180 gradi la scacchiera. Con opportuni metodi e calcoli si identificano poi i valori corretti di 
	 * indici e direzione per le pedine nere così che siano in accordo con la scacchiera classica.
	 * La chiave è una stringa nel seguente formato: cella-numPedine, dove il numPedine sono le pedine che si trovano
	 * nella cella indicata.
	 * Il valore è un arrayList di stringhe che contiene tutte le possibili mosse che quelle pedine in quella cella possono
	 * fare. Le mosse sono codificate con delle stringhe che hanno il seguente formato: i|j|direzione|numPedine.
	 */
	private HashMap<String, ArrayList<String>> allForwardMoves = MoveHandlerUtility.allForwardMoves;
	/*
	 * L'HashMap contenente tutte le possibili mosse di attacco indietro che le pedine possono fare a "scacchiera vuota".
	 * In particolare le pedine considerate sono le sole pedine bianche. Per il calcolo delle mosse delle pedine nere si 
	 * considera la stessa hashmap. Le mosse al suo interno infatti vengono calcolate per i neri come se idealmente si 
	 * ruotasse di 180 gradi la scacchiera. Con opportuni metodi e calcoli si identificano poi i valori corretti di 
	 * indici e direzione per le pedine nere così che siano in accordo con la scacchiera classica.
	 * La chiave è una stringa nel seguente formato: cella-numPedine, dove il numPedine sono le pedine che si trovano
	 * nella cella indicata.
	 * Il valore è un arrayList di stringhe che contiene tutte le possibili mosse che quelle pedine in quella cella possono
	 * fare. Le mosse sono codificate con delle stringhe che hanno il seguente formato: i|j|direzione|numPedine.
	 */
	private HashMap<String, ArrayList<String>> allBackwardAttackMoves = MoveHandlerUtility.allBackwardAttackMoves;

	//intero che rappresenta la chessboard
	private int[] board;
	
	//colore che rappresenta il giocatore che deve eseguire la mossa
	private Color player;
	
	/*
	 * vettore di stringhe di 64 posizioni in cui in ogni posizione è indicato il nome della corrispondente cella nel
	 * vettore indicante la scacchiera
	 */
	private String[] chessboardCells;
	
	
	public MoveHandler() {
		createChessboardCells();
	}
		
	/*
	 * Metodo per ottenere le mosse lecite che il giocatore può fare a partire dalla posizione delle pedine nella scacchiera
	 * corrente. Usa il metodo privato che prende lo stesso nome.
	 */
	public ArrayList<Move> allLecitMoves (int[] board, Color player) {
		this.board=board;
		this.player=player;
		return allLecitMoves();
		
	}
	
	public ArrayList<Move> allCellLecitMoves (int[] board, Color player, int index) {
		this.board = board;
		this.player = player;
		return allCellLecitMoves(index);
	}
	
	/*
	 * Metodo per ottenere le mosse lecite che il giocatore può fare. Viene invocato dopo che sono stati settati la
	 * scacchiera corrente e il giocatore che deve fare la mossa.
	 */
	private ArrayList<Move> allLecitMoves() {
		ArrayList<Move> allLecitMoves = new ArrayList<>();
		ArrayList<String> allPawnMoves;
		String[] partsResult;
		int iDest, jDest;
		int pawnNumber;
		String pawnKey;
		
		for (int i=0; i<board.length; i++) {
			
			//si verifica il caso in cui il giocatore sia bianco e si debbano scegliere le mosse per lui
			if (player==Color.WHITE && board[i]>0 && board[i]!=20) {
				
				//si crea la chiave da usare nell'hashmap
				pawnKey = chessboardCells[i]+"-"+board[i];
				//si prende dall'hashmap il valore corrispondente alla chiave
				allPawnMoves = allForwardMoves.get(pawnKey);
				
				/*
				 * Si scorre allPawnMoves per scegliere tra tutte quelle presenti quali sono le configurazioni corrette
				 * in base a quella corrente.
				 */
				for (String j: allPawnMoves) {
					
					//prelevano le informazioni necessarie dal valore dell'hashmap
					partsResult = j.split("/");
					iDest = Integer.parseInt(partsResult[0]);
					jDest = Integer.parseInt(partsResult[1]);
					pawnNumber = Integer.parseInt(partsResult[3]);
					
					//si verifica se la mossa prelevata sia di tipo OUT_OF_BOARD
					if (iDest<0 || iDest>7 || jDest<0 || jDest>7)
						allLecitMoves.add(new Move(chessboardCells[i], Enum.valueOf(Direction.class, partsResult[2]), pawnNumber, MoveType.OUT_OF_BOARD));
					//si verifica che la mossa sia corretta e in qual caso si crea l'oggetto move corrispondente
					else {
						int destValue = board[iDest*8+jDest];
						if (destValue == 20) 
							allLecitMoves.add(new Move(chessboardCells[i], Enum.valueOf(Direction.class, partsResult[2]), pawnNumber, MoveType.FORWARD_MOVEMENT));				
						else if (destValue>0)
							allLecitMoves.add(new Move(chessboardCells[i], Enum.valueOf(Direction.class, partsResult[2]), pawnNumber, MoveType.MERGE));
						else if (destValue<0 && Math.abs(destValue)<=pawnNumber)
							allLecitMoves.add(new Move(chessboardCells[i], Enum.valueOf(Direction.class, partsResult[2]), pawnNumber, MoveType.FORWARD_ATTACK));
					}
				}
				
				allPawnMoves = allBackwardAttackMoves.get(pawnKey);
				
				/*
				 * Si scorre allPawnMoves per scegliere tra tutte quelle presenti quali sono le configurazioni corrette
				 * in base a quella corrente.
				 */
				for (String j: allPawnMoves) {
					
					//prelevano le informazioni necessarie dal valore dell'hashmap
					if (j!="") {
						partsResult = j.split("/");
						iDest = Integer.parseInt(partsResult[0]);
						jDest = Integer.parseInt(partsResult[1]);
						int destValue = board[iDest*8+jDest];
						pawnNumber = Integer.parseInt(partsResult[3]);
						
						//si verifica che la mossa di attacco indicata da j sia corretta e in caso positivo si crea l'oggetto mossa e lo si aggiunge al risultato
						if (destValue<0 && Math.abs(destValue)<=pawnNumber)
							allLecitMoves.add(new Move(chessboardCells[i], Enum.valueOf(Direction.class, partsResult[2]), pawnNumber, MoveType.BACKWARD_ATTACK));				
					}
				}
						
			}
			
			//si verifica il caso in cui il giocatore sia nero e si debbano scegliere le mosse per lui
			if (player==Color.BLACK && board[i]<0) {
				
				/*
				 * Poichè si considera l'hashmap come ruotata di 180 gradi bisogna trasformare la pedina nera in quella
				 * che sarebbe la corrispondente bianca. Quindi il numero della cella si calcola facendo 63-i e come 
				 * numero di pedina si deve prendere il valore assoluto delle pedine nere.
				 */
				pawnKey = chessboardCells[63-i]+"-"+Math.abs(board[i]);
				//si prende dall'hashmap il valore corrispondente alla chiave
				allPawnMoves = allForwardMoves.get(pawnKey);
				
				/*
				 * Si scorre allPawnMoves per scegliere tra tutte quelle presenti quali sono le configurazioni corrette
				 * in base a quella corrente.
				 */
				for (String j: allPawnMoves) {
					
					//prelevano le informazioni necessarie dal valore dell'hashmap
					partsResult = j.split("/");
					iDest = 7-(Integer.parseInt(partsResult[0]));
					jDest = 7-(Integer.parseInt(partsResult[1]));
					pawnNumber = Integer.parseInt(partsResult[3]);
					
					//si verifica che la mossa prelevata sia di tipo OUT_OF_BOARD
					if (iDest<0 || iDest>7 || jDest<0 || jDest>7)
						allLecitMoves.add(new Move(chessboardCells[i], getRotatedDirection(Enum.valueOf(Direction.class, partsResult[2])), pawnNumber, MoveType.OUT_OF_BOARD));
					//si verifica che la mossa sia corretta e in qual caso si crea l'oggetto move corrispondente
					else {
						int destValue = board[iDest*8+jDest];
						if (destValue == 20) 
							allLecitMoves.add(new Move(chessboardCells[i], getRotatedDirection(Enum.valueOf(Direction.class, partsResult[2])), pawnNumber, MoveType.FORWARD_MOVEMENT));				
						else if (destValue<0)
							allLecitMoves.add(new Move(chessboardCells[i], getRotatedDirection(Enum.valueOf(Direction.class, partsResult[2])), pawnNumber, MoveType.MERGE));				
						else if (destValue>0 && destValue<=pawnNumber)
							allLecitMoves.add(new Move(chessboardCells[i], getRotatedDirection(Enum.valueOf(Direction.class, partsResult[2])), pawnNumber, MoveType.FORWARD_ATTACK));				
					}
				}
				
				allPawnMoves = allBackwardAttackMoves.get(pawnKey);
				
				/*
				 * Si scorre allPawnMoves per scegliere tra tutte quelle presenti quali sono le configurazioni corrette
				 * in base a quella corrente.
				 */
				for (String j: allPawnMoves) {
					
					//prelevano le informazioni necessarie dal valore dell'hashmap
					if (j!="") {
						partsResult = j.split("/");
						iDest = 7-(Integer.parseInt(partsResult[0]));
						jDest = 7-(Integer.parseInt(partsResult[1]));
						int destValue = board[iDest*8+jDest];
						pawnNumber = Integer.parseInt(partsResult[3]);
						
						//si verifica che la mossa di attacco indicata da j sia corretta e in caso positivo si crea l'oggetto mossa e lo si aggiunge al risultato
						if (destValue>0 && destValue<=pawnNumber)
							allLecitMoves.add(new Move(chessboardCells[i], getRotatedDirection(Enum.valueOf(Direction.class, partsResult[2])), pawnNumber, MoveType.BACKWARD_ATTACK));				
					}
				}	
			}
		}
		
		return allLecitMoves;
	}
	
	private ArrayList<Move> allCellLecitMoves(int index) {
		ArrayList<Move> allLecitMoves = new ArrayList<>();
		ArrayList<String> allPawnMoves;
		String[] partsResult;
		int iDest, jDest;
		int pawnNumber;
		String pawnKey;
			
		//si verifica il caso in cui il giocatore sia bianco e si debbano scegliere le mosse per lui
		if (player==Color.WHITE && board[index]>0  && board[index]!=20) {
			
			//si crea la chiave da usare nell'hashmap
			pawnKey = chessboardCells[index]+"-"+board[index];
			//si prende dall'hashmap il valore corrispondente alla chiave
			allPawnMoves = allForwardMoves.get(pawnKey);
			
			/*
			 * Si scorre allPawnMoves per scegliere tra tutte quelle presenti quali sono le configurazioni corrette
			 * in base a quella corrente.
			 */
			for (String j: allPawnMoves) {
				
				//prelevano le informazioni necessarie dal valore dell'hashmap
				partsResult = j.split("/");
				iDest = Integer.parseInt(partsResult[0]);
				jDest = Integer.parseInt(partsResult[1]);
				pawnNumber = Integer.parseInt(partsResult[3]);
				
				//si verifica che la mossa prelevata sia di tipo OUT_OF_BOARD
				if (iDest<0 || iDest>7 || jDest<0 || jDest>7)
					allLecitMoves.add(new Move(chessboardCells[index], Enum.valueOf(Direction.class, partsResult[2]), pawnNumber, MoveType.OUT_OF_BOARD));
				//si verifica che la mossa sia corretta e in qual caso si crea l'oggetto move corrispondente
				else {
					int destValue = board[iDest*8+jDest];
					if (destValue == 20) 
						allLecitMoves.add(new Move(chessboardCells[index], Enum.valueOf(Direction.class, partsResult[2]), pawnNumber, MoveType.FORWARD_MOVEMENT));				
					else if (destValue>0)
						allLecitMoves.add(new Move(chessboardCells[index], Enum.valueOf(Direction.class, partsResult[2]), pawnNumber, MoveType.MERGE));
					else if (destValue<0 && Math.abs(destValue)<=pawnNumber)
						allLecitMoves.add(new Move(chessboardCells[index], Enum.valueOf(Direction.class, partsResult[2]), pawnNumber, MoveType.FORWARD_ATTACK));
				}
			}
			
			allPawnMoves = allBackwardAttackMoves.get(pawnKey);
			
			/*
			 * Si scorre allPawnMoves per scegliere tra tutte quelle presenti quali sono le configurazioni corrette
			 * in base a quella corrente.
			 */
			for (String j: allPawnMoves) {
				
				//prelevano le informazioni necessarie dal valore dell'hashmap
				if (j!="") {
					partsResult = j.split("/");
					iDest = Integer.parseInt(partsResult[0]);
					jDest = Integer.parseInt(partsResult[1]);
					int destValue = board[iDest*8+jDest];
					pawnNumber = Integer.parseInt(partsResult[3]);
					
					//si verifica che la mossa di attacco indicata da j sia corretta e in caso positivo si crea l'oggetto mossa e lo si aggiunge al risultato
					if (destValue<0 && Math.abs(destValue)<=pawnNumber)
						allLecitMoves.add(new Move(chessboardCells[index], Enum.valueOf(Direction.class, partsResult[2]), pawnNumber, MoveType.BACKWARD_ATTACK));				
				}
			}
		}
		
		//si verifica il caso in cui il giocatore sia nero e si debbano scegliere le mosse per lui
		if (player==Color.BLACK && board[index]<0) {
			
			/*
			 * Poichè si considera l'hashmap come ruotata di 180 gradi bisogna trasformare la pedina nera in quella
			 * che sarebbe la corrispondente bianca. Quindi il numero della cella si calcola facendo 63-i e come 
			 * numero di pedina si deve prendere il valore assoluto delle pedine nere.
			 */
			pawnKey = chessboardCells[63-index]+"-"+Math.abs(board[index]);
			//si prende dall'hashmap il valore corrispondente alla chiave
			allPawnMoves = allForwardMoves.get(pawnKey);
			
			/*
			 * Si scorre allPawnMoves per scegliere tra tutte quelle presenti quali sono le configurazioni corrette
			 * in base a quella corrente.
			 */
			for (String j: allPawnMoves) {
				
				//prelevano le informazioni necessarie dal valore dell'hashmap
				partsResult = j.split("/");
				iDest = 7-(Integer.parseInt(partsResult[0]));
				jDest = 7-(Integer.parseInt(partsResult[1]));
				pawnNumber = Integer.parseInt(partsResult[3]);
				
				//si verifica che la mossa prelevata sia di tipo OUT_OF_BOARD
				if (iDest<0 || iDest>7 || jDest<0 || jDest>7)
					allLecitMoves.add(new Move(chessboardCells[index], getRotatedDirection(Enum.valueOf(Direction.class, partsResult[2])), pawnNumber, MoveType.OUT_OF_BOARD));
				//si verifica che la mossa sia corretta e in qual caso si crea l'oggetto move corrispondente
				else {
					int destValue = board[iDest*8+jDest];
					if (destValue == 20) 
						allLecitMoves.add(new Move(chessboardCells[index], getRotatedDirection(Enum.valueOf(Direction.class, partsResult[2])), pawnNumber, MoveType.FORWARD_MOVEMENT));				
					else if (destValue<0)
						allLecitMoves.add(new Move(chessboardCells[index], getRotatedDirection(Enum.valueOf(Direction.class, partsResult[2])), pawnNumber, MoveType.MERGE));				
					else if (destValue>0 && destValue<=pawnNumber)
						allLecitMoves.add(new Move(chessboardCells[index], getRotatedDirection(Enum.valueOf(Direction.class, partsResult[2])), pawnNumber, MoveType.FORWARD_ATTACK));				
				}
			}
			
			allPawnMoves = allBackwardAttackMoves.get(pawnKey);
			
			/*
			 * Si scorre allPawnMoves per scegliere tra tutte quelle presenti quali sono le configurazioni corrette
			 * in base a quella corrente.
			 */
			for (String j: allPawnMoves) {
				
				//prelevano le informazioni necessarie dal valore dell'hashmap
				if (j!="") {
					partsResult = j.split("/");
					iDest = 7-(Integer.parseInt(partsResult[0]));
					jDest = 7-(Integer.parseInt(partsResult[1]));
					int destValue = board[iDest*8+jDest];
					pawnNumber = Integer.parseInt(partsResult[3]);
					
					//si verifica che la mossa di attacco indicata da j sia corretta e in caso positivo si crea l'oggetto mossa e lo si aggiunge al risultato
					if (destValue>0 && destValue<=pawnNumber)
						allLecitMoves.add(new Move(chessboardCells[index], getRotatedDirection(Enum.valueOf(Direction.class, partsResult[2])), pawnNumber, MoveType.BACKWARD_ATTACK));				
				}
			}
		}
		
		return allLecitMoves;
	}
	
	private Direction getRotatedDirection (Direction d) {
		switch (d) {
			case N: return Direction.S;
			case S: return Direction.N;
			case E: return Direction.W;
			case W: return Direction.E;
			case NE: return Direction.SW;
			case NW: return Direction.SE;
			case SE: return Direction.NW;
			default: return Direction.NE; //case SW
		}
	}
	
	/*
	 * Crea un vettore di stringhe di 64 posizioni che rappresenta la scacchiera indicando i nomi di ogni cella.
	 */
	private void createChessboardCells () {
		this.chessboardCells = new String[64];
		int indexTmp, index, indexCell;
		for (int i=0; i<8; i++) {
			indexTmp = i*8;
			for (int j=0; j<8; j++) {
				index = indexTmp+j;
				indexCell = j+1;
				switch (i) {
					case 0: chessboardCells[index] = "H"+indexCell; break;
					case 1: chessboardCells[index] = "G"+indexCell; break;
					case 2: chessboardCells[index] = "F"+indexCell; break;
					case 3: chessboardCells[index] = "E"+indexCell; break;
					case 4: chessboardCells[index] = "D"+indexCell; break;
					case 5: chessboardCells[index] = "C"+indexCell; break;
					case 6: chessboardCells[index] = "B"+indexCell; break;
					default: chessboardCells[index] = "A"+indexCell; break;
				}	
			}
		}
	}
	
	/*public Chessboard applyMove(Chessboard currentBoard, Move m) {
		int[] board = currentBoard.getChessboard();
		int whitePawns = currentBoard.getWhitePawnsNumber();
		int blackPawns = currentBoard.getBlackPawnsNumber();
		
		int sourceIndex = m.getSourceIndex();
		int destIndex = m.getDestinationIndex();
		int pawnsNumber = m.getPawnsNumber();
		boolean outofboard = m.isOutOfBoard();
		
		int sourcePawns = board[sourceIndex];
		
		if (sourcePawns>0) { //sta giocando il bianco
			//poichè le pedine si stanno spostando il valore di board nel sourceIndex deve essere decrementato del numero di pedine che si stanno spostando
			board[sourceIndex]-=pawnsNumber;
			
			if (outofboard) 
				whitePawns -= pawnsNumber;
			else { 
				int destPawns = board[destIndex];
				if (destPawns<0 && Math.abs(destPawns)<=pawnsNumber) { //attack
					blackPawns -= Math.abs(destPawns);
					board[destIndex] = pawnsNumber;
				}
				else if (destPawns>0 && destPawns!=20) //merge
					board[destIndex] += pawnsNumber;
				else if (destPawns==20) //forward_movement
					board[destIndex] = pawnsNumber;
			}
				
						
		}
		else { //sta giocando il nero
			/*
			 * Poichè si è nel caso del giocatore nero le pedine saranno negative e se queste si stanno spostando si deve
			 * aumentare il numero che si trova nella cella.
			 */
			/*board[sourceIndex]+=pawnsNumber;
			
			if (outofboard) 
				blackPawns -= pawnsNumber;
			else { 
				int destPawns = board[destIndex];
				if (destPawns>0 && destPawns<=pawnsNumber) { //attack
					whitePawns -= destPawns;
					board[destIndex] = pawnsNumber*(-1);
				}
				else if (destPawns<0) //merge
					board[destIndex] -= pawnsNumber;
				else if (destPawns==20) //forward_movement
					board[destIndex] = pawnsNumber*(-1);
			}
			
		}
		
		
		//se si sono spostate tutte le pedine allora il valore sarà 0, ma se così è allora bisogna impostarlo a 20 perchè è una casella nera.
		if (board[sourceIndex]==0)
			board[sourceIndex]=20;
		
		Chessboard c = new Chessboard(board);
		c.setBlackPawnsNumber(blackPawns);
		c.setWhitePawnsNumber(whitePawns);
		currentBoard.setBlackPawnsNumber(blackPawns);
		currentBoard.setWhitePawnsNumber(whitePawns);
		return c;
	}*/
	
	public static void applyMove(Chessboard currentBoard, Move m) {
		int[] board = currentBoard.getChessboard();
		int whitePawns = currentBoard.getWhitePawnsNumber();
		int blackPawns = currentBoard.getBlackPawnsNumber();
		
		int sourceIndex = m.getSourceIndex();
		int destIndex = m.getDestinationIndex();
		int pawnsNumber = m.getPawnsNumber();
		boolean outofboard = m.isOutOfBoard();
		
		int sourcePawns = board[sourceIndex];
		
		if (sourcePawns>0) { //sta giocando il bianco
			//poichè le pedine si stanno spostando il valore di board nel sourceIndex deve essere decrementato del numero di pedine che si stanno spostando
			board[sourceIndex]-=pawnsNumber;
			
			if (outofboard) 
				whitePawns -= pawnsNumber;
			else { 
				int destPawns = board[destIndex];
				if (destPawns<0 && Math.abs(destPawns)<=pawnsNumber) { //attack
					blackPawns -= Math.abs(destPawns);
					board[destIndex] = pawnsNumber;
				}
				else if (destPawns>0 && destPawns!=20) //merge
					board[destIndex] += pawnsNumber;
				else if (destPawns==20) //forward_movement
					board[destIndex] = pawnsNumber;
			}
				
						
		}
		else { //sta giocando il nero
			/*
			 * Poichè si è nel caso del giocatore nero le pedine saranno negative e se queste si stanno spostando si deve
			 * aumentare il numero che si trova nella cella.
			 */
			board[sourceIndex]+=pawnsNumber;
			
			if (outofboard) 
				blackPawns -= pawnsNumber;
			else { 
				int destPawns = board[destIndex];
				if (destPawns>0&& destPawns<=pawnsNumber) { //attack
					whitePawns -= destPawns;
					board[destIndex] = pawnsNumber*(-1);
				}
				else if (destPawns<0) //merge
					board[destIndex] -= pawnsNumber;
				else if (destPawns==20) //forward_movement
					board[destIndex] = pawnsNumber*(-1);
			}
			
		}
		
		
		//se si sono spostate tutte le pedine allora il valore sarà 0, ma se così è allora bisogna impostarlo a 20 perchè è una casella nera.
		if (board[sourceIndex]==0)
			board[sourceIndex]=20;
		
		
		currentBoard.setBlackPawnsNumber(blackPawns);
		currentBoard.setWhitePawnsNumber(whitePawns);
		
	}
	public  Move jumpTurn(Chessboard b,Color player) {
		Move m=null;
		String s;
		
		for(int i=0;i<64;i++) {
			if(b.getChessboard()[i]<0&&player==Color.BLACK) {
				s=chessboardCells[i];
				m=new Move(s,Direction.N,0);
				break;
				
			}
			else if(b.getChessboard()[i]>0&&b.getChessboard()[i]!=20&&player==Color.WHITE) {
				s=chessboardCells[i];
				m=new Move(s,Direction.N,0);
				break;
				
			}
			
		}
		return m;
	}
	
	
	
}