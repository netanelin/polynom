import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class MonomTest {

    @Test
    void defaultConstructor(){
       Monom m = new Monom();

       assertEquals(0, m.get_coefficient());
       assertEquals(0, m.get_power());
    }

    @Test
    void inputConstructor(){
        Monom m0 = new Monom(2, 5);
        Monom m1 = new Monom((1.0 / 4), 2);

        assertEquals(2, m0.get_coefficient());
        assertEquals(5, m0.get_power());
        assertEquals(0.25, m1.get_coefficient());
        assertEquals(2, m1.get_power());

        boolean exceptionThrown = false;
        try {
            Monom m2 = new Monom(1, -2);
        } catch (RuntimeException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown, "negative power was excepted");
    }

    @Test
    void copyConstructor(){
        Monom m = new Monom(2, 5);
        Monom mCopy = new Monom(m);

        assertEquals(m,mCopy);
    }

    @Test
    void stringConstructor(){
        String [] valid = {"x ","7 x","-x","4.78","3.9X^71"};
        String [] invalid = {"%$","2^3",""," ","xX","(4","x32","--3X"};

        for(int i=0; i<valid.length; i++) {
            String c = valid[i];
            Monom m = new Monom (c);
        }
        for(int i=0; i<invalid.length; i++) {
            String c = invalid[i];
            boolean exceptionThrown = false;
            try {
                Monom m = new Monom (c);
            }
            catch (Exception e) {
                exceptionThrown = true;
            }

            assertTrue(exceptionThrown, "invalid String: " + c + ". was excepted");
        }
    }

    @Test
    void f() {
        Monom m = new Monom(4.0 / 5, 4);

        assertEquals(12.8,m.f(2));
    }

    @Test
    void add() {
        Monom m0 = new Monom(1, 3);
        Monom m1 = new Monom(2, 3);
        Monom m2 = new Monom(3, 0);

        boolean exceptionThrown = false;
        try {
            m0.add(m2);
        } catch (RuntimeException e) {
            exceptionThrown = true;
        }

        m0.add(m1);
        m2.add(m2);

        assertEquals(new Monom(3,3),m0);
        assertEquals(new Monom(6,0),m2);
        assertTrue(exceptionThrown, "Monom with different power excepted");
    }

    @Test
    void multiply() {
        Monom m0 = new Monom ();
        Monom m1 = new Monom (2,2);
        Monom m2 = new Monom (5,3);

        m0.multiply(m1);
        m1.multiply(m2);

        assertEquals(new Monom(0,0),m0);
        assertEquals(new Monom(10,5),m1);
    }

    @Test
    void derivative() {
        Monom m0 = new Monom ();
        Monom m1 = new Monom (3,2);

        m0.derivative();
        m1.derivative();

        assertEquals(new Monom(0,0),m0);
        assertEquals(new Monom(6,1),m1);
    }

    @Test
    void equals() {
        Monom m0 = new Monom("3x^2");
        Monom m1 = new Monom("3x^2");
        Monom m2 = new Monom("5");

        Object obj0 = new Monom("3x^2");
        Object obj1 = new Object();

        assertTrue(m0.equals(m1));
        assertTrue(!m0.equals(m2));
        assertTrue(m0.equals(obj0));
        assertTrue(!m0.equals(obj1));
    }

    @Test
    void toStringTest() {
        Monom m0 = new Monom ();
        Monom m1 = new Monom (0,1);
        Monom m2 = new Monom (5,0);
        Monom m3 = new Monom (1,2);
        Monom m4 = new Monom (5,1);
        Monom m5 = new Monom (5,3);

        String a = m0.toString();
        String b = m1.toString();
        String c = m2.toString();
        String d = m3.toString();
        String e = m4.toString();
        String f = m5.toString();

        assertEquals("0",a);
        assertEquals("0",b);
        assertEquals("5.0",c);
        assertEquals("x^2",d);
        assertEquals("5.0x",e);
        assertEquals("5.0x^3",f);

    }
}