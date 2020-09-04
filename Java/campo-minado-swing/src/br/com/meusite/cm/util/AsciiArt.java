package br.com.meusite.cm.util;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class AsciiArt {

	public static void showMessage(String text) {
		
		int width = 100; // Comprimento máximo do texto
		int height = 15; // Altura do espaço utilizado no console
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics graphics = image.getGraphics();
		graphics.setFont(new Font("SansSerif", Font.BOLD, 10));
				
		Graphics2D graphics2D = (Graphics2D) graphics;
		graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		graphics2D.drawString(text.toUpperCase(), 5, 10);
		
		for (int y = 0; y < height; y++) {
		    StringBuilder stringBuilder = new StringBuilder();
		 
		    for (int x = 0; x < width; x++) {
		        stringBuilder.append(image.getRGB(x, y) == -16777216 ? " " : "@");
		    }
		 
		    System.out.println(stringBuilder);
		}
	}

}
