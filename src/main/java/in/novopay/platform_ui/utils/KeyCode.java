package in.novopay.platform_ui.utils;

import java.util.HashMap;

class KeyCode {

	HashMap<String, Integer> keyCodes = new HashMap<String, Integer>();
	HashMap<String, Integer> numKeyCodes = new HashMap<String, Integer>();

	public KeyCode() {
		// for alpha keypad
		keyCodes.put("0", 8);
		keyCodes.put("1", 9);
		keyCodes.put("2", 10);
		keyCodes.put("3", 11);
		keyCodes.put("4", 12);
		keyCodes.put("5", 13);
		keyCodes.put("6", 14);
		keyCodes.put("7", 15);
		keyCodes.put("8", 16);
		keyCodes.put("9", 17);
		keyCodes.put("A", 29);
		keyCodes.put("B", 30);
		keyCodes.put("C", 31);
		keyCodes.put("D", 32);
		keyCodes.put("E", 33);
		keyCodes.put("F", 34);
		keyCodes.put("G", 35);
		keyCodes.put("H", 36);
		keyCodes.put("I", 37);
		keyCodes.put("J", 38);
		keyCodes.put("K", 39);
		keyCodes.put("L", 40);
		keyCodes.put("M", 41);
		keyCodes.put("N", 42);
		keyCodes.put("O", 43);
		keyCodes.put("P", 44);
		keyCodes.put("Q", 45);
		keyCodes.put("R", 46);
		keyCodes.put("S", 47);
		keyCodes.put("T", 48);
		keyCodes.put("U", 49);
		keyCodes.put("V", 50);
		keyCodes.put("W", 51);
		keyCodes.put("X", 52);
		keyCodes.put("Y", 53);
		keyCodes.put("Z", 54);
		keyCodes.put(" ", 62);
		keyCodes.put("leftShift", 59);

		numKeyCodes.put("0", 144);
		numKeyCodes.put("1", 145);
		numKeyCodes.put("2", 146);
		numKeyCodes.put("3", 147);
		numKeyCodes.put("4", 148);
		numKeyCodes.put("5", 149);
		numKeyCodes.put("6", 150);
		numKeyCodes.put("7", 151);
		numKeyCodes.put("8", 152);
		numKeyCodes.put("9", 153);
	}

	public int getKeyCode(String key) {
		return keyCodes.get(key);
	}

	public int getNumKeyCode(String key) {
		return numKeyCodes.get(key);
	}

}
