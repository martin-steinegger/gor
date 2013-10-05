package org.de.lmu.propra.domain;

public class SequenceSequence {
	private Sequence sequence1;
	private Sequence sequence2;
	private long alignid;

	
	public SequenceSequence(){
	}
	public SequenceSequence(Sequence sequence1, Sequence sequence2, long alignid) {
		this.sequence1 = sequence1;
		this.sequence2 = sequence2;
		this.alignid = alignid;
	}

	public Sequence getSequence1() {
		return sequence1;
	}

	public Sequence getSequence2() {
		return sequence2;
	}

	public void setSequence1(Sequence sequence1) {
		this.sequence1 = sequence1;
	}

	public void setSequence2(Sequence sequence2) {
		this.sequence2 = sequence2;
	}

	public long getAlignid() {
		return alignid;
	}

	public void setAlignid(long alignid) {
		this.alignid = alignid;
	}

}
