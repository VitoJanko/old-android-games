import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;




public class PanelOpener {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	     
		ObjectPanel object = new ObjectPanel();
		LevelPanel level = new LevelPanel();
		CommandPanel command = new CommandPanel();
		level.command = command;
		level.objPanel = object;
		object.levelPanel = level;
		object.command = command;
		object.objects.addKeyListener(level.level);
		command.setFocusable(true);
		command.addKeyListener(level.level);
		command.objPanel = object;
		command.lvlPanel = level;
		
	    Okno o = new Okno("Objects",900,100,300,600);
	    o.add(object);
	    o.setVisible(true);
	    
	    Okno o2 = new Okno("Level",100,100,800,480);
	    o2.add(level);
	    o2.setVisible(true);
	    
	    Okno o3 = new Okno("Menu",1200,100,250,600);
	    o3.add(command);
	    o3.setVisible(true);
	    
	}
}


@SuppressWarnings("serial")
class Okno extends JFrame {
	  public Okno(String naslov, int x, int y, int sirina, int visina) {
	    setTitle(naslov);
	    setLocation(x,y);
	    setSize(sirina,visina);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  }
	  
	  public Okno(String naslov) {
	    setTitle(naslov);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    Toolkit tk=Toolkit.getDefaultToolkit();
	    Dimension d=tk.getScreenSize();
	    int sirina=d.width;
	    int visina=d.height;
	    setBounds(sirina/4,visina/4,sirina/3,visina/2);
	  } 
	  
	  public Okno(String naslov, int i) {
		    setTitle(naslov);
		    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    Toolkit tk=Toolkit.getDefaultToolkit();
		    Dimension d=tk.getScreenSize();
		    int sirina=d.width;
		    int visina=d.height;
		    setBounds(sirina/4+sirina/3+30,visina/4,sirina/4,visina/3);
		  } 
}