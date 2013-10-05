package org.de.lmu.propra.gor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.de.lmu.propra.domain.PredictResult;
import org.de.lmu.propra.domain.Sequence;
import org.de.lmu.propra.domain.SequenceSequence;
import org.de.lmu.propra.domain.TrainModel;
import org.de.lmu.propra.util.GorOption;

public class GorPredictImpl implements GorPredict {

	private Gor1 gor1;
	private Gor3 gor3;
	private Gor4 gor4;
	private Gor5 gor5;
	private Gor gor;
	private GorOption gor5option;
	private GorOption currGorOption;
	final private GorPostProcessor processor = new GorPostProcessor();
	private boolean postProcessor = false;

	public GorPredictImpl() {
		gor1 = new Gor1();
		gor3 = new Gor3();
		gor4 = new Gor4();
		gor5 = new Gor5Impl();
	}
	
	@Override
	public void setMethod(GorOption methode, GorOption optionForGor5) {
		currGorOption=methode;
		switch (methode) {
		case GOR1:
			gor = gor1;
			gor5option = null;
			break;
		case GOR3:
			gor = gor3;
			gor5option = null;
			break;
		case GOR4:
			gor = gor4;
			gor5option = null;
			break;
		case GOR5:

			gor5option = optionForGor5;
			switch (optionForGor5) {
			case GOR1:
				gor = gor1;
				break;
			case GOR3:
				gor = gor3;
				break;
			case GOR4:
				gor = gor4;
				break;
			}
			break;
		}
	}

	@Override
	public TrainModel train(List<SequenceSequence> list) {
		TrainModel tm=gor1.train(list);
		tm.setGor3(gor3.train(list).getGor3());
		tm.setGor4(gor4.train(list).getGor4());
		return tm;
	}

	@Override
	public PredictResult predict(List<Sequence> seq) {
		if (seq != null && seq.size() != 0) {
			PredictResult pr;
			if (gor5option == null) {
				pr =  gor.predict(seq.get(0).getSequence());
				pr.setPbdid(seq.get(0).getPdbId());
			} else {
				pr = gor5.predict(gor, seq);
			}
			if(postProcessor==true){
				pr=processor.process(pr);
				pr=processor.process(pr);
			}
			return pr;
		} else {
			throw new RuntimeException("Prediction list is empty");
		}
	}

	public Gor getGor() {
		return gor;
	}

	public Gor1 getGor1() {
		return gor1;
	}

	public Gor3 getGor3() {
		return gor3;
	}

	public Gor4 getGor4() {
		return gor4;
	}

	public Gor5 getGor5() {
		return gor5;
	}

	public void setGor(Gor gor) {
		this.gor = gor;
	}

	public void setGor1(Gor1 gor1) {
		this.gor1 = gor1;
	}

	public void setGor3(Gor3 gor3) {
		this.gor3 = gor3;
	}

	public void setGor4(Gor4 gor4) {
		this.gor4 = gor4;
	}

	public void setGor5(Gor5 gor5) {
		this.gor5 = gor5;
	}

	@Override
	public void setModel(TrainModel model) {
		gor.setModel(model);
	}

	@Override
	public void writeModelToFile(String filename) {
		gor.writeModelToFile(filename);
	}

	public GorOption getCurrGorOption() {
		return currGorOption;
	}

	public void setCurrGorOption(GorOption currGorOption) {
		this.currGorOption = currGorOption;
	}

	@Override
	public GorOption readModelToFile(Reader read) {
		final BufferedReader reader = (BufferedReader) read;
		String line=null;
		try {
			while((line=reader.readLine())!=null){
				if(line.startsWith("//")){
					final String matrixValue=line.replace("// ", "").trim();
					if(matrixValue.equals("Matrix3D"))
						setMethod(GorOption.GOR1, GorOption.GOR1);
					else if(matrixValue.equals("Matrix4D"))
						setMethod(GorOption.GOR3, GorOption.GOR3);
					else if(matrixValue.equals("Matrix5D"))
						setMethod(GorOption.GOR4, GorOption.GOR4);
				}
				break;
			}
			gor.readModelToFile(reader);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return currGorOption;
	}

	@Override
	public void setPostProcessor(boolean on) {
		postProcessor=on;
	}

}
