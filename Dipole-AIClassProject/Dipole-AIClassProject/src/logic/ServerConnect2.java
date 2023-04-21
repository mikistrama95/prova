package logic;

import java.io.*;


import java.net.*;

import representetion.*;


public class ServerConnect2 {

	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		BufferedReader br ;
		PrintWriter pw;
		//lettura dell 'host e della porta da tastiera!
		try {
			
			System.out.println("attendo il messaggio dal server");
			InetAddress addr;
			if (args.length == 0) addr = InetAddress.getByName(null);
			else addr = InetAddress.getByName(args[0]);
		
			Socket tSocket = new Socket(addr, 8901);
			System.out.println("attendo il messaggio dal server");
			pw = new PrintWriter(tSocket.getOutputStream(), true);
			br = new BufferedReader(new InputStreamReader(tSocket.getInputStream()));
			String risposta;
			String[] tokens;
			String [] parameters;
			SearchAlgoritmDipole3 sa=new SearchAlgoritmDipole3();
			Boolean play=true;
			Move m=null;
			
			while(play) {
					risposta = br.readLine();
					if(risposta==null) {
						continue;
					}
					System.out.println("Risposta:"+risposta);
					
				    tokens=risposta.split(" ");
					
				    switch(tokens[0]) {
				    case "WELCOME":
				    	if(tokens[1].equals("White")) 
							sa.setColor(Color.WHITE);
						else
							sa.setColor(Color.BLACK);
						sa.playGame();
						break;//appena so il colore posso inizializzare l'algoritmo e richiamare il precalcolo e tengo le mosse precalcolate
						//in questo oggetto. bisogna anche valutare se conviene calcolare nel warm up le mosse in anticipo o tutte l scacchiere e se gioco per
						//primo calcolcare la prossima mossa.
				    
				    case "OPPONENT_MOVE":
				    	parameters=tokens[1].split(",");
						String source_index=parameters[0];
					    Direction dir= Enum.valueOf(Direction.class, parameters[1]);//OVVIAMENTE NON RICEVO OGGETTI Direction, QUINDI DA TRASFORMARE IN MODO OPPORTUNO
						int pawnsNumber=Integer.parseInt(parameters[2]);//trasformo in int 
						m=new Move(source_index,dir,pawnsNumber);
						m.isOutOfBoard();
						sa.convalidMove(m);
						sa.calcNextMove();
						break;
				    	
				    case "YOUR_TURN":
				    	m=sa.getNextMove();
				    	String nextMove=m.moveToString();
				    	
						pw.println(nextMove);
						break;
				    
				    case "VALID_MOVE":
				    	sa.convalidMove(m);	
				    	break;
				    	
				    case "TIMEOUT":break;
				    	
					case "ILLEGAL_MOVE":break;
				    		
				    case "VICTORY":play=false;break;
				    	
					case "TIE":play =false;break;
				    	
				    case "DEFEAT":play=false;break;
				    		
				    default:break;
				    }
					
					
				
			}
			br.close();
			pw.close();
			tSocket.close();
			} catch (Exception e) {
			    
			    e.printStackTrace();
			}

	}

	
	

}

