import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;



@SuppressWarnings("serial")
public class CommandPanel extends JPanel implements ActionListener{

	JTextField depth;
	JTextField width;
	JTextField height;
	JTextField zoom;

	JCheckBox depthLayer;
	JCheckBox background;
	JCheckBox monocolor;
	
	ObjectPanel objPanel;
	LevelPanel lvlPanel;
	
	CommandPanel(){
		
//		GridLayout layout = new GridLayout(0,2);
//		this.setLayout(layout);
		
		JButton n = new JButton("New");
		this.add(n);
		n.setActionCommand("new");
		n.addActionListener(this);
		
		JButton l = new JButton("Load");
		this.add(l);
		l.setActionCommand("load");
		l.addActionListener(this);
		
		JButton s = new JButton("Save");
		this.add(s);
		s.setActionCommand("save");
		s.addActionListener(this);
		
		JLabel lab5 = new JLabel("Base depth: ", JLabel.LEFT);
		this.add(lab5);
		
		depth = new JTextField(10);
		this.add(depth);
		depth.setText("0");
		
		depth.addActionListener(new java.awt.event.ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
		       changeDepthView();      
		    }
		});
		
		JLabel lab1 = new JLabel("Level width: ", JLabel.LEFT);
		this.add(lab1);
		
		width = new JTextField(10);
		this.add(width);
		width.setText("6000");
		
		width.addActionListener(new java.awt.event.ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
		       update();      
		    }
		});
		
		JLabel lab2 = new JLabel("Level height: ", JLabel.LEFT);
		this.add(lab2);
		
		height = new JTextField(10);
		this.add(height);
		height.setText("2000");
		
		height.addActionListener(new java.awt.event.ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
		       update();      
		    }
		});
		
		JLabel lab3 = new JLabel("Editor zoom: ", JLabel.LEFT);
		this.add(lab3);
		
		zoom = new JTextField(10);
		this.add(zoom);
		zoom.setText("1");
		
		zoom.addActionListener(new java.awt.event.ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
		       update();      
		    }
		});
		
		JLabel lab7 = new JLabel("Show background ", JLabel.LEFT);
		this.add(lab7);
		
		background = new JCheckBox();
		this.add(background);
		
		background.addActionListener(new java.awt.event.ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
		       lvlPanel.level.repaint();     
		    }
		});
		background.setSelected(true);
		
		JLabel lab8 = new JLabel("Place in monocolor", JLabel.LEFT);
		this.add(lab8);
		
		monocolor = new JCheckBox();
		this.add(monocolor);			
		JLabel lab6 = new JLabel("Show only one depth layer ", JLabel.LEFT);
		this.add(lab6);
		
		monocolor.addActionListener(new java.awt.event.ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
		       lvlPanel.level.repaint();     
		    }
		});
		
		depthLayer = new JCheckBox();
		this.add(depthLayer);
		
		depthLayer.addActionListener(new java.awt.event.ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
		       lvlPanel.level.repaint();     
		    }
		});
		
		
	}
	
	public void changeDepthView(){
		int d = Integer.valueOf(depth.getText());
		lvlPanel.level.baseDepth = d;
		lvlPanel.level.repaint();
	}
	
	public boolean getMonocolored(){
		return monocolor.isSelected();
	}
	
	public boolean getPaintBackground(){
		return background.isSelected();
	}
	
	public boolean getOnlyOneLayer(){
		return depthLayer.isSelected();
	}
	
	public int getBaseDepth(){
		return Integer.valueOf(depth.getText());
	}
	
	public void actionPerformed(ActionEvent e) {
		if("save".equals(e.getActionCommand())) saveLevel();
		if("load".equals(e.getActionCommand())) loadLevel();
		if("update".equals(e.getActionCommand())) update();
		if("new".equals(e.getActionCommand())) resetLevel();
	}
	
	public double logOfBase(double base, double num) {
	    return Math.log(num) / Math.log(base);
	}
	
	public void resetLevel(){
		lvlPanel.level.imageObjects = new ArrayList<ImageObject>();
		lvlPanel.level.repaint();
	}
	
	public void loadLevel(){
		JFileChooser fc = new JFileChooser();
		String workingDir = System.getProperty("user.dir");
		fc.setCurrentDirectory(new File(workingDir+"./Levels"));
		fc.setApproveButtonText("Load");
		int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String w = reader.readLine();
				String h = reader.readLine();
				width.setText(w);
				height.setText(h);
				update();
				String line = reader.readLine();
				lvlPanel.level.imageObjects = new ArrayList<ImageObject>();
				while(line!=null){
					String[] tokens = line.split(" ");
					if(tokens.length>0){
						int notches = (int) Math.round(logOfBase(0.9,Double.valueOf(tokens[4])));
						lvlPanel.level.loadObject(new double[]{Double.valueOf(tokens[1]),Double.valueOf(tokens[2])}, 
								Double.valueOf(tokens[0]), Double.valueOf(tokens[3]), notches,
								Double.valueOf(tokens[5]),Boolean.valueOf(tokens[6]),Boolean.valueOf(tokens[7]),
								Boolean.valueOf(tokens[8]), Boolean.valueOf(tokens[9]), Double.valueOf(tokens[10]),
								Double.valueOf(tokens[11]), Double.valueOf(tokens[12]),Double.valueOf(tokens[13]));
					}
					line = reader.readLine();
				}
				lvlPanel.level.repaint();		
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
	}
	
	public void saveLevel(){
		float w = Float.valueOf(width.getText());
		float h = Float.valueOf(height.getText());
		JFileChooser fc = new JFileChooser();
		String workingDir = System.getProperty("user.dir");
		fc.setCurrentDirectory(new File(workingDir+"./Levels"));
		fc.setApproveButtonText("Save");
		int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
            	String filePath = file.getAbsolutePath();
            	if(!filePath.endsWith(".lvl")) {
            	    file = new File(filePath + ".lvl");
            	}
    			PrintWriter writer = new PrintWriter(file);
    			writer.println(w);
    			writer.println(h);
    			for(ImageObject io : lvlPanel.level.imageObjects){
    				double[] center = lvlPanel.level.returnCenter(io);
    				double[] dimensions = lvlPanel.level.returnDimensions(io);
    				double scale = Math.pow(0.9,io.notches);
    				int rotated = io.rotated*90;
    				writer.println(io.type+" "+center[0]+" "+center[1]+" "+io.depth+" "+scale+" "+
    						rotated+" "+io.reflectedLR+" "+io.reflectedUD+" "+io.isDark+" "+
    						io.hasModel+" "+dimensions[0]+" "+dimensions[1]+" "+dimensions[2]+" "
    						+dimensions[3]);
    			}
    			writer.close();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        } 
		
	}
	
	public void changeZoom(int notches){
		float z= Float.valueOf(zoom.getText());
		double nz = ((int)((z * Math.pow(0.9,notches))*100))/100.0;
		zoom.setText(nz+"");
		update();
	}
	
	public void update(){
		float z= Float.valueOf(zoom.getText())*4;
		float w = Float.valueOf(width.getText());
		float h = Float.valueOf(height.getText());
		lvlPanel.update(-(int)(w/2), (int)(w/2), -(int)(h/2), (int)(h/2), z);
	}
}


