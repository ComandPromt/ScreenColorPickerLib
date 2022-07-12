
package com.bric.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

import com.bric.awt.PaintUtils;

public class ColorPickerPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public static int MAX_SIZE = 325;

	private int mode = ColorPicker.BRI;

	private Point point = new Point(0, 0);

	private Vector<ChangeListener> changeListeners;

	float hue = -1, sat = -1, bri = -1;

	int red = -1, green = -1, blue = -1;

	MouseInputListener mouseListener = new MouseInputAdapter() {

		public void mousePressed(MouseEvent e) {

			requestFocus();

			Point p = e.getPoint();

			int size = Math.min(MAX_SIZE, Math.min(getWidth() - imagePadding.left - imagePadding.right,
					getHeight() - imagePadding.top - imagePadding.bottom));

			p.translate(-(getWidth() / 2 - size / 2), -(getHeight() / 2 - size / 2));

			if (mode == ColorPicker.BRI || mode == ColorPicker.SAT) {

				double radius = ((double) size) / 2.0;

				double x = p.getX() - size / 2.0;

				double y = p.getY() - size / 2.0;

				double r = Math.sqrt(x * x + y * y) / radius;

				double theta = Math.atan2(y, x) / (Math.PI * 2.0);

				if (r > 1)
					r = 1;

				if (mode == ColorPicker.BRI) {

					setHSB((float) (theta + .25f), (float) (r), bri);

				}

				else {

					setHSB((float) (theta + .25f), sat, (float) (r));

				}

			}

			else if (mode == ColorPicker.HUE) {

				float s = ((float) p.x) / ((float) size);

				float b = ((float) p.y) / ((float) size);

				if (s < 0)

					s = 0;

				if (s > 1)

					s = 1;

				if (b < 0)

					b = 0;

				if (b > 1)

					b = 1;

				setHSB(hue, s, b);

			}

			else {

				int x2 = p.x * 255 / size;

				int y2 = p.y * 255 / size;

				if (x2 < 0)

					x2 = 0;

				if (x2 > 255)

					x2 = 255;

				if (y2 < 0)

					y2 = 0;

				if (y2 > 255)

					y2 = 255;

				if (mode == ColorPicker.RED) {

					setRGB(red, x2, y2);

				}

				else if (mode == ColorPicker.GREEN) {

					setRGB(x2, green, y2);

				}

				else {

					setRGB(x2, y2, blue);

				}

			}

		}

		public void mouseDragged(MouseEvent e) {

			mousePressed(e);

		}

	};

	KeyListener keyListener = new KeyAdapter() {

		public void keyPressed(KeyEvent e) {

			int dx = 0;

			int dy = 0;

			if (e.getKeyCode() == KeyEvent.VK_LEFT) {

				dx = -1;

			}

			else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {

				dx = 1;

			}

			else if (e.getKeyCode() == KeyEvent.VK_UP) {

				dy = -1;

			}

			else if (e.getKeyCode() == KeyEvent.VK_DOWN) {

				dy = 1;

			}

			int multiplier = 1;

			if (e.isShiftDown() && e.isAltDown()) {

				multiplier = 10;

			}

			else if (e.isShiftDown() || e.isAltDown()) {

				multiplier = 5;

			}

			if (dx != 0 || dy != 0) {

				int size = Math.min(MAX_SIZE, Math.min(getWidth() - imagePadding.left - imagePadding.right,
						getHeight() - imagePadding.top - imagePadding.bottom));

				int offsetX = getWidth() / 2 - size / 2;

				int offsetY = getHeight() / 2 - size / 2;

				mouseListener.mousePressed(
						new MouseEvent(ColorPickerPanel.this, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0,
								point.x + multiplier * dx + offsetX, point.y + multiplier * dy + offsetY, 1, false));
			}

		}

	};

	FocusListener focusListener = new FocusListener() {

		public void focusGained(FocusEvent e) {

			repaint();

		}

		public void focusLost(FocusEvent e) {

			repaint();

		}

	};

	ComponentListener componentListener = new ComponentAdapter() {

		public void componentResized(ComponentEvent e) {

			regeneratePoint();

			regenerateImage();

		}

	};

	BufferedImage image = new BufferedImage(MAX_SIZE, MAX_SIZE, BufferedImage.TYPE_INT_ARGB);

	public ColorPickerPanel() {

		setMaximumSize(new Dimension(MAX_SIZE + imagePadding.left + imagePadding.right,
				MAX_SIZE + imagePadding.top + imagePadding.bottom));

		setPreferredSize(new Dimension(10, 32));

		setRGB(0, 0, 0);

		addMouseListener(mouseListener);

		addMouseMotionListener(mouseListener);

		setFocusable(true);

		addKeyListener(keyListener);

		addFocusListener(focusListener);

		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

		addComponentListener(componentListener);

	}

	public void addChangeListener(ChangeListener l) {

		if (changeListeners == null)

			changeListeners = new Vector<ChangeListener>();

		if (changeListeners.contains(l))
			return;

		changeListeners.add(l);

	}

	public void removeChangeListener(ChangeListener l) {

		if (changeListeners == null)

			return;

		changeListeners.remove(l);

	}

	protected void fireChangeListeners() {

		if (changeListeners == null)

			return;

		for (int a = 0; a < changeListeners.size(); a++) {

			ChangeListener l = (ChangeListener) changeListeners.get(a);

			try {

				l.stateChanged(new ChangeEvent(this));

			}

			catch (RuntimeException e) {

				e.printStackTrace();

			}

		}

	}

	Insets imagePadding = new Insets(6, 6, 6, 6);

	public void paint(Graphics g) {

		super.paint(g);

		Graphics2D g2 = (Graphics2D) g;

		int size = Math.min(MAX_SIZE, Math.min(getWidth() - imagePadding.left - imagePadding.right,
				getHeight() - imagePadding.top - imagePadding.bottom));

		g2.translate(getWidth() / 2 - size / 2, getHeight() / 2 - size / 2);

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Shape shape;

		if (mode == ColorPicker.SAT || mode == ColorPicker.BRI) {

			shape = new Ellipse2D.Float(0, 0, size, size);
		}

		else {

			Rectangle r = new Rectangle(0, 0, size, size);

			shape = r;

		}

		if (hasFocus()) {

			PaintUtils.paintFocus(g2, shape, 5);

		}

		if (!(shape instanceof Rectangle)) {

			g2.translate(2, 2);

			g2.setColor(new Color(0, 0, 0, 20));

			g2.fill(new Ellipse2D.Float(-2, -2, size + 4, size + 4));

			g2.setColor(new Color(0, 0, 0, 40));

			g2.fill(new Ellipse2D.Float(-1, -1, size + 2, size + 2));

			g2.setColor(new Color(0, 0, 0, 80));

			g2.fill(new Ellipse2D.Float(0, 0, size, size));

			g2.translate(-2, -2);

		}

		g2.drawImage(image, 0, 0, size, size, 0, 0, size, size, null);

		if (shape instanceof Rectangle) {

			Rectangle r = (Rectangle) shape;

			PaintUtils.drawBevel(g2, r);

		}

		else {

			g2.setColor(new Color(0, 0, 0, 120));

			g2.draw(shape);

		}

		g2.setColor(Color.white);

		g2.setStroke(new BasicStroke(1));

		g2.draw(new Ellipse2D.Float(point.x - 3, point.y - 3, 6, 6));

		g2.setColor(Color.black);

		g2.draw(new Ellipse2D.Float(point.x - 4, point.y - 4, 8, 8));

		g.translate(-imagePadding.left, -imagePadding.top);

	}

	public void setMode(int mode) {

		if (!(mode == ColorPicker.HUE || mode == ColorPicker.SAT || mode == ColorPicker.BRI || mode == ColorPicker.RED
				|| mode == ColorPicker.GREEN || mode == ColorPicker.BLUE))
			throw new IllegalArgumentException("The mode must be HUE, SAT, BRI, RED, GREEN, or BLUE.");

		if (this.mode == mode)

			return;

		this.mode = mode;

		regenerateImage();

		regeneratePoint();

	}

	public void setRGB(int r, int g, int b) {

		if (r < 0 || r > 255)
			throw new IllegalArgumentException("The red value (" + r + ") must be between [0,255].");

		if (g < 0 || g > 255)
			throw new IllegalArgumentException("The green value (" + g + ") must be between [0,255].");

		if (b < 0 || b > 255)
			throw new IllegalArgumentException("The blue value (" + b + ") must be between [0,255].");

		if (red != r || green != g || blue != b) {

			if (mode == ColorPicker.RED || mode == ColorPicker.GREEN || mode == ColorPicker.BLUE) {

				int lastR = red;

				int lastG = green;

				int lastB = blue;

				red = r;

				green = g;

				blue = b;

				if (mode == ColorPicker.RED) {

					if (lastR != r) {

						regenerateImage();

					}

				}

				else if (mode == ColorPicker.GREEN) {

					if (lastG != g) {

						regenerateImage();

					}

				}

				else if (mode == ColorPicker.BLUE) {

					if (lastB != b) {

						regenerateImage();

					}

				}

			}

			else {

				float[] hsb = new float[3];

				Color.RGBtoHSB(r, g, b, hsb);

				setHSB(hsb[0], hsb[1], hsb[2]);

				return;

			}

			regeneratePoint();

			repaint();

			fireChangeListeners();

		}

	}

	public float[] getHSB() {

		return new float[] { hue, sat, bri };

	}

	public int[] getRGB() {

		return new int[] { red, green, blue };

	}

	public void setHSB(float h, float s, float b) {

		if (Float.isInfinite(h) || Float.isNaN(h))

			throw new IllegalArgumentException("The hue value (" + h + ") is not a valid number.");

		while (h < 0)

			h++;

		while (h > 1)

			h--;

		if (s < 0 || s > 1)

			throw new IllegalArgumentException("The saturation value (" + s + ") must be between [0,1]");

		if (b < 0 || b > 1)

			throw new IllegalArgumentException("The brightness value (" + b + ") must be between [0,1]");

		if (hue != h || sat != s || bri != b) {

			if (mode == ColorPicker.HUE || mode == ColorPicker.BRI || mode == ColorPicker.SAT) {

				float lastHue = hue;

				float lastBri = bri;

				float lastSat = sat;

				hue = h;

				sat = s;

				bri = b;

				if (mode == ColorPicker.HUE) {

					if (lastHue != hue) {

						regenerateImage();

					}

				}

				else if (mode == ColorPicker.SAT) {

					if (lastSat != sat) {

						regenerateImage();

					}

				}

				else if (mode == ColorPicker.BRI) {

					if (lastBri != bri) {

						regenerateImage();

					}

				}

			}

			else {

				Color c = new Color(Color.HSBtoRGB(h, s, b));

				setRGB(c.getRed(), c.getGreen(), c.getBlue());

				return;

			}

			Color c = new Color(Color.HSBtoRGB(hue, sat, bri));

			red = c.getRed();

			green = c.getGreen();

			blue = c.getBlue();

			regeneratePoint();

			repaint();

			fireChangeListeners();

		}

	}

	private void regeneratePoint() {

		int size = Math.min(MAX_SIZE, Math.min(getWidth() - imagePadding.left - imagePadding.right,
				getHeight() - imagePadding.top - imagePadding.bottom));

		if (mode == ColorPicker.HUE || mode == ColorPicker.SAT || mode == ColorPicker.BRI) {

			if (mode == ColorPicker.HUE) {

				point = new Point((int) (sat * size), (int) (bri * size));

			}

			else if (mode == ColorPicker.SAT) {

				double theta = hue * 2 * Math.PI - Math.PI / 2;

				if (theta < 0)
					theta += 2 * Math.PI;

				double r = bri * size / 2;

				point = new Point((int) (r * Math.cos(theta) + .5 + size / 2.0),
						(int) (r * Math.sin(theta) + .5 + size / 2.0));

			}

			else if (mode == ColorPicker.BRI) {

				double theta = hue * 2 * Math.PI - Math.PI / 2;

				if (theta < 0)

					theta += 2 * Math.PI;

				double r = sat * size / 2;

				point = new Point((int) (r * Math.cos(theta) + .5 + size / 2.0),
						(int) (r * Math.sin(theta) + .5 + size / 2.0));

			}

		}

		else if (mode == ColorPicker.RED) {

			point = new Point((int) (green * size / 255f + .49f), (int) (blue * size / 255f + .49f));

		}

		else if (mode == ColorPicker.GREEN) {

			point = new Point((int) (red * size / 255f + .49f), (int) (blue * size / 255f + .49f));

		}

		else if (mode == ColorPicker.BLUE) {

			point = new Point((int) (red * size / 255f + .49f), (int) (green * size / 255f + .49f));

		}

	}

	private int[] row = new int[MAX_SIZE];

	private synchronized void regenerateImage() {

		int size = Math.min(MAX_SIZE, Math.min(getWidth() - imagePadding.left - imagePadding.right,
				getHeight() - imagePadding.top - imagePadding.bottom));

		if (mode == ColorPicker.BRI || mode == ColorPicker.SAT) {

			float bri2 = this.bri;

			float sat2 = this.sat;

			float radius = ((float) size) / 2f;

			float hue2;

			float k = 1.2f;

			for (int y = 0; y < size; y++) {

				float y2 = (y - size / 2f);

				for (int x = 0; x < size; x++) {

					float x2 = (x - size / 2f);

					double theta = Math.atan2(y2, x2) - 3 * Math.PI / 2.0;

					if (theta < 0)
						theta += 2 * Math.PI;

					double r = Math.sqrt(x2 * x2 + y2 * y2);

					if (r <= radius) {

						if (mode == ColorPicker.BRI) {

							hue2 = (float) (theta / (2 * Math.PI));

							sat2 = (float) (r / radius);

						}

						else {

							hue2 = (float) (theta / (2 * Math.PI));

							bri2 = (float) (r / radius);

						}

						row[x] = Color.HSBtoRGB(hue2, sat2, bri2);

						if (r > radius - k) {

							int alpha = (int) (255 - 255 * (r - radius + k) / k);

							if (alpha < 0)

								alpha = 0;

							if (alpha > 255)
								alpha = 255;

							row[x] = row[x] & 0xffffff + (alpha << 24);

						}

					}

					else {

						row[x] = 0x00000000;

					}

				}

				image.getRaster().setDataElements(0, y, size, 1, row);

			}

		}

		else if (mode == ColorPicker.HUE) {

			float hue2 = this.hue;

			for (int y = 0; y < size; y++) {

				float y2 = ((float) y) / ((float) size);

				for (int x = 0; x < size; x++) {

					float x2 = ((float) x) / ((float) size);

					row[x] = Color.HSBtoRGB(hue2, x2, y2);

				}

				image.getRaster().setDataElements(0, y, image.getWidth(), 1, row);

			}

		}

		else {

			int red2 = red;

			int green2 = green;

			int blue2 = blue;

			for (int y = 0; y < size; y++) {

				float y2 = ((float) y) / ((float) size);

				for (int x = 0; x < size; x++) {

					float x2 = ((float) x) / ((float) size);

					if (mode == ColorPicker.RED) {

						green2 = (int) (x2 * 255 + .49);

						blue2 = (int) (y2 * 255 + .49);

					}

					else if (mode == ColorPicker.GREEN) {

						red2 = (int) (x2 * 255 + .49);

						blue2 = (int) (y2 * 255 + .49);

					}

					else {

						red2 = (int) (x2 * 255 + .49);

						green2 = (int) (y2 * 255 + .49);

					}

					row[x] = 0xFF000000 + (red2 << 16) + (green2 << 8) + blue2;

				}

				image.getRaster().setDataElements(0, y, size, 1, row);

			}

		}

		repaint();

	}

}
