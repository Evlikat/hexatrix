package net.evlikat.hexatrix.axial;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jun 27, 2014)
 */
public class AxialDirection implements AxialVector {

    public static AxialDirection Right = new AxialDirection(1, 0);
    public static AxialDirection RightBack = new AxialDirection(1, -1);
    public static AxialDirection Back = new AxialDirection(0, -1);
    public static AxialDirection Left = new AxialDirection(-1, 0);
    public static AxialDirection LeftForward = new AxialDirection(-1, 1);
    public static AxialDirection Forward = new AxialDirection(0, 1);

    private final int dq;
    private final int dr;

    private AxialDirection(int dq, int dr) {
        this.dq = dq;
        this.dr = dr;
    }

    @Override
    public int getQ() {
        return dq;
    }

    @Override
    public int getR() {
        return dr;
    }
}
