package org.de.lmu.propra.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.de.lmu.propra.domain.Sequence;
import org.de.lmu.propra.domain.SequenceSequence;

public class FastaReaderFunction {

	public static ArrayList<SequenceSequence> readDSSP(final String path) {
		BufferedReader br;
		ArrayList<SequenceSequence> retArray = new ArrayList<SequenceSequence>();
		try {
			br = new BufferedReader(new FileReader(path));
			String line = null;
			Sequence seq1 = null;
			Sequence seq2 = null;
			String pdbId = "";
			while ((line = br.readLine()) != null) {
				if (line.startsWith(">") == true) {
					if (seq1 != null && seq2 != null) {
						retArray.add(new SequenceSequence(seq1, seq2, 0));
						seq1 = null;
						seq2 = null;
					}
					final String[] headSplit = line.split(" ");
					pdbId = (headSplit.length > 1) ? headSplit[1] : "";

				} else if (line.startsWith("AS") == true) {
					final String[] aaLine = line.split(" ");
					final String aaseq = aaLine[1];
					seq1 = new Sequence(0, aaseq,
							SequenceType.AA.getSequenceType(), pdbId);
					// "DSSP");

				} else if (line.startsWith("SS") == true) {
					final String[] ssLine = line.split(" ");
					final String ssseq = ssLine[1];
					seq2 = new Sequence(0, ssseq,
							SequenceType.ALIGNMENT_SECSTRUCT.getSequenceType(), pdbId);
					// "DSSP");
				}
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return retArray;
	}
}
