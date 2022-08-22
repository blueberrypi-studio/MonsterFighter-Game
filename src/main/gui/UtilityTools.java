package main.gui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class UtilityTools {
	/**
	 * 
	 * @param original
	 * @param width
	 * @param height
	 * @return scaled image for better rendering performance
	 */
	public BufferedImage ScaleImage(BufferedImage original, int width, int height) {
		// RENDERING PERFORMANCE BOOST USING SCALING
			BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
			Graphics2D g2 = scaledImage.createGraphics();
			g2.drawImage(original, 0, 0, width, height, null);
			g2.dispose();
			
			return scaledImage;
	}
}
