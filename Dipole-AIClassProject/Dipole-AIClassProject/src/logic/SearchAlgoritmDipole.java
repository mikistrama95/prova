package logic;

import java.util.ArrayList;


import representetion.*;


public class SearchAlgoritmDipole{
	
	//la mossa che restituisco man mano come risposta;
	private Color player;
    protected Move nextMove;
    private int nextMoveIndex;
	private static final double MAX = 1000000;
    private static final double MIN = -1000000;
	private MoveHandler moveHandler;
	public Chessboard realboard;
	private int maxMoves;
	int size;
	private boolean drawn,maxwin,minwin;
	public SearchAlgoritmDipole(){
		moveHandler=new MoveHandler();
		realboard= new Chessboard();
		
		// TODO Auto-generated constructor stub
	}
	public void setColor(Color player) {
		this.player=player;
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
		if(maxMoves==120&&board.getBlackPawnsNumber()<board.getWhitePawnsNumber()&&maximizing)return true;
		if(board.getBlackPawnsNumber()==0&&maximizing)return true;
		if(board.getWhitePawnsNumber()==0&&!maximizing)return true;
		return false;
	}
	
	
	public double ChessboardEvaluation(Chessboard board) {// anche se è sbagliato si sta ssumendo per ora
        // TODO Auto-generated method stub                          //che il colore 0=white=maximizer e l'opposto 
        double score=0; 
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
        score=+0;  
        return score ;   
        }                   
        
      
        score+=number_white*103-(number_black*103);//score per il numero di pedine

        
        
        //qua devo scorrermi tutte le lecitMoves e aumentare lo score a seconda del tipo di mossa
        //il problema è che ogni volta che richiamo alllectimoves all'interno fa un altro while
        //chiedere ad ale se può fare un metodo per estituire tutte le lecitmoves a seconda dell'indice che invio al metodoCe
        i=0;
        
        while(i<Chessboard.SIDE_LENGTH*Chessboard.SIDE_LENGTH) {
             
            if(chessboard[i] !=20 && chessboard[i] !=0) {
                sourceIndex=i;
                resto=sourceIndex%8;
                dist=(sourceIndex-resto)/8;
                
                //controllo la distanza delle pedine alla fine della scacchiera
                if(chessboard[sourceIndex]<0) {
                    size=0;
                    score-=(chessboard[sourceIndex]^2)/4;
                    allLecitMoves=moveHandler.allCellLecitMoves(chessboard, Color.BLACK,i);
                    size=allLecitMoves.size();                   
                    score+=scoreFromEating(board,sourceIndex,allLecitMoves);
                    score-=size*1.2;//mosse da quella posizione
                    score-=dist*4;//distanza dalla fine della scacchiera
                   /* if(dist>=4) {
						score-=dist*2;
						
					}*/
                   
                }
                if(chessboard[sourceIndex]>0) {
                    size=0;
                    score+=(chessboard[sourceIndex]^2)/4;
                    allLecitMoves=moveHandler.allCellLecitMoves(chessboard, Color.WHITE,i);
                    size=allLecitMoves.size();
                    score+=scoreFromEating(board,sourceIndex,allLecitMoves);
                    score+=size*1.2;
                    score+=(7-dist)*4;//distanza dalla fine della scacchiera
                    /*if(dist<4) {
						score+=(7-dist)*2;
						
					}*/
                }
            }
            i++;
        }
        return score;
    }

    
    public void printChessboard(Chessboard board) {
        // TODO Auto-generated method stub
        
    }
    
    

    private double scoreFromEating(Chessboard board, int sourceIndex, ArrayList<Move> allLecitMoves) {
        double score=0;
        int val=24;
       
        int val2=20;
        int[] chessboard=board.getChessboard(); 
  
        for(int j=0; j<allLecitMoves.size();j++) {
            Move m= allLecitMoves.get(j);        
            int dIndex=m.getDestinationIndex();
            int distance=m.getPawnsNumber();
            
            
            if(m.getType()==MoveType.BACKWARD_ATTACK ) {
                score+=chessboard[dIndex]*-val;
                //score+=scoreFromCounterEating(board,dIndex, distance,sourceIndex);
             }
            
            /*if(m.getType()==MoveType.SIDE_ATTACK) {
                score+=chessboard[dIndex]*-val1;
                //score+=scoreFromCounterEating(board,dIndex, distance,sourceIndex);
            }*/
            
            if(m.getType()==MoveType.FORWARD_ATTACK) {
                score+=chessboard[dIndex]*-val2;
                //score+=scoreFromCounterEating(board,dIndex, distance,sourceIndex);
            }
            if(m.getType()==MoveType.OUT_OF_BOARD) {
                size--;
                if(chessboard[sourceIndex]<0) {
                    score+=distance*0.15;
            
                }
                if(chessboard[sourceIndex]>0) {
                    score-=distance*0.15;

                }    
            }
            if(m.getType()==MoveType.MERGE) {
                score+=chessboard[dIndex]*2;
              // score+=scoreFromCounterEating(board,dIndex, distance,sourceIndex);
            }
            
            if(m.getType()==MoveType.FORWARD_MOVEMENT) {
                score+=chessboard[sourceIndex]*2;
              // score+=scoreFromCounterEating(board,dIndex, distance,sourceIndex);
            }
            
           
        }
        
        return score;
    }
	
	/*private double scoreFromCounterEating(Chessboard board, int dIndex,  int distance, int sourceIndex){
		int[] chessboard=board.getChessboard(); 
		double score=0;
		int number;
		int dn,ds,de,dw;
		dw=dIndex%8;
		de=7-dw;
		ds=(dIndex-dw)/8;
		dn=7-ds;
		//int j=0;
		//while(j<Chessboard.SIDE_LENGTH*Chessboard.SIDE_LENGTH) {
		for(int j=1;j<=7;j++) {//j è il raggio di movimento(diagonale orizzontale verticale)
			if((j%2)!=0) {//se j è pari invece posso muovermi in tutte e 8 le direzioni
			//considero il caso in cui muovo pedine in numero dispari quindi solo in diagonale	
				/*nel caso in cui ci sono pedoine avversarie posso mangiare? */
				   //creo una nuova move, se la move e viene rispettata la condizione di mangiata allora mangio
					//e creo una nuova scacchiera
					//se è =0 o altro posiziono le pedine in quella posizione e modifico la scacchiera 
					//sempre attraverso l'oggetto move
		    
				/*    if(j<=dn&&j<=de) {
				    	number=9;
				    	int dIndex2=dIndex+j*number;
				    	if(chessboard[dIndex2]<0 && chessboard[sourceIndex]>0) {
							if( j<=-chessboard[dIndex2]  && distance<=-chessboard[dIndex2] ){
									score-=chessboard[dIndex2]*20;
								}
						}
						if(chessboard[dIndex2]>0 && chessboard[sourceIndex]<0) {
							if(j<=chessboard[dIndex2] && distance<=chessboard[dIndex2]) {
									score+=chessboard[dIndex2]*24;
							}
						}
				    }
				    
				    	
				    if(j<=dn&&j<=dw) {
				    	number=7;
				    	int dIndex2=dIndex+j*number;
				    	if(chessboard[dIndex2]<0 && chessboard[sourceIndex]>0) {
							if(j<=-chessboard[dIndex2] && distance<=-chessboard[dIndex2]){
									score-=chessboard[dIndex2]*20;
								}
						}
						if(chessboard[dIndex2]>0&&chessboard[sourceIndex]<0) {
							if(j<=chessboard[dIndex2] && distance<=chessboard[dIndex2]) {
									score+=chessboard[dIndex2]*24;
							}
						}	
				    }
				    
				    
					
				    if(j<=ds&&j<=de) {
				    	number=-7;
				    	int dIndex2=dIndex+j*number;
				    	if(chessboard[dIndex2]<0 && chessboard[sourceIndex]>0) {
							if(j<=-chessboard[dIndex2] && distance<=-chessboard[dIndex2] ){
									score-=chessboard[dIndex2]*24;
								}
						}
						if(chessboard[dIndex2]>0&&chessboard[sourceIndex]<0) {
							if(j<=chessboard[dIndex2] && distance<=chessboard[dIndex2] ) {
									score+=chessboard[dIndex2]*20;
							}
						}	
				    }
					
				    
				    if(j<=ds&&j<=dw) {
				    	number=-9;
				    	int dIndex2=dIndex+j*number;
				    	if(chessboard[dIndex2]<0 && chessboard[sourceIndex]>0) {
							if(j<=-chessboard[dIndex2] && distance<=-chessboard[dIndex2] ){
									score-=chessboard[dIndex2]*24;
								}
						}
						if(chessboard[dIndex2]>0&&chessboard[sourceIndex]<0) {
							if(j<=chessboard[dIndex2] && distance<=chessboard[dIndex2] ) {
									score+=chessboard[dIndex2]*20;
							}
						}	
				    }
				
			}
			
			else {
				
				if(j<=dn&&j<=de) {
			    	number=9;
			    	int dIndex2=dIndex+j*number;
			    	if(chessboard[dIndex2]<0 && chessboard[sourceIndex]>0) {
						if(j<=-chessboard[dIndex2] && distance<=-chessboard[dIndex2] ){
								score-=chessboard[dIndex2]*20;
							}
					}
					if(chessboard[dIndex2]>0&&chessboard[sourceIndex]<0) {
						if(j<=chessboard[dIndex2] && distance<=chessboard[dIndex2] ) {
								score+=chessboard[dIndex2]*24;
						}
					}	
			    }
				
				
			    if(j<=dn&&j<=dw) {
			    	number=7;
			    	int dIndex2=dIndex+j*number;
			    	if(chessboard[dIndex2]<0 && chessboard[sourceIndex]>0) {
						if(j<=-chessboard[dIndex2] && distance<=-chessboard[dIndex2] ){
								score-=chessboard[dIndex2]*20;
							}
					}
					if(chessboard[dIndex2]>0&&chessboard[sourceIndex]<0) {
						if(j<=chessboard[dIndex2] && distance<=chessboard[dIndex2] ) {
								score+=chessboard[dIndex2]*24;
						}
					}	
			    }
			    
			    
			    if(j<=ds&&j<=de) {
			    	number=-7;
			    	int dIndex2=dIndex+j*number;
			    	if(chessboard[dIndex2]<0 && chessboard[sourceIndex]>0) {
						if(j<=-chessboard[dIndex2] && distance<=-chessboard[dIndex2] ){
								score-=chessboard[dIndex2]*24;
							}
					}
					if(chessboard[dIndex2]>0&&chessboard[sourceIndex]<0) {
						if(j<=chessboard[dIndex2]&& distance<=chessboard[dIndex2] ) {
								score+=chessboard[dIndex2]*20;
						}
					}	
			    }
			    
			    
			    if(j<=ds&&j<=dw) {
			    	number=-9;
			    	int dIndex2=dIndex+j*number;
			    	if(chessboard[dIndex2]<0 && chessboard[sourceIndex]>0) {
						if(j<=-chessboard[dIndex2]&& distance<=-chessboard[dIndex2] ){
								score-=chessboard[dIndex2]*24;
							}
					}
					if(chessboard[dIndex2]>0&&chessboard[sourceIndex]<0) {
						if( j<=chessboard[dIndex2]&& distance<=chessboard[dIndex2] ) {
								score+=chessboard[dIndex2]*20;
						}
					}	
			    }
				/*oltre alle condizioni in diagonale aggiungo verticale e orizzontale quando il raggio
				 *  è pari
				 */

		    
		/*		if(j<=dn) {
					number=8;
					int dIndex2=dIndex+j*number;
			    	if(chessboard[dIndex2]<0 && chessboard[sourceIndex]>0) {
						if(j<=-chessboard[dIndex2]&& distance<=-chessboard[dIndex2] ){
								score-=chessboard[dIndex2]*20;
							}
					}
					if(chessboard[dIndex2]>0&&chessboard[sourceIndex]<0) {
						if(j<=chessboard[dIndex2]&& distance<=chessboard[dIndex2] ) {
								score+=chessboard[dIndex2]*24;
						}
					}	
			    }
				
				if(j<=dw) {
					number=-1;
					int dIndex2=dIndex+j*number;
			    	if(chessboard[dIndex2]<0 && chessboard[sourceIndex]>0) {
						if(j<=-chessboard[dIndex2]&& distance<=-chessboard[dIndex2] ){
								score-=chessboard[dIndex2]*20;
							}
					}
					if(chessboard[dIndex2]>0 && chessboard[sourceIndex]<0) {
						if(j<=chessboard[dIndex2]&& distance<=chessboard[dIndex2] ) {
								score+=chessboard[dIndex2]*20;
						}
					}	
			    }
				
				if(j<=de) {
					number=1;
					int dIndex2=dIndex+j*number;
			    	if(chessboard[dIndex2]<0 && chessboard[sourceIndex]>0) {
						if(j<=-chessboard[dIndex2]&& distance<=-chessboard[dIndex2] ){
								score-=chessboard[dIndex2]*20;
							}
					}
					if(chessboard[dIndex2]>0 && chessboard[sourceIndex]<0) {
						if(j<=chessboard[dIndex2]&& distance<=chessboard[dIndex2] ) {
								score+=chessboard[dIndex2]*20;
						}
					}	
			    }
				
				if(j<=ds) {
					number=-8;
					int dIndex2=dIndex+j*number;
			    	if(chessboard[dIndex2]<0 && chessboard[sourceIndex]>0) {
						if(j<=-chessboard[dIndex2]&& distance<=-chessboard[dIndex2] ){
								score-=chessboard[dIndex2]*24;
							}
					}
					if(chessboard[dIndex2]>0 && chessboard[sourceIndex]<0) {
						if(j<=chessboard[dIndex2]&& distance<=chessboard[dIndex2] ) {
								score+=chessboard[dIndex2]*20;
						}
					}	
			    }
				
			}
			//j++;
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
	public void convalidMove(Move m){	
		moveHandler.applyMove(realboard,m);
	}

	
	public void calcNextMove() {
	   boolean maximizingPlayer=true;
	   if(player==Color.BLACK)maximizingPlayer=false;
       minimax(0,realboard,maximizingPlayer,MIN,MAX);
	}
	
	private double minimax(int depth,Chessboard board,/*move*/ //ogni iterazione del minmax va a modificare l'oggetto chessboard che però è sempre lo stesso
             boolean maximizingPlayer, //
             double alpha,  
             double beta) { 
	
	   double value;
	  
	   
	   
	   
	   if (reachedMaxDepth( board,depth)){/*questo metodo nel nostro caso è variabile, la scelta della profondità di ricerca
		     dovra essere una scelta implementativa ,a seconda delle prestazioni */
		    
		   value=ChessboardEvaluation(board);
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
			   
		       board3=board.boardCopy();
			   moveHandler.applyMove(board3,arrayMoves.get(i));
		       value = minimax(depth + 1,board3, false, alpha, beta); 
		       if(depth==0 && value>=best)nextMoveIndex=i;
		       best = Math.max(best, value);
		         
		         //prende il valore migliore dai figli del nodo
		       alpha = Math.max(alpha, best); //se tra i figli del nodo trovo un valore migliore di quello che ho l aggiorno
		   
		             // Alpha Beta Pruning 
		       if (beta <= alpha) 
		          break; 
		      
		   } 
		         
		          
		    if(depth==0 ){
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
			   board3=board.boardCopy();
			   moveHandler.applyMove(board3,arrayMoves.get(i));  
	           value = minimax(depth + 1,board3, true, alpha, beta);
	           if(depth==0 && value<=best) nextMoveIndex=i; 
	           best = Math.min(best, value); 
	           beta = Math.min(beta, best); 
		   
		             // Alpha Beta Pruning 
	          if (beta <= alpha) 
		           break; 
		     } 
		     
		     if(depth==0 ){
		    	 nextMove=arrayMoves.get(nextMoveIndex);
		     }
		     return best; 
		 } 
 }
}  
  
 
