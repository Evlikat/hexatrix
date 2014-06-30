package net.evlikat.hexatrix.axial;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jun 27, 2014)
 */
public class AxialPosition {

    private final int Q;
    private final int R;

    public AxialPosition(int q, int r) {
        this.Q = q;
        this.R = r;
    }

    public AxialPosition(AxialPosition position) {
        this.Q = position.getQ();
        this.R = position.getR();
    }

    public int getQ() {
        return Q;
    }

    public int getR() {
        return R;
    }

    public AxialPosition relatedTo(AxialPosition otherPos) {
        return new AxialPosition(Q + otherPos.Q, R + otherPos.R);
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
        return this.Q == other.Q && this.R == other.R;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.Q;
        hash = 71 * hash + this.R;
        return hash;
    }

    @Override
    public String toString() {
        return String.format("[{0}, {1}]", Q, R);
    }
}
