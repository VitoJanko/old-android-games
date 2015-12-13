import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class LevelPanel extends JPanel implements KeyListener{

	Level level;
	JScrollPane scrollFrame;
	ObjectPanel objPanel;
	CommandPanel command;

	float FOVX = 85;
	float FOVY = 50;
	float distance = 2000;
	int leftBound = -3000;
	int rightBound = 3000;
	int topBound = 1000;
	int bottomBound = -1000;
	
	float pixelRatio = 4;
	
	LevelPanel(){
		level = new Level();
		level.setPreferredSize(new Dimension((int)((rightBound-leftBound)/pixelRatio),
				(int)((topBound-bottomBound)/pixelRatio)));
		scrollFrame = new JScrollPane(level);
		level.setAutoscrolls(true);
		level.setFocusable(true);
		level.requestFocusInWindow();
		level.outer=this;
		level.pixelRatio = pixelRatio;
		//double screenWidth = 2*distance * Math.tan((FOVX/2)*(Math.PI/180.0)); 
		double screenHeight = 2*distance * Math.tan((FOVY/2)*(Math.PI/180.0)); 
		double screenWidth = screenHeight*1.8;
		System.out.println(screenWidth);
		System.out.println(screenHeight);
		scrollFrame.setPreferredSize(new Dimension((int)(screenWidth/pixelRatio),(int)(screenHeight/pixelRatio)));
		this.add(scrollFrame);
		addKeyListener(this);
	}

	public void update(int leftBound, int rightBound,int bottomBound,int topBound, float pixelRatio){
		this.leftBound = leftBound;
		this.rightBound = rightBound;
		this.topBound = topBound;
		this.bottomBound = bottomBound;
		this.pixelRatio = pixelRatio;
		updateScroller();
		level.updateObjects(pixelRatio);
		level.invalidate();
	}
	
	public void updateScroller(){
		level.setPreferredSize(new Dimension((int)(level.endsetX+level.offsetX+(rightBound-leftBound)/pixelRatio),
				(int)(level.endsetY+level.offsetY+(topBound-bottomBound)/pixelRatio)));
		scrollFrame.revalidate();
	}
	
	
	@Override
	public void keyPressed(KeyEvent e) {
		
	}


	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	


}

@SuppressWarnings("serial")
class Level extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener{
	LevelPanel outer;
	int width;
	int height;
	
	int currentX;
	int currentY;
	
	int lastX;
	int lastY;
	
	float pixelRatio;
	
	double offsetX;
	double offsetY;
	
	double endsetX;
	double endsetY;
	
	int baseDepth;
	
	ArrayList<ImageObject> imageObjects;
	ArrayList<BufferedImage> images;
	ImageObject current = null;
	
	boolean controlPressed;
	boolean shiftPressed;
	boolean altPressed;
	
	BufferedImage background;
	
	Level(){
		images = new ArrayList<BufferedImage>();
		imageObjects = new ArrayList<ImageObject>();
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		addKeyListener(this);
		
		try {
			background = ImageIO.read(new File("./Background/blue512.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void reset(){
		current = null;
		for(ImageObject c : imageObjects)
			c.selected = false;
		repaint();
	}
	
	public void updateObjects(float pixelRatioN){
		for(ImageObject io: imageObjects){
			io.centerX*=(pixelRatio/pixelRatioN);
			io.centerY*=(pixelRatio/pixelRatioN);
		}
		pixelRatio = pixelRatioN;
		repaint();
	}
	
	public void loadObject(double[] center, double type, double depth, double notches, double rotation, 
			boolean reflectLR, boolean reflectUD, boolean isDark, boolean hasModel, double x1, double y1,
			double x2, double y2){
		double x = (center[0]-outer.leftBound)/pixelRatio;
		double y = (-center[1]+(outer.topBound-outer.bottomBound)/2)/pixelRatio;
		ImageObject model = outer.objPanel.objects.models.get((int)type);
		ImageObject n = new ImageObject(model.image, model.trimmed, (int)type);
		n.giveCenter(x, y);
		n.giveDimensions(model.realWidth,model.realHeight);
		n.giveSize((int)depth, (int)notches);
		n.hasModel = hasModel;
		n.giveReflection((int)(rotation/90), reflectLR, reflectUD);
		if(isDark){
			BufferedImage ni = copyImage(n.trimmed.image);
			Utility.gray(ni);
			n.trimmed.image = ni;
			n.isDark = true;
		}
		imageObjects.add(n);
	}
	
	public double[] returnCenter(ImageObject io){
		double cx = io.centerX*pixelRatio+outer.leftBound;
		double cy = -io.centerY*pixelRatio+(outer.topBound-outer.bottomBound)/2;
		return new double[] {cx, cy};
	}
	
	public double[] returnDimensions(ImageObject io){
		double width = io.x2-io.x1;
		double height = io.y2- io.y1;
		double leftBorder = io.trimmed.leftBorder*width;
		double rightBorder = io.trimmed.rightBorder*width;
		double topBorder = io.trimmed.topBorder*height;
		double bottomBorder = io.trimmed.bottomBorder*height;
		
		double x1 = (io.x1-offsetX-leftBorder)*pixelRatio+outer.leftBound;
		double y1 = -(io.y1-offsetY-topBorder)*pixelRatio+(outer.topBound-outer.bottomBound)/2;
		double x2 = (io.x2-offsetX+rightBorder)*pixelRatio+outer.leftBound;
		double y2 = -(io.y2-offsetY+bottomBorder)*pixelRatio+(outer.topBound-outer.bottomBound)/2;
		
		return new double[]{x1,y1,x2,y2};

	}
	
	public double getDepthSize(int depth){
		return outer.distance/(double)(outer.distance-depth);
	}
	
	public static BufferedImage copyImage(BufferedImage source){
	    BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
	    Graphics g = b.getGraphics();
	    g.drawImage(source, 0, 0, null);
	    g.dispose();
	    return b;
	}
	

	public void reflectRotate(ImageObject io){
		BufferedImage image = copyImage(io.trimmed.image);
		BufferedImage img = copyImage(io.trimmed.image);
		if(io.rotated!=0){
			double rotationRequired = Math.toRadians(90*io.rotated);
			double locationX = image.getWidth() / 2;
			double locationY = image.getHeight() / 2;
			AffineTransform txx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
			if(io.rotated==1)
				txx.translate((locationX-locationY),(locationX-locationY));
			if(io.rotated==3)
				txx.translate((locationY-locationX),-(locationX-locationY));
			AffineTransformOp opp = new AffineTransformOp(txx, AffineTransformOp.TYPE_BILINEAR);
	    	img = new BufferedImage(image.getHeight(), image.getWidth(), image.getType());
	    	if(io.rotated%2==0)
	    		img = new BufferedImage( image.getWidth(),image.getHeight(), image.getType());
			opp.filter(image, img);
		}
		if(io.reflectedUD){
			AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
			tx.translate(0, -img.getHeight(null));
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			img = op.filter(img, null);
		}
		if(io.reflectedLR){
			AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
			tx.translate(-img.getWidth(null), 0);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			img = op.filter(img, null);
		}
		io.change = false;
		io.transformed = img;
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		width = getWidth();
		height = getHeight();
		if(outer.command.getPaintBackground()){
			g.drawImage(background, 0,0,width,height,null);
		}
		Graphics2D g2 = (Graphics2D) g;
	    g2.setStroke(new BasicStroke(2));
		updateDepthLayer(g2);
		for(ImageObject i : imageObjects)
			calculateCoordinates(i);	
		g2.setColor(Color.BLACK);
		g2.drawRect((int)offsetX, (int)offsetY, (int)((outer.rightBound-outer.leftBound)/pixelRatio),
				(int)((outer.topBound-outer.bottomBound)/pixelRatio));
		Collections.sort(imageObjects);
		for(ImageObject io : imageObjects){
			if(!outer.command.getOnlyOneLayer() || outer.command.getBaseDepth()==io.depth){
				if(io.selected){
					drawSelected(io,g2);
					if(altPressed)
						drawTooltip(g,io);
				}
				if(io.change)
					reflectRotate(io);
				g.drawImage(io.transformed,(int)io.x1,(int)io.y1,(int)(io.x2-io.x1),(int)(io.y2-io.y1),null);
			}
		}
		
		ImageObject io = outer.objPanel.objects.current;
		
		if(io!=null){
			if(io.change)
				reflectRotate(io);
			double[] coords = returnCoordinates(io);
			g.drawImage(io.transformed,(int)coords[0],(int)coords[2],
					(int)(coords[1]-coords[0]), (int)(coords[3]-coords[2]),null);
			drawSelected(io,g2);
		}
	}

	public void updateDepthLayer(Graphics g){
		g.setColor(Color.GREEN);
		double width = (outer.rightBound-outer.leftBound)/pixelRatio;
		double height = (outer.topBound-outer.bottomBound)/pixelRatio;
		double diffX = -baseDepth * Math.tan((outer.FOVX/2)*(Math.PI/180.0))/pixelRatio; 
		double diffY = -baseDepth * Math.tan((outer.FOVY/2)*(Math.PI/180.0))/pixelRatio; 
		double newHeight = height+2*diffY;
		double newWidth = width+2*diffX;
		
		
		boolean change = false;
		if(diffX>offsetX){
			offsetX = diffX;
			change = true;
		}
		if(diffX>endsetX){
			endsetX = diffX;
			change = true;
		}
		if(diffY>offsetY){
			offsetY = diffY;
			change = true;
		}
		if(diffY>endsetY){
			endsetY = diffY;
			change = true;
		}
		if(change)
			outer.updateScroller();
		if(baseDepth!=0)
			g.drawRect((int)(offsetX-diffX), (int)(offsetY-diffY), (int)(newWidth),(int)(newHeight));
	}
	
	public void drawSelected(ImageObject io, Graphics g2){
		double[] coords = returnCoordinates(io);
		double x1=coords[0], x2 = coords[1], y1=coords[2], y2=coords[3];
		g2.setColor(Color.RED);
		g2.drawRect((int)x1,(int)y1,(int)(x2-x1),(int)(y2-y1));
		FontMetrics metrics = g2.getFontMetrics();
		int h = metrics.getHeight();
		g2.drawString("Depth: "+io.depth, (int)(x2+10), (int)(y1+h));
		double scale = ((int)(Math.pow(0.9,io.notches)*100))/100.0;
		g2.drawString("Scale: "+scale, (int)(x2+10), (int)(y1+3*h));
	}
	
	public void drawTooltip(Graphics g,ImageObject io){
		g.setColor(Color.BLUE);
		double width = io.x2-io.x1;
		double height = io.y2- io.y1;
		double leftBorder = io.trimmed.leftBorder*width;
		double rightBorder = io.trimmed.rightBorder*width;
		double topBorder = io.trimmed.topBorder*height;
		double bottomBorder = io.trimmed.bottomBorder*height;
		g.drawRect((int)(io.x1-leftBorder),(int)(io.y1-topBorder),
				(int)(io.x2-io.x1+rightBorder+leftBorder),(int)(io.y2-io.y1+bottomBorder+topBorder));
//		System.out.println("Width: "+(io.x2-io.x1+rightBorder+leftBorder));
//		System.out.println("Height: "+(io.y2-io.y1+bottomBorder+topBorder));
	}
	
	@Override
	public void mouseDragged(MouseEvent event) {
		int x = event.getX();
		int y = event.getY();
		if(SwingUtilities.isLeftMouseButton(event)){
			if(current!=null && lastX!=-1){
				double cx = current.centerX+(x-lastX);
				double cy = current.centerY+(y-lastY);
				current.giveCenter(cx, cy);
			}
		}
		if(SwingUtilities.isMiddleMouseButton(event)){
			if(lastX!=-1){
				offsetX += (x-lastX);
				offsetY += (y-lastY);
				outer.updateScroller();
			}
		}
		
		lastX = x;
		lastY = y;
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		currentX = event.getX();
		currentY = event.getY();
		if(outer.objPanel.objects.current!=null){
			outer.objPanel.objects.current.giveCenter(currentX-offsetX, currentY-offsetY);
		}
		repaint();
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public double[] returnCoordinates(ImageObject io){
		double cx = io.centerX+offsetX;
		double cy = io.centerY+offsetY;
		double imageWidth = (io.realWidth/pixelRatio)* Math.pow(0.9,io.notches)*getDepthSize(io.depth);
		double imageHeight= (io.realHeight/pixelRatio)* Math.pow(0.9,io.notches)*getDepthSize(io.depth);
		if(io.rotated%2==1){
			double temp = imageWidth;
			imageWidth = imageHeight;
			imageHeight = temp;
		}
		return new double[]{cx-imageWidth/2, cx+imageWidth/2, cy-imageHeight/2, cy+imageHeight/2};
	}
	
	public void calculateCoordinates(ImageObject io){
		double[] coord = returnCoordinates(io);
		io.giveCoordinates(coord[0],coord[1],coord[2],coord[3]);
	}
	
	@Override
	public void mousePressed(MouseEvent event) {
		int x = event.getX();
		int y = event.getY();
		for(ImageObject i : imageObjects)
			calculateCoordinates(i);
		if(SwingUtilities.isLeftMouseButton(event)){
			ImageObject io = outer.objPanel.objects.current;
			if(io!=null){
				ImageObject n = new ImageObject(io.image,io.trimmed,io.type);
				n.giveCenter(x-offsetX, y-offsetY);
				n.giveDimensions(io.realWidth,io.realHeight);
				n.giveSize(io.depth, io.notches);
				n.giveReflection(io.rotated,io.reflectedLR,io.reflectedUD);
				n.giveModelType(io.name, io.hasModel);
				imageObjects.add(n);
				if(outer.command.getMonocolored()){
					BufferedImage ni = copyImage(io.trimmed.image);
					Utility.gray(ni);
					n.trimmed.image = copyImage(ni);
					n.isDark = true;
				}
			}
			else{
				reset();
				Collections.sort(imageObjects);
				for(int i=imageObjects.size()-1; i>=0; i--){
					ImageObject c = imageObjects.get(i);
					if(!outer.command.getOnlyOneLayer() || outer.command.getBaseDepth()==c.depth){
						if(x>c.x1 && x<c.x2 && y>c.y1 && y<c.y2){
							c.selected=true;
							current = c;
							repaint();
							break;
						}
					}
				}
			}
		}
		if(SwingUtilities.isRightMouseButton(event)){
			if(outer.objPanel.objects.current!=null){
				outer.objPanel.objects.reset();
			}
			else if (current!=null)
				reset();
			else{
				Collections.sort(imageObjects);
				for(int i=imageObjects.size()-1; i>=0; i--){
					ImageObject c = imageObjects.get(i);
					if(!outer.command.getOnlyOneLayer() || outer.command.getBaseDepth()==c.depth){
						c.selected = false;
						if(x>c.x1 && x<c.x2 && y>c.y1 && y<c.y2){
							imageObjects.remove(c);
							break;
						}
					}
				}
			}
		}
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		lastX = -1;
		lastY = -1;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent event) {
		int notch = event.getWheelRotation();
		if(shiftPressed){
			outer.command.changeZoom(-notch);
		}
		else if(outer.objPanel.objects.current!=null){
			if(!controlPressed)
				outer.objPanel.objects.current.notches+=notch;
			else
				outer.objPanel.objects.current.depth+=10*notch;
		}
		else if(current!=null){
			if(!controlPressed)
				current.notches+=notch;
			else
				current.depth+=10*notch;
		}
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		ImageObject io = outer.objPanel.objects.current;
		JViewport jp= outer.scrollFrame.getViewport();
		Point p = jp.getViewPosition();
		if(e.getKeyCode()==KeyEvent.VK_CONTROL){
			controlPressed = true;
		}
		if(e.getKeyCode()==KeyEvent.VK_SHIFT){
			shiftPressed = true;
		}
		if(e.getKeyCode()==KeyEvent.VK_SPACE){
			altPressed = true;
		}
		if(e.getKeyCode()==KeyEvent.VK_E){
			if(current!=null){
				current.reflectedLR = !current.reflectedLR;
				current.change = true;
			}
			if(io!=null){
				io.reflectedLR = !io.reflectedLR;
				io.change = true;
			}
		}
		if(e.getKeyCode()==KeyEvent.VK_R){
			if(current!=null){
				current.reflectedUD = !current.reflectedUD;
				current.change = true;
			}
			if(io!=null){
				io.reflectedUD = !io.reflectedUD;
				io.change = true;
			}
		}
		if(e.getKeyCode()==KeyEvent.VK_Q){
			if(current!=null){
				current.rotated = (current.rotated+1)%4;
				current.change = true;
			}
			if(io!=null){
				io.rotated = (io.rotated+1)%4;
				io.change = true;
			}
		}
		if(e.getKeyCode()==KeyEvent.VK_S){
			jp.setViewPosition(new Point(p.x,p.y+10));
			outer.scrollFrame.revalidate();
		}
		if(e.getKeyCode()==KeyEvent.VK_W){
			jp.setViewPosition(new Point(p.x,Math.max(0,p.y-10)));
			outer.scrollFrame.revalidate();
		}
		if(e.getKeyCode()==KeyEvent.VK_A){
			jp.setViewPosition(new Point(Math.max(0, p.x-10),p.y));
			outer.scrollFrame.revalidate();
		}
		if(e.getKeyCode()==KeyEvent.VK_D){
			jp.setViewPosition(new Point(p.x+10,p.y));
			outer.scrollFrame.revalidate();
		}
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_CONTROL){
			controlPressed = false;
		}
		if(e.getKeyCode()==KeyEvent.VK_SHIFT){
			shiftPressed = false;
		}
		if(e.getKeyCode()==KeyEvent.VK_SPACE){
			altPressed = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}
	
}
