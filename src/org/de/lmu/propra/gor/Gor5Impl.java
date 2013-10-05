package org.de.lmu.propra.gor;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.List;

import org.de.lmu.propra.domain.PredictResult;
import org.de.lmu.propra.domain.Sequence;
import org.de.lmu.propra.domain.TrainModel;
import org.de.lmu.propra.util.SecStructCode;

public class Gor5Impl implements Gor5 {
	private double[][][] probability;	
	private PredictResult predictresult = new PredictResult();
	private  int  sequenceCounter;

	@Override
	public PredictResult predict(Gor gor, List<Sequence> list) {
		predictresult = new PredictResult();
		final String toPredict = list.get(0).getSequence();
		predictresult.setPbdid(list.get(0).getPdbId());
		sequenceCounter = list.size();
		// SECSTRUCTS MAXLENGHT OF ALIGN ELMENT FROM SEQ
		probability = new double[TrainModel.SECSTRUCTS][sequenceCounter][toPredict.length()];
		for (int seqIndex = 0; seqIndex < list.size(); seqIndex++) {
			final Sequence seq = list.get(seqIndex);
			final String currSequ = seq.getSequence();
			final String seqToPredict = currSequ.replace("-", "");
			PredictResult pr = gor.predict(seqToPredict);
			if(pr==null)
				continue;
			final String pcString = pr.getPcseq().toString();
			final String peString = pr.getPeseq().toString();
			final String phString = pr.getPhseq().toString();
			mapToMatrix(SecStructCode.COIL,
					pcString.substring(8, pcString.length()-8), currSequ, seqIndex,pr.getPcvalue());
			mapToMatrix(SecStructCode.SHEET,
					peString.substring(8, peString.length()-8), currSequ, seqIndex,pr.getPevalue());
			mapToMatrix(SecStructCode.HELIX,
					phString.substring(8, phString.length()-8), currSequ, seqIndex,pr.getPhvalue());
		
		}
		predictresult.appendAASeq(toPredict);
		for(int strPos = 0; strPos<toPredict.length(); strPos++){
			final double probForC=calcProbability(AbstractGor.COIL,strPos);
			final double probForE=calcProbability(AbstractGor.SHEET,strPos);
			final double probForH=calcProbability(AbstractGor.HELIX,strPos);
			addSecResult(probForC, probForE, probForH);
			addProbability(probForC, probForE, probForH);
		}
		
		
		return 	predictresult;

	}
	

	private void addProbability(double probForC, double probForE,
			double probForH) {
		if (probForC == Double.MIN_NORMAL && probForE == Double.MIN_NORMAL
				&& probForH == Double.MIN_NORMAL){
			predictresult.appendPCSeq("-");
			predictresult.addPCValue(Double.MIN_NORMAL);
			predictresult.appendPESeq("-");
			predictresult.addPEValue(Double.MIN_NORMAL);
			predictresult.appendPHSeq("-");
			predictresult.addPHValue(Double.MIN_NORMAL);
		}else{	
			final int probC = (int) (probForC * 10);
			predictresult.appendPCSeq(probC);
			predictresult.addPCValue(probForC);
			final int probE = (int) (probForE * 10);
			predictresult.appendPESeq(probE);
			predictresult.addPEValue(probForE);
			final int probH = (int) (probForH * 10);
			predictresult.appendPHSeq(probH);
			predictresult.addPHValue(probForH);
		}
	}
	
	
	private void addSecResult(double valueForC, double valueForE,
			double valueForH) {
		if (valueForC > valueForE && valueForC > valueForH) {
			predictresult.appendSSSeq(SecStructCode.COIL.getCode());
		} else if (valueForE > valueForC && valueForE > valueForH) {
			predictresult.appendSSSeq(SecStructCode.SHEET.getCode());
		} else if (valueForH > valueForC && valueForH > valueForE) {
			predictresult.appendSSSeq(SecStructCode.HELIX.getCode());
		} else if (valueForC == Double.MIN_NORMAL && valueForE == Double.MIN_NORMAL
				&& valueForH == Double.MIN_NORMAL){
			predictresult.appendSSSeq('-');
		}else
			throw new RuntimeException();
	}
	
	private double calcProbability(final int sstype, final int strPos){
		double sumProb = 0;
		int counter=0;
		for(int seqIndex=0; seqIndex<sequenceCounter;seqIndex++){
			if(probability[sstype][seqIndex][strPos]!=Double.MIN_NORMAL){
				counter++;
				sumProb += probability[sstype][seqIndex][strPos];
			}
			
		}
		if(counter==0||sumProb==0){
			return Double.MIN_NORMAL;
		}
		return sumProb / counter;
	}

	private void mapToMatrix(SecStructCode ss, String predictionStrings,
			final String sequenceWithGap, final int elmentPos, List<Double> probValue) {
		final int ssindex = AbstractGor.lookupSStoIndex(ss.getCode());
		final CharacterIterator predIt = new StringCharacterIterator(
				predictionStrings);
		final CharacterIterator sequIt = new StringCharacterIterator(
				sequenceWithGap);
		// to skip the first 8 ASCII chars
		int counter =0;
		for (char sequValue=sequIt.first(); sequValue != CharacterIterator.DONE; sequValue=sequIt.next()) {
			if(sequValue!='-'){
				counter++;
			}
			probability[ssindex][elmentPos][sequIt.getIndex()] = Double.MIN_NORMAL;
			if(counter==9)
				break;
		}
		for (char sequValue=sequIt.current(); sequValue != CharacterIterator.DONE; sequValue=sequIt.next()) {
			if (sequValue == '-') {
				probability[ssindex][elmentPos][sequIt.getIndex()] = Double.MIN_NORMAL;
			} else {
				final Character predValue = predIt.current();
				if(predValue==CharacterIterator.DONE)
					break;
				if(predValue=='-'){
					probability[ssindex][elmentPos][sequIt.getIndex()] = Double.MIN_NORMAL;
				}else{
					probability[ssindex][elmentPos][sequIt.getIndex()] = probValue.get(predIt.getIndex());
				}
				predIt.next();
			}
		}
		for (char sequValue=sequIt.current(); sequValue != CharacterIterator.DONE; sequValue=sequIt.next()) {
			probability[ssindex][elmentPos][sequIt.getIndex()] = Double.MIN_NORMAL;
		}
	}
}
