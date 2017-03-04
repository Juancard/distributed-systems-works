package tp0.part_a;

public class Fraccion {

	private int numerador = 1;
	private int denominador = 1;
	
	public Fraccion() {
		super();
	}
	
	public Fraccion(int numerador, int denominador) throws DenominadorCero{
		super();
		if (denominador == 0) throw new DenominadorCero("Denominador no puede ser 0 (cero)");
		this.denominador = denominador;
		this.numerador = numerador;
		this.simplificar();
		this.normalizarSigno();
	}

	private void simplificar() {
		int mcd = this.mcd(this.numerador, this.denominador);
		if (mcd != 0) {
			this.numerador /= mcd;
			this.denominador /= mcd;
		}
	}
	
	private void normalizarSigno(){
		if (this.denominador < 0){
			this.denominador *= -1;
			this.numerador *= -1;
		}
	}
	
	public int mcd(int a, int b){
		if (b == 0) return a;
		return mcd(b, a % b);		
	}
		
	public Fraccion sumar(Fraccion otraFraccion) throws DenominadorCero{
		int nuevoDen = otraFraccion.denominador * this.denominador;
		int nuevoNum = (otraFraccion.numerador * this.denominador) 
				+ (otraFraccion.denominador * this.numerador);
		return new Fraccion(nuevoNum, nuevoDen);
	}
	
	public Fraccion restar(Fraccion otraFraccion) throws DenominadorCero{
		int nuevoDen = otraFraccion.denominador * this.denominador;
		int nuevoNum = (this.numerador * otraFraccion.denominador) 
				- (this.denominador * otraFraccion.numerador);
		
		return new Fraccion(nuevoNum, nuevoDen);
	}
	
	public int compareTo(Fraccion fraccionDada){
		double thisValue = (double) this.numerador / (double) this.denominador;
		double givenValue = (double) fraccionDada.numerador / (double) fraccionDada.denominador;
		double resultado = thisValue - givenValue;
		return (int) Math.round(resultado);
	}
	
	public int getNumerador() {
		return numerador;
	}

	public void setNumerador(int numerador) {
		this.numerador = numerador;
	}

	public int getDenominador() {
		return denominador;
	}

	public void setDenominador(int denominador) throws DenominadorCero {
		if (denominador == 0) {
			throw new DenominadorCero("Denominador no puede ser 0 (cero)");
		}
		this.denominador = denominador;
	}

	@Override
	public String toString() {
		return numerador + "/" + denominador;
	}

}
