package com.colourpicker;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;

public class MouseLogger implements NativeMouseListener {

	public static void main(String[] args) {

		try {

			GlobalScreen.registerNativeHook();

		}

		catch (Exception e) {

			try {

				ColourPicker.cerrar();

			}

			catch (NativeHookException e1) {

				e1.printStackTrace();

			}

		}

		GlobalScreen.addNativeMouseListener(new MouseLogger());

	}

	public void nativeMouseClicked(NativeMouseEvent nativeEvent) {

	}

	public void nativeMousePressed(NativeMouseEvent nativeEvent) {

		try {

			ColourPicker.cerrar();

		}

		catch (NativeHookException e) {

			e.printStackTrace();

		}

	}

	public void nativeMouseReleased(NativeMouseEvent nativeEvent) {

		try {

			ColourPicker.cerrar();

		}

		catch (NativeHookException e) {

			e.printStackTrace();

		}

	}

}
