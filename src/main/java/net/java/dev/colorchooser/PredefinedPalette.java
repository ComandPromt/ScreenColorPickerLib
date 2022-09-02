
package net.java.dev.colorchooser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.util.Arrays;

import javax.swing.UIManager;

class PredefinedPalette extends Palette {

	NamedColor[] colors;

	private int swatchSize = 12;

	private int gap = 1;

	private static final Rectangle scratch = new Rectangle();

	private String name;

	public PredefinedPalette(String name, NamedColor[] colors) {

		this.colors = colors;

		this.name = name;

		Arrays.sort(colors);

		if (colors.length < 14) {

			swatchSize = 24;

		}

		else if (colors.length < 40) {

			swatchSize = 16;

		}

	}

	public java.awt.Color getColorAt(int x, int y) {

		Color result = null;

		int idx = indexForPoint(x, y);

		if (idx != -1 && idx < colors.length) {

			result = colors[idx];

		}

		return result;

	}

	public void paintTo(java.awt.Graphics g) {

		g.setColor(Color.BLACK);

		Dimension size = getSize();

		g.fillRect(0, 0, size.width, size.height);

		for (int i = 0; i < colors.length; i++) {

			Color c = colors[i];

			rectForIndex(i, scratch);

			g.setColor(c);

			g.fillRect(scratch.x, scratch.y, scratch.width, scratch.height);

			if (Color.BLACK.equals(c)) {

				g.setColor(Color.GRAY);

			}

			else {

				g.setColor(c.brighter());

			}

			g.drawLine(scratch.x, scratch.y, scratch.x + scratch.width - 1, scratch.y);

			g.drawLine(scratch.x, scratch.y, scratch.x, scratch.y + scratch.height - 1);

			if (Color.BLACK.equals(c)) {

				g.setColor(Color.GRAY.darker());

			}

			else {

				g.setColor(c.darker());

			}

			g.drawLine(scratch.x + scratch.width - 1, scratch.y + scratch.height - 1, scratch.width + scratch.x - 1,
					scratch.y + 1);

			g.drawLine(scratch.x + scratch.width - 1, scratch.y + scratch.height - 1, scratch.x,
					scratch.y + scratch.height - 1);

		}

	}

	public String getNameAt(int x, int y) {

		NamedColor nc = (NamedColor) getColorAt(x, y);

		if (nc != null) {

			return nc.getDisplayName();

		}

		else {

			return null;

		}

	}

	protected int getCount() {

		return colors.length;

	}

	Dimension calcSize() {

		int count = colors.length;

		double dblWidth = (swatchSize + gap) * Math.sqrt(count);

		int width = Math.round(Math.round(dblWidth));

		int height = width;

		double flr = Math.floor(Math.sqrt(count));

		if (Math.sqrt(count) - flr != 0) {

			height += swatchSize + gap;

		}

		int perRow = width / (swatchSize + gap);

		int perCol = height / (swatchSize + gap);

		width = perRow * (swatchSize + gap) + gap;

		height = perCol * (swatchSize + gap) + gap;

		Dimension result = new Dimension(width, height);

		return result;

	}

	private Dimension size = null;

	public Dimension getSize() {

		if (size == null) {

			size = calcSize();

		}

		return size;

	}

	private int indexForPoint(int x, int y) {

		Dimension d = getSize();

		if (y > d.height || x > d.width || y < 0 || x < 0) {

			return -1;

		}

		int perRow = d.width / (swatchSize + gap);

		int col = x / (swatchSize + gap);

		int row = y / (swatchSize + gap);

		return (row * perRow) + col;

	}

	private void rectForIndex(int idx, final Rectangle r) {

		int count = colors.length;

		Dimension d = getSize();

		int rectsPerRow = d.width / (swatchSize + gap);

		r.x = gap + ((swatchSize + gap) * (idx % rectsPerRow));

		r.y = gap + ((swatchSize + gap) * (idx / rectsPerRow));

		r.width = swatchSize;

		r.height = swatchSize;

	}

	private static Palette[] predefined = null;

	public static Palette[] createDefaultPalettes() {

		if (predefined == null) {

			predefined = makePal();

		}

		return predefined;

	}

	private static final Palette[] makePal() {

		Palette[] result = new Palette[] {

				new PredefinedPalette("svg", SVGColors),

				new PredefinedPalette("system", getSystemColors()),

				RecentColors.getDefault(),

				new PredefinedPalette("swing", getSwingColors())

		};

		return result;

	}

	static class BasicNamedColor extends NamedColor {

		private String name;

		public BasicNamedColor(String name, int r, int g, int b) {

			super(name, r, g, b);

			this.name = name;

		}

		public String getName() {

			return name;

		}

		public String getDisplayName() {

			return ColorChooser.getString(getName());

		}

		public String toString() {

			return "new java.awt.Color(" + getRed() + "," + getGreen() + "," + getBlue() + ")";

		}

		public int compareTo(Object o) {

			Color c = (Color) o;

			int result = avgColor(c) - avgColor(this);

			return result;

		}

		public String getInstantiationCode() {

			return toString();

		}

	}

	private static int avgColor(Color c) {

		return (c.getRed() + c.getGreen() + c.getBlue()) / 3;

	}

	public String getDisplayName() {

		return ColorChooser.getString(name);

	}

	static String getColorName(Color c) {

		for (int i = 0; i < swingColors.length; i++) {

			if (equals(swingColors[i], c)) {

				return swingColors[i].getDisplayName();

			}

		}

		for (int i = 0; i < SVGColors.length; i++) {

			if (equals(SVGColors[i], c)) {

				return SVGColors[i].getDisplayName();

			}

		}

		return null;

	}

	static boolean equals(Color a, Color b) {

		return a.getRGB() == b.getRGB();

	}

	private static final NamedColor[] SVGColors = new BasicNamedColor[] {

			new BasicNamedColor("aliceblue", 240, 248, 255),

			new BasicNamedColor("antiquewhite", 250, 235, 215),

			new BasicNamedColor("aqua", 0, 255, 255),

			new BasicNamedColor("aquamarine", 127, 255, 212),

			new BasicNamedColor("azure", 240, 255, 255),

			new BasicNamedColor("beige", 245, 245, 220),

			new BasicNamedColor("bisque", 255, 228, 196),

			new BasicNamedColor("black", 0, 0, 0),

			new BasicNamedColor("blanchedalmond", 255, 235, 205),

			new BasicNamedColor("blue", 0, 0, 255),

			new BasicNamedColor("blueviolet", 138, 43, 226),

			new BasicNamedColor("brown", 165, 42, 42),

			new BasicNamedColor("burlywood", 222, 184, 135),

			new BasicNamedColor("cadetblue", 95, 158, 160),

			new BasicNamedColor("chartreuse", 127, 255, 0),

			new BasicNamedColor("chocolate", 210, 105, 30),

			new BasicNamedColor("coral", 255, 127, 80),

			new BasicNamedColor("cornflowerblue", 100, 149, 237),

			new BasicNamedColor("cornsilk", 255, 248, 220),

			new BasicNamedColor("crimson", 220, 20, 60),

			new BasicNamedColor("cyan", 0, 255, 255),

			new BasicNamedColor("darkblue", 0, 0, 139),

			new BasicNamedColor("darkcyan", 0, 139, 139),

			new BasicNamedColor("darkgoldenrod", 184, 134, 11),

			new BasicNamedColor("darkgray", 169, 169, 169),

			new BasicNamedColor("darkgreen", 0, 100, 0),

			new BasicNamedColor("darkgrey", 169, 169, 169),

			new BasicNamedColor("darkkhaki", 189, 183, 107),

			new BasicNamedColor("darkmagenta", 139, 0, 139),

			new BasicNamedColor("darkolivegreen", 85, 107, 47),

			new BasicNamedColor("darkorange", 255, 140, 0),

			new BasicNamedColor("darkorchid", 153, 50, 204),

			new BasicNamedColor("darkred", 139, 0, 0),

			new BasicNamedColor("darksalmon", 233, 150, 122),

			new BasicNamedColor("darkseagreen", 143, 188, 143),

			new BasicNamedColor("darkslateblue", 72, 61, 139),

			new BasicNamedColor("darkslategray", 47, 79, 79),

			new BasicNamedColor("darkslategrey", 47, 79, 79),

			new BasicNamedColor("darkturquoise", 0, 206, 209),

			new BasicNamedColor("darkviolet", 148, 0, 211),

			new BasicNamedColor("deeppink", 255, 20, 147),

			new BasicNamedColor("deepskyblue", 0, 191, 255),

			new BasicNamedColor("dimgray", 105, 105, 105),

			new BasicNamedColor("dimgrey", 105, 105, 105),

			new BasicNamedColor("dodgerblue", 30, 144, 255),

			new BasicNamedColor("firebrick", 178, 34, 34),

			new BasicNamedColor("floralwhite", 255, 250, 240),

			new BasicNamedColor("forestgreen", 34, 139, 34),

			new BasicNamedColor("fuchsia", 255, 0, 255),

			new BasicNamedColor("gainsboro", 220, 220, 220),

			new BasicNamedColor("ghostwhite", 248, 248, 255),

			new BasicNamedColor("gold", 255, 215, 0),

			new BasicNamedColor("goldenrod", 218, 165, 32),

			new BasicNamedColor("gray", 128, 128, 128),

			new BasicNamedColor("grey", 128, 128, 128),

			new BasicNamedColor("green", 0, 128, 0),

			new BasicNamedColor("greenyellow", 173, 255, 47),

			new BasicNamedColor("honeydew", 240, 255, 240),

			new BasicNamedColor("hotpink", 255, 105, 180),

			new BasicNamedColor("indianred", 205, 92, 92),

			new BasicNamedColor("indigo", 75, 0, 130),

			new BasicNamedColor("ivory", 255, 255, 240),

			new BasicNamedColor("khaki", 240, 230, 140),

			new BasicNamedColor("lavender", 230, 230, 250),

			new BasicNamedColor("lavenderblush", 255, 240, 245),

			new BasicNamedColor("lawngreen", 124, 252, 0),

			new BasicNamedColor("lemonchiffon", 255, 250, 205),

			new BasicNamedColor("lightblue", 173, 216, 230),

			new BasicNamedColor("lightcoral", 240, 128, 128),

			new BasicNamedColor("lightcyan", 224, 255, 255),

			new BasicNamedColor("lightgoldenrodyellow", 250, 250, 210),

			new BasicNamedColor("lightgray", 211, 211, 211),

			new BasicNamedColor("lightgreen", 144, 238, 144),

			new BasicNamedColor("lightgrey", 211, 211, 211),

			new BasicNamedColor("lightpink", 255, 182, 193),

			new BasicNamedColor("lightsalmon", 255, 160, 122),

			new BasicNamedColor("lightseagreen", 32, 178, 170),

			new BasicNamedColor("lightskyblue", 135, 206, 250),

			new BasicNamedColor("lightslategray", 119, 136, 153),

			new BasicNamedColor("lightslategrey", 119, 136, 153),

			new BasicNamedColor("lightsteelblue", 176, 196, 222),

			new BasicNamedColor("lightyellow", 255, 255, 224),

			new BasicNamedColor("lime", 0, 255, 0),

			new BasicNamedColor("limegreen", 50, 205, 50),

			new BasicNamedColor("linen", 250, 240, 230),

			new BasicNamedColor("magenta", 255, 0, 255),

			new BasicNamedColor("maroon", 128, 0, 0),

			new BasicNamedColor("mediumaquamarine", 102, 205, 170),

			new BasicNamedColor("mediumblue", 0, 0, 205),

			new BasicNamedColor("mediumorchid", 186, 85, 211),

			new BasicNamedColor("mediumpurple", 147, 112, 219),

			new BasicNamedColor("mediumseagreen", 60, 179, 113),

			new BasicNamedColor("mediumslateblue", 123, 104, 238),

			new BasicNamedColor("mediumspringgreen", 0, 250, 154),

			new BasicNamedColor("mediumturquoise", 72, 209, 204),

			new BasicNamedColor("mediumvioletred", 199, 21, 133),

			new BasicNamedColor("midnightblue", 25, 25, 112),

			new BasicNamedColor("mintcream", 245, 255, 250),

			new BasicNamedColor("mistyrose", 255, 228, 225),

			new BasicNamedColor("moccasin", 255, 228, 181),

			new BasicNamedColor("navajowhite", 255, 222, 173),

			new BasicNamedColor("navy", 0, 0, 128),

			new BasicNamedColor("oldlace", 253, 245, 230),

			new BasicNamedColor("olive", 128, 128, 0),

			new BasicNamedColor("olivedrab", 107, 142, 35),

			new BasicNamedColor("orange", 255, 165, 0),

			new BasicNamedColor("orangered", 255, 69, 0),

			new BasicNamedColor("orchid", 218, 112, 214),

			new BasicNamedColor("palegoldenrod", 238, 232, 170),

			new BasicNamedColor("palegreen", 152, 251, 152),

			new BasicNamedColor("paleturquoise", 175, 238, 238),

			new BasicNamedColor("palevioletred", 219, 112, 147),

			new BasicNamedColor("papayawhip", 255, 239, 213),

			new BasicNamedColor("peachpuff", 255, 218, 185),

			new BasicNamedColor("peru", 205, 133, 63),

			new BasicNamedColor("pink", 255, 192, 203),

			new BasicNamedColor("plum", 221, 160, 221),

			new BasicNamedColor("powderblue", 176, 224, 230),

			new BasicNamedColor("purple", 128, 0, 128),

			new BasicNamedColor("red", 255, 0, 0),

			new BasicNamedColor("rosybrown", 188, 143, 143),

			new BasicNamedColor("royalblue", 65, 105, 225),

			new BasicNamedColor("saddlebrown", 139, 69, 19),

			new BasicNamedColor("salmon", 250, 128, 114),

			new BasicNamedColor("sandybrown", 244, 164, 96),

			new BasicNamedColor("seagreen", 46, 139, 87),

			new BasicNamedColor("seashell", 255, 245, 238),

			new BasicNamedColor("sienna", 160, 82, 45),

			new BasicNamedColor("silver", 192, 192, 192),

			new BasicNamedColor("skyblue", 135, 206, 235),

			new BasicNamedColor("slateblue", 106, 90, 205),

			new BasicNamedColor("slategray", 112, 128, 144),

			new BasicNamedColor("slategrey", 112, 128, 144),

			new BasicNamedColor("snow", 255, 250, 250),

			new BasicNamedColor("springgreen", 0, 255, 127),

			new BasicNamedColor("steelblue", 70, 130, 180),

			new BasicNamedColor("tan", 210, 180, 140),

			new BasicNamedColor("teal", 0, 128, 128),

			new BasicNamedColor("thistle", 216, 191, 216),

			new BasicNamedColor("tomato", 255, 99, 71),

			new BasicNamedColor("turquoise", 64, 224, 208),

			new BasicNamedColor("violet", 238, 130, 238),

			new BasicNamedColor("wheat", 245, 222, 179),

			new BasicNamedColor("white", 255, 255, 255),

			new BasicNamedColor("whitesmoke", 245, 245, 245),

			new BasicNamedColor("yellow", 255, 255, 0),

			new BasicNamedColor("yellowgreen", 154, 205, 50)

	};

	static class SwingColor extends BasicNamedColor {

		public SwingColor(String name, int r, int g, int b) {

			super(name, r, g, b);

		}

		public String toString() {

			return "UIManager.getColor(" + getName() + ")";

		}

		public String getDisplayName() {

			return getName();

		}

		public String getInstantiationCode() {

			return toString();

		}

	}

	private static SwingColor[] swingColors = null;

	private static SwingColor[] getSwingColors() {

		if (swingColors != null) {

			return swingColors;

		}

		java.util.List l = new java.util.ArrayList();

		Color c;

		c = UIManager.getColor("windowText");

		if (c != null) {

			l.add(new SwingColor("windowText", c.getRed(), c.getGreen(), c.getBlue()));

		}

		c = UIManager.getColor("activeCaptionBorder");

		if (c != null) {

			l.add(new SwingColor("activeCaptionBorder", c.getRed(), c.getGreen(), c.getBlue()));

		}

		c = UIManager.getColor("inactiveCaptionText");

		if (c != null) {

			l.add(new SwingColor("inactiveCaptionText", c.getRed(), c.getGreen(), c.getBlue()));

		}

		c = UIManager.getColor("controlLtHighlight");

		if (c != null) {

			l.add(new SwingColor("controlLtHighlight", c.getRed(), c.getGreen(), c.getBlue()));

		}

		c = UIManager.getColor("inactiveCaptionBorder");

		if (c != null) {

			l.add(new SwingColor("inactiveCaptionBorder", c.getRed(), c.getGreen(), c.getBlue()));

		}

		c = UIManager.getColor("textInactiveText");

		if (c != null) {

			l.add(new SwingColor("textInactiveText", c.getRed(), c.getGreen(), c.getBlue()));

		}

		c = UIManager.getColor("control");

		if (c != null) {

			l.add(new SwingColor("control", c.getRed(), c.getGreen(), c.getBlue()));

		}

		c = UIManager.getColor("textText");

		if (c != null) {

			l.add(new SwingColor("textText", c.getRed(), c.getGreen(), c.getBlue()));

		}

		c = UIManager.getColor("menu");

		if (c != null) {

			l.add(new SwingColor("menu", c.getRed(), c.getGreen(), c.getBlue()));

		}

		c = UIManager.getColor("windowBorder");

		if (c != null) {

			l.add(new SwingColor("windowBorder", c.getRed(), c.getGreen(), c.getBlue()));

		}

		c = UIManager.getColor("infoText");

		if (c != null) {

			l.add(new SwingColor("infoText", c.getRed(), c.getGreen(), c.getBlue()));

		}

		c = UIManager.getColor("menuText");

		if (c != null) {

			l.add(new SwingColor("menuText", c.getRed(), c.getGreen(), c.getBlue()));

		}

		c = UIManager.getColor("textHighlightText");

		if (c != null) {

			l.add(new SwingColor("textHighlightText", c.getRed(), c.getGreen(), c.getBlue()));

		}

		c = UIManager.getColor("activeCaptionText");

		if (c != null) {

			l.add(new SwingColor("activeCaptionText", c.getRed(), c.getGreen(), c.getBlue()));

		}

		c = UIManager.getColor("textHighlight");

		if (c != null) {

			l.add(new SwingColor("textHighlight", c.getRed(), c.getGreen(), c.getBlue()));

		}

		c = UIManager.getColor("controlShadow");

		if (c != null) {

			l.add(new SwingColor("controlShadow", c.getRed(), c.getGreen(), c.getBlue()));

		}

		c = UIManager.getColor("controlText");

		if (c != null) {

			l.add(new SwingColor("controlText", c.getRed(), c.getGreen(), c.getBlue()));

		}

		c = UIManager.getColor("menuPressedItemF");

		if (c != null) {

			l.add(new SwingColor("menuPressedItemF", c.getRed(), c.getGreen(), c.getBlue()));

		}

		c = UIManager.getColor("menuPressedItemB");

		if (c != null) {

			l.add(new SwingColor("menuPressedItemB", c.getRed(), c.getGreen(), c.getBlue()));

		}

		c = UIManager.getColor("info");

		if (c != null) {

			l.add(new SwingColor("info", c.getRed(), c.getGreen(), c.getBlue()));

		}

		c = UIManager.getColor("controlHighlight");

		if (c != null) {

			l.add(new SwingColor("controlHighlight", c.getRed(), c.getGreen(), c.getBlue()));

		}

		c = UIManager.getColor("scrollbar");

		if (c != null) {

			l.add(new SwingColor("scrollbar", c.getRed(), c.getGreen(), c.getBlue()));

		}

		c = UIManager.getColor("window");

		if (c != null) {

			l.add(new SwingColor("window", c.getRed(), c.getGreen(), c.getBlue()));

		}

		c = UIManager.getColor("inactiveCaption");

		if (c != null) {

			l.add(new SwingColor("inactiveCaption", c.getRed(), c.getGreen(), c.getBlue()));

		}

		c = UIManager.getColor("controlDkShadow");

		if (c != null) {

			l.add(new SwingColor("controlDkShadow", c.getRed(), c.getGreen(), c.getBlue()));

		}

		c = UIManager.getColor("activeCaption");

		if (c != null) {

			l.add(new SwingColor("activeCaption", c.getRed(), c.getGreen(), c.getBlue()));

		}

		c = UIManager.getColor("text");

		if (c != null) {

			l.add(new SwingColor("text", c.getRed(), c.getGreen(), c.getBlue()));

		}

		c = UIManager.getColor("desktop");

		if (c != null) {

			l.add(new SwingColor("desktop", c.getRed(), c.getGreen(), c.getBlue()));

		}

		swingColors = (SwingColor[]) l.toArray(new SwingColor[0]);

		return swingColors;

	}

	private static final String systemGenerate[] = { "activeCaption", "activeCaptionBorder", "activeCaptionText",
			"control", "controlDkShadow", "controlHighlight", "controlLtHighlight", "controlShadow", "controlText",
			"desktop", "inactiveCaption", "inactiveCaptionBorder", "inactiveCaptionText", "info", "infoText", "menu",
			"menuText", "scrollbar", "text", "textHighlight", "textHighlightText", "textInactiveText", "textText",
			"window", "windowBorder", "windowText" };

	private static final Color sColors[] = {

			SystemColor.activeCaption, SystemColor.activeCaptionBorder,

			SystemColor.activeCaptionText, SystemColor.control, SystemColor.controlDkShadow,

			SystemColor.controlHighlight, SystemColor.controlLtHighlight, SystemColor.controlShadow,

			SystemColor.controlText, SystemColor.desktop, SystemColor.inactiveCaption,

			SystemColor.inactiveCaptionBorder, SystemColor.inactiveCaptionText, SystemColor.info, SystemColor.infoText,

			SystemColor.menu, SystemColor.menuText, SystemColor.scrollbar, SystemColor.text, SystemColor.textHighlight,

			SystemColor.textHighlightText, SystemColor.textInactiveText, SystemColor.textText, SystemColor.window,

			SystemColor.windowBorder, SystemColor.windowText

	};

	private static class SysColor extends BasicNamedColor {

		public SysColor(String name, Color scolor) {

			super(name, scolor.getRed(), scolor.getGreen(), scolor.getBlue());

		}

		public String toString() {

			return "SystemColor." + getName();

		}

		public String getDisplayName() {

			return getName();

		}

	}

	private static NamedColor[] systemColors = null;

	private static NamedColor[] getSystemColors() {

		if (systemColors == null) {

			systemColors = new BasicNamedColor[sColors.length];

			for (int i = 0; i < sColors.length; i++) {

				systemColors[i] = new SysColor(systemGenerate[i], sColors[i]);

			}

		}

		return systemColors;

	}

}
