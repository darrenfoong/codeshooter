package codeshooter.utils;

import java.awt.Color;
import java.awt.Graphics2D;

public class Text {
	public static void drawCentreString(Graphics2D g2d, String text, Color textColor, float fontSize, int x, int y, int centre) {
		g2d.setFont(g2d.getFont().deriveFont(fontSize));
		g2d.setColor(textColor);
		double width = g2d.getFontMetrics().getStringBounds(text, g2d).getWidth();
		drawString(g2d, text, textColor, fontSize, (int) (x+centre-(width/2)), y);
	}

	public static void drawString(Graphics2D g2d, String text, Color textColor, float fontSize, int x, int y) {
		g2d.setFont(g2d.getFont().deriveFont(fontSize));
		g2d.setColor(textColor);
		g2d.drawString(text, x, y);
	}
}
