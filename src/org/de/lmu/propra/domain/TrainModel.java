package org.de.lmu.propra.domain;

import java.util.Arrays;

public class TrainModel {
	final public static int WINDOWSIZE = 17;
	final public static int AMINOACID = 20;
	final public static int SECSTRUCTS = 3;

	private int[][][] gor1;
	private int[][][][] gor3;
	private int[][][][][] gor4;

	public TrainModel() {
		gor1 = new int[SECSTRUCTS][AMINOACID][WINDOWSIZE];
		for (int[][] matrix : gor1)
			for (int[] row : matrix)
				Arrays.fill(row, 1);

		gor3 = new int[SECSTRUCTS][AMINOACID][AMINOACID][WINDOWSIZE];
		for (int[][][] d3matrix : gor3)
			for (int[][] matrix : d3matrix)
				for (int[] row : matrix)
					Arrays.fill(row, 1);

		gor4 = new int[SECSTRUCTS][AMINOACID][WINDOWSIZE][AMINOACID][WINDOWSIZE];
		for (int[][][][] d4matrix : gor4)
			for (int[][][] d3matrix : d4matrix)
				for (int[][] matrix : d3matrix)
					for (int[] row : matrix)
						Arrays.fill(row, 1);
	}

	// int[][][] gor2;

	public int[][][] getGor1() {
		return gor1;
	}

	/**
	 * Get Gor3 Matrix (TrainingModel)<br/>
	 * [SECSTRUCTS][MIDDELAA][AMINOACID][WINDOWSIZE]<br/>
	 * 
	 * @return int[][][][] [SECSTRUCTS][MIDDELAA][AMINOACID][WINDOWSIZE]
	 */
	public int[][][][] getGor3() {
		return gor3;
	}
	/**
	 * Get Gor4 Matrix (TrainingModel)<br/>
	 * @return int[][][][][] [SECSTRUCTS][AMINOACIDTTO1POS][AMINOACIDTTO2POS][1WPOS][2WPOS]
	 */
	public int[][][][][] getGor4() {
		return gor4;
	}

	public void setGor1(int[][][] gor1) {
		this.gor1 = gor1;
	}

	public void setGor3(int[][][][] gor3) {
		this.gor3 = gor3;
	}

	public void setGor4(int[][][][][] gor4) {
		this.gor4 = gor4;
	}


}
