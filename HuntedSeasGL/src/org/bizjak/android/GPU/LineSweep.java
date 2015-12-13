package org.bizjak.android.GPU;

import java.util.PriorityQueue;

public class LineSweep {

	static PriorityQueue<Event> q;
	static LineTree t;
	
	public static boolean sweep(Line[] lines){
		q = new PriorityQueue<Event>();
		t = new LineTree();
		
		for(Line line : lines){
			Event s = new Event(line,line.x1,Event.START);
			Event e = new Event(line,line.x2,Event.END);
			q.add(s);
			q.add(e);
		}
		
		float collided = -9999;
		
		while(!q.isEmpty()){
			Event event = q.poll();
			if(event.event==Event.START){
				System.out.println("Line "+event.line.name+" start at "+event.position);
				t.add(event.line,event.position);
				Line pre = t.findPredecessor(event.line, event.position);
				Line suc = t.findSuccessor(event.line, event.position);
				if (pre!=null){
					System.out.println("Checking "+pre.name+" and "+event.line.name);
					if(isCollision(pre,event.line,event.position))
						return true;
				}
				if (suc!=null){
					System.out.println("Checking "+suc.name+" and "+event.line.name);
					if(isCollision(suc,event.line,event.position))
						return true;
				}
			}
			if(event.event==Event.END){
				System.out.println("Line "+event.line.name+" ends at "+event.position);
				Line pre = t.findPredecessor(event.line, event.position);
				Line suc = t.findSuccessor(event.line, event.position);
				t.delete(event.line, event.position);
				if (pre!=null && suc!=null){
					System.out.println("Checking "+pre.name+" and "+suc.name);
					if(isCollision(pre,suc,event.position))
						return true;
				}
			}
			if(event.event==Event.COLLIDE){
				System.out.println("Testing for previous collision...");
				if(Math.abs(event.position-collided)>0.001){
					Line l1 = event.line;
					Line l2 = event.line2;
					System.out.println("Line "+l1.name+" and "+l2.name+" intersect at "+event.position);
					t.switchLines(l1,l2,event.position-0.001f);
					collided = event.position;
					if(l1.compareTo(l2, event.position+0.001f)>0){
						System.out.println(l1.name+" is above");
						Line pre = t.findPredecessor(l2, event.position+0.001f);
						Line suc = t.findSuccessor(l1, event.position+0.001f);
						if(pre!=null){
							System.out.println("Checking "+pre.name+" and "+l2.name);
							if(isCollision(pre,l2,event.position))
								return true;
						}
						
						if(suc!=null){
							System.out.println("Checking "+suc.name+" and "+l1.name);
							if (isCollision(l1,suc,event.position))
								return true;
						}
					}
					else{
						System.out.println(l2.name+" is above");
						Line pre = t.findPredecessor(l1, event.position+0.001f);
						Line suc = t.findSuccessor(l2, event.position+0.001f);
						if(pre!=null){
							System.out.println("Checking "+pre.name+" and "+l1.name);
							if(isCollision(pre,l1,event.position))
								return true;
						}
						if(suc!=null){
							System.out.println("Checking "+suc.name+" and "+l2.name);
							if(suc!=null && isCollision(l2,suc,event.position))
								return true;
						}
					}
				}
			}
			t.print();
			System.out.println("-------");
		}
		
		return false;
	}
	
	public static boolean isCollision(Line l1, Line l2, float point){
		boolean collide = false;
		float x0 = 0;
		if(l1.k==l2.k){
			if(l1.n==l2.n){
				if(l1.x1>l2.x1 && l1.x1<l2.x2 ){
					x0 = l1.x1;
					collide = true;
				}
				else if(l2.x1>l1.x1 && l2.x1<l1.x2 ){
					x0 = l2.x1;
					collide = true;
				}
			}
		}
		else{
			x0 = -(l1.n-l2.n)/(l1.k-l2.k);
			if(x0>l1.x1 && x0>l2.x1 && x0<l1.x2 && x0<l2.x2)
				collide = true;
		}
		if(collide){
			if(l1.type==l2.type){
				Event c = new Event(l1,l2,x0,Event.COLLIDE);
				if(x0>point){
					LineSweep.q.add(c);
					System.out.println("Collision detected at "+x0);
				}
			}
			else{
				return true;
			}
		}
		return false;
	}
	
	
}

class Event implements Comparable<Event>{
	public static int START = 0;
	public static int END = 1;
	public static int COLLIDE = 2;
	Line line;
	Line line2;
	float position;
	int event;
	
	Event(Line line, float position, int event){
		this.position=position;
		this.event=event;
		this.line=line;
	}
	
	Event(Line line,Line line2, float position, int event){
		this.position=position;
		this.event=event;
		this.line=line;
		this.line2=line2;
	}

	public int compareTo(Event another) {
		if (position>another.position)
			return 1;
		else if (position==another.position)
			if(event==Event.END && another.event==Event.START)
				return 1;
			else if (event==Event.START && another.event==Event.END)
				return -1;
			else
				return 0;
		else
			return -1;
	}


}

class Line{
	float x1;
	float y1;
	float x2;
	float y2;
	float k;
	float n;
	int type;
	String name;
	
	Line(float x1, float y1, float x2, float y2, int type, String name){
		this.type=type;
		this.x1=x1;
		this.y1=y1;
		this.x2=x2;
		this.y2=y2;
		this.k = (y2-y1)/(x2-x1);
		this.n = y1-k*x1;
		this.name=name;
	}
	
	public int compareTo(Line another,float point) {
		float relevant = point*k+n;
		float relevant2 = another.k*point+another.n;
		if (relevant>relevant2)
			return 1;
		else if (relevant==relevant2)
			return 0;
		else
			return -1;
	}
}

class LineTree{
	LineNode root;
	
	public void print(){
		System.out.println(root.print());
	}
	
	public void add(Line line, float point){
		if (root==null)
			root = new LineNode(line,null);
		else{
			root.insert(line,point);
		}
	}
	
	public void delete(Line line, float point){
		if(root!=null)
			root.delete(line,point,this);
	}
	
	public Line findPredecessor(Line line, float point){
		if(root!=null)
			return root.findLine(line,point).findPredecessor();
		else
			return null;
	}
	
	public Line findSuccessor(Line line, float point){
		if(root!=null)
			return root.findLine(line,point).findSuccessor();
		else
			return null;
	}
	
	public void switchLines(Line l1, Line l2,float point){
		if(root!=null){
			LineNode n1 = root.findLine(l1, point);
			LineNode n2 = root.findLine(l2, point);
			Line l = n1.content;
			n1.content = n2.content;
			n2.content = l;
		}
	}
	
}

class LineNode{
	Line content;
	LineNode left;
	LineNode right;
	LineNode parent;
	LineNode(Line content,LineNode parent){
		this.parent=parent;
		this.content=content;
	}
	
	public String print(){
		String leftS = "";
		String rightS = "";
		if(left!=null)
			leftS = left.print();
		if(right!=null)
			rightS = right.print(); 
		return "("+leftS+") "+content.name+" ("+rightS+")";
	}
	
	public void insert(Line line, float point){
		if (line.compareTo(content, point)<=0){
			if(left==null)
				left = new LineNode(line,this);
			else
				left.insert(line, point);
		}
		else{
			if(right==null)
				right = new LineNode(line,this);
			else
				right.insert(line, point);
		}
	}
	
	public void delete(Line line, float point, LineTree tree){
		if(line==content){
			if(left==null && right==null){
				if(parent==null)
					tree.root=null;
				else if(parent.left==this)
					parent.left=null;
				else if(parent.right==this)
					parent.right=null;
			}
			else if(left==null && right!=null){
				if(parent==null)
					tree.root = right;
				else if(parent.left==this)
					parent.left=right;
				else if(parent.right==this)
					parent.right=right;
			}
			else if(left!=null && right==null){
				if(parent==null)
					tree.root=right;
				else if(parent.left==this)
					parent.left=left;
				else if(parent.right==this)
					parent.right=left;
			}
			else{
				left.findToDelete(this,point,tree);
			}
		}
		else if (line.compareTo(content, point)<=0){
			if(left!=null)
				left.delete(line, point,tree);
		}
		else{
			if(right!=null)
				right.delete(line, point,tree);
		}
	}
	
	public void findToDelete(LineNode n,float point,LineTree tree){
		if(right!=null)
			right.findToDelete(n,point,tree);
		else{
			Line l = n.content;
			n.content=content;
			content = l;
			delete(l,point,tree);
		}
	}
	
	public LineNode findLine(Line line,float point){
		if(content==line){
			return this;
		}
		else{
			if (line.compareTo(content, point)<=0){
				if(left==null)
					return null;
				else
					return left.findLine(line, point);
			}
			else{
				if(right==null)
					return null;
				else
					return right.findLine(line, point);
			}
		}
	}
	
	public Line findRightmost(){
		if(right!=null)
			return right.findRightmost();
		else
			return content;
	}
	
	public Line findLeftmost(){
		if(left!=null)
			return left.findLeftmost();
		else
			return content;
	}
	
	public Line findPredecessor(){
		if (left!=null){
			return left.findRightmost();
		}
		else if (parent!=null && parent.right==this)
			return parent.content;
		else{
			LineNode p = parent;
			while(p!=null){
				if(p.parent!=null && p.parent.right==p)
					return p.parent.content;
				p = p.parent;
			}
			return null;
		}
			
	}
	
	public Line findSuccessor(){
		if (right!=null){
			return right.findLeftmost();
		}
		else if (parent!=null && parent.left==this)
			return parent.content;
		else{
			LineNode p = parent;
			while(p!=null){
				if(p.parent!=null && p.parent.left==p)
					return p.parent.content;
				p = p.parent;
			}
			return null;
		}
	}
}