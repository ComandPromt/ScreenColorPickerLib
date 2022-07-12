package com.colourpicker;

import java.awt.Robot;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class KeyLogger implements NativeKeyListener {

	Robot robot;

	public KeyLogger(Robot r) {

		this.robot = r;

	}

	int x = 0;

	int y = 0;

	public void nativeKeyTyped(NativeKeyEvent nativeEvent) {

	}

	public void nativeKeyPressed(NativeKeyEvent nativeEvent) {

		try {

			if (!(robot instanceof Robot)) {

				robot = new Robot();

			}

			x = ColourPicker.frame.getX();

			y = ColourPicker.frame.getY();

			x -= 10;

			switch (nativeEvent.getKeyCode()) {

			case 57421:

				x++;

				y += 9;

				break;

			case 57419:

				x--;

				y += 9;

				break;

			case 57416:

				y += 8;

				break;

			case 57424:

				y += 10;

				break;

			default:

				ColourPicker.cerrar();

				break;
			}

			robot.mouseMove(x, ++y);

		}

		catch (Exception e) {

		}

	}

	public void nativeKeyReleased(NativeKeyEvent nativeEvent) {

	}

}
