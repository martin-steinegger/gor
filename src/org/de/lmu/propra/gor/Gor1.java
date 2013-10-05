package org.de.lmu.propra.gor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

import org.de.lmu.propra.domain.TrainModel;
import org.de.lmu.propra.util.SecStructCode;

public class Gor1 extends AbstractGor {

	protected void calcOverAll() {
		int[][][] model = trainmodel.getGor1();
		double[] overall = frequencemodel.getFrequenceOverAllSecStruct();
		final int coilIndex = lookupSStoIndex(SecStructCode.COIL.getCode());
		final int sheetIndex = lookupSStoIndex(SecStructCode.SHEET.getCode());
		final int helixIndex = lookupSStoIndex(SecStructCode.HELIX.getCode());
		for (int secStruct = 0; secStruct < 3; secStruct++) { // struct
			for (int aminoAcid = 0; aminoAcid < 20; aminoAcid++) { // AS
				for (int winPos = 0; winPos < 17; winPos++) {
					overall[secStruct] += model[secStruct][aminoAcid][winPos];
				}
			}
		}
		final double[] frequStructDifference = frequencemodel
				.getFrequStructDiffGor1();
		frequStructDifference[COIL] = Math
				.log((overall[sheetIndex] + overall[helixIndex])
						/ overall[coilIndex]);
		frequStructDifference[SHEET] = Math
				.log((overall[coilIndex] + overall[helixIndex])
						/ overall[sheetIndex]);
		frequStructDifference[HELIX] = Math
				.log((overall[sheetIndex] + overall[coilIndex])
						/ overall[helixIndex]);
	}

	@Override
	final protected void processWindowTrain(final InnerWindowIterator it) {
		Character currAA = null;
		final int[][][] matrix = trainmodel.getGor1();
		final int ssindex = lookupSStoIndex(it.getMiddleCharSeq2());
		while ((currAA = it.next()) != null) {
			final int aaindex = lookupAAtoIndex(currAA);
			if (aaindex == -1) {
				continue;
			}

			final int posInWindow = it.getCurrPosInWindow();

			matrix[ssindex][aaindex][posInWindow] += 1;
		}

	}

	protected double calcProbability(final SecStructCode ss,
			final double[] valueForStruct) {
		// FORMEL solve log(x/(1-x))+log(y/z)=a for x =
		// e^i_delta*P(S)/(e^(i_delta)*P(S)+P(NOT S))
		// i_delta = f(sa)/fs - f(nsa)/fns
		// http://www.wolframalpha.com/input/?i=solve+log%28x%2F%281-x%29%29%2Blog%28y%2Fz%29%3Da+for+x

		double[] overAllCount = frequencemodel.getFrequenceOverAllSecStruct();
		final double FofSoverall = overAllCount[lookupSStoIndex(ss.getCode())];
		double notFofSoverall = 0.0;
		for (SecStructCode sec : SecStructCode.getInversElements(ss)) {
			notFofSoverall += overAllCount[lookupSStoIndex(sec.getCode())];
		}
		final double i_delta = valueForStruct[lookupSStoIndex(ss.getCode())];

		final double PofSoverall = FofSoverall / (notFofSoverall);

		double result = (PofSoverall * Math.exp(i_delta))
				/ (1.0 + (PofSoverall * Math.exp(i_delta)));
		return result;
	}

	protected double[] processWindowPredict(final InnerWindowIterator it) {
		double valueForC = 0.0;
		double valueForE = 0.0;
		double valueForH = 0.0;
		double forrealvalueForC = 0.0;
		double forrealvalueForE = 0.0;
		double forrealvalueForH = 0.0;
		Character currAA = null;
		final int coilIndex = lookupSStoIndex(SecStructCode.COIL.getCode());
		final int sheetIndex = lookupSStoIndex(SecStructCode.SHEET.getCode());
		final int helixIndex = lookupSStoIndex(SecStructCode.HELIX.getCode());
		final int[][][] matrix = trainmodel.getGor1();
		final double[] frequStructDifference = frequencemodel
				.getFrequStructDiffGor1();
		while ((currAA = it.next()) != null) {
			final int aaindex = lookupAAtoIndex(currAA);
			if (aaindex == -1) {
				continue;
			}
			final int currPos = it.getCurrPosInWindow();
			valueForC = matrix[coilIndex][aaindex][currPos];
			valueForE = matrix[sheetIndex][aaindex][currPos];
			valueForH = matrix[helixIndex][aaindex][currPos];
			forrealvalueForC += Math.log(valueForC / (valueForE + valueForH))
					+ frequStructDifference[coilIndex];
			forrealvalueForE += Math.log(valueForE / (valueForC + valueForH))
					+ frequStructDifference[sheetIndex];
			forrealvalueForH += Math.log(valueForH / (valueForE + valueForC))
					+ frequStructDifference[helixIndex];
		}

		// calcprop
		double[] retVal = { forrealvalueForC, forrealvalueForE,
				forrealvalueForH };
		return retVal;

	}

	@Override
	public void writeModelToFile(String filename) {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(new File(
					filename)));
			final int[][][] matrix = trainmodel.getGor1();
			writer.write("// Matrix3D\n\n");
			writer.write("\n\n=C=\n\t\n");
			writeMatrix(matrix[COIL], writer);
			writer.write("\n\n=E=\n\t\n");
			writeMatrix(matrix[SHEET], writer);
			writer.write("\n\n=H=\n\t\n");
			writeMatrix(matrix[HELIX], writer);
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public TrainModel readModelToFileImpl(Reader read) {
		final BufferedReader reader;
		final TrainModel model = new TrainModel();
		int[][][] gormatix= trainmodel.getGor1();
		try {
			reader = new BufferedReader(read);
			String line=null; 
			int currStructIndex=0;
			while((line=reader.readLine())!=null){
				if(line.startsWith("=")){
					final String[] head=line.split(",");
					final char secStruct= head[head.length-1].replace("=", "").charAt(0);
					currStructIndex=lookupSStoIndex(secStruct);
					gormatix[currStructIndex]=readMatrix(reader);
				}
			}
			
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		model.setGor1(gormatix);
		return model;
	}

}
