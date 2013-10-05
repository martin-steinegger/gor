package org.de.lmu.propra.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import org.de.lmu.propra.domain.Sequence;

public class FastaALNReaderFunction {	
	public static ArrayList<Sequence> readALN(final Reader reader) {
		BufferedReader br;
		final ArrayList<Sequence> retArray = new ArrayList<Sequence>();
		try {
			br = new BufferedReader(reader);
			String line = null;
			String pbdId="";
			while ((line = br.readLine()) != null) {
				if (line.startsWith(">") == true) {
					final String[] headSplit = line.split(" ");

					pbdId = (headSplit.length > 1) ? headSplit[1]
							: "";

				} else if (line.startsWith("AS") == true) {
					final String[] aaLine = line.split("\\s+");
					final String aaseq = aaLine[1];
					final Sequence seq1 = new Sequence(0, aaseq,
							SequenceType.AA.getSequenceType(), pbdId);
					retArray.add(seq1);
				} else if (Character.isDigit(line.charAt(0)) == true) {
					final String[] ssLine = line.split("\\s+");
					final String ssseq = ssLine[1];
					final Sequence seq2 = new Sequence(0, ssseq,
							SequenceType.ALIGNMENT_SECSTRUCT.getSequenceType(), pbdId);
					retArray.add(seq2);
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
