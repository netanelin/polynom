import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

class PolynomTest {

	@Test
	void defaultConstructorTest() {
		Polynom p1 = new Polynom();

		Iterator<Monom> it = p1.iterator();

		assertTrue(!it.hasNext(),"error: default Constructor");
	}

	@Test
	void copyConstructorTest(){
		Polynom p2 = new Polynom();
		Monom m1 = new Monom(3,5);
		Monom m2 = new Monom(2,4);

		p2.add(m1);
		p2.add(m2);

		Polynom p2Copy = new Polynom(p2);
		assertEquals(p2,p2Copy);
	}

	@Test
	void stringConstructorTest() {
		String [] good = {"3-7+5-2",
				"x^3 + 5X^2 + x + 4",
				"-x^3 + 5x^2 +2",
				"-x^2-5-x^2-x^3-7" ,
				"0x^5+3",
				"3x^0-5x",
				"", " "};
		String [] bad = {"(-x^2+(3x^5))","x^2--3x"};

		for(int i=0; i<good.length; i++) {
			String c = good[i];
			Polynom m1 = new Polynom (c);
		}

		for(int i=0; i<bad.length; i++) {
			String c = bad[i];
			boolean exceptionThrown = false;
			try {
				Polynom m2 = new Polynom (c);
			}
			catch (Exception e) {
				exceptionThrown = true;
			}
			assertTrue(exceptionThrown,"error: String Constructor");
		}
	}

	@Test
	void addMonomTest() {
		Polynom p = new Polynom("5x^3 + 4x^2 - 3x + 2");
		Monom m0 = new Monom ();
		Monom m1 = new Monom (6,4);
		Monom m2 = new Monom (1,2);
		Monom m3 = new Monom (3,1);
		Monom m4 = new Monom (-2,0);

		p.add(m0);
		p.add(m1);
		p.add(m2);
		p.add(m3);
		p.add(m4);

		Polynom target = new Polynom("6x^4 + 5x^3 + 5x^2");

		assertEquals(p,target);
	}

	@Test
	void addPolyTest() {
		Polynom p0 = new Polynom("5x^3 + 4x^2 - 3x + 2");
		Polynom p1 = new Polynom("6x^4 + 2x - 2");
		Polynom target = new Polynom("6x^4 + 5x^3 + 4x^2 - x");

		p0.add(p1);

		assertEquals(p0,target);
	}

	@Test
	void substractTest() {
		Polynom p0 = new Polynom("5x^3 + 4x^2 - 3x + 2");
		Polynom p1 = new Polynom("-6x^4 - 2x + 2");
		Polynom target = new Polynom("6x^4 + 5x^3 + 4x^2 - x");

		p0.substract(p1);

		assertEquals(p0,target);
	}

	@Test
	void multiplyTest() {
		Polynom p0 = new Polynom("3x^2 + x");
		Polynom p1 = new Polynom("4x + 2");
		Polynom target = new Polynom("12x^3 + 10x^2 + 2x");

		p0.multiply(p1);

		assertEquals(p0,target);
	}

	@Test
	void equalsTest() {
		Polynom p0 = new Polynom("3x^2 + x");
		Polynom p1 = new Polynom("3x^2 + x");
		Polynom p2 = new Polynom("3x^3 + 1");
		Polynom p3 = new Polynom();
		Polynom p4 = new Polynom("0");
		Polynom p5 = new Polynom("2");



		assertTrue(p0.equals(p1),"error: func equals");
		assertTrue(!p0.equals(p2),"error: func equals");
		assertTrue(p3.equals(p4),"error: func equals");
		assertTrue(!p3.equals(p5),"error: func equals");
	}

	@Test
	void IsZeroTest() {
		Polynom p0 = new Polynom();
		Polynom p1 = new Polynom("3x^2 + x");

		assertTrue(p0.isZero() && !p1.isZero(),"error: func zero");
	}

	@Test
	void rootTest() {
		boolean exceptionThrown = false;

		//check if smaller epsilon gives more precise answer
		Polynom p0 = new Polynom("x"); // cuts x axis at: 0
		double result1 = p0.root(-0.5 ,2 , 0.5);
		double result2 = p0.root(-0.5 ,2 , 0.25);
		double result3 = p0.root(-0.5 ,2 , 0.01);
		assertTrue(Math.abs(result1)>=Math.abs(result2),"error: func root");
		assertTrue(Math.abs(result2)>=Math.abs(result3),"error: func root");

		//check how root handles different cases
		Polynom p1 = new Polynom("x^3 - x"); // cuts x axis at: -1,0,1. see: https://www.desmos.com/calculator/9fwg0quutq
		final double eps = 0.01;

		//one cut
		double cut1 = p1.root(-0.5 , 0.75 , eps);

		//3 cuts
		double cut2 = p1.root(-1.5 , 2 , eps);

		//x0 || x1 are cuts
		double cut3 = p1.root(0 , 1.5 , eps);
		double cut4 = p1.root(-0.5 , 1 , eps);

		//bad cases:

		// both x0 && x1 are above x axis or below it
		try {
			double cut5 = p1.root(-1.5, 0.5, eps);
		}
		catch (RuntimeException e){
			exceptionThrown = true;
		}

		assertTrue(exceptionThrown,"error: func root");

		//no cut
		exceptionThrown = false;
		try {
			double cut7 = p1.root(1.5 , 2 , eps);
		}
		catch (Exception e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown,"error: func root");

		exceptionThrown = false;
		try {
			double cut8 = p1.root(-2, -1.5 , eps);
		}
		catch (Exception e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown,"error: func root");

		//x1<x0
		exceptionThrown = false;
		try {
			double cut9 = p1.root(1.5, 0.5 , eps);
		}
		catch (Exception e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown,"error: func root");

		exceptionThrown = false;
		try {
			double cut6 = p1.root(-0.75, -0.25, eps);
		}
		catch (Exception e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown,"error: func root");

		//eps=0
		exceptionThrown = false;
		try {
			double cut10 = p0.root(2, 4, 0);
		}
		catch (Exception e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown,"error: func root");

		//eps<0
		exceptionThrown = false;
		try {
			double cut11 = p0.root(2, 4, -0.01);
		}
		catch (Exception e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown,"error: func root");
	}

	@Test
	void copyTest() {
		Polynom p0 = new Polynom("3x^2 + x");
		Polynom_able p1 =p0.copy();

		assertEquals(p1,p0);
	}

	@Test
	void derivativeTest() {
		Polynom p = new Polynom("3.5x^3 - 2.3x^2 + 15x - 32");
		Polynom_able target = new Polynom("10.5x^2 - 4.6x + 15");
		Polynom_able der = p.derivative();

		assertEquals(der,target);
		assertTrue(!p.equals(target));
	}

	@Test
	void funcTest() {
		Polynom p = new Polynom("3.5x^3 - 2.3x^2 + 15x - 32");

		double result1 = p.f(-2);
		double result2 = p.f(0);
		double result3 = p.f(1);
		double result4 = p.f(2.5);

		double target1 = -99.2;
		double target2 = -32;
		double target3 = -15.8;
		double target4 = 45.8125;

		assertEquals(result1,target1);
		assertEquals(result2,target2);
		assertEquals(result3,target3);
		assertEquals(result4,target4);
	}

	@Test
	void toStringTest() {
		Polynom p = new Polynom();
		Polynom p1 = new Polynom("3.5x^3 - 2.3x^2 + 15x - 32");

		assertEquals("0",p.toString());
		assertEquals("3.5x^3-2.3x^2+15.0x-32.0",p1.toString());
	}

	@Test
	void areaTest() {
		//check if smaller epsilon gives more precise answer
		Polynom p0 = new Polynom("-x^2 + 9");
		double result1 = p0.area(-3 ,3 , 0.5);
		double result2 = p0.area(-3 ,3 , 0.25);
		double result3 = p0.area(-3 ,3 , 0.01);

		final int target1 = 36;

		assertTrue(Math.abs(target1-result1)>=Math.abs(target1-result2),"error: func area");
		assertTrue(Math.abs(target1-result2)>=Math.abs(target1-result3),"error: func area");

		//bad cases:

		//eps = 0
		boolean exceptionThrown = false;
		try {
			double result4 = p0.area(2, 4, 0);
		}
		catch (Exception e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown,"error: func area");

		//eps < 0
		exceptionThrown = false;
		try {
			double result4 = p0.area(2, 4, -0.01);
		}
		catch (Exception e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown,"error: func area");


		//check if area calculates only areas above x axis
		Polynom p1 = new Polynom("x^3 - x");	//positive at: -1<x<0 && x>1. negative at: 0<x<1 && x<-1
		final double eps = 0.01;
		double result6 = p1.area(-1 ,1 , eps);
		double result7 = p1.area(-1 ,0 , eps);
		double diff = Math.abs(result6-result7);
		assertTrue(diff<eps,"error: func area");
	}

	@Test
	void areaBeneathXaxisTest() {
		//check if smaller epsilon gives more precise answer
		Polynom p0 = new Polynom("-x^2 + 9");
		double result1 = p0.areaBeneathXaxis(-3 ,3 , 0.5);
		double result2 = p0.areaBeneathXaxis(-3 ,3 , 0.25);
		double result3 = p0.areaBeneathXaxis(-3 ,3 , 0.01);

		final int target1 = 36;

		assertTrue(Math.abs(target1-result1)>=Math.abs(target1-result2),"error: func areaBeneathXaxis");
		assertTrue(Math.abs(target1-result2)>=Math.abs(target1-result3),"error: func areaBeneathXaxis");

		//bad cases:

		//eps = 0
		boolean exceptionThrown = false;
		try {
			double result4 = p0.areaBeneathXaxis(2, 4, 0);
		}
		catch (Exception e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown,"error: func areaBeneathXaxis");

		//eps < 0
		exceptionThrown = false;
		try {
			double result4 = p0.areaBeneathXaxis(2, 4, -0.01);
		}
		catch (Exception e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown,"error: func areaBeneathXaxis");


		//check if areaBeneathXaxis calculates only areas beneath x axis
		Polynom p1 = new Polynom("x^3 - x");	//positive at: -1<x<0 && x>1. negative at: 0<x<1 && x<-1
		final double eps = 0.01;
		double result6 = p1.areaBeneathXaxis(-1 ,1 , eps);
		double result7 = p1.areaBeneathXaxis(0 ,1 , eps);
		double diff = Math.abs(result6-result7);
		assertTrue(diff<eps,"error: func areaBeneathXaxis");
	}
}
