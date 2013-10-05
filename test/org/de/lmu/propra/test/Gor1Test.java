package org.de.lmu.propra.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.de.lmu.propra.domain.PredictResult;
import org.de.lmu.propra.domain.SequenceSequence;
import org.de.lmu.propra.gor.AbstractGor;
import org.de.lmu.propra.gor.Gor1;
import org.de.lmu.propra.util.FastaAASSReaderFunction;
import org.junit.Test;

public class Gor1Test {
	final static String aatocheck="ELHPDKWTVQPIVLPEKDSWTVNDIQKLVGKLNWASQIYPGIKVR" +
			"QLCKLLRGTKALTEVIPLTEEAELELAENREILKEPVHGVYYDPSKDLIAEIQKQGQGQW" +
			"TYQIYQEPFKNLKTGKYARMRGAHTNDVKQLTEAVQKITTESIVIWGKTPKFKLPIQKET" +
			"WETWWTEYWQATWIPEWEFVNTPPLVKLWYQ";
	final static String org = "CECCCCCCCCCCCCCCCCCCEHHHHHHHHHHHHHHHHHCCCCCCHHHHH" +
			"CCCCCCCCCCECCCCHHHHHHHHHHHHHCCCCCCCCCCCCCCCCEEEEEEEECCEEEEEE" +
			"EEECCEEEEEEEEECCCCCCCCHHHHHHHHHHHHHHHHHHHHCCCCEEEECECHHHHHHH" +
			"HHHHCCCCCCCEEEECCCCCHHHHCCC";
	final static String predseque="--------CCEEECCCCCCCEEHHHHHHHCCCCCHCCCCCCCHHH" +
			"HHHHHHCCCCCHHHECHHHHHHHHHHHHHHHHHCCCCCCEECCCCCHHHHHHHHHCCCCE" +
			"EHHHHHCCHHHHHCCHHHHHHCCCCCCHHHHHHHHHHCCCCEEEECCCCCCCCCCCCCCC" +
			"CCHCEHHHCHCCCCCCHCCCCCC--------";

	@Test
	public void testGor1VsReference() {

		
		FileReader reader = null;
		try {
			reader = new FileReader(new File("data/GOR/CB513DSSP.db"));
		} catch (FileNotFoundException e) {
		}
		ArrayList<SequenceSequence> listseqseq = FastaAASSReaderFunction
				.readDSSP(reader);
		AbstractGor gor = new Gor1();
		gor.train(listseqseq);
		PredictResult pr=gor.predict(aatocheck);
	//	GorPostProcessor processor = new GorPostProcessor();
	//	pr=processor.process(pr);
	//	processor = new GorPostProcessor();
		//pr=processor.process(pr);
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
	public void testGor1Writer() {

		FileReader reader = null;
		try {
			reader = new FileReader(new File("data/GOR/CB513DSSP.db"));
		} catch (FileNotFoundException e) {
		}
		ArrayList<SequenceSequence> listseqseq = FastaAASSReaderFunction
				.readDSSP(reader);
		AbstractGor gor = new Gor1();
		gor.train(listseqseq);
		gor.writeModelToFile("testoutput/gor1.txt");
		
	}
	
	@Test
	public void testGor1Read() {

		FileReader reader = null;
		try {
			reader = new FileReader(new File("data/GOR/CB513DSSP.db"));
		
		ArrayList<SequenceSequence> listseqseq = FastaAASSReaderFunction
				.readDSSP(reader);
		AbstractGor gor = new Gor1();
		gor.train(listseqseq);
		gor.readModelToFile(new FileReader(new File(
				"testoutput/gor1.txt")));
		System.out.println("");
		
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
	
	
	
	@Test
	public void testGorRead() {

		FileReader reader = null;
		try {
			reader = new FileReader(new File("testoutput/trainset.db"));
		
		ArrayList<SequenceSequence> listseqseq = FastaAASSReaderFunction
				.readDSSP(reader);
		AbstractGor gor = new Gor1();
		gor.train(listseqseq);

		gor.writeModelToFile("testoutput/gor1.txt");
		} catch (FileNotFoundException e) {
		}
	}
}
