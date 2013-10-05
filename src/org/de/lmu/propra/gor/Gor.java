package org.de.lmu.propra.gor;

import java.io.Reader;
import java.util.List;

import org.de.lmu.propra.domain.PredictResult;
import org.de.lmu.propra.domain.SequenceSequence;
import org.de.lmu.propra.domain.TrainModel;

public interface Gor {
	public void setModel(TrainModel model);
	public TrainModel train(List<SequenceSequence> list);
	public PredictResult predict(String seqseq);
	public void writeModelToFile(final String filename);
	public void readModelToFile(Reader read);
}
