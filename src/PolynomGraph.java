import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.filters.Convolution;
import de.erichseifert.gral.data.filters.Filter.Mode;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.lines.DefaultLineRenderer2D;
import de.erichseifert.gral.ui.InteractivePanel;
import javax.swing.JFrame;
import java.awt.*;

/**
 * This class extends JFrame and creates a GUI of a polynom function graph.
 * this class uses gral-core-0.11 library.
 * @author Eitan lichtman & Netanel Indik
 * @version 1.0
 */
public class PolynomGraph extends JFrame{

    //****************** Private Methods and Data *****************
    private static final double eps = 0.01;
    private Polynom poly;
    private double x0;
    private double x1;

    private static DataTable points (Polynom poly, double x0, double x1, double eps){
        DataTable data = new DataTable(Double.class, Double.class);
        double x = x0;
        double y;
        while (x <= x1) {
            y = poly.f(x);
            data.add(x, y);
            x += eps;
        }
        return data;
    }

    private static DataTable extremeP(Polynom poly, double x0, double x1, double eps) {
        DataTable exP = new DataTable(Double.class, Double.class);
        Polynom der = (Polynom)poly.derivative();

        if(!der.equals(new Polynom("0"))){
        double x=x0;
        while (x <= x1) {
            if(der.f(x)==0 || der.f(x)*der.f(x+eps)<0 )
                exP.add(x,poly.f(x));
            x += eps;
        }
        }
        return exP;
    }

    //****************** Public Methods and Data *****************

    public Polynom getPoly() {
        return poly;
    }

    public void setPoly(Polynom poly) {
        this.poly = poly;
    }

    public double getX0() {
        return x0;
    }

    public void setX0(double x0) {
        this.x0 = x0;
    }

    public double getX1() {
        return x1;
    }

    public void setX1(double x1) {
        this.x1 = x1;
    }

    /**
     * this constructor create
     * @param poly polynom to draw
     * @param x0 left domain boundary
     * @param x1 right domain boundary
     */
    public PolynomGraph(Polynom poly, double x0 , double x1) throws RuntimeException{

        //input check
        if(x0>=x1 || x1-x0<eps)
            throw new RuntimeException("Error: invalid domain");

        this.poly = new Polynom(poly);
        this.x0 = x0;
        this.x1 = x1;
    }

    /**
     * creates a graphical representation of the given polynom within the given domain
     */
    public void draw(){
        //polynom graph guide-points
        DataTable data = points(poly,x0,x1,eps);

        //polynom graph extremePoints-points
        DataTable extremePoints = extremeP(poly,x0,x1,eps);

        //creating convolution from guide-points
        DataSource Curve = new Convolution(data, null, Mode.REPEAT, 1);

        //creating curve of the graph with extreme points
        XYPlot plot = new XYPlot(Curve,extremePoints);

        //setting the curve to be pointless
        plot.setPointRenderers(Curve, null);

        //setting the initiate window boundaries
        plot.getAxis(XYPlot.AXIS_X).setAutoscaled(true);
        plot.getAxis(XYPlot.AXIS_Y).setAutoscaled(true);

        //setting interactive panel;
        getContentPane().add(new InteractivePanel(plot));

        //frame location and size
        setLocation(250, 50);
        setSize(new Dimension(650, 450));

        //creating the lines
        DefaultLineRenderer2D lines = new DefaultLineRenderer2D();
        lines.setColor(Color.BLUE);

        //setting the plot to contain the lines
        plot.setLineRenderers(Curve,lines);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
