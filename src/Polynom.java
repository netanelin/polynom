import java.util.*;
import java.util.List;

/**
 * This class represents a Polynom with add, multiply functionality, it also supports the following:
 * 1. Riemann's Integral: https://en.wikipedia.org/wiki/Riemann_integral
 * 2. Finding a numerical value between two values (currently support root only f(x)=0).
 * 3. Derivative
 * @authors Eitan Lichtman & Netanel Indik
 * @version 1.0
 */
public class Polynom implements Polynom_able {

//**********Private Methods and Data**********

	private List<Monom> poly;


//	  Auxiliary function for reducing Polynom.
//	  It will go over the Monoms in the polynom and erase those with coefficient 0.
//	  used after derivative action, addition and substraction.

	private void reduce(){
		Iterator<Monom> it = iterator();
		Monom current;
		while(it.hasNext()){
			current=it.next();
			if (current.get_coefficient()==0) {
				it.remove();
			}
		}
	}

//	  This takes a string and returns the same string excluding spaces.
//	  Its mostly for help string constructor handle the input right.
//	  @param s the original input string.
//	  @return the input string with no spaces.

	private static String reduceSpaces(String s) {
		StringTokenizer stk = new StringTokenizer(s);
		String spaceless = "";
		while(stk.hasMoreTokens()) {
			spaceless += stk.nextToken();
		}
		return spaceless;
	}

	//**********Public Methods**********

	/**
	 * Empty constructor
	 * only declares array list.
	 */
	public Polynom() {
		poly = new ArrayList<Monom>();
	}

	/**
	 * Copy constructor
	 * Deep copies given Polynom into new Polynom.
	 * @param ot given Polynom.
	 */
	public Polynom(Polynom ot) {
		poly = new ArrayList<Monom>();
		Iterator<Monom> it = ot.iterator();
		while (it.hasNext()) {
			Monom copy = new Monom(it.next());
			poly.add(copy);
		}
	}

	/**
	 * String constructor
	 * Builds new Polynom us represented by input string.
	 * @param s input string.
	 * @throws RuntimeException if input is invalid.
	 */
	public Polynom(String s) throws RuntimeException {
		s = reduceSpaces(s);
		if(s.equals("0")){
			poly = new ArrayList<Monom>();
		}
		else {
			Polynom tempP = new Polynom();
			if (s.contains("--") || s.contains("-+") || s.contains("+-") || s.contains("++"))
				throw new RuntimeException("error: invalid input");
			StringTokenizer stk1 = new StringTokenizer(s, "+");
			String temp = "";
			boolean minus;
			while (stk1.hasMoreTokens()) {
				temp = stk1.nextToken();
				minus = false;
				if (temp.charAt(0) == '-') {
					minus = true;
				}
				StringTokenizer stk2 = new StringTokenizer(temp, "-");
				if (!minus) {
					Monom m = new Monom(stk2.nextToken());
					tempP.add(m);
				}
				while (stk2.hasMoreTokens()) {
					Monom m = new Monom("-" + stk2.nextToken());
					tempP.add(m);
				}

			}
			poly = new ArrayList<Monom>();
			Iterator<Monom> it = tempP.iterator();
			while (it.hasNext()) {
				poly.add(it.next());
			}
		}
	}

	/**
	 *
	 * @return array list iterator of the Polynom.
	 */
	public Iterator<Monom> iterator() {
		return poly.iterator();
	}

	/**
	 * This method adds a Monom to this Polynom.
	 * It will add the given Monom at the correct place inside the Polynom.
	 * Also, it will reduce the polynom if any of it's monoms have a _coefficient that's zero.
	 * @param m given monom to add
	 */
	public void add(Monom m) {
		Iterator<Monom> it = iterator();
		Comparator<Monom> co = new Monom_Comperator();
		Monom current = new Monom();
		if (poly.isEmpty())
			poly.add(m);
		else {
			boolean done = false;
			while(!done && it.hasNext() ){
				current=it.next();
				if(co.compare(m,current)==0){
					current.add(m);
					done = true;
				}
				if( co.compare(m,current)<0 ){
					poly.add(poly.indexOf(current), m );
					done = true;
				}
			}
			if(!done)
				poly.add(m);

			reduce();
		}
	}

	/**
	 * This method substracts a given Polinom_able from this Polynom.
	 * @param p1 given Polynom_able
	 */
	@Override
	public void substract(Polynom_able p1) {
		Iterator<Monom> it = p1.iterator();
		while(it.hasNext()){
			Monom cur = it.next();
			Monom m = new Monom(-cur.get_coefficient(), cur.get_power());
			add(m);
		}
	}

	/**
	 * This method multiplies this Polynom with a given Polinom_able.
	 * @param p1 given Polynom_able.
	 */
	@Override
	public void multiply(Polynom_able p1) {
		Polynom temp = new Polynom(this);
		this.poly.clear();
		Monom current = new Monom();
		Iterator<Monom> it1 = p1.iterator();
		while(it1.hasNext()){
			Iterator<Monom> it2 = temp.iterator();
			current = it1.next();
			while(it2.hasNext()){
				Monom current2 = new Monom(current);
				current2.multiply(it2.next());
				this.add(current2);
			}
		}
	}

	/**
<<<<<<< HEAD
	 * This method compares this Polynom with a given Polynom_able.
=======
	 * This method compares this Polynom with a given Object.
>>>>>>> 6b86a499d0af114321b8cf3fdd5fe084252a91c1
	 * @param obj given Object.
	 * @return true if the Polynoms are equal.
	 */
	@Override
	public boolean equals(Object obj) {

		if(!(obj instanceof Polynom)) {
			return false;
		}
		Polynom p = new Polynom((Polynom)obj);

		Iterator<Monom> it1 = p.iterator();
		Iterator<Monom> it2 = this.iterator();
		while (it1.hasNext() && it2.hasNext()){
			if(!it2.next().equals(it1.next()))
				return false;
		}
		return !it1.hasNext() && !it2.hasNext();
	}

	/**
	 * @return true if this polynom is the zero polynomial.
	 */
	@Override
	public boolean isZero() {
		return poly.size() == 0;
	}

	/**
	 * This method finds a value x' (x0<=x'<=x1) that is less than eps away from an x that gives F(x)=0.
	 * In every interaction we find the middle between x0 and x1 and continue searching in the correct half
	 * until the difference between x0 and x1 is less than eps
	 * @param x0 starting point
	 * @param x1 end point
	 * @param eps step (positive) value
	 * @return x' that is less than eps away from an x that gives F(x)=0.
	 */
	@Override
	public double root(double x0, double x1, double eps) {
		if(this.f(x0)==0) {
			return x0;
		}
		if(this.f(x1)==0) {
			return x1;
		}
		if((x0>x1)
				|| (eps<=0)
				||(this.f(x0)>0 && this.f(x1)>0)
				||(this.f(x0)<0 && this.f(x1)<0) ) {
			throw new java.lang.RuntimeException("error: cannot find root");
		}
		if (x1-x0<eps) {
			return x0;
		}
		double mid = (x0+x1)/2;
		if((this.f(x0)>0 && this.f(mid)<0) ||
				(this.f(x0)<0 && this.f(mid)>0)) {
			return root(x0,mid,eps);
		}
		else {
			return root(mid,x1,eps);
		}
	}

	/**
	 * create a deep copy of this Polynom
	 * @return Polynom_able copy of this Polynom.
	 */
	@Override
	public Polynom_able copy() {
		Polynom p1 = new Polynom(this);
		return p1;
	}

	/**
	 * This method creates a new Polynom which is the derivative of this Polynom
	 * @return the Polynom derivative.
	 */
	@Override
	public Polynom_able derivative() {
		Polynom deri = new Polynom(this);
		Iterator<Monom> it = deri.iterator();
		while(it.hasNext()){
			it.next().derivative();
		}
		deri.reduce();
		return deri;
	}

	/**
	 * This method calculates the area above X-axis
	 * using a Riman's integral between x0 and x1 in eps steps.
	 * @param x0 starting point
	 * @param x1 end point
	 * @param eps positive step value
	 * @return the approximated area above X-axis below this function bounded in the range of [x0,x1]
	 */
	@Override
	public double area(double x0, double x1, double eps) {
		if(eps<=0)
			throw new java.lang.RuntimeException("error: cannot find area");
		if (x0>x1)
			return 0;

		double currentX = x0;
		double sum = 0;
		while(currentX<x1){
			if (f(currentX)>0) {
				sum += eps*f(currentX);
			}
			currentX += eps;
		}
		return sum;
	}
	
	
	/**
	 * This method calculates the area over the polynom and beneath X-axis
	 * using a Riman's integral between x0 and x1 in eps steps.
	 * @param x0 starting point
	 * @param x1 end point
	 * @param eps positive step value
	 * @return the approximated area above X-axis below this function bounded in the range of [x0,x1]
	 */
	
	public double areaBeneathXaxis(double x0, double x1, double eps) {
		if(eps<=0)
			throw new java.lang.RuntimeException("error: cannot find area");
		if (x0>x1)
			return 0;

		double currentX = x0;
		double sum = 0;
		while(currentX<x1){
			if (f(currentX)<0) {
				sum += eps*(-f(currentX));
			}
			currentX += eps;
		}
		return sum;
	}

	/**
	 * add a given Polynom to this Polynom.
	 * @param ot given Polynom
	 */
	public void add(Polynom_able ot){
		Iterator<Monom> it = ot.iterator();
		while(it.hasNext()){
			add(it.next());
		}
	}

	/**
	 * This method returns the function value for a given x. (f(x))
	 * @param x
	 * @return Polynom value at x.
	 */
	public double f(double x) {
		Iterator<Monom> it = iterator();
		double sum = 0;
		while (it.hasNext()) {
			sum += it.next().f(x);
		}
		return sum;
	}

	/**
	 * This method is for representing a Polynom by a string.
	 * for example: 3*x^2-2*x^1+1*x^0 = "3x^2-2x+1"
	 * @return string representation of this Polynom.
	 */
	public String toString() {
		Iterator<Monom> it = poly.iterator();
		String polyString = "";
		Monom current = new Monom();

		if (poly.isEmpty()) {
			polyString = "0";
		} else {
			polyString = it.next().toString();
			while (it.hasNext()) {
				current = it.next();
				if (current.get_coefficient() > 0) {
					polyString += "+" + current.toString();
				} else {
					polyString += current.toString();
				}
			}
		}
		return polyString;
	}

}
