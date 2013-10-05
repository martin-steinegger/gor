package org.de.lmu.propra.gor;

import java.io.Reader;
import java.util.List;

import org.de.lmu.propra.domain.PredictResult;
import org.de.lmu.propra.domain.Sequence;
import org.de.lmu.propra.domain.SequenceSequence;
import org.de.lmu.propra.domain.TrainModel;
import org.de.lmu.propra.util.GorOption;

public interface GorPredict {
	public void setModel(TrainModel model);
	public void setMethod(GorOption methode,GorOption optionForGor5);
	public TrainModel train(List<SequenceSequence> list);
	public PredictResult predict(List<Sequence> list);
	public void writeModelToFile(final String filename);
	public GorOption readModelToFile(Reader read);
	public void setPostProcessor(boolean on);
}
