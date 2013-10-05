

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import org.biojava3.core.sequence.ProteinSequence;
import org.biojava3.core.sequence.compound.AminoAcidCompound;
import org.biojava3.core.sequence.compound.AminoAcidCompoundSet;
import org.biojava3.core.sequence.io.FastaReader;
import org.biojava3.core.sequence.io.FileProxyProteinSequenceCreator;
import org.biojava3.core.sequence.io.GenericFastaHeaderParser;
import org.de.lmu.propra.domain.PredictResult;
import org.de.lmu.propra.domain.Sequence;
import org.de.lmu.propra.gor.GorPredict;
import org.de.lmu.propra.gor.GorPredictImpl;
import org.de.lmu.propra.util.FastaALNReaderFunction;
import org.de.lmu.propra.util.GorOption;

public class GorPredictRunner {
	public static void main(String[] args) throws Exception {
		OptionParser parser = new OptionParser() {
			{

				accepts("seq", "path to dssp training file").withOptionalArg()
						.ofType(File.class);
				accepts("model", "path to fileoutput").withRequiredArg()
						.ofType(File.class);
				accepts("maf", "path to directory with .aln files (GOR V)")
						.withRequiredArg().ofType(File.class);
				accepts("format", "txt|html").withRequiredArg().ofType(
						String.class);
				accepts("probabilities",
						"include the probabilities in the output (0-9, coloring in html)");

			}
		};

		OptionSet options = parser.parse(args);

		if (options.has("model") == false || options.has("format") == false) {
			parser.printHelpOn(System.out);
			return;
		}
		final boolean probFlag = (options.has("probabilities")) ? true : false;
		File seq = null;
		if (options.has("seq")) {
			seq = (File) options.valueOf("seq");
		} else if (options.has("maf")) {
			seq = (File) options.valueOf("maf");
		} else {
			System.err.println("seq or maf is required");
			return;
		}
		final File model = (File) options.valueOf("model");
		final String format = (String) options.valueOf("format");
		final boolean isHtml;
		if (format.equals("html"))
			isHtml = true;
		else
			isHtml = false;

		GorPredict predict = new GorPredictImpl();
		GorOption currGor = predict.readModelToFile(new BufferedReader(
				new FileReader(model)));
		final List<PredictResult> listPredictResults = new ArrayList<PredictResult>();
		if (options.has("seq")) {
			File file = seq;
			FastaReader<ProteinSequence, AminoAcidCompound> fastaProxyReader = new FastaReader<ProteinSequence, AminoAcidCompound>(
					file,
					new GenericFastaHeaderParser<ProteinSequence, AminoAcidCompound>(),
					new FileProxyProteinSequenceCreator(file,
							AminoAcidCompoundSet.getAminoAcidCompoundSet()));
			LinkedHashMap<String, ProteinSequence> proteinProxySequences = fastaProxyReader
					.process();
			for (String key : proteinProxySequences.keySet()) {
				final List<org.de.lmu.propra.domain.Sequence> toPredict = new ArrayList<org.de.lmu.propra.domain.Sequence>();
				ProteinSequence proteinSequence = proteinProxySequences
						.get(key);
				toPredict.add(new Sequence(0, proteinSequence.toString(), "AA",
						key));
				listPredictResults.add(predict.predict(toPredict));
			}
		} else {
			List<org.de.lmu.propra.domain.Sequence> toPredict;
			predict.setMethod(GorOption.GOR5, currGor);
			for (File alnFile : seq.listFiles()) {
				toPredict=FastaALNReaderFunction.readALN(new BufferedReader(
						new FileReader(alnFile.getAbsolutePath())));
			
				final PredictResult pr=predict.predict(toPredict);
				if(pr!=null){
					pr.setPbdid(toPredict.get(0).getPdbId());
					listPredictResults.add(pr);
				}
			}
		}

		if (isHtml == false) {
			for (PredictResult pr : listPredictResults) {
				if(pr==null)
					continue;
				System.out.print("> ");
				System.out.println(pr.getPbdid());
				System.out.print("AS ");
				System.out.println(pr.getAaseq().toString());
				System.out.print("PS ");
				System.out.println(pr.getSsseq().toString());
				if (probFlag) {
					System.out.print("PH ");
					System.out.println(pr.getPhseq().toString());
					System.out.print("PE ");
					System.out.println(pr.getPeseq().toString());
					System.out.print("PC ");
					System.out.println(pr.getPcseq().toString());
				}
			}
		} else {
			System.out.println("<html>");
			System.out.println("<head></head>");
			System.out.println("<body>");
			for (PredictResult pr : listPredictResults) {
				if(pr==null)
					continue;
				System.out.print("> ");
				System.out.print(pr.getPbdid());
				System.out.print("<br/>");

				System.out.print("AS ");
				System.out.print(pr.getAaseq().toString());
				System.out.print("<br/>");

				System.out.print("PS ");
				System.out.println(pr.getSsseq().toString());
				System.out.print("<br/>");
				if (probFlag) {
					System.out.print("PH ");
					System.out.println(pr.getPhseq().toString());
					System.out.print("<br/>");
					System.out.print("PE ");
					System.out.println(pr.getPeseq().toString());
					System.out.print("<br/>");
					System.out.print("PC ");
					System.out.println(pr.getPcseq().toString());
					System.out.print("<br/>");
				}
			}
			System.out.println("</body>");
			System.out.println("</html>");
		}
	}
}