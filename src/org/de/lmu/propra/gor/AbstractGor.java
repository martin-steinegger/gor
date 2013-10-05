package org.de.lmu.propra.gor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

import org.de.lmu.propra.domain.PredictResult;
import org.de.lmu.propra.domain.SequenceSequence;
import org.de.lmu.propra.domain.TrainModel;
import org.de.lmu.propra.util.SecStructCode;

abstract public class AbstractGor implements Gor {
	final static protected int midPos = 8;
	final public static int COIL = 0;
	final public static int SHEET = 1;
	final public static int HELIX = 2;
	protected FrequenceModel frequencemodel;
	protected TrainModel trainmodel;
	protected PredictResult predictresult;

	// A C D E F G H I K L M
	final private static int[] lookupAA = { 0, -1, 1, 2, 3, 4, 5, 6, 7, -1, 8,
			9, 10,
			// N P Q R S T V W Y
			11, -1, 12, 13, 14, 15, 16, -1, 17, 18, -1, 19, -1 };
	// A C D E F G H I K L M
	// N P Q R S T V W Y
	final protected static int[] reversAA = { 'A', 'C', 'D', 'E', 'F', 'G', 'H', 
											'I', 'K', 'L', 'M', 'N', 'P', 'Q',
											'R', 'S', 'T', 'V', 'W', 'Y' };

	final private static int[] lookupSS = { 0, -1, 1, -1, -1, 2 };

	public AbstractGor() {
		trainmodel = new TrainModel();
		frequencemodel = new FrequenceModel();
	}

	public TrainModel train(List<SequenceSequence> list) {
		for (SequenceSequence seqseq : list) {

			final String aaseq = seqseq.getSequence1().getSequence();

			final String ssseq = seqseq.getSequence2().getSequence();
			if(aaseq.length()<17){
				continue;
			}
			final WindowIterator windowIterator = new WindowIterator(aaseq,
					ssseq);
			InnerWindowIterator innerWindowIterator = null;
			while ((innerWindowIterator = windowIterator.next()) != null) {
				processWindowTrain(innerWindowIterator);
			}
		}
		calcOverAll();
		return trainmodel;
	}

	public PredictResult predict(String seqseq) {
		predictresult = new PredictResult();
		final String aaseq = seqseq;
		
		if(aaseq.length()<17){
			return null;
		}
		final WindowIterator windowIterator = new WindowIterator(aaseq, aaseq);
	
		predictresult.appendAASeq(aaseq);
		InnerWindowIterator innerWindowIterator = null;
		predictresult.appendSSSeq("--------");
		predictresult.appendPHSeq("--------");
		predictresult.appendPCSeq("--------");
		predictresult.appendPESeq("--------");
		while ((innerWindowIterator = windowIterator.next()) != null) {
			double[] valueForSecStruct = processWindowPredict(innerWindowIterator);
			final double probForC;
			final double probForE;
			final double probForH;
			if (valueForSecStruct[0] != Double.MIN_NORMAL&&
				valueForSecStruct[1] !=	Double.MIN_NORMAL&&
				valueForSecStruct[2] !=	Double.MIN_NORMAL) 
			{
				probForC = calcProbability(SecStructCode.COIL,
						valueForSecStruct);
				probForE = calcProbability(SecStructCode.SHEET,
						valueForSecStruct);
				probForH = calcProbability(SecStructCode.HELIX,
						valueForSecStruct);
			} else {
				probForC = Double.MIN_NORMAL;
				probForE = Double.MIN_NORMAL;
				probForH = Double.MIN_NORMAL;
			}
			addSecResult(probForC, probForE, probForH);
			addProbability(probForC, probForE, probForH);
		}
		predictresult.appendSSSeq("--------");
		predictresult.appendPHSeq("--------");
		predictresult.appendPCSeq("--------");
		predictresult.appendPESeq("--------");
		return predictresult;
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
		}else if (valueForC==valueForE&&valueForE==valueForH&&valueForH!=0){
			predictresult.appendSSSeq(SecStructCode.COIL.getCode());
		}else
			throw new RuntimeException();
	}

	abstract protected void calcOverAll();

	abstract protected double calcProbability(SecStructCode ss,
			double[] proabArray);

	abstract protected double[] processWindowPredict(
			final InnerWindowIterator it);

	abstract protected void processWindowTrain(final InnerWindowIterator it);

	abstract protected TrainModel readModelToFileImpl(Reader read);
	
	public TrainModel getTrainModel() {
		return trainmodel;
	}

	public PredictResult getPredictModel() {
		return predictresult;
	}

	public void setModel(TrainModel model) {
		frequencemodel = new FrequenceModel();
		this.trainmodel = model;
		calcOverAll();
	}
	
	public void readModelToFile(Reader read){
		setModel(readModelToFileImpl(read));
	}

	public static int lookupAAtoIndex(char aa) {
		return lookupAA[aa - 65];
	}

	public static int lookupSStoIndex(char ss) {
		return lookupSS[ss - 67];
	}
	
	protected void writeMatrix(final int[][] matrixToWrite, Writer writer){
		int[] row;
		try {
			for (int i = 0; i < matrixToWrite.length; i++) {
				row = matrixToWrite[i];
				writer.write(reversAA[i]);
				writer.write('\t');	
				for (int j = 0; j < row.length; j++) {
					writer.write(String.valueOf(matrixToWrite[i][j]));
					writer.write('\t');	
				}
				writer.write('\n');
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected int[][] readMatrix(Reader reader){
		final int[][] retMatrix= new int[TrainModel.AMINOACID][TrainModel.WINDOWSIZE];
		BufferedReader read=(BufferedReader)reader;
		try {
			String ln;
			while((ln = read.readLine() )!=null)
				if(ln.startsWith("A")){
					break;
				}
			for(int lineIt=0; lineIt < TrainModel.AMINOACID;lineIt++){
				String[] lineArray=ln.split("\t");
				String[] numbersString=Arrays.copyOfRange(lineArray, 1, 
													lineArray.length);
				int[] row = new int[numbersString.length];
				for (int i = 0; i < numbersString.length; i++) {
					row[i] = Integer.parseInt(numbersString[i]);
				}
				retMatrix[lineIt]=row;
				ln=read.readLine();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return retMatrix;
	}

}
