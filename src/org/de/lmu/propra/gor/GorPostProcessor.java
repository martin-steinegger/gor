package org.de.lmu.propra.gor;

import org.de.lmu.propra.domain.PredictResult;

public class GorPostProcessor {

	public PredictResult process(PredictResult result) {
		final String asstruct = result.getAaseq().toString();
		final String ssstruct = result.getSsseq().toString();
		final WindowIterator windowIterator = new WindowIterator(ssstruct,asstruct
				);
		InnerWindowIterator inWIt = null;
		final StringBuilder ssBuild = new StringBuilder();
		ssBuild.append("--------");
		while ((inWIt = windowIterator.next()) != null) {
			Character currAA = null;

			while ((currAA = inWIt.next()) != null) {
				char midChar=inWIt.getMiddleCharSeq1();
				if(midChar=='-'){
					ssBuild.append(midChar);
					continue;
				}
				
				final int indexMid = inWIt.getWindowSize() / 2;
				char posMinusOne = inWIt.getCharAtSeq1(indexMid- 1);
				char posMinusTwo = inWIt.getCharAtSeq1(indexMid - 2);
				char posPlusOne = inWIt.getCharAtSeq1(indexMid + 2);
				char posPlusTwo = inWIt.getCharAtSeq1(indexMid + 3);
				if (posMinusOne!='-'&&posMinusOne == posMinusTwo && posMinusTwo == posPlusOne
						&& posMinusOne == posPlusTwo&&midChar!=posMinusOne) {
					ssBuild.append(posMinusOne);
					break;
				}else{
					ssBuild.append(midChar);
					break;
				}
			}
		}
		ssBuild.append("--------");
		PredictResult pr = new PredictResult();
		pr.appendAASeq(result.getAaseq().toString());
		pr.appendPCSeq(result.getPcseq().toString());
		pr.appendPESeq(result.getPeseq().toString());
		pr.appendPHSeq(result.getPhseq().toString());
		pr.appendSSSeq(ssBuild.toString());
		return pr;

	}

}
