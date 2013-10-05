package org.de.lmu.propra.gor;

import java.util.Iterator;


public class WindowIterator implements Iterator<InnerWindowIterator> {
	public final int SEQUENCE1 = 1;
	public final int SEQUENCE2 = 2;
	private String sequence1;
	private String sequence2;
	final private int windowSize = 17;
	final private int halfWindowDist = (17 / 2);
	final private int midDist = halfWindowDist + 1;
	private int currPos;
	private int sequenceLength;

	public WindowIterator(final String sequence1, final String sequence2) {
		if (sequence1.length() != sequence2.length())
			throw new RuntimeException("Sequences should have the same size");
		if (sequence1.length() < windowSize)
			throw new RuntimeException("Sequnece to small");
		this.sequence1 = sequence1;
		this.sequence2 = sequence2;
		sequenceLength = this.sequence1.length();
		currPos = midDist - 1; // for first value
	}

	@Override
	public boolean hasNext() {
		if (((currPos + 1) + halfWindowDist) <= sequenceLength)
			return true;
		else
			return false;
	}

	@Override
	public InnerWindowIterator next() {
		if (hasNext() == true) {
			currPos += 1;
			return new InnerWindowIterator(sequence1, sequence2, currPos
					- halfWindowDist - 1, currPos + halfWindowDist);

		} else {

			return null;
		}

	}

	public String getSeq2Window() {
		return sequence2.substring(currPos - halfWindowDist - 1, currPos
				+ halfWindowDist);
	}

	public String getSeq1Window() {
		return sequence1.substring(currPos - halfWindowDist - 1, currPos
				+ halfWindowDist);
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}


}
