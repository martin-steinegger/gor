package org.de.lmu.propra.domain;


public class Sequence {
	private long id;
	private String sequnece;
	private String sequenceType;
	private String sequenceHash;
	private String pdbId;
	private String chain;


	public Sequence() {
	}

	public Sequence(long id, String sequence,String sequenceType, String pdbId) {
		super();
		this.id = id;
		this.sequnece = sequence;
		this.sequenceType = sequenceType;
		this.pdbId = pdbId;
	}

	public long getId() {
		return id;
	}

	public String getSequence() {
		return sequnece;
	}



	public void setId(long id) {
		this.id = id;
	}

	public void setSequence(String sequnece) {
		this.sequnece = sequnece;
	}


	public String getSequenceType() {
		return sequenceType;
	}

	public void setSequenceType(String sequenceType) {
		this.sequenceType = sequenceType;
	}
	
	public String getSequenceHash() {
		return sequenceHash;
	}

	public void setSequenceHash(String sequenceHash) {
		this.sequenceHash = sequenceHash;
	}
	
	public String getPdbId() {
		return pdbId;
	}

	public void setPdbId(String pdbId) {
		this.pdbId = pdbId;
	}

	public String getChain() {
		return chain;
	}

	public void setChain(String chain) {
		this.chain = chain;
	}
	
	
}
