package org.de.lmu.propra.gor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

import org.de.lmu.propra.domain.TrainModel;
import org.de.lmu.propra.util.SecStructCode;

public class Gor4 extends AbstractGor {
	protected void calcOverAll() {
		int[][][][][] model = trainmodel.getGor4();
		double[][][] overall = frequencemodel.getFrequenceGor4();
		final int coilIndex = lookupSStoIndex(SecStructCode.COIL.getCode());
		final int sheetIndex = lookupSStoIndex(SecStructCode.SHEET.getCode());
		final int helixIndex = lookupSStoIndex(SecStructCode.HELIX.getCode());
		for (int secStruct = 0; secStruct < 3; secStruct++) // struct
			for (int aminoAcid1 = 0; aminoAcid1 < 20; aminoAcid1++)  // AA1
				for (int winPos1 = 0; winPos1 < 17; winPos1++)  // Win1
					for (int aminoAcid2 = 0; aminoAcid2 < 20; aminoAcid2++)  // AA2
						for (int winPos2 = 0; winPos2 < 17; winPos2++)  //
							overall[secStruct][aminoAcid1][winPos1] += model[secStruct][aminoAcid1][winPos1][aminoAcid2][winPos2];
		double [] summe = frequencemodel.getFrequenceOverAllSecStruct();
		for (int secStruct = 0; secStruct < 3; secStruct++) // struct
			for (int aminoAcid1 = 0; aminoAcid1 < 20; aminoAcid1++)  // AA1
				for (int winPos1 = 0; winPos1 < 17; winPos1++)  // Win1
					summe[secStruct]+=overall[secStruct][aminoAcid1][winPos1];
		
		final double[][][] frequStructDiff = frequencemodel.getFrequStructDiffGor4();
		for (int aaPos1 = 0; aaPos1 < 20; aaPos1++) {
			for (int winPos1 = 0; winPos1 < 17; winPos1++) {
				frequStructDiff[COIL][aaPos1][winPos1] =  Math.log( overall[coilIndex][aaPos1][winPos1]   /(overall[sheetIndex][aaPos1][winPos1] + overall[helixIndex][aaPos1][winPos1]) );
				frequStructDiff[SHEET][aaPos1][winPos1] = Math.log( overall[sheetIndex][aaPos1][winPos1] /(overall[coilIndex][aaPos1][winPos1] + overall[helixIndex][aaPos1][winPos1]) );
				frequStructDiff[HELIX][aaPos1][winPos1] = Math.log( overall[helixIndex][aaPos1][winPos1] /(overall[sheetIndex][aaPos1][winPos1] + overall[coilIndex][aaPos1][winPos1]) );
			}
		}
	}

	@Override
	final protected void processWindowTrain(final InnerWindowIterator it) {
		Character currAA = null;
		final int[][][][][] matrix = trainmodel.getGor4();
		final int ssindex = lookupSStoIndex(it.getMiddleCharSeq2());

		while ((currAA = it.next()) != null) {
			final int aa1index = lookupAAtoIndex(currAA);
			if (aa1index == -1) {
				continue;
			}
			final int win1pos = it.getCurrPosInWindow();

			for (int l = win1pos + 1; l < it.getWindowSize(); l++) {
				final int win2Pos = l;
				final int aa2index = lookupAAtoIndex(it.getCharAtSeq1(l));
				if (aa2index == -1) {
					continue;
				}
				matrix[ssindex][aa1index][win1pos][aa2index][win2Pos] += 1;
			}
		}
	}

	protected double calcProbability(final SecStructCode ss,
			final double[] valueForStruct) {
		// e^(z)/(e^(z)+1)
		// z = 2/17 ......


		final double i_delta = valueForStruct[lookupSStoIndex(ss.getCode())];


		double result = (Math.exp(i_delta))
				/ (1.0 + Math.exp(i_delta));
		return result;
	}

	protected double[] processWindowPredict(final InnerWindowIterator it) {
		 double valForC = 0.0;
		 double valForE = 0.0;
		 double valForH = 0.0;
		 double forrealvalueForC=0.0;
		 double forrealvalueForE=0.0;
		 double forrealvalueForH=0.0;
		 final double m=8;
		 final int coilIndex=lookupSStoIndex(SecStructCode.COIL.getCode());
		 final int sheetIndex=lookupSStoIndex(SecStructCode.SHEET.getCode());
		 final int helixIndex= lookupSStoIndex(SecStructCode.HELIX.getCode());
		 final int[][][][][] model = trainmodel.getGor4();
		 final double[][][] frequStructDifference =frequencemodel.getFrequStructDiffGor4(); // LOG
		 Character currAA=null;
		 while ((currAA=it.next()) != null) {
			final int aaPos1 = lookupAAtoIndex(currAA);
			if (aaPos1 == -1) {
				continue;
			}
			final int win1pos = it.getCurrPosInWindow();

			for (int l = win1pos + 1; l < it.getWindowSize(); l++) {
				final int win2Pos = l;
				final int aaPos2 = lookupAAtoIndex(it.getCharAtSeq1(l));
				if (aaPos2 == -1) {
					continue;
				}
			 	valForC = model[coilIndex] [aaPos1][win1pos][aaPos2][win2Pos];
			 	valForE = model[sheetIndex] [aaPos1][win1pos][aaPos2][win2Pos];
			 	valForH = model[helixIndex] [aaPos1][win1pos][aaPos2][win2Pos];
			
			 	forrealvalueForC+=Math.log(valForC/(valForE+valForH))-((2*m-1)/(2*m+1)*frequStructDifference[coilIndex][aaPos1][win1pos]);
			 	forrealvalueForE+=Math.log(valForE/(valForC+valForH))-((2*m-1)/(2*m+1)*frequStructDifference[sheetIndex][aaPos1][win1pos]);
			 	forrealvalueForH+=Math.log(valForH/(valForE+valForC))-((2*m-1)/(2*m+1)*frequStructDifference[helixIndex][aaPos1][win1pos]);
			}
		 }
		 // calcprop
		 forrealvalueForC*=(2/(2*m+1));
		 forrealvalueForE*=(2/(2*m+1));
		 forrealvalueForH*=(2/(2*m+1));
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
			final int[][][][][] matrix = trainmodel.getGor4();
			writer.write("// Matrix5D\n\n");
			for(int aa1 =0;aa1<matrix[COIL].length;aa1++){
				for(int window1 =0;window1<matrix[COIL][aa1].length;window1++){
					writer.write("\n=");
					writer.write(reversAA[aa1]);
					writer.write(",");
					writer.write(String.valueOf(window1));
					writer.write(",");
					writer.write("C=\n\n");
					writeMatrix(matrix[COIL][aa1][window1], writer);
					writer.write("\n=");
					writer.write(reversAA[aa1]);
					writer.write(",");
					writer.write(String.valueOf(window1));
					writer.write(",");
					writer.write("E=\n\n");
					writeMatrix(matrix[SHEET][aa1][window1], writer);
					writer.write("\n=");
					writer.write(reversAA[aa1]);
					writer.write(",");
					writer.write(String.valueOf(window1));
					writer.write(",");
					writer.write("H=\n\n");
					writeMatrix(matrix[HELIX][aa1][window1], writer);
				}
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
		int[][][][][] gormatix= trainmodel.getGor4();
		try {
			reader = new BufferedReader(read);
			String line=null; 
			int currStructIndex=0;
			int currAA1Index=0;
			while((line=reader.readLine())!=null){
				if(line.startsWith("=")){
					final String[] head=line.split(",");
					final char secStruct=head[head.length-1].replace("=", "").charAt(0);
					currStructIndex=lookupSStoIndex(secStruct);
					final char aa1= head[0].replace("=", "").charAt(0);
					currAA1Index=lookupAAtoIndex(aa1);
					final int windowPos=Integer.parseInt(head[1]);
		
					gormatix[currStructIndex][currAA1Index][windowPos]=readMatrix(reader);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		model.setGor4(gormatix);
		return model;
	}
	
	
	

}
