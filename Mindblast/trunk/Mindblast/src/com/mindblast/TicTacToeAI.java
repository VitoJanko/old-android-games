package com.mindblast;

import java.util.LinkedList;

public class TicTacToeAI extends Thread{
	int[][] igralnaPlosca = new int[3][3];
	int[] poteza = new int[2];
	LinkedList<int[]> avaliableMoves = new LinkedList<int[]>();
	public int difficulty = 8;
	int[] temp;// = {0,0};
	public boolean thinking = true;
	TicTacToeView view;
	
	
	public TicTacToeAI(TicTacToeView view){
		this.view = view;
	}
	
	
	
	public void run(){
		
		while(thinking){			
			synchronized (this) {
			    try {
					this.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		if(thinking){
			miniMax(igralnaPlosca,difficulty,true);
			sendMove();
		}
		}
	}
	
	public void endAI(){
		thinking = false;
		synchronized (this) {
		    this.notify();
		}
	}
	
	public void sendMove(){
		view.zamenjajPlosco(temp[0], temp[1],2);
	}
	
	public void makeMove(int[][] igralnaPloscaee){
		for(int i=0;i<igralnaPlosca.length;i++){
			for(int j=0;j<igralnaPlosca[i].length;j++){
				igralnaPlosca[i][j] = igralnaPloscaee[i][j];
			}
		}
		
		synchronized (this) {
		    this.notify();
		}
//		miniMax(igralnaPlosca,difficulty,true);
//		return temp;
	}
	

	public int miniMax(int[][] igrisce,int depth,boolean max){
		if(end() || depth == 0) return evaluate(!max,depth);
		
		int alpha = Integer.MIN_VALUE;
		LinkedList<int[] > moves = generateMove();
		
		for(int i=0;i<moves.size();i++){
			makeMove(moves.get(i),max);
//			alpha = Math.max(alpha, -miniMax(igralnaPlosca,(--depth)));
			int nw = -miniMax(igralnaPlosca,(--depth),!max);
			if(alpha <= nw) {
				temp = moves.get(i);
				alpha = nw;
			}
			depth++;
			undoMove(moves.get(i));
		}
		return alpha;
	}
	
	private LinkedList<int[]> generateMove(){
		LinkedList<int[]> list = new LinkedList<int[]>();
		
		for(int i=0;i<igralnaPlosca.length;i++){
			for(int j=0;j<igralnaPlosca[i].length;j++){
				if(igralnaPlosca[i][j] == 0){
					int[] tempMove = {i,j};
					int naslednje = (int)(Math.random()*list.size());
					list.add(naslednje,tempMove);
				}
			}
		}
		return list;
	}
	
	public void makeMove(int[] temp,boolean max){
		if(max) igralnaPlosca[temp[0]][temp[1]] = 2;
		else igralnaPlosca[temp[0]][temp[1]] = 1;
	}
	
	public void undoMove(int[] temp){
		igralnaPlosca[temp[0]][temp[1]] = 0;
	}
	
	public int evaluate(boolean max,int depth){
//		int eval = 0;
//		int eval2 = 0;
//		
//		int X = 0;
//		int O = 0;
//		
//		if(max){
//			for(int i=0;i<3;i++){
//				X = 0;
//				O = 0;
//				if(igralnaPlosca[i][0] == 1) X++;
//				else if (igralnaPlosca[i][0] == 2) O++;
//				
//				if(igralnaPlosca[i][1] == 1) X++;
//				else if (igralnaPlosca[i][1] == 2) O++;
//				
//				if(igralnaPlosca[i][2] == 1) X++;
//				else if (igralnaPlosca[i][2] == 2) O++;
//				
//				if(X == 0 && O == 0) eval += 1;
//				else if(X == 1 && O == 1) eval += 0;
//				else if (O == 1 && X == 0) eval += 10;
//				else if(O == 2 && X == 0) eval += 100;
//				else if(O == 3) eval += 1500;
//			}
//			
//			for(int i=0;i<3;i++){
//				X = 0;
//				O = 0;
//				if(igralnaPlosca[0][i] == 1) X++;
//				else if (igralnaPlosca[0][i] == 2) O++;
//				
//				if(igralnaPlosca[1][i] == 1) X++;
//				else if (igralnaPlosca[1][i] == 2) O++;
//				
//				if(igralnaPlosca[2][i] == 1) X++;
//				else if (igralnaPlosca[2][i] == 2) O++;
//				
//				if(X == 0 && O == 0) eval += 1;
//				else if(X == 1 && O == 1) eval += 0;
//				else if (O == 1 && X == 0) eval += 10;
//				else if(O == 2 && X == 0) eval += 100;
//				else if(O == 3) eval += 1500;
//			}
//			
//			X = 0;
//			O = 0;
//			if(igralnaPlosca[0][0] == 1) X++;
//			else if (igralnaPlosca[0][0] == 2) O++;
//			
//			if(igralnaPlosca[1][1] == 1) X++;
//			else if (igralnaPlosca[1][1] == 2) O++;
//			
//			if(igralnaPlosca[2][2] == 1) X++;
//			else if (igralnaPlosca[2][2] == 2) O++;
//			
//			if(X == 0 && O == 0) eval += 1;
//			else if(X == 1 && O == 1) eval += 0;
//			else if (O == 1 && X == 0) eval += 10;
//			else if(O == 2 && X == 0) eval += 100;
//			else if(O == 3) eval += 1500;
//			
//			X = 0;
//			O = 0;
//			
//			if(igralnaPlosca[2][0] == 1) X++;
//			else if (igralnaPlosca[2][0] == 2) O++;
//			
//			if(igralnaPlosca[1][1] == 1) X++;
//			else if (igralnaPlosca[1][1] == 2) O++;
//			
//			if(igralnaPlosca[0][2] == 1) X++;
//			else if (igralnaPlosca[0][2] == 2) O++;
//			
//			if(X == 0 && O == 0) eval += 1;
//			else if(X == 1 && O == 1) eval += 0;
//			else if (O == 1 && X == 0) eval += 10;
//			else if(O == 2 && X == 0) eval += 100;
//			else if(O == 3) eval += 1500;
//		}else{///////////// ZA NASPROTNIKA////// MIN/////
//			for(int i=0;i<3;i++){
//				X = 0;
//				O = 0;
//				if(igralnaPlosca[i][0] == 1) X++;
//				else if (igralnaPlosca[i][0] == 2) O++;
//				
//				if(igralnaPlosca[i][1] == 1) X++;
//				else if (igralnaPlosca[i][1] == 2) O++;
//				
//				if(igralnaPlosca[i][2] == 1) X++;
//				else if (igralnaPlosca[i][2] == 2) O++;
//				
//				if(X == 0 && O == 0) eval2 += 1;
//				else if(X == 1 && O == 1) eval2 += 0;
//				else if (X == 1 && O == 0) eval2 += 10;
//				else if(X == 2 && O == 0) eval2 += 100;
//				else if(X == 3) eval2 += 1000;
//			}
//			
//			for(int i=0;i<3;i++){
//				X = 0;
//				O = 0;
//				if(igralnaPlosca[0][i] == 1) X++;
//				else if (igralnaPlosca[0][i] == 2) O++;
//				
//				if(igralnaPlosca[1][i] == 1) X++;
//				else if (igralnaPlosca[1][i] == 2) O++;
//				
//				if(igralnaPlosca[2][i] == 1) X++;
//				else if (igralnaPlosca[2][i] == 2) O++;
//				
//				if(X == 0 && O == 0) eval2 += 1;
//				else if(X == 1 && O == 1) eval2 += 0;
//				else if (X == 1 && O == 0) eval2 += 10;
//				else if(X == 2 && O == 0) eval2 += 100;
//				else if(X == 3) eval2 += 1000;
//			}
//			
//			X = 0;
//			O = 0;
//			if(igralnaPlosca[0][0] == 1) X++;
//			else if (igralnaPlosca[0][0] == 2) O++;
//			
//			if(igralnaPlosca[1][1] == 1) X++;
//			else if (igralnaPlosca[1][1] == 2) O++;
//			
//			if(igralnaPlosca[2][2] == 1) X++;
//			else if (igralnaPlosca[2][2] == 2) O++;
//			
//			if(X == 0 && O == 0) eval2 += 1;
//			else if(X == 1 && O == 1) eval2 += 0;
//			else if (X == 1 && O == 0) eval2 += 10;
//			else if(X == 2 && O == 0) eval2 += 100;
//			else if(X == 3) eval2 += 1000;
//			
//			X = 0;
//			O = 0;
//			
//			if(igralnaPlosca[2][0] == 1) X++;
//			else if (igralnaPlosca[2][0] == 2) O++;
//			
//			if(igralnaPlosca[1][1] == 1) X++;
//			else if (igralnaPlosca[1][1] == 2) O++;
//			
//			if(igralnaPlosca[0][2] == 1) X++;
//			else if (igralnaPlosca[0][2] == 2) O++;
//			
//			if(X == 0 && O == 0) eval2 += 1;
//			else if(X == 1 && O == 1) eval2 += 0;
//			else if (X == 1 && O == 0) eval2 += 10;
//			else if(X == 2 && O == 0) eval2 += 100;
//			else if(X == 3) eval2 += 1000;
//			
//			//eval2 *= (-1);
//		}
//			eval *= -1;
//			int tm = 0;
//			tm = eval+eval2;
//			tm *= depth*2;
//		return tm;
		int eval = -50;
		int win = 0;
		int temp = 0;
		
		//vrstice
		for(int i=0;i<3;i++){
			temp=igralnaPlosca[i][0]*igralnaPlosca[i][1]*igralnaPlosca[i][2];
			if(temp == 1){
				win = 1;
				
			}else if(temp == 8){
				win = 8;
			}
		}
		
		//stolpci
		for(int i=0;i<3;i++){
			temp=igralnaPlosca[0][i]*igralnaPlosca[1][i]*igralnaPlosca[2][i];
			if(temp == 1){
				win = 1;
			}else if(temp == 8){
				win = 8;
			}
		}
		
		temp=igralnaPlosca[0][0]*igralnaPlosca[1][1]*igralnaPlosca[2][2];
		if(temp == 1){
			win = 1;
		}else if(temp == 8){
			win = 8;
		}
		temp=igralnaPlosca[0][2]*igralnaPlosca[1][1]*igralnaPlosca[2][0];
		if(temp == 1){
			win = 1;
		}else if(temp == 8){
			win = 8;
		}
		
		if(win== 1){
			eval = 100;
		}else if(win == 8){
			eval = -200;
		}
		
		if(max) eval = eval*depth;
		else eval = (-1)*eval*depth;
		return eval;
	}
	
	public boolean end(){
		if(generateMove().isEmpty()) return true;
		else{
			int win = 0;
			int temp = 0;
			
			//vrstice
			for(int i=0;i<3;i++){
				temp=igralnaPlosca[i][0]*igralnaPlosca[i][1]*igralnaPlosca[i][2];
				if(temp == 1){
					win = 1;
					
				}else if(temp == 8){
					win = 8;
				}
			}
			
			//stolpci
			for(int i=0;i<3;i++){
				temp=igralnaPlosca[0][i]*igralnaPlosca[1][i]*igralnaPlosca[2][i];
				if(temp == 1){
					win = 1;
				}else if(temp == 8){
					win = 8;
				}
			}
			
			temp=igralnaPlosca[0][0]*igralnaPlosca[1][1]*igralnaPlosca[2][2];
			if(temp == 1){
				win = 1;
			}else if(temp == 8){
				win = 8;
			}
			temp=igralnaPlosca[0][2]*igralnaPlosca[1][1]*igralnaPlosca[2][0];
			if(temp == 1){
				win = 1;
			}else if(temp == 8){
				win = 8;
			}
			
			if(win== 1){
				return true;
			}else if(win == 8){
				return true;
			}
		}
		
		return false;
	}
	

}