package org.de.lmu.propra.domain;

/**
 * an object of this class is the final result of the GorValidation interface,
 * and classes that implement it. It contains all data that are needed in order
 * to create the detailed output of GOR.
 * 
 * @author nikos
 * 
 */

// TODO: remember to fix teh summary problem!!!

public class GorValidationObj {
	private String id;
	private double Q3, SOV, QH, QC, QE, SOV_H, SOV_C, SOV_E;
	private String aminoAcids, prediction, reference;

	/**
	 * explicit constructor, with all parameters needed for output. It will
	 * rarely be called upon, may be deleted in the future.
	 * 
	 * @param id
	 *            the identifier of the examined sequence
	 * @param q3
	 *            the Q3 score
	 * @param sOV
	 *            the SOV (Segment OVerlap)
	 * @param qH
	 *            Q-score for helices
	 * @param qC
	 *            Q-score for coil
	 * @param qE
	 *            Q-score for sheets
	 * @param sOV_H
	 *            SOV score for helices
	 * @param sOV_C
	 *            SOV score for coil
	 * @param sOV_E
	 *            SOV score for sheets
	 * @param aminoAcids
	 *            a String; the aminoacid sequence of the protein
	 * @param prediction
	 *            the predicted secondary structure as a string
	 * @param reference
	 *            the reference secondary structure, usually from experiments
	 */
	public GorValidationObj(String id, double q3, double sOV, double qH,
			double qC, double qE, double sOV_H, double sOV_C, double sOV_E,
			String aminoAcids, String prediction, String reference) {
		this.id = id;
		Q3 = q3;
		SOV = sOV;
		QH = qH;
		QC = qC;
		QE = qE;
		SOV_H = sOV_H;
		SOV_C = sOV_C;
		SOV_E = sOV_E;
		this.aminoAcids = aminoAcids;
		this.prediction = prediction;
		this.reference = reference;
	}

	public GorValidationObj(String id2, String aminoAcids, String prediction,
			String reference) {
		super();
		this.id = id2;
		this.aminoAcids = aminoAcids;
		this.prediction = prediction;
		this.reference = reference;
	}

	public GorValidationObj() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getQ3() {
		return Q3;
	}

	public void setQ3(double q3) {
		Q3 = q3;
	}

	public double getSOV() {
		return SOV;
	}

	public void setSOV(double sOV) {
		SOV = sOV;
	}

	public double getQH() {
		return QH;
	}

	public void setQH(double qH) {
		QH = qH;
	}

	public double getQC() {
		return QC;
	}

	public void setQC(double qC) {
		QC = qC;
	}

	public double getQE() {
		return QE;
	}

	public void setQE(double qE) {
		QE = qE;
	}

	public double getSOV_H() {
		return SOV_H;
	}

	public void setSOV_H(double sOV_H) {
		SOV_H = sOV_H;
	}

	public double getSOV_C() {
		return SOV_C;
	}

	public void setSOV_C(double sOV_C) {
		SOV_C = sOV_C;
	}

	public double getSOV_E() {
		return SOV_E;
	}

	public void setSOV_E(double sOV_E) {
		SOV_E = sOV_E;
	}

	public String getAminoAcids() {
		return aminoAcids;
	}

	public void setAminoAcids(String aminoAcids) {
		this.aminoAcids = aminoAcids;
	}

	public String getPrediction() {
		return prediction;
	}

	public void setPrediction(String prediction) {
		this.prediction = prediction;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	@Override
	public String toString() {
		String result = ">" + id + " " + Q3 + " " + SOV + " " + QH + " "
				+ QE + " " + QC + " " + SOV_H + " " + SOV_E + " " + SOV_C
				+ "\n";
		result = result.concat("AS " + aminoAcids + "\n");
		result = result.concat("PS " + "--------" + prediction + "--------"
				+ "\n");
		result = result.concat("SS " + reference + "\n");

		return result+"\n";
	}

	public String toBriefString() {
		String result = ">" + id + " " + Q3 + " " + SOV + " " + QH + " "
				+ QE + " " + QC + " " + SOV_H + " " + SOV_E + " " + SOV_C
				+ "\n";
		return result;
	}
}
