package aNewWorld0001;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class DrawDetails {
	
	//image to draw
	private Image img;
	
	//Position
	private int posx;
	private int posy;
	
	//totalwidth and height
	private int width;
	private int height;
	
	public DrawDetails() {
		
	}
	
	
	/**
	 * Returns the image to be drawn
	 * @return
	 */
	public Image getImage() {
		return this.img;
	}
	/**
	 * sets the image to be drawn
	 */
	public void setImage(Image img) {
		this.img = img;
	}

	/**
	 * returns x position (from left)
	 * @return
	 */
	public int getPosX() {
		return this.posx;
	}
	/**
	 * sets x position (from left)
	 * @param posX
	 */
	public void setPosX(int posX) {
		this.posx = posX;
	}

	/**
	 * returns y position (from above)
	 * @return
	 */
	public int getPosY() {
		return this.posy;
	}
	/**
	 * sets y position (from above)
	 * @param posY
	 */
	public void setPosY(int posY) {
		this.posy = posY;
	}

	/**
	 * returns width
	 * @return
	 */
	public int getWidth() {
		return this.width;
	}
	/**
	 * sets width
	 * @param width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * returns height
	 * @return
	 */
	public int getHeight() {
		return this.height;
	}
	/**
	 * sets height
	 * @param height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Setting all variables at once
	 * @param img
	 * the to be drawn image
	 * @param posx
	 * X position
	 * @param posy
	 * Y position
	 * @param width
	 * total width
	 * @param height
	 * total height
	 */
	public void setAll(Image img, int posx, int posy, int width, int height) {
		this.img = img;
		this.posx = posx;
		this.posy = posy;
		this.width = width;
		this.height = height;
	}
	
	// image altering static methods
	/**
	 * rotation of an (square) image
	 */
	public static BufferedImage rotate2(BufferedImage bufferedimage, int centerx, int centery, int degrees) {
		//First made a separate copy of the image
		BufferedImage image = new BufferedImage(bufferedimage.getWidth(), bufferedimage.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		//calculated the radians out of the degrees
		double radians = degrees * Math.PI / 180;
		
		//Basically a mask that alters the image, as far as I understood
		AffineTransform affine = new AffineTransform();
		
		//a mask to calculate the likely position of a coordinate after rotating it
		double[][] mask = {
				{Math.cos(radians), (Math.sin(radians) * -1)},
				{Math.sin(radians), Math.cos(radians)}
		};
		int zx, zy;
		
		//Calculating the new position of the centerpixel
		//and using it, to know how much to move the image to cancel out the movement of the rotation
		zx = (int)Math.round(centerx * mask[0][0] 
				+ centery * mask[0][1]);
		zx = (zx < 0 ? zx * -1 + (centerx) : (centerx) - zx);
		//above for X and below for Y
		zy = (int)Math.round((centerx * mask[1][0]) 
				+ (centery * mask[1][1]));
		zy = (zy < 0 ? zy * -1 + (centery) : (centery) - zy);
		
		//moving the image back to a position from where the rotation will move it to the original position
		affine.setToTranslation(zx, zy);
		//rotating the image
		affine.rotate(radians);
		
		//Not sure, but in my experience that class makes the mask apply to images and not just to the output
		AffineTransformOp affineOp = new AffineTransformOp(affine, AffineTransformOp.TYPE_BILINEAR);
		
		//alters the bufferedimage and pastes it into image
		affineOp.filter(bufferedimage, image);
		
		//returning of the rotated image
		return image;
	}
	
	/**
	 * closest attempt at rotating an image before giving up and using AffineTransform anyway
	 */
	public static BufferedImage rotate(BufferedImage bufferedimage, int centerx, int centery, int degrees) {
		BufferedImage image = new BufferedImage(bufferedimage.getWidth(), bufferedimage.getHeight(), BufferedImage.TYPE_INT_ARGB);
		//degrees = 45;
		double radians = degrees * Math.PI / 180;
		
		double[][] mask = {
				{Math.cos(radians), (Math.sin(radians) * -1)},
				{Math.sin(radians), Math.cos(radians)}
		};
		int x, y, zx, zy;
		
		zx = (int)Math.round(centerx * mask[0][0] 
				+ centery * mask[0][1]);
		zx = (zx < 0 ? zx * -1 + (centerx + 1) : (centerx + 1) - zx);
		zy = (int)Math.round((centerx * mask[1][0]) 
				+ (centery * mask[1][1]));
		zy = (zy < 0 ? zy * -1 + (centery + 1) : (centery + 1) - zy);
		
		for(int i = 0; i < bufferedimage.getWidth(); i++) {
			for(int j = 0; j < bufferedimage.getHeight(); j++) {
				x = (int) Math.round(i * mask[0][0] + j * mask[0][1]);
				y = (int) Math.round(i * mask[1][0] + j * mask[1][1]);
				x += zx;
				y += zy;
				if(x < 0 || y < 0 || x >= image.getWidth() || y >= image.getHeight()) {
					System.out.print("(" + x + "/" + y + "); ");
					continue;
				}
				image.setRGB(x, y, 
						bufferedimage.getRGB(i, j));
			}
		}
		
		return image;
	}
	
	/** 
	 * Deep Copy of bufferedImage
	 * otherwise alterations of one image affect all outputs of the image (if I phrased that correctly)
	 * copied from stack overflow, have yet to look into how it works
	 */
	public static BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
	
}
