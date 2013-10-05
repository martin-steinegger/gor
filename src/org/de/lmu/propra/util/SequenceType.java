package org.de.lmu.propra.util;

public enum SequenceType {
	ORF("ORF"), AA("AA"), DNA("DNA"), GEN("GEN"), RNA("RNA"), ALIGNMENT_SECSTRUCT(
			"ALIGNMENT_SECSTRUCT"), ALIGNMENT_AA("ALIGNMENT_AA");

	private String sequenceType;

	private SequenceType(String c) {
		sequenceType = c;
	}

	public String getSequenceType() {
		return sequenceType;
	}

}