package logic;

import java.util.ArrayList;


import representetion.*;


public class SearchAlgoritmDipole2{
	
	//la mossa che restituisco man mano come risposta;
	private Color player;
    protected Move nextMove;
    private int nextMoveIndex;
	private static final double MAX = 1000000;
    private static final double MIN = -1000000;
	private MoveHandler moveHandler;
	public Chessboard realboard;
	private int maxMoves;
	private boolean drawn,maxwin,minwin;
	int size=0;
	float numeropedine,nsize,noCalDist,backAttack,forAttack,outBoard,merge,forwardMovement,pippo;
	int elevato,div;
	
	public SearchAlgoritmDipole2(float numeropedine,float nsize,float noCalDist,float backAttack,float forAttack,float outBoard,float merge,float pippo, float forwardMovement,int elevato,int div){
		moveHandler=new MoveHandler();
		realboard= new Chessboard();
		this.backAttack=backAttack;
		
		this.forAttack=forAttack;
		this.pippo=pippo;
		this.forwardMovement=forwardMovement;
		
		this.merge=merge;
		this.noCalDist=noCalDist;
		this.nsize=nsize;
		this.numeropedine=numeropedine;
		this.outBoard=outBoard;
		
		this.elevato=elevato;
		this.div=div;
		
		// TODO Auto-generated constructor stub
	}
	public void setColor(Color player) {
		this.player=player;
	}
	public void azzera() {
		realboard=new Chessboard();
	}

	
	public boolean drawnChessboard(Chessboard board) {
		if(maxMoves==120&&board.getBlackPawnsNumber()==board.getWhitePawnsNumber())return true;
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean wonChessboard(Chessboard board, boolean maximizing) {
		// TODO Auto-generated method stub
		//aggiungere i casi in cui il contatore finisce e uno ha piu pedin edell altro
		if(maxMoves==120&&board.getBlackPawnsNumber()>board.getWhitePawnsNumber()&&!maximizing)return true;
		if(maxMoves==120&&board.getBlackPawnsNumber()==board.getWhitePawnsNumber()&&!maximizing)return true;
		if(maxMoves==120&&board.getBlackPawnsNumber()<board.getWhitePawnsNumber()&&maximizing)return true;
		if(board.getBlackPawnsNumber()==0&&maximizing)return true;
		if(board.getWhitePawnsNumber()==0&&!maximizing)return true;
		return false;
	}
	
	
	public double ChessboardEvaluation(Chessboard board,boolean maximizing) {// anche se è sbagliato si sta ssumendo per ora
		// TODO Auto-generated method stub                          //che il colore 0=white=maximizer e l'opposto 
		double score=0;                                              //per colore 1,probabilmente conviene invertire
		int[] chessboard=board.getChessboard();
		ArrayList<Move> allLecitMoves;
		
		int i;
		int dist;
		int resto;
		int sourceIndex;
		//color solo con max e min
		// IMPORTANTE:si sta facendo caso in cui la rappresentazione di chessboard è fatta con un array monodimensionale
		
		/* CONDIZIONE DI NUMERO DI PEDINE*/
		
		int number_white=board.getWhitePawnsNumber();
		int number_black=board.getBlackPawnsNumber();
		
		
	    if(maxwin) {
	    	score+=MAX;
	        return score;
	    }
		
        
		if(minwin) {        
		score+=MIN;  
		return score ;   
		}                  
           
		        
		if(drawn) {        
		score=0;  
		return score ;   
		}                   
        
	    
		score+=number_white*numeropedine-(number_black*numeropedine);//score per il numero di pedine

		
		
		//qua devo scorrermi tutte le lecitMoves e aumentare lo score a seconda del tipo di mossa
		//il problema è che ogni volta che richiamo alllectimoves all'interno fa un altro while
		//chiedere ad ale se può fare un metodo per estituire tutte le lecitmoves a seconda dell'indice che invio al metodoCe
		i=0;
		//boolean s;
		while(i<Chessboard.SIDE_LENGTH*Chessboard.SIDE_LENGTH) {
			//s=false;
			if(chessboard[i] !=20 && chessboard[i] !=0) {
				sourceIndex=i;
				resto=sourceIndex%8;
				dist=(sourceIndex-resto)/8;
				
				//controllo la distanza delle pedine alla fine della scacchiera
				if(chessboard[sourceIndex]<0) {
					size=0;
					score-=(chessboard[sourceIndex]^elevato)/div;
					allLecitMoves=moveHandler.allCellLecitMoves(chessboard, Color.BLACK,i);
					size=allLecitMoves.size();
					//if(!maximizing)s=true;
					score+=scoreFromEating(board,sourceIndex,allLecitMoves,maximizing);
					score-=size*nsize;//mosse da quella posizione
					score-=dist*noCalDist;//distanza dalla fine della scacchiera
					/*if(dist>=4) {
						score-=dist*pippo;
						
					}*/
				}
				if(chessboard[sourceIndex]>0) {
					size=0;
					score+=(chessboard[sourceIndex]^elevato)/div;
					allLecitMoves=moveHandler.allCellLecitMoves(chessboard, Color.WHITE,i);
					size=allLecitMoves.size();
					//if(maximizing)s=true;
					score+=scoreFromEating(board,sourceIndex,allLecitMoves,maximizing);
					score+=size*nsize;
					score+=(7-dist)*noCalDist;//distanza dalla fine della scacchiera
					/*if(dist<4) {
						score+=(7-dist)*pippo;
						
					}*/
				}
			}i++;
		}
		
		return score;
	}

	
	public void printChessboard(Chessboard board) {
		// TODO Auto-generated method stub
		
	}
	
	

	private double scoreFromEating(Chessboard board, int sourceIndex, ArrayList<Move> allLecitMoves,boolean s) {
		double score=0;
	    
		int[] chessboard=board.getChessboard(); 
		for(int j=0; j<allLecitMoves.size();j++) {
			Move m= allLecitMoves.get(j);		
			int dIndex=m.getDestinationIndex();
			int distance=m.getPawnsNumber();
			if(m.getType()==MoveType.BACKWARD_ATTACK ) {
				
				    score+=chessboard[dIndex]*-this.backAttack;
					
				//score+=scoreFromCounterEating(board,dIndex, distance,sourceIndex);
 			}
			if(m.getType()==MoveType.FORWARD_ATTACK) {
				
				    score+=chessboard[dIndex]*-this.forAttack;
				//score+=scoreFromCounterEating(board,dIndex, distance,sourceIndex);
			}
			
			/*if(m.getType()==MoveType.SIDE_ATTACK) {
				
			    score+=chessboard[dIndex]*-this.sideAttack;
			//score+=scoreFromCounterEating(board,dIndex, distance,sourceIndex);
			}*/
			
			if(m.getType()==MoveType.OUT_OF_BOARD) {
				size--;
				if(chessboard[sourceIndex]<0) {
					score+=distance*this.outBoard;
				   	
				}
				if(chessboard[sourceIndex]>0) {
					score-=distance*this.outBoard;
					
				}	
			}
			if(m.getType()==MoveType.MERGE) {
				score+=chessboard[dIndex]*this.merge;
				//score+=scoreFromCounterEating(board,dIndex, distance,sourceIndex);
			}
		
			
			if(m.getType()==MoveType.FORWARD_MOVEMENT ) {
				
			    score+=chessboard[sourceIndex]*this.forwardMovement;
				
			//score+=scoreFromCounterEating(board,dIndex, distance,sourceIndex);
			}
			
			
			
		}
		
		return score;
	}
/*	
	private double scoreFromCounterEating(Chessboard board, int dIndex,  int distance, int sourceIndex){
		int[] chessboard=board.getChessboard(); 
		double score=0;
		int number;
		int dn,ds,de,dw;
		dw=dIndex%8;
		de=7-dw;
		ds=(dIndex-dw)/8;
		dn=7-ds;
		for(int j=distance;j<=7;j++) {//j è il raggio di movimento(diagonale orizzontale verticale)
			if((j%2)!=0) {//se j è pari invece posso muovermi in tutte e 8 le direzioni
			//considero il caso in cui muovo pedine in numero dispari quindi solo in diagonale	
				/*nel caso in cui ci sono pedoine avversarie posso mangiare? */
				   //creo una nuova move, se la move e viene rispettata la condizione di mangiata allora mangio
					//e creo una nuova scacchiera
					//se è =0 o altro posiziono le pedine in quella posizione e modifico la scacchiera 
					//sempre attraverso l'oggetto move
/*		    
				    if(j<=dn&&j<=de) {
				    	number=9;
				    	int dIndex2=dIndex+j*number;
				    	if(chessboard[dIndex2]<0 && chessboard[sourceIndex]>0) {
							if(j>=distance && j<=chessboard[dIndex2]){
									score+=distance*-1.8;
								}
						}
						if(chessboard[dIndex2]>0 && chessboard[sourceIndex]<0) {
							if(j>=distance && j<=chessboard[dIndex2]) {
									score+=distance*1.2;
							}
						}
				    }
				    
				    	
				    if(j<=dn&&j<=dw) {
				    	number=7;
				    	int dIndex2=dIndex+j*number;
				    	if(chessboard[dIndex2]<0 && chessboard[sourceIndex]>0) {
							if(j>=distance && j<=chessboard[dIndex2]){
									score+=distance*-1.8;
								}
						}
						if(chessboard[dIndex2]>0&&chessboard[sourceIndex]<0) {
							if(j>=distance && j<=chessboard[dIndex2]) {
									score+=distance*1.2;
							}
						}	
				    }
				    
				    
					
				    if(j<=ds&&j<=de) {
				    	number=-7;
				    	int dIndex2=dIndex+j*number;
				    	if(chessboard[dIndex2]<0 && chessboard[sourceIndex]>0) {
							if(j>=distance && j<=chessboard[dIndex2]){
									score+=distance*-1.2;
								}
						}
						if(chessboard[dIndex2]>0&&chessboard[sourceIndex]<0) {
							if(j>=distance && j<=chessboard[dIndex2]) {
									score+=distance*1.8;
							}
						}	
				    }
					
				    
				    if(j<=ds&&j<=dw) {
				    	number=-9;
				    	int dIndex2=dIndex+j*number;
				    	if(chessboard[dIndex2]<0 && chessboard[sourceIndex]>0) {
							if(j>=distance && j<=chessboard[dIndex2]){
									score+=distance*-1.2;
								}
						}
						if(chessboard[dIndex2]>0&&chessboard[sourceIndex]<0) {
							if(j>=distance && j<=chessboard[dIndex2]) {
									score+=distance*1.8;
							}
						}	
				    }
				
			}
			
			else {
				
				if(j<=dn&&j<=de) {
			    	number=9;
			    	int dIndex2=dIndex+j*number;
			    	if(chessboard[dIndex2]<0 && chessboard[sourceIndex]>0) {
						if(j>=distance && j<=chessboard[dIndex2]){
								score+=distance*-1.8;
							}
					}
					if(chessboard[dIndex2]>0&&chessboard[sourceIndex]<0) {
						if(j>=distance && j<=chessboard[dIndex2]) {
								score+=distance*1.2;
						}
					}	
			    }
				
				
			    if(j<=dn&&j<=dw) {
			    	number=7;
			    	int dIndex2=dIndex+j*number;
			    	if(chessboard[dIndex2]<0 && chessboard[sourceIndex]>0) {
						if(j>=distance && j<=chessboard[dIndex2]){
								score+=distance*-1.8;
							}
					}
					if(chessboard[dIndex2]>0&&chessboard[sourceIndex]<0) {
						if(j>=distance && j<=chessboard[dIndex2]) {
								score+=distance*1.2;
						}
					}	
			    }
			    
			    
			    if(j<=ds&&j<=de) {
			    	number=-7;
			    	int dIndex2=dIndex+j*number;
			    	if(chessboard[dIndex2]<0 && chessboard[sourceIndex]>0) {
						if(j>=distance && j<=chessboard[dIndex2]){
								score+=distance*-1.2;
							}
					}
					if(chessboard[dIndex2]>0&&chessboard[sourceIndex]<0) {
						if(j>=distance && j<=chessboard[dIndex2]) {
								score+=distance*1.8;
						}
					}	
			    }
			    
			    
			    if(j<=ds&&j<=dw) {
			    	number=-9;
			    	int dIndex2=dIndex+j*number;
			    	if(chessboard[dIndex2]<0 && chessboard[sourceIndex]>0) {
						if(j>=distance && j<=chessboard[dIndex2]){
								score+=distance*-1.2;
							}
					}
					if(chessboard[dIndex2]>0&&chessboard[sourceIndex]<0) {
						if(j>=distance && j<=chessboard[dIndex2]) {
								score+=distance*1.8;
						}
					}	
			    }
				/*oltre alle condizioni in diagonale aggiungo verticale e orizzontale quando il raggio
				 *  è pari
				 */

	/*	    
				if(j<=dn) {
					number=8;
					int dIndex2=dIndex+j*number;
			    	if(chessboard[dIndex2]<0 && chessboard[sourceIndex]>0) {
						if(j>=distance && j<=chessboard[dIndex2]){
								score+=distance*-1.8;
							}
					}
					if(chessboard[dIndex2]>0&&chessboard[sourceIndex]<0) {
						if(j>=distance && j<=chessboard[dIndex2]) {
								score+=distance*1.2;
						}
					}	
			    }
				
				if(j<=dw) {
					number=-1;
					int dIndex2=dIndex+j*number;
			    	if(chessboard[dIndex2]<0 && chessboard[sourceIndex]>0) {
						if(j>=distance && j<=chessboard[dIndex2]){
								score+=distance*-1.5;
							}
					}
					if(chessboard[dIndex2]>0&&chessboard[sourceIndex]<0) {
						if(j>=distance && j<=chessboard[dIndex2]) {
								score+=distance*1.5;
						}
					}	
			    }
				
				if(j<=de) {
					number=1;
					int dIndex2=dIndex+j*number;
			    	if(chessboard[dIndex2]<0 && chessboard[sourceIndex]>0) {
						if(j>=distance && j<=chessboard[dIndex2]){
								score+=distance*-1.5;
							}
					}
					if(chessboard[dIndex2]>0&&chessboard[sourceIndex]<0) {
						if(j>=distance && j<=chessboard[dIndex2]) {
								score+=distance*1.5;
						}
					}	
			    }
				
				if(j<=ds) {
					number=-8;
					int dIndex2=dIndex+j*number;
			    	if(chessboard[dIndex2]<0 && chessboard[sourceIndex]>0) {
						if(j>=distance && j<=chessboard[dIndex2]){
								score+=distance*-1.2;
							}
					}
					if(chessboard[dIndex2]>0&&chessboard[sourceIndex]<0) {
						if(j>=distance && j<=chessboard[dIndex2]) {
								score+=distance*1.8;
						}
					}	
			    }
				
			}
		}	
	return score;
	}*/

					
	

	
	public Chessboard makeMove(Chessboard board, int color, Move move) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public boolean reachedMaxDepth(Chessboard board, int depth) {
		// TODO Auto-generated method stub
		/*questo metodo è chiamato ad ogni livello del minmax e quindi ad ogni chiamata di esso. Se abbiamo raggiunto
		 * un nodo foglia allora dovrà restituire true e ne verrà calcolata la funzione euristica. Ma come faccio a 
		 * capire se è un nodo  foglia?o meglio. Come definire la profondità massima da raggiungere partendo da un nodo dell'albero?
		 * Si può ad esempio secegliere una profomdità massima di 5 livelli.
		 */
		drawn=false;
		maxwin=false;
		minwin=false;
		
		
		if(depth==4)
			return true;
		if(drawnChessboard(board)) {
			drawn=true;
			return true;
		}
		
			
		if(wonChessboard(board,true)) {
			maxwin=true;
			return true;
		}
			
	    if(wonChessboard(board,false)) {
	    	minwin=true;
	    	return true;
	    }
		    
			
		return false;
	}

	public Move getNextMove() {
		return nextMove;
		// TODO Auto-generated method stub
		
	}
	
	public  void playGame() {
		// TODO Auto-generated method stub
		/* 15 secondi di warm up, inizializzo la struttura dati (hashmap) in cui calcolo e memorizzo le tutte le mosse(richaimo un utility)
		 calcola tutte le mosse con moveHandler
		 if(player==Color.WHITE){
		    calcNextMove();
		 */	
		calcNextMove();
	}
	public int convalidMove(Move m){	
		moveHandler.applyMove(realboard,m);
		if(maxMoves==120&&realboard.getBlackPawnsNumber()>realboard.getWhitePawnsNumber())return 1;
		if(maxMoves==120&&realboard.getBlackPawnsNumber()==realboard.getWhitePawnsNumber())return 2;
		if(maxMoves==120&&realboard.getBlackPawnsNumber()<realboard.getWhitePawnsNumber())return 0;
		if(realboard.getBlackPawnsNumber()==0)return 0;
		if(realboard.getWhitePawnsNumber()==0)return 1;
		return -1;
	}

	
	public void calcNextMove() {
	   boolean maximizingPlayer=true;
	   if(player==Color.BLACK)maximizingPlayer=false;
       minimax(0,realboard,maximizingPlayer,MIN,MAX);
	}
	
	public void printValues() {
		
			System.out.print(numeropedine+","+nsize+","+noCalDist+","+backAttack+","+forAttack+","+outBoard+","+merge+","+pippo+", "+forwardMovement+", "+elevato+", "+div+"+...+");
		}
	
	
	private double minimax(int depth,Chessboard board,/*move*/ //ogni iterazione del minmax va a modificare l'oggetto chessboard che però è sempre lo stesso
             boolean maximizingPlayer, //
             double alpha,  
             double beta) { 
	
	   double value;
	  
	   
	   
	   
	   if (reachedMaxDepth( board,depth)){/*questo metodo nel nostro caso è variabile, la scelta della profondità di ricerca
		     dovra essere una scelta implementativa ,a seconda delle prestazioni */
		    
		   value=ChessboardEvaluation(board,maximizingPlayer);
		   return value;
	   } 
	   //if(depth==0) {nextMoveIndex=-1;}
	   
	   if (maximizingPlayer) { 
		         
		   double best = MIN; 
		   ArrayList<Move> arrayMoves=moveHandler.allLecitMoves(board.getChessboard(), Color.WHITE);/*qua devo prendere tutte le possibili mosse che vengono restutuite
		     dall'utility in cui precalcolo tutte le mosse.e filtrarle attraverso la configurazione corrente della scacchiera
		     */
		   
		   int possibleMoves=arrayMoves.size();
		   Chessboard board3;
		   
		   for (int i = 0; i < possibleMoves ; i++){ 
			   Move m=arrayMoves.get(i);
		       board3=board.boardCopy();
			   moveHandler.applyMove(board3,m);
			   /*if(((depth==3&&(m.getType()==MoveType.BACKWARD_ATTACK)||(m.getType()==MoveType.FORWARD_ATTACK)))&&deepe<1) {
				   value = minimax(depth +1,board3, false, alpha, beta,deepe+1);  
			   }
			   else{*/
				   value = minimax(depth + 1,board3, false, alpha, beta); 
			   //}
		       if(depth==0 && value>=best)nextMoveIndex=i;
		       best = Math.max(best, value);
		         
		         //prende il valore migliore dai figli del nodo
		       alpha = Math.max(alpha, best); //se tra i figli del nodo trovo un valore migliore di quello che ho l aggiorno
		   
		             // Alpha Beta Pruning 
		       if (beta <= alpha) 
		          break; 
		      
		   } 
		         
		          
		    if(depth==0 ){
		    	if(possibleMoves==0) {
		    		nextMove=moveHandler.jumpTurn(board, Color.WHITE);
		    	}
		    	else
		       nextMove=arrayMoves.get(nextMoveIndex);
		    }
		    return best;
	   } 
	   else{ 
		   double best = MAX; 
		   ArrayList<Move> arrayMoves=moveHandler.allLecitMoves(board.getChessboard(), Color.BLACK);/*qua devo prendere tutte le possibili mosse che vengono restutuite
		     dall'utility in cui precalcolo tutte le mosse.e filtrarle attraverso la configurazione corrente della scacchiera*/
		   int possibleMoves=arrayMoves.size();
		   Chessboard board3;
		   for (int i = 0; i < possibleMoves; i++) { 
			   Move m=arrayMoves.get(i);
			   board3=board.boardCopy();
			   moveHandler.applyMove(board3,m);
			   /*if(((depth==3&&(m.getType()==MoveType.BACKWARD_ATTACK)||(m.getType()==MoveType.FORWARD_ATTACK)))&&deepe<1) {
				   value = minimax(depth+1 ,board3, true, alpha, beta,deepe+1);  
			   }
			   else{*/
				   value = minimax(depth +1,board3, true, alpha, beta); 
			   //}
	           
	           if(depth==0 && value<=best) nextMoveIndex=i; 
	           best = Math.min(best, value); 
	           beta = Math.min(beta, best); 
		   
		             // Alpha Beta Pruning 
	          if (beta <= alpha) 
		           break; 
		     } 
		     
		     if(depth==0 ){
		    	 if(possibleMoves==0) {
			    		nextMove=moveHandler.jumpTurn(board, Color.WHITE);
			    	}
			    	else
		    	 nextMove=arrayMoves.get(nextMoveIndex);
		     }
		     return best; 
		 } 
 }
}  
