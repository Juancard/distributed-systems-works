package tp0.part_a;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

public class FraccionTest {

	@Test
	public void testNumerador() {
		int num = 1;
		Fraccion fraccion = new Fraccion();
		fraccion.setNumerador(num);
		Assert.assertEquals(fraccion.getNumerador(), num);
	}
	@Test
	public void testNumerador2() throws DenominadorCero {
		int num = 2;
		int den = 5;
		Fraccion fraccion = new Fraccion(num, den);
		Assert.assertEquals(fraccion.getNumerador(), num);
	}
	@Test
	public void testDenominador() throws DenominadorCero {
		int den = 3;
		Fraccion fraccion = new Fraccion();
		fraccion.setDenominador(den);
		Assert.assertEquals(fraccion.getDenominador(), den);
	}
	@Test
	public void testDenominador2() throws DenominadorCero {
		int num = 3;
		int den = 7;
		Fraccion fraccion = new Fraccion(num, den);
		Assert.assertEquals(fraccion.getDenominador(), den);	
	}
	
	@Test
	public void testMcd() {
		Fraccion fraccion = new Fraccion();

		Assert.assertEquals(fraccion.mcd(42,56), 14);	
		Assert.assertNotSame(fraccion.mcd(42,56), 15);			
	}
	
	@Test
	public void denominadorCeroFail() {
		try {
			new Fraccion(2, 0);
		} catch (DenominadorCero e) {
			Assert.assertTrue(true);
			return;
		}
		Assert.fail("No Exception 'DenominadorCero' thrown");
	}
	
	@Test
	public void simplifica() throws DenominadorCero {
		int num = -2;
		int den = 10;
		int newNum = -1;
		int newDen = 5;
		
		Fraccion f = new Fraccion(num, den);

		Assert.assertEquals(f.getNumerador(), newNum);
		Assert.assertEquals(f.getDenominador(), newDen);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void suma() throws DenominadorCero {
		int num1 = -2;
		int den1 = 10;
		int num2 = 3;
		int den2 = -4;
		int numResultado = -19;
		int denResultado = 20;
		
		Fraccion f1 = new Fraccion(num1, den1);
		Fraccion f2 = new Fraccion(num2, den2);
		Fraccion resultado = f1.sumar(f2);
		
		Assert.assertEquals(resultado.getNumerador(), numResultado);
		Assert.assertEquals(resultado.getDenominador(), denResultado);
		
		Assert.assertNotSame(resultado.getNumerador(), 38);
		Assert.assertNotSame(resultado.getNumerador(), -40);
	}

	@Test
	public void resta() throws DenominadorCero {
		int num1 = 1;
		int den1 = 2;
		int num2 = 2;
		int den2 = -5;
		int numResultado = 9;
		int denResultado = 10;
		
		Fraccion f1 = new Fraccion(num1, den1);
		Fraccion f2 = new Fraccion(num2, den2);
		Fraccion resultado = f1.restar(f2);

		Assert.assertEquals(resultado.getNumerador(), numResultado);
		Assert.assertEquals(resultado.getDenominador(), denResultado);
		
		Assert.assertNotSame(resultado.getNumerador(), -9);
		Assert.assertNotSame(resultado.getNumerador(), 10);
	}
	
	@Test
	public void compareTo() throws DenominadorCero {
		int num1 = 1, den1 = 2, num2 = 2, den2 = -5;
		
		Fraccion f1 = new Fraccion(num1, den1);
		Fraccion f2 = new Fraccion(num2, den2);

		Assert.assertTrue(f1.compareTo(f2) > 0);
		Assert.assertTrue(f2.compareTo(f1) < 0);
		Assert.assertTrue(f1.compareTo(f1) == 0);

	}
}
