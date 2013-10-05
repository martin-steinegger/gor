package org.de.lmu.propra.util;

import java.util.ArrayList;
import java.util.List;

public enum SecStructCode {
	HELIX('H'), SHEET('E'), COIL('C');

	private char code;

	private SecStructCode(char c) {
		code = c;
	}

	public char getCode() {
		return code;
	}

	public static SecStructCode parseCode(String code) {
		if (code == null)
			return null;
		if (code.equals("HELIX") || code.equals("G") || code.equals("H"))
			return HELIX;
		else if (code.equals("TURN") || code.equals("I") || code.equals("T")
				|| code.equals("S"))
			return COIL;
		else if (code.equals("STRAND") || code.equals("B") || code.equals("E"))
			return SHEET;
		return null;

	}

	public static List<SecStructCode> getInversElements(SecStructCode ss) {
		List<SecStructCode> retList = new ArrayList<SecStructCode>();
		for (SecStructCode otherStruct : SecStructCode.values()) {
			if (ss.equals(otherStruct) == false) {
				retList.add(otherStruct);
			}
		}
		return retList;
	}
}