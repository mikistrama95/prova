package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import representetion.Color;
import representetion.Move;

public class Tournament {
	
	
	
	public static void main(String[] args) {
		/* ho ad esempio 8 parametri nella valutazione, genero quindi 8 coeefficienti random per ogni giocatore che devo creare
		 * immagino che voglio creare 200 giocatori, faccio un for di 200 iterazioni , ora ad ogni giocatore imposto i suoi 
		 * valori e li posso associare al' id del giocatore,faccio un hasmap di id in cui salvo lo score, ora ogni volta pche vince una martita aumento lo scor
		 * di uncerto valore, e lo diminuisco se perde, finite tutte le partite possibili stampo i valore piu altri dell hashmap con i parametri
		 * 
		 */
			  Random random = new Random();
			  int num=7;
			  
			float numeropedine,nsize,noCalDist,backAttack,forAttack,outBoard,merge,pippo,forwardMovement;
			int elevato,div;
			
		
		//HashMap<Integer,ArrayList<Float>> h=new HashMap<Integer,ArrayList<Float>>(); //<
		float[] scores=new float[num];
		
		
			for(int i=0;i<num;i++) {
			scores[i]=0;
		}
		
		
		
	    SearchAlgoritmDipole2[] functions=new SearchAlgoritmDipole2[num]; 
		
		//creazione dei giocatori
		for(int i =0;i<num;i++) {
//	    	//ArrayList<Float> valori=new ArrayList<Float>();
	    	 
	    	 
	 		int a = 100; // numero minimo
	 		int b = 300; // numero massimo
	 		int c = ((b-a) + 1);
	 		numeropedine = random.nextInt(c) + a+ random.nextFloat();
	 		
	 		
	 		
	 		a = 4; // numero minimo
	 		b =70 ; // numero massimo
	 		c = ((b-a) + 1);
	 		nsize = random.nextInt(c) + a + random.nextFloat();
	 		
	 		
	 		
	 		a = 1; // numero minimo
	 		b = 150; // numero massimo
	 		c = ((b-a) + 1);
	 		noCalDist = random.nextInt(c) + a+random.nextFloat();
	 		
	 		a = 70; // numero minimo
	 		b = 260; // numero massimo
	 		c = ((b-a) + 1);
	 		backAttack = random.nextInt(c) + a+random.nextFloat();
	 		
	 		a = 70; // numero minimo
	 		b = 250; // numero massimo
	 		c = ((b-a) + 1);
	 		forAttack = random.nextInt(c) + a+random.nextFloat();
	 		
	 
	 		
	 		a = 0; // numero minimo
	 		b = 50; // numero massimo
	 		c = ((b-a) + 1);
	 		outBoard = random.nextInt(c) + a+random.nextFloat();
	 		
	 		a = 0; // numero minimo
	 		b = 50; // numero massimo
	 		c = ((b-a) + 1);
	 		merge = random.nextInt(c) + a+random.nextFloat();
	 		
	 		a = 6; // numero minimo
	 		b = 100; // numero massimo
	 		c = ((b-a) + 1);
	 		pippo = random.nextInt(c) + a+random.nextFloat();
	 		
	 		a = 3; // numero minimo
	 		b = 50; // numero massimo
	 		c = ((b-a) + 1);
	 		forwardMovement = random.nextInt(c) + a+random.nextFloat();
	 		
	 		a = 1; // numero minimo
	 		b = 8; // numero massimo
	 		c = ((b-a) + 1);
	 		elevato = random.nextInt(c)+a;
	 		
	 		a = 1; // numero minimo
	 		b = 16; // numero massimo
	 		c = ((b-a) + 1);
	 		div = random.nextInt(c)+a;
	 		
	 		  //[,valor1,valore2...valore8]
	 		/*valori.add(numeropedine);
	 		valori.add(nsize);
	 		valori.add(calDist);
	 		valori.add(noCalDist);
	 		valori.add(backAttack);
	 		valori.add(forAttack);
	 		valori.add(outBoard);
	 		valori.add(merge);*/
	 		
	 		//h.put(i, valori); //[<0,valori0[],<1,valori1[]>.....]
	 		
	    	SearchAlgoritmDipole2 sa=new SearchAlgoritmDipole2( numeropedine, nsize, noCalDist, backAttack, forAttack,outBoard, merge,pippo,forwardMovement,elevato,div) ;
	    	functions[i]=sa;
	    	
	    		
	     }
		
		//match tutti contro tutti
		
		
		SearchAlgoritmDipole2 sa1,sa2;
		boolean play;
		Move m,m2;
		
		for(int i=0;i<num;i++) {
			
			
			
			sa1=functions[i];
			
	    	
			sa1.setColor(Color.WHITE);
			for(int j=0;j<num;j++) {
				
			   play=true;
			   if(i==j) {
				   continue;
			   }
			   
			  
			   sa2=functions[j];
			   
		    	
			   sa2.setColor(Color.BLACK);
			   //giocano
			   int esito;
			   
				
			   while(play) {
				   
				   sa1.calcNextMove();
				   m=sa1.getNextMove();
				   esito=sa1.convalidMove(m);
				   if(esito==0) {
					   play=false;
					   scores[i]+=3;
					   
					   functions[i].azzera();
					   functions[j].azzera();
					 
					   break;
				   }
				   else if(esito==1) {
					   play=false;
					   
					   scores[j]+=3;
					   functions[i].azzera();
					   functions[j].azzera();
					  
					   break;
				   }
				   else if(esito==2) {
					   play=false;
				   scores[i]+=1;
				   scores[j]+=1;
				   functions[i].azzera();
				   functions[j].azzera();
				  
				   break;
				   }
				   sa2.convalidMove(m);
				   
				   sa2.calcNextMove();
				   m2=sa2.getNextMove();
				   esito=sa2.convalidMove(m2);
				   if(esito==0) {
					   play=false;
					   scores[i]+=3;
					   
					   functions[i].azzera();
					   functions[j].azzera();
					   
					   break;
				   }
				   else if(esito==1) {
					   play=false;
					   
					   scores[j]+=3;
					   
					   functions[i].azzera();
					   functions[j].azzera();
					  
					   break;
				   }
				   else if(esito==2) {
					   play=false;
				   scores[i]+=1;
				   scores[j]+=1;
				   functions[i].azzera();
				   functions[j].azzera();
				   
				   break;
				   
				   }
				   sa1.convalidMove(m2);
				  
			   }
			}
		}
		ArrayList<Integer> al=new ArrayList<Integer>();
		
		int index=-1;
		float value;
		for(int i=0;i<1;i++) {
			value=-1000;
			for(int j=0;j<num;j++) {
				if(scores[j]>value&&!(al.contains(j))) {
					value=scores[j];
					index=j;
				}
				
			}
			if(index!=-1) {
			al.add(index);}
			index=-1;
			
			
		}
			
	for(int i=0;i<1;i++) {
		System.out.println(al.get(i)+":");
		
		    functions[al.get(i)].printValues();
		    System.out.println("");
		    System.out.println(scores[al.get(i)]);
			
		
		
	}
	
	for(int i=0;i<num;i++) {
		System.out.println(i+":");
		
		    functions[i].printValues();
		    System.out.println(scores[i]);
			
		
		
	}
	
	}
	
	
	

}
