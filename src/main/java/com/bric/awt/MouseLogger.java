package com.bric.awt;

import org.jnativehook.GlobalScreen;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;

public class MouseLogger implements NativeMouseListener {

	public static void main(String[] args) {

		try {

			GlobalScreen.registerNativeHook();

		}

		catch (Exception e) {

			ColourPicker.cerrar();

		}

		GlobalScreen.addNativeMouseListener(new MouseLogger());

	}

	public void nativeMouseClicked(NativeMouseEvent nativeEvent) {

	}

	public void nativeMousePressed(NativeMouseEvent nativeEvent) {

		ColourPicker.cerrar();

	}

	public void nativeMouseReleased(NativeMouseEvent nativeEvent) {

		ColourPicker.cerrar();

	}

}
