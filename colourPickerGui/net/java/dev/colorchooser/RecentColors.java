
package net.java.dev.colorchooser;

import java.awt.Color;
import java.awt.Dimension;
import java.security.AccessControlException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.prefs.Preferences;

class RecentColors extends Palette {

	private Palette palette;

	private boolean changed = true;

	private RecentColors() {
	}

	private Palette getWrapped() {

		if (changed || palette == null) {

			palette = createPalette();

			changed = false;

		}

		return palette;

	}

	public java.awt.Color getColorAt(int x, int y) {

		return getWrapped().getColorAt(x, y);

	}

	public String getDisplayName() {

		try {

			return ResourceBundle.getBundle("org.netbeans.swing.colorchooser.Bundle").getString("recent"); 

		} catch (MissingResourceException mre) {

			return "Recent colors";

		}

	}

	public Dimension getSize() {

		Dimension result = ((PredefinedPalette) getWrapped()).calcSize();

		return result;

	}

	public void paintTo(java.awt.Graphics g) {

		getWrapped().paintTo(g);

	}

	public String getNameAt(int x, int y) {

		return getWrapped().getNameAt(x, y);

	}

	Stack stack = new Stack();

	void add(Color c) {

		if (c instanceof RecentColor) {

			return;

		}

		if (stack.indexOf(c) == -1) {

			String name = c instanceof PredefinedPalette.BasicNamedColor
					? ((PredefinedPalette.BasicNamedColor) c).getDisplayName()
					: null;

			String toString = c instanceof PredefinedPalette.BasicNamedColor
					? ((PredefinedPalette.BasicNamedColor) c).toString()
					: null;

			Color col = new RecentColor(name, c.getRed(), c.getGreen(), c.getBlue(), toString);

			stack.push(col);

			changed = true;

			palette = null;

			if (c instanceof NamedColor) {

				addToNameCache((NamedColor) c);

			}

			saveToPrefs();

		}

	}

	public static final String INNER_DELIMITER = "^$";

	public static final String OUTER_DELIMITER = "!*";

	public void saveToPrefs() {

		Preferences prefs = getPreferences();

		if (prefs == null)
			return;

		int count = 0;

		StringBuffer sb = new StringBuffer();

		Stack stack = new Stack();

		stack.addAll(this.stack);

		while (!stack.isEmpty() && count < 64) {

			count++;

			Color c = (Color) stack.pop();

			if (c instanceof DummyColor) {

				break;

			}

			String name = "null";

			if (c instanceof PredefinedPalette.BasicNamedColor) {

				PredefinedPalette.BasicNamedColor nc = (PredefinedPalette.BasicNamedColor) c;

				name = nc.getDisplayName();

			}

			if (name == "null") {

				name = null;

			}

			sb.append(name);

			sb.append(INNER_DELIMITER);

			sb.append(c.getRed());

			sb.append(INNER_DELIMITER);

			sb.append(c.getGreen());

			sb.append(INNER_DELIMITER);

			sb.append(c.getBlue());

			sb.append(INNER_DELIMITER);

			if (c instanceof PredefinedPalette.BasicNamedColor) {

				sb.append(c.toString());

			}

			else {

				sb.append('x');

			}

			sb.append(OUTER_DELIMITER); 

		}

		prefs.put("recentColors", sb.toString()); 

	}

	static Map namedMap = null;

	static NamedColor findNamedColor(Color color) {

		if (namedMap == null) {

			return null;

		}

		NamedColor result = (NamedColor) namedMap.get(new Integer(color.getRGB()));

		return result;

	}

	static void addToNameCache(NamedColor color) {

		if (namedMap == null) {

			namedMap = new HashMap(40);

		}

		namedMap.put(new Integer(color.getRGB()), color);

	}

	private Preferences getPreferences() {

		try {

			Preferences base = Preferences.userNodeForPackage(getClass());

			return base.node("1.5"); 

		}

		catch (AccessControlException ace) {

			return null;

		}

	}

	public void loadFromPrefs() {

		Preferences prefs = getPreferences();

		if (prefs == null)
			return;

		String s = prefs.get("recentColors", null); 

		stack = new Stack();

		Color[] col = new Color[64];

		Arrays.fill(col, new DummyColor());

		int count = 63;

		try {

			if (s != null) {

				StringTokenizer tok = new StringTokenizer(s, OUTER_DELIMITER); 

				while (tok.hasMoreTokens() && count >= 0) {

					String curr = tok.nextToken();

					StringTokenizer tk2 = new StringTokenizer(curr, INNER_DELIMITER); 

					while (tk2.hasMoreTokens()) {

						String name = tk2.nextToken();

						if ("null".equals(name)) {

							name = null;

						}

						int r = Integer.parseInt(tk2.nextToken());

						int g = Integer.parseInt(tk2.nextToken());

						int b = Integer.parseInt(tk2.nextToken());

						String toString = tk2.nextToken();

						if ("x".equals(toString)) { 

							col[count] = new RecentColor(name, r, g, b);

						}

						else {

							col[count] = new RecentColor(name, r, g, b, toString);

						}

						addToNameCache((NamedColor) col[count]);

					}

					count--;

				}

			}

			stack.addAll(Arrays.asList(col));

		}

		catch (Exception e) {

		}

	}

	private Palette createPalette() {

		PredefinedPalette.BasicNamedColor[] nc = (PredefinedPalette.BasicNamedColor[]) stack
				.toArray(new PredefinedPalette.BasicNamedColor[0]);

		return new PredefinedPalette("", nc);

	}

	private class RecentColor extends PredefinedPalette.BasicNamedColor {

		String displayName;

		String toString = null;

		public RecentColor(String name, int r, int g, int b) {

			super(name, r, g, b);

			displayName = name;

		}

		public RecentColor(String name, int r, int g, int b, String toString) {

			this(name, r, g, b);

			displayName = name;

			this.toString = toString;

		}

		public int compareTo(Object o) {

			return stack.indexOf(o) - stack.indexOf(this);

		}

		public String getDisplayName() {

			return displayName;

		}

		public boolean equals(Object o) {

			if (o instanceof Color) {

				Color c = (Color) o;

				return c.getRGB() == getRGB();

			}

			return false;

		}

		public int hashCode() {

			return getRGB();

		}

		public String toString() {

			if (toString != null) {

				return toString;

			}

			else {

				return "new java.awt.Color(" + getRed() + "," + getGreen() + "," + getBlue() + ")"; 

			}

		}

	}

	private static RecentColors defaultInstance = null;

	public static final RecentColors getDefault() {

		if (defaultInstance == null) {

			defaultInstance = new RecentColors();

			((RecentColors) defaultInstance).loadFromPrefs();

		}

		return defaultInstance;

	}

	private class DummyColor extends RecentColor {

		public DummyColor() {

			super(null, 0, 0, 0);

		}

		public String getDisplayName() {

			return null;

		}

		public boolean equals(Object o) {

			return o == this;

		}

		public int hashCode() {

			return System.identityHashCode(this);

		}

	}

}
