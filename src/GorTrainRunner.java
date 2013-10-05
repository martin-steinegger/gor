
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import org.de.lmu.propra.domain.SequenceSequence;
import org.de.lmu.propra.gor.GorPredict;
import org.de.lmu.propra.gor.GorPredictImpl;
import org.de.lmu.propra.util.FastaAASSReaderFunction;
import org.de.lmu.propra.util.GorOption;

public class GorTrainRunner {
	public static void main(String[] args) throws Exception {
		OptionParser parser = new OptionParser() {
			{

				accepts("db", "path to dssp training file").withRequiredArg()
						.ofType(File.class);
				accepts("model", "path to fileoutput").withRequiredArg()
						.ofType(File.class);
				accepts("method", "<gor1|gor3|gor4>").withRequiredArg().ofType(
						String.class);
			}
		};

		OptionSet options = parser.parse(args);

		if (options.has("db") == false || options.has("model") == false
				|| options.has("method") == false) {
			parser.printHelpOn(System.out);
			return;
		}
		final File db = (File) options.valueOf("db");
		final File model = (File) options.valueOf("model");
		final String method = (String) options.valueOf("method");
		List<SequenceSequence> toTrainList = FastaAASSReaderFunction
				.readDSSP(new BufferedReader(new FileReader(db)));
		GorPredict predict = new GorPredictImpl();
		if (method.equals("gor1")) {
			predict.setMethod(GorOption.GOR1, GorOption.GOR1);
		} else if (method.equals("gor3")) {
			predict.setMethod(GorOption.GOR3, GorOption.GOR3);
		} else if (method.equals("gor4")) {
			predict.setMethod(GorOption.GOR4, GorOption.GOR4);
		} else {
			System.err.println("Wrong methode option");
			return;
		}
		predict.train(toTrainList);

		predict.writeModelToFile(model.getAbsolutePath());

	}
}