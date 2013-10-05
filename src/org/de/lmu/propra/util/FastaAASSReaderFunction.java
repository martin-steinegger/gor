package org.de.lmu.propra.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import org.de.lmu.propra.domain.Sequence;
import org.de.lmu.propra.domain.SequenceSequence;

public class FastaAASSReaderFunction {	
	private static String preFixForSS="SS";

	public static ArrayList<SequenceSequence> readDSSP(final Reader reader) {
		BufferedReader br;
		ArrayList<SequenceSequence> retArray = new ArrayList<SequenceSequence>();
		try {
			br = new BufferedReader(reader);
			String line = null;
			Sequence seq1 = null;
			Sequence seq2 = null;
			String pbdId="";
			while ((line = br.readLine()) != null) {
				if (line.startsWith(">") == true) {
					if (seq1 != null && seq2 != null) {
						retArray.add(new SequenceSequence(seq1, seq2, 0));
						seq1 = null;
						seq2 = null;
					}
					final String[] headSplit = line.split(" ");

					pbdId = (headSplit.length > 1) ? headSplit[1]
							: "";

				} else if (line.startsWith("AS") == true) {
					final String[] aaLine = line.split("\\s+");
					final String aaseq = aaLine[1];
					seq1 = new Sequence(0, aaseq,
							SequenceType.AA.getSequenceType(), pbdId);

				} else if (line.startsWith(preFixForSS) == true) {
					final String[] ssLine = line.split("\\s+");
					final String ssseq = ssLine[1];
					
					seq2 = new Sequence(0, ssseq,
							SequenceType.ALIGNMENT_SECSTRUCT.getSequenceType(), pbdId);
				}
			}
			retArray.add(new SequenceSequence(seq1, seq2, 0));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return retArray;
	}

	public static void setPreFixForSS(String preFixForSS) {
		FastaAASSReaderFunction.preFixForSS = preFixForSS;
	}
	
}
