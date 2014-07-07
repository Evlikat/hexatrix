package net.evlikat.hexatrix.axial;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jun 27, 2014)
 */
public class AxialPosition implements AxialVector {

    private final int q;
    private final int r;

    public AxialPosition(int q, int r) {
        this.q = q;
        this.r = r;
    }

    public AxialPosition(AxialPosition position) {
        this.q = position.getQ();
        this.r = position.getR();
    }

    public int getQ() {
        return q;
    }

    public int getR() {
        return r;
    }

    public AxialPosition plus(AxialVector otherPos) {
        return new AxialPosition(q + otherPos.getQ(), r + otherPos.getR());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AxialPosition other = (AxialPosition) obj;
        return this.q == other.q && this.r == other.r;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.q;
        hash = 71 * hash + this.r;
        return hash;
    }

    @Override
    public String toString() {
        return String.format("[%d, %d]", q, r);
    }

    public int getLine() {
        return r + q / 2;
    }
}
