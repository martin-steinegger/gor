package org.de.lmu.propra.gor;

import org.de.lmu.propra.domain.TrainModel;

public class FrequenceModel {
	final private double[] frequenceOverAllSecStruct = new double[TrainModel.SECSTRUCTS];
	final private double[][] frequenceOverAllGor3 = new double[TrainModel.SECSTRUCTS][TrainModel.AMINOACID];
	final private double[][][] frequenceOverAllGor4 = new double[TrainModel.SECSTRUCTS][TrainModel.AMINOACID][TrainModel.WINDOWSIZE];

	final private double[] frequStructDifferenceGor1 = new double[TrainModel.SECSTRUCTS]; // log(f_nS/f_S)
	final private double[][] frequStructDifferenceGor3 = new double[TrainModel.SECSTRUCTS][TrainModel.AMINOACID]; //log(F_nSj_aj / F_sj_aj)
	final private double[][][] frequStructDifferenceGor4 = new double[TrainModel.SECSTRUCTS][TrainModel.AMINOACID][TrainModel.WINDOWSIZE];; //log(F_nSj_aj+k / F_sj_aj+k)
	/**
	 * Get frequenceOverAllMatrix (TrainingModel)<br/>
	 * [SECSTRUCTS][AMINOACID]<br/>
	 * 
	 * @return int[][][][] [SECSTRUCTS][MIDDELAA][AMINOACID][WINDOWSIZE]
	 */
	public double[][] getFrequenceGor3() {
		return frequenceOverAllGor3;
	}

	public double[] getFrequenceOverAllSecStruct() {
		return frequenceOverAllSecStruct;
	}

	public double[] getFrequStructDiffGor1() {
		return frequStructDifferenceGor1;
	}

	public double[][] getFrequStructDiffGor3() {
		return frequStructDifferenceGor3;
	}
	
	public double[][][] getFrequStructDiffGor4() {
		return frequStructDifferenceGor4;
	}

	public double[][][] getFrequenceGor4() {
		return frequenceOverAllGor4;
	}
}
