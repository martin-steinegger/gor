package org.de.lmu.propra.gor;

import java.util.Iterator;


public class InnerWindowIterator implements Iterator<Character> {
	private String sequence1;
	private String sequence2;
	private int currPos;
	private int start;
	private int end;

	public InnerWindowIterator(final String sequence1, final String sequence2,
			int start, int end) {
		this.sequence1 = sequence1;
		this.sequence2 = sequence2;
		this.start = start;
		this.currPos = start - 1;
		this.end = end;
	}

	@Override
	public boolean hasNext() {
		if ((currPos + 1) < end)
			return true;
		else
			return false;
	}

	@Override
	public Character next() {
		if (hasNext() == true) {
			currPos += 1;
			return sequence1.charAt(currPos);

		} else {

			return null;
		}
	}

	public Character getSeq1Char() {
		return sequence1.charAt(currPos);
	}

	public Character getSeq2Char() {
		return sequence2.charAt(currPos);
	}

	public int getCurrPosInWindow() {
		return currPos - start;
	}

	public char getMiddleCharSeq1() {
		return sequence1.charAt(start+(end - start) / 2);
	}
	
	public char getMiddleCharSeq2() {
		return sequence2.charAt(start+(end - start) / 2);
	}

	public int getWindowSize() {
		return (end-start);
	}

	public char getCharAtSeq1(int pos) {
		return sequence1.charAt(start+pos);
	}
	public char getCharAtSeq2(int pos) {
		return sequence2.charAt(start+pos);
	}
	
	public boolean isMiddle() {
		return (currPos == (end - start) / 2) ? true : false;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	public int getCurrPos() {
		return currPos;
	}

}
