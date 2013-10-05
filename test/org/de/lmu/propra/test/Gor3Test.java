package org.de.lmu.propra.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.de.lmu.propra.domain.PredictResult;
import org.de.lmu.propra.domain.SequenceSequence;
import org.de.lmu.propra.gor.AbstractGor;
import org.de.lmu.propra.gor.Gor3;
import org.de.lmu.propra.util.FastaAASSReaderFunction;
import org.junit.Test;

public class Gor3Test {
	final static String aatocheck="ELHPDKWTVQPIVLPEKDSWTVNDIQKLVGKLNWASQIYPGIKVR" +
			"QLCKLLRGTKALTEVIPLTEEAELELAENREILKEPVHGVYYDPSKDLIAEIQKQGQGQW" +
			"TYQIYQEPFKNLKTGKYARMRGAHTNDVKQLTEAVQKITTESIVIWGKTPKFKLPIQKET" +
			"WETWWTEYWQATWIPEWEFVNTPPLVKLWYQ";
	
//	final String aatocheck="AYIAKQRQISFVKSHFSRQLEERLGLIEVQAPILSRVGDGTQDNL" +
//			"SGAEKAVQVKVKALPDAQFEVVHSLAKWKXQTLGQHDFSAGEGLYTHMKALRPDEDRLSP" +
//			"LHSVYVDQWDWERVMGDGERQFSTLKSTVEAIWAGIKATEAAVSEEFGLAPFLPDQIHFV" +
//			"HSQELLSRYPDLDAKGRERAIAKDLGAVFLVGIGGKLSDGHRHDVRAPDYDDWSTPSELG" +
//			"HAGLNGDILVWNPVLEDAFELSSMGIRVDADTLKHQLALTGDEDRLELEWHQALLRGEMP" +
//			"QTIGGGIGQSRLTMLLLQLPHIGQVQAGVWPAAVRESVPSLL";
	final static  String org = "CECCCCCCCCCCCCCCCCCCEHHHHHHHHHHHHHHHHHCCCCCCHHHHH" +
			"CCCCCCCCCCECCCCHHHHHHHHHHHHHCCCCCCCCCCCCCCCCEEEEEEEECCEEEEEE" +
			"EEECCEEEEEEEEECCCCCCCCHHHHHHHHHHHHHHHHHHHHCCCCEEEECECHHHHHHH" +
			"HHHHCCCCCCCEEEECCCCCHHHHCCC";
	final static String predseque="--------CCECCCHCCCCCEHHHHEHECHCHEECHCCCCCCCHH" +
			"EHHHCHCHCCCHCHHCHCHHHHHHHHHHHHHHHHCCCCEECCCHCCHHHHHHHHHCCHCC" +
			"EEHCHHCCHCHHCHHCCHHHCHCCCHCHHHHHHHHHHCECHEEECCCCCHCCCCHCCCCH" +
			"ECHHEHECHCCCCCCCHCCCCCH--------";
	

	@Test
	public void testGor1VsReference() {
		final String aatocheck="AYIAKQRQISFVKSHFSRQLEERLGLIEVQAPILSRVGDGTQDNL" +
				"SGAEKAVQVKVKALPDAQFEVVHSLAKWKRQTLGQHDFSAGEGLYTHMKALRPDEDRLSP" +
				"LHSVYVDQWDWERVMGDGERQFSTLKSTVEAIWAGIKATEAAVSEEFGLAPFLPDQIHFV" +
				"HSQELLSRYPDLDAKGRERAIAKDLGAVFLVGIGGKLSDGHRHDVRAPDYDDWSTPSELG" +
				"HAGLNGDILVWNPVLEDAFELSSMGIRVDADTLKHQLALTGDEDRLELEWHQALLRGEMP" +
				"QTIGGGIGQSRLTMLLLQLPHIGQVQAGVWPAAVRESVPSLL";
		
//		final String aatocheck="AYIAKQRQISFVKSHFSRQLEERLGLIEVQAPILSRVGDGTQDNL" +
//				"SGAEKAVQVKVKALPDAQFEVVHSLAKWKXQTLGQHDFSAGEGLYTHMKALRPDEDRLSP" +
//				"LHSVYVDQWDWERVMGDGERQFSTLKSTVEAIWAGIKATEAAVSEEFGLAPFLPDQIHFV" +
//				"HSQELLSRYPDLDAKGRERAIAKDLGAVFLVGIGGKLSDGHRHDVRAPDYDDWSTPSELG" +
//				"HAGLNGDILVWNPVLEDAFELSSMGIRVDADTLKHQLALTGDEDRLELEWHQALLRGEMP" +
//				"QTIGGGIGQSRLTMLLLQLPHIGQVQAGVWPAAVRESVPSLL";
		final String org = "CHHHHHHHHHHHHHHHHHHHHHCCCEEECCCCCEEECCCCCCCCCCCCC" +
				"CCCCCCCCCCCCCCEEECCCCCCHHHHHHHHCCCCCCCEEEEEEEEECCCCCCCCCCCCC" +
				"EEEEEEEEEECCCCCCCHHHHHHHHHHHHHHHHHHHHHHHHHHCCCCCCCCCCEEEEHHH" +
				"HHHHCCCCCHHHHHHHHHHHHCEEEEECCCCCCCCCCCCCCCCCCCECCCCECCCCCECC" +
				"EEEEEEEECCCCEEEECEEEEEECCHHHHHHHHHHHCCHHHHHCHHHHHHHCCCCCCEEE" +
				"EEEEHHHHHHHHCCCCCHHHCCCCCCCHHHHCCCCCCC";
		final String predseque="--------HHECHHHHEHHHHHHHHHHHCCCHHEEECCCCCCHEC" +
				"HHCHHHHHEHEHCCHHCCEHHHHHHHHHCHHCCECCCCHCHCHHHHHHHHCCCCCHCCCH" +
				"CHEEECECCEHHHECCCCCCCHHHHHHEHHHHHCHCHHHCHHHHHHHCCCECCCCCEEEC" +
				"HHHHHHHCCHHCCHCCHHHHHHHHCHCEEEEECEHCCCCCEEEECECCCCCCCCCEHHCH" +
				"HCCCCCCEEECCHCCCHHHHHHHHHCECECHHHHHHHHCHHCHHHHHHHHHHHHHHHCCE" +
				"HECHHCCHCHHHHHHHHCCCCCCECEECCCEHEH--------";
		
		FileReader reader = null;
		try {
			reader = new FileReader(new File("data/GOR/CB513DSSP.db"));
		} catch (FileNotFoundException e) {
		}
		ArrayList<SequenceSequence> listseqseq = FastaAASSReaderFunction
				.readDSSP(reader);
		AbstractGor gor = new Gor3();
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
	public void testGor3VsReference() {

		
		FileReader reader = null;
		try {
			reader = new FileReader(new File("data/GOR/CB513DSSP.db"));
		} catch (FileNotFoundException e) {
		}
		ArrayList<SequenceSequence> listseqseq = FastaAASSReaderFunction
				.readDSSP(reader);
		
		AbstractGor gor = new Gor3();
		for(int i = 0;i < 20;i++){
			gor.train(listseqseq);
			PredictResult pr=gor.predict(aatocheck);
			System.out.println(org);
			System.out.println(pr.getSsseq().toString());
			System.out.println(predseque);
			System.out.println(pr.getPcseq().toString());
			System.out.println(pr.getPeseq().toString());
			System.out.println(pr.getPhseq().toString());
		}
//		TestCase.assertEquals(aatocheck,pr.getAaseq().toString());
//		TestCase.assertEquals(predseque,pr.getSsseq().toString());
		
	}
	
	@Test
	public void testGor3Writer() {

		FileReader reader = null;
		try {
			reader = new FileReader(new File("data/GOR/CB513DSSP.db"));
		} catch (FileNotFoundException e) {
		}
		ArrayList<SequenceSequence> listseqseq = FastaAASSReaderFunction
				.readDSSP(reader);
		AbstractGor gor = new Gor3();
		
		gor.train(listseqseq);
		gor.writeModelToFile("testoutput/gor3.txt");
		
	}
	
	
	@Test
	public void testGor3Read() {

		try {
		
		AbstractGor gor = new Gor3();
		gor.readModelToFile(new FileReader(new File(
				"testoutput/gor3.txt")));
		
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
