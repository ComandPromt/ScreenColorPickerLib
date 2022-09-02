
package com.bric.awt;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class PaintUtils {

	public final static Color[] whites = new Color[] {

			new Color(255, 255, 255, 50),

			new Color(255, 255, 255, 100),

			new Color(255, 255, 255, 150)

	};

	public final static Color[] blacks = new Color[] {

			new Color(0, 0, 0, 50),

			new Color(0, 0, 0, 100),

			new Color(0, 0, 0, 150)

	};

	public static Color getFocusRingColor() {

		Object obj = UIManager.getColor("focusRing");

		if (obj instanceof Color)

			return (Color) obj;

		return new Color(64, 113, 167);

	}

	public static void paintFocus(Graphics2D g, Shape shape, int biggestStroke) {

		Color focusColor = getFocusRingColor();

		Color[] focusArray = new Color[] {

				new Color(focusColor.getRed(), focusColor.getGreen(), focusColor.getBlue(), 255),

				new Color(focusColor.getRed(), focusColor.getGreen(), focusColor.getBlue(), 170),

				new Color(focusColor.getRed(), focusColor.getGreen(), focusColor.getBlue(), 110)

		};

		g.setStroke(new BasicStroke(biggestStroke));

		g.setColor(focusArray[2]);

		g.draw(shape);

		g.setStroke(new BasicStroke(biggestStroke - 1));

		g.setColor(focusArray[1]);

		g.draw(shape);

		g.setStroke(new BasicStroke(biggestStroke - 2));

		g.setColor(focusArray[0]);

		g.draw(shape);

		g.setStroke(new BasicStroke(1));

	}

	public static void drawBevel(Graphics g, Rectangle r) {

		drawColors(blacks, g, r.x, r.y + r.height, r.x + r.width, r.y + r.height, SwingConstants.SOUTH);

		drawColors(blacks, g, r.x + r.width, r.y, r.x + r.width, r.y + r.height, SwingConstants.EAST);

		drawColors(whites, g, r.x, r.y, r.x + r.width, r.y, SwingConstants.NORTH);

		drawColors(whites, g, r.x, r.y, r.x, r.y + r.height, SwingConstants.WEST);

		g.setColor(new Color(120, 120, 120));

		g.drawRect(r.x, r.y, r.width, r.height);

	}

	private static void drawColors(Color[] colors, Graphics g, int x1, int y1, int x2, int y2, int direction) {

		for (int a = 0; a < colors.length; a++) {

			g.setColor(colors[colors.length - a - 1]);

			switch (direction) {

			case SwingConstants.SOUTH:

				g.drawLine(x1, y1 - a, x2, y2 - a);

				break;

			case SwingConstants.NORTH:

				g.drawLine(x1, y1 + a, x2, y2 + a);

				break;

			case SwingConstants.EAST:

				g.drawLine(x1 - a, y1, x2 - a, y2);

				break;

			case SwingConstants.WEST:

				g.drawLine(x1 + a, y1, x2 + a, y2);

				break;

			}

		}

	}

}
