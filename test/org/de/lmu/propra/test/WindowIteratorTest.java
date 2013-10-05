package org.de.lmu.propra.test;

import junit.framework.TestCase;

import org.de.lmu.propra.gor.InnerWindowIterator;
import org.de.lmu.propra.gor.WindowIterator;
import org.junit.Test;

public class WindowIteratorTest {
	@Test
	public void testIteratorFitsOneTimes() {
		WindowIterator winit = new WindowIterator("DWVIPPINLPENSRGPL",
				"DWVIPPINLPENSRGPL");
		InnerWindowIterator windowit = null;

		Character currChar = null;
		int counter = 0;

		while ((windowit = winit.next()) != null) {
			StringBuilder window = new StringBuilder();
			int checkCurrPos = 0;
			TestCase.assertEquals('L', windowit.getMiddleCharSeq1());
			while ((currChar = windowit.next()) != null) {
				window.append(currChar);

				TestCase.assertEquals(checkCurrPos,
						windowit.getCurrPosInWindow());
				checkCurrPos++;
			}
			System.out.println(window.toString());
			TestCase.assertEquals("DWVIPPINLPENSRGPL", window.toString());
			TestCase.assertEquals("DWVIPPINLPENSRGPL", winit.getSeq1Window());
			counter++;
		}
		TestCase.assertEquals(1, counter);
	}

	@Test
	public void testIteratorFitsTwoTimes() {
		final String[] check = { "DWVIPPINLPENSRGPL", "WVIPPINLPENSRGPLP" };
		WindowIterator winit = new WindowIterator("DWVIPPINLPENSRGPLP",
				"DWVIPPINLPENSRGPLP");
		InnerWindowIterator windowit = null;
		Character currChar = null;
		int counter = 0;

		while ((windowit = winit.next()) != null) {

			if (counter == 0) {
				TestCase.assertEquals('L', windowit.getMiddleCharSeq1());
			}
			if (counter == 1)
				TestCase.assertEquals('P', windowit.getMiddleCharSeq1());

			StringBuilder window = new StringBuilder();
			while ((currChar = windowit.next()) != null) {
				window.append(currChar);
			}
			TestCase.assertEquals(check[counter], window.toString());
			TestCase.assertEquals(check[counter], winit.getSeq1Window());
			counter++;
		}
		TestCase.assertEquals(2, counter);
	}

	@Test
	public void testIteratorFitsThreeTimes() {
							      
		final String[] check = { "DWVIPPINLPENSRGPL", "WVIPPINLPENSRGPLP",
				"VIPPINLPENSRGPLPD" };
		WindowIterator winit = new WindowIterator("DWVIPPINLPENSRGPLPD",
				"DWVIPPINLPENSRGPLPD");
		InnerWindowIterator windowit = null;
		Character currChar = null;
		int counter = 0;
		while ((windowit = winit.next()) != null) {
			StringBuilder window = new StringBuilder();
			while ((currChar = windowit.next()) != null) {
				window.append(currChar);
			}
			TestCase.assertEquals(check[counter], window.toString());
			TestCase.assertEquals(check[counter], winit.getSeq1Window());
			counter++;
		}
		TestCase.assertEquals(3, counter);
	}

	@Test
	public void testIterotorFitMany() {
		final String toCheck = "RTDCYGNVNRIDTTGASCKTAKPEGLSYCGVSASKKIAERDLQAMD"
				+ "RYKTIIKKVGEKLCVEPAVIAGIISRESHAGKVLKNGWGDRGNGFGLMQVDKRSHKPQG"
				+ "TWNGEVHITQGTTILINFIKTIQKKFPSWTKDQQLKGGISAYNAGAGNVRSYARMDIGT"
				+ "THDDYANDVVARAQYYKQHGY"; // 185
		WindowIterator winit = new WindowIterator(toCheck, toCheck);
		InnerWindowIterator windowit = null;
		Character currChar = null;
		int counter = 0;
		int counterGes = 0;
		StringBuilder window = new StringBuilder();
		
		while ((windowit = winit.next()) != null) {
			window = new StringBuilder();
			StringBuilder aaseq = new StringBuilder();
			StringBuilder ssseq = new StringBuilder();
			while ((currChar = windowit.next()) != null) {
				aaseq.append(currChar);
				ssseq.append(windowit.getSeq2Char());
				window.append(currChar);
				counterGes++;
			}
			System.out.println(aaseq.toString());		
			System.out.println(ssseq.toString());
			System.out.println();
			counter++;
		}
		
		TestCase.assertEquals("YANDVVARAQYYKQHGY",window.toString());
		TestCase.assertEquals(169, counter);
		TestCase.assertEquals(169 * 17, counterGes);

	}

}
