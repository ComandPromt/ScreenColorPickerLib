package net.java.dev.colorchooser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

final class ContinuousPalette extends Palette {

	public static final int SMALL_SPEC_WIDTH = 128;

	public static final int SMALL_SPEC_HEIGHT = 64;

	public static final int LARGE_SPEC_WIDTH = 200;

	public static final int LARGE_SPEC_HEIGHT = 100;

	public static final int SPEC_IMAGE_COUNT = 8;

	private BufferedImage img = null;

	private boolean initialized = false;

	private float saturation = 1f;

	private boolean verticalHue = true;

	private float grayStripSize = 0.05f;

	private String name;

	ContinuousPalette(String name, int width, int height, float saturation) {

		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		this.setSaturation(saturation);

		this.name = name;

	}

	private ContinuousPalette(String name, int width, int height, float saturation, boolean vHue) {

		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		this.setSaturation(saturation);

		this.verticalHue = vHue;

		this.name = name;

	}

	protected void initImage() {

		int currColor;

		for (int x = 0; x < img.getWidth(); x++) {

			for (int y = 0; y < img.getHeight(); y++) {

				currColor = getColorAt(x, y).getRGB();

				img.setRGB(x, y, currColor);

			}

		}

	}

	public final void initializeImage() {

		if (!initialized)
			initImage();

	}

	public void paintTo(java.awt.Graphics g) {

		if (g != null) {

			initializeImage();

			((Graphics2D) g).drawRenderedImage(img, AffineTransform.getTranslateInstance(0, 0));

			initialized = true;

		}

	}

	public java.awt.Color getColorAt(int x, int y) {

		float hue;

		float brightness;

		float workingSaturation;

		boolean inGrayStrip = ((float) y) / img.getHeight() > (1 - grayStripSize);

		if (verticalHue) {

			hue = ((float) y) / img.getHeight();

			brightness = ((float) x) / img.getWidth();

		}

		else {

			if (inGrayStrip)

				return grayValueFromX(x);

			hue = 1 - (((float) x) / img.getWidth());

			brightness = 1 - ((float) y) / img.getHeight();

		}

		brightness = brightness * 2;

		if (brightness > 1) {

			workingSaturation = saturation - ((brightness - 1) * saturation);

			brightness = 1;

		}

		else {

			workingSaturation = saturation;

		}

		java.awt.Color newColor = java.awt.Color.getHSBColor(hue, workingSaturation, brightness);

		return newColor;

	}

	public java.awt.Color colorFromPoint(final Point p) {

		int x = (int) p.getX();

		int y = (int) p.getY();

		return getColorAt(x, y);

	}

	protected java.awt.Color grayValueFromX(int x) {

		java.awt.Color newColor = java.awt.Color.getHSBColor(0, 0, ((float) x) / img.getWidth());

		return newColor;

	}

	public float getSaturation() {

		return saturation;

	}

	public void setSaturation(float saturation) {

		if (this.saturation != saturation) {

			this.saturation = saturation;

			doChange();

		}

	}

	public boolean isVerticalHue() {

		return verticalHue;

	}

	public void setVerticalHue(boolean verticalHue) {

		if (this.verticalHue != verticalHue) {

			this.verticalHue = verticalHue;

			doChange();

		}

	}

	protected void doChange() {

		initialized = false;

	}

	public void setGrayStripSize(float grayStripSize) {

		float workingGrayStripSize = grayStripSize;

		if (workingGrayStripSize > 1)
			workingGrayStripSize = 1;

		if (workingGrayStripSize != grayStripSize) {

			this.grayStripSize = grayStripSize;

			doChange();

		}

	}

	public float getGrayStripSize() {

		return this.grayStripSize;

	}

	public Dimension getSize() {

		return new Dimension(img.getWidth(), img.getHeight());

	}

	public String getNameAt(int x, int y) {

		Color c = getColorAt(x, y);

		StringBuffer sb = new StringBuffer();

		sb.append(c.getRed());

		sb.append(',');

		sb.append(c.getGreen());

		sb.append(',');

		sb.append(c.getBlue());

		return sb.toString();

	}

	private static Palette[] defaultPalettes = null;

	public static Palette[] createDefaultPalettes() {

		if (defaultPalettes == null) {

			defaultPalettes = new Palette[] {

					new ContinuousPalette("satLarge", LARGE_SPEC_WIDTH, LARGE_SPEC_HEIGHT, 1f, false), 

					new ContinuousPalette("unsatLarge", LARGE_SPEC_WIDTH, LARGE_SPEC_HEIGHT, 0.4f, false), 

					new ContinuousPalette("satLargeHoriz", LARGE_SPEC_WIDTH, LARGE_SPEC_HEIGHT, 1f, true), 

					new ContinuousPalette("unsatLargeHoriz", LARGE_SPEC_WIDTH, LARGE_SPEC_HEIGHT, 0.4f, true) 

			};

		}

		return defaultPalettes;

	}

	public String getDisplayName() {

		return ColorChooser.getString(name);

	}

}
