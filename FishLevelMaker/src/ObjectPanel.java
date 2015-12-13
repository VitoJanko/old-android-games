import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class ObjectPanel extends JPanel{
	
	Objects objects;
	JScrollPane scrollFrame;
	LevelPanel levelPanel;
	CommandPanel command;
	
	ObjectPanel(){
		objects = new Objects();
		objects.setPreferredSize(new Dimension(240,500));
		objects.setFocusable(true);
		scrollFrame = new JScrollPane(objects);
		objects.setAutoscrolls(true);
		objects.outer=this;
		scrollFrame.setPreferredSize(new Dimension(objects.imageBorder*3+objects.imageSize*2,530));
		this.add(scrollFrame);
	}
	
}

class Objects extends JPanel implements MouseListener{

	private static final long serialVersionUID = 1L;
	ArrayList<Integer[]> colorsPolyp;
	ArrayList<Integer[]> colorsBackground;
	//ArrayList<BufferedImage> images;
	ArrayList<ImageObject> imageObjects;
	HashMap<String,Integer> types;
	HashMap<Integer,ImageObject> models;
	int maxType = -1;
	int width;
	int height;
	int size = 17;
	int imageSize = 100;
	int imageBorder = 30;
	ObjectPanel outer;
	ImageObject current;
	
	Objects(){
		colorsPolyp = new ArrayList<Integer[]>();
		colorsBackground = new ArrayList<Integer[]>();
		loadTypes();
		loadImages();
		loadData();
		saveTypes();
		addMouseListener(this);
	}
	
	public void saveTypes(){
		try {
			PrintWriter writer = new PrintWriter(new File("./Objects.txt"));
			for(String key : types.keySet()){
				int type = types.get(key);
				writer.println(type+" "+key);
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadTypes(){
		try {
			BufferedReader reader = new BufferedReader(new FileReader("./Objects.txt"));
			types = new HashMap<String,Integer>();
			String line = reader.readLine();
			while(line!=null){
				String[] tokens = line.split(" ");
				if(tokens.length>=2){
					int type = Integer.valueOf(tokens[0]);
					types.put(tokens[1], type);
					if(type>maxType)
						maxType = type;
				}
				line = reader.readLine();
			}
			
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void loadData(){
		try{
			File EEFolder = new File("./ObjectModels");
			File[] files = null;
			if (EEFolder.isDirectory()){
				files = EEFolder.listFiles();
				for(ImageObject io : imageObjects){
					boolean found = false;
					for(int i=0; i<files.length; i++){
						if(files[i].getName().equals(io.name+".obj")){
							BufferedReader reader = new BufferedReader(new FileReader(files[i]));
							String line = reader.readLine();
							float minX = Float.MAX_VALUE;
							float maxX = Float.MIN_VALUE;
							float minY = Float.MAX_VALUE;
							float maxY = Float.MIN_VALUE;
							while(line!=null){
								String[] tokens = line.split(" ");
								if(tokens[0].equals("v")){
									float x = Float.valueOf(tokens[1]);
									float y = Float.valueOf(tokens[2]);
									if(x<minX) minX = x;
									if(x>maxX) maxX = x;
									if(y<minY) minY = y;
									if(y>maxY) maxY = y;
								}
								line = reader.readLine();
							}
							io.realHeight=maxY-minY;
							io.realWidth=maxX-minX;
							reader.close();
							found = true;
							io.hasModel = true;
							break;
						}
					}
					if(!found){
						//System.out.println(io.name);
						io.hasModel = false;
						if(io.trimmed.ratio<=1){
							io.realWidth = 500;
							io.realHeight = io.realWidth*io.trimmed.ratio;
						}
						else{
							io.realHeight = 500;
							io.realWidth = io.realHeight*(1/io.trimmed.ratio);
						}
					}
				}
			}
			//System.out.println(imageObjects.size());
		}
		catch(Exception e){e.printStackTrace();}
	}
	
	public void loadImages(){
		try{
			models = new HashMap<Integer,ImageObject>();
			imageObjects = new ArrayList<ImageObject>();
			File EEFolder = new File("./ObjectDrawables");
			File[] files = null;
			if (EEFolder.isDirectory()){
				files = EEFolder.listFiles();
				for(File file : files){
					if(!file.getName().contains(".svn")){
						BufferedImage img = ImageIO.read(file);
						int type =-1;
						String filename = file.getName().split("\\.")[0];
						if(types.containsKey(filename))
							type = types.get(filename);
						else{
							type = maxType+1;
							maxType++;
							types.put(filename,type);
						}
						System.out.println(filename);
						ImageObject model = new ImageObject(img,Utility.trimImage(img),type);
						model.name = filename;
						imageObjects.add(model);
						models.put(type, model);
						//System.out.println(img.getWidth() +" "+ img.getHeight());
						
					}
				}
			}
			System.out.println(imageObjects.size());
		}
		catch(Exception e){e.printStackTrace();}
	}
	
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		width = getWidth();
		height = getHeight();
		
		 g.setColor(new Color(0,150,200));
		 g.fillRect(0, 0, width, height);
		
		int startY = imageBorder;
		int startX = imageBorder;
		boolean left = true;
		
		for(ImageObject io : imageObjects){
			BufferedImage img = io.image;
			g.drawImage(img,startX,startY,imageSize,imageSize,null);
			io.giveCoordinates(startX, startX+imageSize, startY, startY+imageSize);
			if(io.selected){
				Graphics2D g2 = (Graphics2D) g;
			    g2.setStroke(new BasicStroke(3));
			    g2.setColor(Color.RED);
			    g2.drawRect(startX, startY, imageSize,imageSize);
			}
			if(left)
				startX+=imageSize+imageBorder;
			else{
				startX-=imageSize+imageBorder;
				startY+=imageSize+imageBorder;
			}
			left=!left;
			
			Dimension preffered = getPreferredSize();
			if(preffered.height<startY+imageSize){
				setPreferredSize(new Dimension(preffered.width,startY+imageSize));
				revalidate();
				repaint();
			}
		}
	}

	public void reset(){
		current = null;
		for(ImageObject c : imageObjects){
			c.selected = false;
			c.notches = 0;
		}
		repaint();
	}
	
	@Override
	public void mousePressed(MouseEvent event) {
		Point p = event.getPoint();
		int x = p.x;
		int y = p.y;
		for(ImageObject c : imageObjects){
			c.selected = false;
			c.notches = 0;
			if(x>c.x1 && x<c.x2 && y>c.y1 && y<c.y2){
				c.selected=true;
				c.depth = outer.command.getBaseDepth();
				current = c;
				repaint();
			}
		}
		outer.levelPanel.level.reset();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}

class ImageObject implements Comparable<ImageObject>{
	double x1;
	double x2;
	double y1;
	double y2;
	int depth;
	double centerX;
	double centerY;
	double realWidth;
	double realHeight;
	BufferedImage image;
	BufferedImage transformed;
	TrimmedImage trimmed;
	boolean selected;
	int notches;
	int type;
	int rotated ;
	boolean reflectedLR;
	boolean reflectedUD;
	boolean change;
	String name;
	boolean hasModel;
	boolean isDark;

	public ImageObject(BufferedImage image, TrimmedImage trimmed, int type) {
		super();
		this.image = image;
		this.type = type;
		this.transformed = trimmed.image;	
		selected = false;
		notches = 0;
		depth = 0;
		rotated = 0;
		reflectedLR = false;
		reflectedUD = false;
		change = true;
		TrimmedImage trimm = new TrimmedImage(trimmed.leftBorder,trimmed.rightBorder,trimmed.topBorder,
				trimmed.bottomBorder,trimmed.ratio,trimmed.image);
		this.trimmed = trimm;
	}

	public void giveModelType(String name, boolean hasModel){
		this.name = name;
		this.hasModel = hasModel;
	}
	
	public void giveCoordinates(double x1, double x2, double y1, double y2){
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}
	
	public void giveSize(int depth, int notches){
		this.depth = depth;
		this.notches = notches;
	}
	
	public void giveCenter(double x, double y){
		this.centerX = x;
		this.centerY = y;
	}
	
	public void giveDimensions(double width, double height){
		this.realWidth = width;
		this.realHeight =  height;
	}

	public void giveReflection(int rotation, boolean reflectionLR, boolean reflectionUD){
		this.rotated = rotation;
		this.reflectedLR = reflectionLR;
		this.reflectedUD = reflectionUD;
	}
	
	
	@Override
	public int compareTo(ImageObject img) {
		return depth-img.depth;
	}

}
