package org.de.lmu.propra.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.de.lmu.propra.domain.Sequence;
import org.de.lmu.propra.domain.SequenceSequence;

public class DsspFunction {
	private String pathToDsspBin = "/Users/aluucard/Documents/workspace/propra_backend/tools/dsspcmbi";
	private String pathToDir;
	private String pathToDsspFile;
	public DsspFunction() {
		this("/Users/aluucard/Documents/workspace/propra_backend/tools/dsspcmbi");
	}

	public DsspFunction(final String pathToDsspBin){
		this(pathToDsspBin	,"data/DSSP");
	}
	
	public DsspFunction(final String pathToDsspBin,final String pathToDir) {
		this.pathToDsspBin = pathToDsspBin;
		this.pathToDir=pathToDir;
	}
	
	
	
	public ArrayList<SequenceSequence> getByPdbid(final String pdbid){
		final String middle = pdbid.substring(1,3).toLowerCase();
		return readDSSP(pathToDir +"/" +  middle + "/"+pdbid+".dssp");
	}

	public ArrayList<SequenceSequence> readDSSP(final String dsspFile) {
		ArrayList<SequenceSequence> retArray = new ArrayList<SequenceSequence>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(dsspFile));
			String line;
			StringBuilder aaseq = new StringBuilder();
			StringBuilder secseq = new StringBuilder();
			boolean modelStart = false;
			String beforeChain = "A"; // Anfang von chain
			while ((line = br.readLine()) != null) {
				if (line.startsWith("  #")) {
					modelStart = true;
					continue; // step over header
				}
				if (modelStart == false)
					continue;
				else {
					final String chain = line.substring(11, 12);
					if (beforeChain.equals(chain)||beforeChain.equals(" ")) {
						char aa = line.substring(13, 14).charAt(0);
						//http://swift.cmbi.ru.nl/gv/dssp/HTML/descrip.html <- lower case
						if (Character.isLowerCase(aa))
							aaseq.append("C");
						else if (aa =='!')
							continue;
						else
							aaseq.append(aa);

						char sstruct = line.substring(16, 17).charAt(0);
						if (sstruct == ' ')
							sstruct = SecStructCode.parseCode("TURN").getCode();
						else
							sstruct = SecStructCode.parseCode("" + sstruct)
									.getCode();
						secseq.append(sstruct);
					} else {
						SequenceSequence seqseq = new SequenceSequence();
						seqseq.setSequence1(new Sequence(0,
								aaseq.toString().trim(), SequenceType.AA
										.getSequenceType(), ""));
						seqseq.setSequence2(new Sequence(0,
								secseq.toString().trim(),
								SequenceType.ALIGNMENT_SECSTRUCT
										.getSequenceType(), ""));
						retArray.add(seqseq);
						aaseq = new StringBuilder();
						secseq = new StringBuilder();
						
					}
					// dostuff
					beforeChain = chain;
				}
			}
			SequenceSequence seqseq = new SequenceSequence();
			seqseq.setSequence1(new Sequence(0, aaseq.toString(),
					 SequenceType.AA.getSequenceType(),	""));
			seqseq.setSequence2(new Sequence(0, secseq.toString(),
				SequenceType.ALIGNMENT_SECSTRUCT
					.getSequenceType(), ""));
			retArray.add(seqseq);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return retArray;
	}

	public void convertPDBToDSSP(String pdbfile, String dsspfile) {
		ProcessBuilder builder = new ProcessBuilder(pathToDsspBin, pdbfile,
				dsspfile);
		this.pathToDsspFile=dsspfile;
		builder.redirectErrorStream(true);
		Process curProcess;
		try {
			curProcess = builder.start();
			curProcess.waitFor();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

	}

	public String getPathToDsspFile() {
		return pathToDsspFile;
	}
	
	
}
