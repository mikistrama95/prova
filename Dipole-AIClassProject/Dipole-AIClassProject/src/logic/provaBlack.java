package logic;

import java.util.Scanner;

import representetion.*;

public class provaBlack {
    public static void main(String[] args) {
        boolean play=true;
        Move m;
        SearchAlgoritmDipole sa=new SearchAlgoritmDipole();
        Scanner input=new Scanner(System.in);
        String[] parameters;
        while(play) {
            sa.setColor(Color.BLACK);
            sa.playGame();
            
            System.out.println("inserisci la  tua mossa=...");
            String risposta=input.nextLine();
            parameters=risposta.split(",");
            
            String source_index=parameters[0];
            Direction dir= Enum.valueOf(Direction.class, parameters[1]);//OVVIAMENTE NON RICEVO OGGETTI Direction, QUINDI DA TRASFORMARE IN MODO OPPORTUNO
            int pawnsNumber=Integer.parseInt(parameters[2]);//trasformo in int 
            m=new Move(source_index,dir,pawnsNumber);
            
            sa.convalidMove(m);
            
            for(int i=7;i>=0;i--) {
            	System.out.print("[ ");
            	for(int j=0;j<8;j++) {
            		System.out.print(sa.realboard.getChessboard()[i*8+j]+",");
            	}
            	System.out.print("] ");
            	System.out.println("");
            }
            
            
            sa.calcNextMove();
            m=sa.getNextMove();
            System.out.println("Mossa:."+m.moveToString());
            sa.convalidMove(m);
           
            
            
                
          
            }
            input.close();
            
        
    }
       
       /* int[] vettore=new int[8];
        for(int index=0;index<8;index++) {
            
        vettore[index]=index;
       
        }
        for(int index=0;index<8;index++) {
            System.out.print(vettore[index]);
           
            }
            
        modifica(vettore);
        for(int index=0;index<8;index++) {
            System.out.print(vettore[index]);
           
            }
        
    }
  private static void modifica(int[] vettore) {
      for(int index=0;index<8;index++) {
            
            vettore[index]=index-10;
           
            }
      
  }
  private static void modifica(Chessboard board) {
      int[] chess=board.getChessboard();
       chess[0]=2000;
    */  
  
}