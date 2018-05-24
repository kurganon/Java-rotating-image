package aNewWorld0001;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class DrawDetails {
	
	//Zu zeichnendes Bild
	private Image img;
	
	//Position im Panel
	private int posx;
	private int posy;
	
	//Gesamtbreite und - höhe
	private int breite;
	private int hoehe;
	
	public DrawDetails() {
		
	}
	
	
	/**
	 * Rückgabe dessen was gezeichnet werden soll.
	 * @return
	 */
	public Image getImage() {
		return this.img;
	}
	/*
	 * Bestimmen dessen was gezeichnet werden soll
	 */
	public void setImage(Image img) {
		this.img = img;
	}

	/**
	 * Rückgabe der X Position (von links)
	 * @return
	 */
	public int getPosX() {
		return this.posx;
	}
	/**
	 * Bestimmen der X Position (von links)
	 * @param posX
	 */
	public void setPosX(int posX) {
		this.posx = posX;
	}

	/**
	 * Rückgabe der Y Position (von oben)
	 * @return
	 */
	public int getPosY() {
		return this.posy;
	}
	/**
	 * Bestimmen der Y Position (von oben)
	 * @param posY
	 */
	public void setPosY(int posY) {
		this.posy = posY;
	}

	/**
	 * Rückgabe der Breite
	 * @return
	 */
	public int getBreite() {
		return this.breite;
	}
	/**
	 * Bestimmen der Breite
	 * @param breite
	 */
	public void setBreite(int breite) {
		this.breite = breite;
	}

	/**
	 * Rückgabe der Höhe
	 * @return
	 */
	public int getHoehe() {
		return this.hoehe;
	}
	/**
	 * Bestimmen der Höhe
	 * @param hoehe
	 */
	public void setHoehe(int hoehe) {
		this.hoehe = hoehe;
	}

	/**
	 * Alle Variablen gleichzeitig bestimmen
	 * @param img
	 * Das zu zeichnende Bild
	 * @param posx
	 * Die X Position
	 * @param posy
	 * Die Y Position
	 * @param breite
	 * Breitenverzerrung
	 * @param hoehe
	 * Höhenverzerrung
	 */
	public void setAll(Image img, int posx, int posy, int breite, int hoehe) {
		this.img = img;
		this.posx = posx;
		this.posy = posy;
		this.breite = breite;
		this.hoehe = hoehe;
	}
	
	/**
	 * Bildbearbeitende Funktionen, die statisch aufgerufen werden können
	 * Zum drehen eines (quadratischen) Bilds
	 */
	public static BufferedImage rotate2(BufferedImage bufferedimage, int centerx, int centery, int degrees) {
		//Erst eine frische Kopie des Bilds gemacht
		BufferedImage image = new BufferedImage(bufferedimage.getWidth(), bufferedimage.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		//Aus dem Winkel in Grad den Winkel in Radians berechnet
		double radians = degrees * Math.PI / 180;
		
		//Ein Objekt dass Bilder neu berechnet
		// wenn man damit umzugehen weiß
		AffineTransform affine = new AffineTransform();
		
		//Eine Maske zur berechnungder wahrscheinlichen neuen
		// Position der Koordinaten
		double[][] mask = {
				{Math.cos(radians), (Math.sin(radians) * -1)},
				{Math.sin(radians), Math.cos(radians)}
		};
		int zx, zy;
		
		//Berechnung der neuen Koordinaten
		//und direkt anschließend den Wert den man
		//zum korrigieren der Positionsveränderung durch
		// die Bildbearbeitung braucht
		zx = (int)Math.round(centerx * mask[0][0] 
				+ centery * mask[0][1]);
		zx = (zx < 0 ? zx * -1 + (centerx) : (centerx) - zx);
		//oben für X und unten für Y
		zy = (int)Math.round((centerx * mask[1][0]) 
				+ (centery * mask[1][1]));
		zy = (zy < 0 ? zy * -1 + (centery) : (centery) - zy);
		
		// Theta ist der Winkel in Radians
		//Verschiebung des Bilds, damit es am Ende dort landet wo es angefangen hat
		affine.setToTranslation(zx, zy);
		//Drehen des Bilds dem Winkel entsprechend
		affine.rotate(radians);
		
		//Objekt, dass die Änderungen/Maske auch auf Bilder anwendet
		// und nicht nur bei der Ausgabe
		AffineTransformOp affineOp = new AffineTransformOp(affine, AffineTransformOp.TYPE_BILINEAR);
		
		//Veränderung des Bilds "bufferedimage" und dann speichern in "image"
		affineOp.filter(bufferedimage, image);
		
		//Verklaren der Pixel, evtl. für Performance auskommentieren:
		/*
		int tempargb;
		for(int i = 0; i < image.getWidth(); i++) {
			for(int j = 0; j < image.getHeight(); j++) {
				tempargb = image.getRGB(i, j);
				
				if(((tempargb>>24) & 0xFF) <= 0x88) {
			        int color = tempargb & 0x00ffffff;
			        int alpha = 0x00 << 24;
			        tempargb = color | alpha;
				}else if(((tempargb>>24) & 0xFF) > 0x88) {
			        int color = tempargb & 0x00ffffff;
			        int alpha = 0xff << 24;
			        tempargb = color | alpha;
				}
				image.setRGB(i, j, tempargb);
			}
		}
		*/
		
		//Rückgabe des gedrehten Bilds
		return image;
	}
	
	/**
	 * Bildbearbeitende Funktionen, die statisch aufgerufen werden können
	 * Zum drehen eines (quadratischen) Bilds
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
	 * Sonst blenden alle der Buchstaben aus und nicht nur die, die ich nur kurz brauche
	 * soweit nicht ganz sicher wie das funktioniert
	 */
	public static BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
	
}
