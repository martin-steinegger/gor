package org.de.lmu.propra.gor;

import java.util.List;

import org.de.lmu.propra.domain.PredictResult;
import org.de.lmu.propra.domain.Sequence;

public interface Gor5 {
	public PredictResult predict(Gor gor,List<Sequence> list);
}
