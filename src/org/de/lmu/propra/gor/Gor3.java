package org.de.lmu.propra.gor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

import org.de.lmu.propra.domain.TrainModel;
import org.de.lmu.propra.util.SecStructCode;

public class Gor3 extends AbstractGor {
	
	protected void calcOverAll() {
		int [][][][] model = trainmodel.getGor3();
		
		double [][] overall= frequencemodel.getFrequenceGor3();
		final int coilIndex=lookupSStoIndex(SecStructCode.COIL.getCode());
		final int sheetIndex=lookupSStoIndex(SecStructCode.SHEET.getCode());
		final int helixIndex= lookupSStoIndex(SecStructCode.HELIX.getCode());
		for (int secStruct=0; secStruct<3; secStruct++) { // struct
			for (int aminoMidAcid=0; aminoMidAcid<20; aminoMidAcid++) { //  AS	
				for (int aminoAcid=0; aminoAcid<20; aminoAcid++) { //  AS	
					overall[secStruct][aminoMidAcid] += model[secStruct][aminoMidAcid][aminoAcid][8];
				}
			}
		}	
		double [] summe = frequencemodel.getFrequenceOverAllSecStruct();
		for (int secStruct=0; secStruct<3; secStruct++) { // struct
			for (int aminoMidAcid=0; aminoMidAcid<20; aminoMidAcid++) { //  AS	
				summe[secStruct] += overall[secStruct][aminoMidAcid];
			}
		}
		final double[][] frequStructDiff = frequencemodel.getFrequStructDiffGor3();
		for (int aa=0; aa<20; aa++) { 
			frequStructDiff[COIL][aa]  = Math.log((overall[sheetIndex][aa] + overall[helixIndex][aa]) / overall[coilIndex][aa]);
			frequStructDiff[SHEET][aa] = Math.log((overall[coilIndex][aa] +  overall[helixIndex][aa]) / overall[sheetIndex][aa]);
			frequStructDiff[HELIX][aa] = Math.log((overall[sheetIndex][aa] + overall[ coilIndex][aa]) / overall[ helixIndex][aa]);
		}
	}
	
	
	@Override
	final protected void processWindowTrain(final InnerWindowIterator it) {
		Character currAA = null;
		final int[][][][] matrix = trainmodel.getGor3();
		final int ssindex = lookupSStoIndex(it.getMiddleCharSeq2());
		final int aamidindex = lookupAAtoIndex(it.getMiddleCharSeq1());
		if (aamidindex == -1) {
			return;
		}
		while ((currAA = it.next()) != null) {
			final int aaindex = lookupAAtoIndex(currAA);
			if (aaindex == -1) {
				continue;
			}

			final int posInWindow = it.getCurrPosInWindow();

			matrix[ssindex][aamidindex][aaindex][posInWindow] += 1;
		}

	}

	protected double calcProbability(final SecStructCode ss,
			final double[] valueForStruct) {
		// FORMEL solve log(x/(1-x))+log(y/z)=a for x =
		// e^i_delta*P(S)/(e^(i_delta)*P(S)+P(NOT S))
		// i_delta = f(sa)/overAllCount - f(nsa)/fns
		// http://www.wolframalpha.com/input/?i=solve+log%28x%2F%281-x%29%29%2Blog%28y%2Fz%29%3Da+for+x

		final double[] frequStructDifference = frequencemodel.getFrequenceOverAllSecStruct();
		
		final double FofSoverall = frequStructDifference[lookupSStoIndex(ss.getCode())];
		double notFofSoverall = 0.0;
		for (SecStructCode sec : SecStructCode.getInversElements(ss)) {
			notFofSoverall += frequStructDifference[lookupSStoIndex(sec.getCode())];
		}
		final double i_delta = valueForStruct[lookupSStoIndex(ss.getCode())];
		
		final double PofSoverall=FofSoverall      /      (notFofSoverall);

		double result = (PofSoverall * Math.exp(i_delta))
				/(1.0 + (PofSoverall * Math.exp(i_delta)));
		return result;
	}

	protected double[] processWindowPredict(final InnerWindowIterator it) {
		double valForC = 0.0;
		double valForE = 0.0;
		double valForH = 0.0;
		double forrealvalueForC=0.0;
		double forrealvalueForE=0.0;
		double forrealvalueForH=0.0;
		final int coilIndex=lookupSStoIndex(SecStructCode.COIL.getCode());
		final int sheetIndex=lookupSStoIndex(SecStructCode.SHEET.getCode());
		final int helixIndex= lookupSStoIndex(SecStructCode.HELIX.getCode());
		final int[][][][] model = trainmodel.getGor3();
		final double[][] frequStructDifference = frequencemodel.getFrequStructDiffGor3();
		Character currAA=null;
		while ((currAA=it.next()) != null) {
			final int currPos = it.getCurrPosInWindow();
			final int midPos = lookupAAtoIndex(it.getMiddleCharSeq1());
			if(midPos == -1){
				final double[] retVal = { Double.MIN_NORMAL, Double.MIN_NORMAL, Double.MIN_NORMAL };
				return retVal;
			}
			final int currAAPos = lookupAAtoIndex(currAA);
			if (currAAPos == -1) {
				continue;
			}
			valForC = model[coilIndex] [midPos][currAAPos][currPos];
			valForE = model[sheetIndex][midPos][currAAPos][currPos];
			valForH = model[helixIndex][midPos][currAAPos][currPos];

			forrealvalueForC+=Math.log(valForC/(valForE+valForH))+frequStructDifference[coilIndex][midPos];
			forrealvalueForE+=Math.log(valForE/(valForC+valForH))+frequStructDifference[sheetIndex][midPos];
			forrealvalueForH+=Math.log(valForH/(valForE+valForC))+frequStructDifference[helixIndex][midPos];
		}
	
		// calcprop
		double[] retVal = { forrealvalueForC, forrealvalueForE, forrealvalueForH };
		return retVal;

	}


	@Override
	public void writeModelToFile(String filename) {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(new File(
					filename)));
			final int[][][][] matrix = trainmodel.getGor3();
			writer.write("// Matrix4D\n\n");
			for(int i =0;i<matrix[COIL].length;i++){
					writer.write("\n\n=");
					writer.write(reversAA[i]);
					writer.write(",");
					writer.write("C=\n\t\n");
					writeMatrix(matrix[COIL][i], writer);
					writer.write("\n\n=");
					writer.write(reversAA[i]);
					writer.write(",");
					writer.write("E=\n\t\n");
					writeMatrix(matrix[SHEET][i], writer);
					writer.write("\n\n=");
					writer.write(reversAA[i]);
					writer.write(",");
					writer.write("H=\n\t\n");
					writeMatrix(matrix[HELIX][i], writer);
			}
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}	
	}


	@Override
	public TrainModel readModelToFileImpl(Reader read) {
			final BufferedReader reader;
			TrainModel model = new TrainModel();
			int[][][][] gormatix= trainmodel.getGor3();
			try {
				reader = new BufferedReader(read);
				String line=null; 
				int currStructIndex=0;
				int currAAIndex=0;
				while((line=reader.readLine())!=null){
					if(line.startsWith("=")){
						final String[] head=line.split(",");
						final char secStruct=head[head.length-1].replace("=", "").charAt(0);
						currStructIndex=lookupSStoIndex(secStruct);
						final char aa= head[0].replace("=", "").charAt(0);
						currAAIndex=lookupAAtoIndex(aa);
						gormatix[currStructIndex][currAAIndex]=readMatrix(reader);
					}
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			model.setGor3(gormatix);
			return model;
	}





}
