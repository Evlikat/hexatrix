package net.evlikat.hexatrix.entities;

import net.evlikat.hexatrix.axial.AxialDirection;
import net.evlikat.hexatrix.axial.AxialPosition;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Evlikat
 */
public class Fields {

    private final Map<Hexagon, AxialPosition> hexToPos;
    private final Map<AxialPosition, Hexagon> posToHex;

    private final Lock lock = new ReentrantLock(true);

    public Fields() {
        this.hexToPos = new HashMap<Hexagon, AxialPosition>();
        this.posToHex = new HashMap<AxialPosition, Hexagon>();
    }

    public void put(AxialPosition position, Hexagon hexagon) {
        lock.lock();
        try {
            hexToPos.put(hexagon, position);
            posToHex.put(position, hexagon);
        } finally {
            lock.unlock();
        }
    }

    public Hexagon get(AxialPosition position) {
        return posToHex.get(position);
    }

    public AxialPosition get(Hexagon hexagon) {
        return hexToPos.get(hexagon);
    }

    public Collection<AxialPosition> getPositions() {
        return posToHex.keySet();
    }

    public Collection<Hexagon> getHexagons() {
        return hexToPos.keySet();
    }

    public boolean contains(AxialPosition axialPosition) {
        return posToHex.containsKey(axialPosition);
    }

    public Fields with(Fields fields) {
        Fields result = new Fields();
        for (Map.Entry<Hexagon, AxialPosition> entry : this.hexToPos.entrySet()) {
            result.put(entry.getValue(), entry.getKey());
        }
        for (Map.Entry<Hexagon, AxialPosition> entry : fields.hexToPos.entrySet()) {
            result.put(entry.getValue(), entry.getKey());
        }
        return result;
    }
    
    public Collection<Hexagon> getTopFields() {
        // column to top hexagon
        Map<Integer, Hexagon> topHexagons = new HashMap<Integer, Hexagon>();
        // column to top hexagon height
        Map<Integer, Integer> topHexagonHeights = new HashMap<Integer, Integer>();
        for (Map.Entry<AxialPosition, Hexagon> entry : posToHex.entrySet()) {
            int q = entry.getKey().getQ();
            int r = entry.getKey().getR();
            Integer topHexR = topHexagonHeights.get(q);
            if (topHexR == null || topHexR < r) {
                topHexagons.put(q, entry.getValue());
                topHexagonHeights.put(q, r);
            }
        }
        return topHexagons.values();
    }

    public void remove(AxialPosition position) {
        lock.lock();
        try {
            Hexagon hex = posToHex.get(position);
            posToHex.remove(position);
            hexToPos.remove(hex);
        } finally {
            lock.unlock();
        }
    }

    public boolean isEmpty() {
        return hexToPos.isEmpty();
    }

    public void clear() {
        lock.lock();
        try {
            posToHex.clear();
            hexToPos.clear();
        } finally {
            lock.unlock();
        }
    }

    public List<AxialPosition> moveBatch(List<AxialPosition> fallingParts, AxialDirection direction) {
        lock.lock();
        try {
            Map<AxialPosition, Hexagon> newHexagonPositions = new LinkedHashMap<>();
            for (AxialPosition fallingPart : fallingParts) {
                Hexagon hex = posToHex.get(fallingPart);
                final AxialPosition newPosition = fallingPart.plus(direction);
                // re-set position
                hex.setPosition(newPosition);
                hexToPos.put(hex, newPosition);
                // remove all old part positions
                posToHex.remove(fallingPart);
                newHexagonPositions.put(newPosition, hex);
            }
            // add new positions at once
            posToHex.putAll(newHexagonPositions);
            return new ArrayList<>(newHexagonPositions.keySet());
        } finally {
            lock.unlock();
        }
    }
}
