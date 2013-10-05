package org.de.lmu.propra.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.de.lmu.propra.domain.PredictResult;
import org.de.lmu.propra.domain.SequenceSequence;
import org.de.lmu.propra.gor.AbstractGor;
import org.de.lmu.propra.gor.Gor4;
import org.de.lmu.propra.util.FastaAASSReaderFunction;
import org.junit.Test;

public class Gor4Test {
	final static String aatocheck="ELHPDKWTVQPIVLPEKDSWTVNDIQKLVGKLNWASQIYPGIKVR" +
			"QLCKLLRGTKALTEVIPLTEEAELELAENREILKEPVHGVYYDPSKDLIAEIQKQGQGQW" +
			"TYQIYQEPFKNLKTGKYARMRGAHTNDVKQLTEAVQKITTESIVIWGKTPKFKLPIQKET" +
			"WETWWTEYWQATWIPEWEFVNTPPLVKLWYQ";
	final static String org = "CECCCCCCCCCCCCCCCCCCEHHHHHHHHHHHHHHHHHCCCCCCHHHHH" +
			"CCCCCCCCCCECCCCHHHHHHHHHHHHHCCCCCCCCCCCCCCCCEEEEEEEECCEEEEEE" +
			"EEECCEEEEEEEEECCCCCCCCHHHHHHHHHHHHHHHHHHHHCCCCEEEECECHHHHHHH" +
			"HHHHCCCCCCCEEEECCCCCHHHHCCC";
	final static String predseque="--------HEEECEEEEEHCEHEEHEEEEEECEEEHECCEECEHE" +
			"EHHHCHCHHCECEECCECCHHHHHHHHHHHHHHEEECEEEEEEEECEHHEEHEHEEEHEE" +
			"HEECEEEEHCECEHECEEEEEEEEHHEHEEHEHEHEECEHEEECCCHECEECEEEHEEEH" +
			"EEHECHEHEEEHCCEEEEHCEHH--------";
	
	@Test
	public void testGor1VsReference() {
	FileReader reader = null;
		try {
			reader = new FileReader(new File("data/GOR/CB513DSSP.db"));
		} catch (FileNotFoundException e) {
		}
		ArrayList<SequenceSequence> listseqseq = FastaAASSReaderFunction
				.readDSSP(reader);
		AbstractGor gor = new Gor4();
		gor.train(listseqseq);
		PredictResult pr=gor.predict(aatocheck);	
		System.out.println(org);
		System.out.println(pr.getSsseq().toString());
		System.out.println(predseque);
		System.out.println(pr.getPcseq().toString());
		System.out.println(pr.getPeseq().toString());
		System.out.println(pr.getPhseq().toString());

		
		TestCase.assertEquals(aatocheck,pr.getAaseq().toString());
		TestCase.assertEquals(predseque,pr.getSsseq().toString());
		
	}
	
	

	@Test
	public void testGor4Writer() {

		FileReader reader = null;
		try {
			reader = new FileReader(new File("data/GOR/CB513DSSP.db"));
		} catch (FileNotFoundException e) {
		}
		ArrayList<SequenceSequence> listseqseq = FastaAASSReaderFunction
				.readDSSP(reader);
		AbstractGor gor = new Gor4();
		gor.train(listseqseq);
		gor.writeModelToFile("testoutput/gor4.txt");
		
	}
	
	

	
	@Test
	public void testGor4Read() {

		try {
		
		AbstractGor gor = new Gor4();
		gor.readModelToFile(new FileReader(new File(
				"testoutput/gor4.txt")));
		
		PredictResult pr=gor.predict(aatocheck);
		System.out.println(org);
		System.out.println(pr.getSsseq().toString());
		System.out.println(predseque);
		System.out.println(pr.getPcseq().toString());
		System.out.println(pr.getPeseq().toString());
		System.out.println(pr.getPhseq().toString());
		
		TestCase.assertEquals(aatocheck,pr.getAaseq().toString());
		TestCase.assertEquals(predseque,pr.getSsseq().toString());
		} catch (FileNotFoundException e) {
		}
	}
}
