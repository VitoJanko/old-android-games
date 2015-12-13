import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.DataBufferByte;

public class Utility {

	public static void gray(BufferedImage source) {
		int rgb=new Color(0,100,120,255).getRGB();

		for(int i=0; i<source.getWidth(); i++){
			for(int j = 0; j<source.getHeight(); j++){
				int color = source.getRGB(i, j);
				int alpha = (color>>24) & 0xff;
				if(alpha!=0)
					source.setRGB(i,j,rgb);
			}
		}
    }
	
	
	 public static BufferedImage desaturate(BufferedImage source) {
	        ColorConvertOp colorConvert = new ColorConvertOp(ColorSpace
	                .getInstance(ColorSpace.CS_GRAY), null);
	        colorConvert.filter(source, source);
	        return source;
	    }

	
	public static TrimmedImage trimImage(BufferedImage img) {
		final byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
		int width = img.getWidth();
		int height = img.getHeight();
		int x0, y0, x1, y1;                      // the new corners of the trimmed image
		int i, j;                                // i - horizontal iterator; j - vertical iterator
		leftLoop:
			for (i = 0; i < width; i++) {
				for (j = 0; j < height; j++) {
					if (pixels[(j*width+i)*4] != 0) { // alpha is the very first byte and then every fourth one
						break leftLoop;
					}
				}
			}
		x0 = i;
		topLoop:
			for (j = 0; j < height; j++) {
				for (i = 0; i < width; i++) {
					if (pixels[(j*width+i)*4] != 0) {
						break topLoop;
					}
				}
			}
		y0 = j;
		rightLoop:
			for (i = width-1; i >= 0; i--) {
				for (j = 0; j < height; j++) {
					if (pixels[(j*width+i)*4] != 0) {
						break rightLoop;
					}
				}
			}
		x1 = i+1;
		bottomLoop:
			for (j = height-1; j >= 0; j--) {
				for (i = 0; i < width; i++) {
					if (pixels[(j*width+i)*4] != 0) {
						break bottomLoop;
					}
				}
			}
		y1 = j+1;
		
		double newWidth = x1-x0;
		double newHeight = y1-y0;
		
		
		//System.out.println("Picture ratio: "+(newWidth/newHeight));
		
		return new TrimmedImage(x0/newWidth,(width-x1)/newWidth,y0/newHeight,
				(height-y1)/newHeight,(newHeight/newWidth),img.getSubimage(x0, y0, x1-x0, y1-y0));
	}
}

class TrimmedImage{
	double leftBorder;
	double rightBorder;
	double topBorder;
	double bottomBorder;
	double ratio; 
	BufferedImage image;
	
	public TrimmedImage(double leftBorder, double rightBorder, double topBorder,
			double bottomBorder, double ratio, BufferedImage image) {
		super();
		this.leftBorder = leftBorder;
		this.rightBorder = rightBorder;
		this.topBorder = topBorder;
		this.bottomBorder = bottomBorder;
		this.image = image;
		this.ratio = ratio;
	}
	
}

