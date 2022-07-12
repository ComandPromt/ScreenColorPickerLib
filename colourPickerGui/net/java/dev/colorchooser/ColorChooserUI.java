package net.java.dev.colorchooser;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;

import com.bric.swing.ColorPicker;

public abstract class ColorChooserUI extends ComponentUI {

	protected ColorChooserUI() {

	}

	public final void installUI(JComponent jc) {

		installListeners((ColorChooser) jc);

		init((ColorChooser) jc);

	}

	public final void uninstallUI(JComponent jc) {

		uninstallListeners((ColorChooser) jc);

		uninit((ColorChooser) jc);

	}

	protected void init(ColorChooser c) {

	}

	protected void uninit(ColorChooser c) {

	}

	protected void installListeners(ColorChooser c) {

		L l = new L();

		c.addMouseListener(l);

		c.addFocusListener(l);

		c.addKeyListener(l);

		c.putClientProperty("uiListener", l);

	}

	protected void uninstallListeners(ColorChooser c) {

		Object o = c.getClientProperty("uiListener");

		if (o instanceof L) {

			L l = (L) o;

			c.removeMouseListener(l);

			c.removeFocusListener(l);

			c.removeKeyListener(l);

		}

	}

	protected int paletteIndexFromKeyCode(final KeyEvent ke) {

		int keyCode = ke.getKeyCode();

		int result = (keyCode == KeyEvent.VK_SHIFT) ? 1 : 0;

		if (MAC) {

			result += (keyCode == KeyEvent.VK_META) ? 2 : 0;

		}

		else {

			result += (keyCode == KeyEvent.VK_CONTROL) ? 2 : 0;

		}

		result += (keyCode == KeyEvent.VK_ALT) ? 4 : 0;

		return result;

	}

	protected int paletteIndexFromModifiers(InputEvent me) {

		int mods = me.getModifiersEx();

		int result = ((mods & me.SHIFT_DOWN_MASK) != 0) ? 1 : 0;

		result += ((mods & InputEvent.CTRL_DOWN_MASK) != 0) ? 2 : 0;

		result += ((mods & InputEvent.ALT_DOWN_MASK) != 0) ? 4 : 0;

		return result;

	}

	protected void keyboardInvoke(final ColorChooser colorChooser) {

		if (!colorChooser.isEnabled()) {

			Toolkit.getDefaultToolkit().beep();

			return;

		}

		Container top = colorChooser.getTopLevelAncestor();

		Color result = ColorPicker.showDialog(top, colorChooser.getColor());

		if (result != null) {

			colorChooser.setColor(result);

		}

	}

	protected void fireColorChanged(ColorChooser chooser) {

		chooser.fireActionPerformed(new ActionEvent(chooser, ActionEvent.ACTION_PERFORMED, "color"));

	}

	public Dimension getMaximumSize(JComponent c) {

		if (!c.isMaximumSizeSet()) {

			return getPreferredSize(c);

		}

		else {

			return super.getMaximumSize(c);

		}

	}

	public Dimension getMinimumSize(JComponent c) {

		if (!c.isMinimumSizeSet()) {

			return getPreferredSize(c);

		}

		else {

			return super.getMinimumSize(c);

		}

	}

	public Dimension getPreferredSize(JComponent c) {

		if (!c.isPreferredSizeSet()) {

			return new Dimension(24, 24);

		}

		else {

			return super.getPreferredSize(c);

		}

	}

	static boolean MAC = false;

	static {

		try {

			MAC = System.getProperty("mrj.version") != null;

		}

		catch (SecurityException e) {

		}

	}

	private class L extends MouseAdapter implements FocusListener, KeyListener {

		private int paletteIndex = 0;

		void initPaletteIndex(ColorChooser c, MouseEvent me) {

			paletteIndex = paletteIndexFromModifiers(me);

			checkRange(c);

		}

		private void checkRange(ColorChooser chooser) {

			Palette[] p = chooser.getPalettes();

			if (paletteIndex >= p.length) {

				paletteIndex = p.length - 1;

			}

		}

		public void mouseClicked(MouseEvent e) {

			Object o = e.getSource();

			if (o instanceof ColorChooser) {

				ColorChooser chooser = (ColorChooser) e.getSource();

				keyboardInvoke(chooser);

			}

		}

		public void mousePressed(MouseEvent me) {

			if (me.isPopupTrigger())

				return;

			ColorChooser chooser = (ColorChooser) me.getSource();

			if (!chooser.isEnabled()) {

				Toolkit.getDefaultToolkit().beep();

				return;

			}

			Point p = me.getPoint();

			SwingUtilities.convertPointToScreen(p, chooser);

			initPaletteIndex(chooser, me);

			chooser.requestFocus();

			me.consume();

		}

		public void mouseReleased(MouseEvent me) {

			if (me.isPopupTrigger())

				return;

			ColorChooser chooser = (ColorChooser) me.getSource();

			if (!chooser.isEnabled()) {

				Toolkit.getDefaultToolkit().beep();

				return;

			}

		}

		public void focusGained(FocusEvent e) {

			ColorChooser chooser = (ColorChooser) e.getSource();

			chooser.repaint();

		}

		public void focusLost(FocusEvent e) {

			ColorChooser chooser = (ColorChooser) e.getSource();

			chooser.repaint();

		}

		public void keyTyped(KeyEvent e) {

		}

		public void keyPressed(KeyEvent e) {

			processKeyEvent(e, true);

		}

		public void keyReleased(KeyEvent e) {

			processKeyEvent(e, false);

		}

		protected void processKeyEvent(KeyEvent ke, boolean pressed) {

			ColorChooser chooser = (ColorChooser) ke.getSource();

			if (ke.getKeyCode() == KeyEvent.VK_ALT || ke.getKeyCode() == KeyEvent.VK_CONTROL
					|| ke.getKeyCode() == KeyEvent.VK_SHIFT) {

				ke.consume();

			}

			else if ((ke.getKeyCode() == KeyEvent.VK_SPACE || ke.getKeyCode() == KeyEvent.VK_ENTER)
					&& ke.getID() == KeyEvent.KEY_RELEASED) {

				keyboardInvoke(chooser);

			}

		}

	}

}
