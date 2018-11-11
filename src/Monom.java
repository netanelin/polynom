import java.util.StringTokenizer;

/**
 * This class represents a simple "Monom" of shape a*x^b, where a is a real number and b is an integer (summed a none negative),
 * see: https://en.wikipedia.org/wiki/Monomial 
 * The class implements function and support simple operations as: construction, value at x, derivative, add and multiply. 
 * @author Netanel Indik & Eitan Lichtman
 * @version 1.0
 */

public class Monom implements function {

	//****************** Private Methods and Data *****************
	private double _coefficient;
	private int _power;

	private void set_coefficient(double a){
		this._coefficient = a;
	}
	private void set_power(int p) {
		this._power = p;
	}

	/**
	 * This takes a string and returns the same string excluding spaces.
	 * Its mostly for help string constructor handle the input right.
	 * @param s the original input string.
	 * @return the input string with no spaces.
	 */
	private static String reduceSpaces(String s) {
		StringTokenizer stk = new StringTokenizer(s);
		String spaceless = "";
		while(stk.hasMoreTokens()) {
			spaceless += stk.nextToken();
		}
		return spaceless;
	}

	/**
	 *Auxiliary function for string Monom constructor.
	 *This for building a Monom object from string.
	 * it will determine wither the input is valid
	 * and if so it will build the right monom for the data extracted from the string.
	 * @param s the user inout string
	 * @return a Monom object
	 * @throws RuntimeException for invalid inputs.
	 */
	private static Monom validString(String s) throws RuntimeException {
		Monom m = new Monom();
		s=reduceSpaces(s);
		s=s.toLowerCase();
		if (!s.contains("x")) {
			m.set_coefficient(Double.parseDouble(s));	//power is already 0
			return m;
		}
		if (s.indexOf('x')==0){
			m.set_coefficient(1);
		}
		else if (s.indexOf('x')==1 && s.charAt(0)=='-') {
			m.set_coefficient(-1);
		}
		else {
			m.set_coefficient(Double.parseDouble(s.substring(0, s.indexOf('x'))));
		}

		if (s.indexOf('x')==s.length()-1) {
			m.set_power(1);
		}
		else if (s.charAt(s.indexOf('x')+1)!='^') {
			throw new java.lang.RuntimeException("error: invalid input");
		}
		else {
			int a = Integer.parseInt(s.substring(s.indexOf('^')+1));
			if (a<0)
				throw new java.lang.RuntimeException("error: power must be non negative");
			m.set_power(a);
		}
		return m;
	}


	//****************** Public Methods and Data *****************

	public double get_coefficient() {
		return _coefficient;
	}

	public int get_power() {
		return _power;
	}

	/**
	 *Empty constructor
	 *Sets coefficient and power to 0
	 */
	public Monom() {
		_coefficient=0;
		_power=0;
	}

	/**
	 *Constructor
	 * Sets coefficient and power as the user input
	 * @param coefficient
	 * @param power
	 * @throws RuntimeException if user try to set negative power
	 */
	public Monom(double coefficient, int power) throws RuntimeException{
		if (power<0){
			throw new java.lang.RuntimeException("error: power must be non negative");
		}
		_coefficient = coefficient;
		_power = power;
	}

	/**
	 * String constructor
	 * set coefficient and power us extracted data from input string.
	 * This assisted by the function validString.
	 * @param s input string
	 */
	public Monom(String s){
		Monom a = new Monom(validString(s));
		this._coefficient=a.get_coefficient();
		this._power=a.get_power();
	}

	/**
	 * Copy constructor
	 * Deep copies given Monom into new Monom.
	 * @param ot given Monom
	 */
	public Monom(Monom ot) {
		if (ot==null){
			throw new java.lang.RuntimeException("error: other Monom cannot be null");
		}
		this.set_coefficient(ot.get_coefficient());
		this.set_power(ot.get_power());

	}

	/**
	 * This implement value at x. (f(x))
	 * @param x
	 * @return Monom value at given x
	 */
	@Override
	public double f(double x) {
		return _coefficient*Math.pow(x,_power);
	}

	/**
	 * This for adding two Monoms.
	 * It will only add Monoms with similar powers.
	 * It will add given Monom to this Monom.
	 * @param ot input Monom
	 * @throws RuntimeException if the Monoms have different powers.
	 */
	public void add(Monom ot ) throws RuntimeException{
		if( ot._power==this._power) {
			set_coefficient(_coefficient + ot._coefficient);
		} else{
			throw new java.lang.RuntimeException("error: can't add Monoms with different powers");
		}
	}

	/**
	 * This for multiplying two Monoms.
	 * It will multiply this Monom by given Monom.
	 * @param ot given Monom.
	 */
	public void multiply(Monom ot ){
		if(this.get_coefficient() == 0 || ot.get_coefficient() == 0) {
			_coefficient=0 ;
			_power=0 ;
		}
		else {
			_power+=ot._power;
			_coefficient=_coefficient*ot._coefficient;
		}
	}

	/**
	 * It will change this monom to its derivative.
	 */
	public void derivative(){
		if (_power!=0) {
			_coefficient = _coefficient * _power;
			_power--;
		}
		else {
			_coefficient=0;
		}

	}

	/**
	 * This for comparing this Monom with given Monom.
	 * @param ot givem Monom.
	 * @return true if given Monom equals this Monom.
	 */
	public boolean equals(Object ot) {
		if (ot instanceof Monom) {
			Monom m = (Monom)ot;
			return this.get_coefficient() == m.get_coefficient() && this.get_power() == m.get_power();
		}
		else{
			return false;
		}
	}

	/**
	 * This for representing Monom by string in the native writing form.
	 * For example the Monom 3*x^0 will represented by "3" etc'.
	 * @return string representation of this Monom.
	 */
	public String toString(){
		String s = "";
		if(_coefficient==0)
			s += "0";
		else if (_power == 0)
			s += _coefficient;
		else{
			if(_coefficient==1)
				s+="x";
			else if(_coefficient==-1)
				s+="-x";
			else{
				s+= _coefficient+"x";
			}
			if (_power!=1)
				s+="^"+_power;
		}
		return s;


	}

}
