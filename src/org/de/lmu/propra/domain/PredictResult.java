package org.de.lmu.propra.domain;

import java.util.ArrayList;
import java.util.List;

public class PredictResult {
	final private StringBuilder aaseq = new StringBuilder();
	final private StringBuilder ssseq = new StringBuilder();
	final private StringBuilder pcseq = new StringBuilder();
	final private StringBuilder peseq = new StringBuilder();
	final private StringBuilder phseq = new StringBuilder();
	private StringBuilder reference = new StringBuilder();
	private String pbdid; 
	final private List<Double> pcvalue = new ArrayList<Double>();
	final private List<Double> pevalue = new ArrayList<Double>();
	final private List<Double> phvalue = new ArrayList<Double>();

	public List<Double> getPcvalue() {
		return pcvalue;
	}

	public List<Double> getPevalue() {
		return pevalue;
	}

	public List<Double> getPhvalue() {
		return phvalue;
	}

	// int[][][] gor2;
	public StringBuilder getAaseq() {
		return aaseq;
	}

	public StringBuilder getSsseq() {
		return ssseq;
	}

	public StringBuilder getPcseq() {
		return pcseq;
	}

	public StringBuilder getPeseq() {
		return peseq;
	}

	public StringBuilder getPhseq() {
		return phseq;
	}

	public void appendAASeq(Character aa) {
		aaseq.append(aa);
	}

	public void appendSSSeq(Character ss) {
		ssseq.append(ss);
	}

	public void appendSSSeq(String ssstring) {
		ssseq.append(ssstring);
	}

	public void appendPCSeq(int probForC) {
		pcseq.append(probForC);
	}

	public void appendPESeq(int probability) {
		peseq.append(probability);
	}

	public void appendPHSeq(int probability) {
		phseq.append(probability);
	}

	public void appendAASeq(String aas) {
		aaseq.append(aas);
	}

	public void appendPHSeq(String string) {
		phseq.append(string);
	}

	public void appendPCSeq(String string) {
		pcseq.append(string);
	}

	public void appendPESeq(String string) {
		peseq.append(string);
	}

	public void addPEValue(Double value) {
		pevalue.add(value);
	}

	public void addPCValue(Double value) {
		pcvalue.add(value);
	}

	public void addPHValue(Double value) {
		phvalue.add(value);
	}
	public Double getPEValue(int index){
		return pevalue.get(index);
	}
	
	public Double getPHValue(int index){
		return phvalue.get(index);
	}
	
	public Double getPCValue(int index){
		return pcvalue.get(index);
	}

	public StringBuilder getReference() {
		return reference;
	}

	public void setReference(StringBuilder reference) {
		this.reference = reference;
	}

	public String getPbdid() {
		return pbdid;
	}

	public void setPbdid(String pbdid) {
		this.pbdid = pbdid;
	}
	
}
