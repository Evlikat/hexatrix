package net.evlikat.hexatrix.entities;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import net.evlikat.hexatrix.axial.AxialDirection;
import net.evlikat.hexatrix.axial.AxialPosition;

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

    public void remove(AxialPosition position) {
        lock.lock();
        try {
            posToHex.remove(position);
            for (Map.Entry<Hexagon, AxialPosition> entry : hexToPos.entrySet()) {
                if (entry.getValue().equals(position)) {
                    hexToPos.remove(entry.getKey());
                    return;
                }
            }
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

    public void moveBatch(List<AxialPosition> fallingParts, AxialDirection direction) {
        lock.lock();
        try {
            Map<AxialPosition, Hexagon> newHexagonPositions = new HashMap<AxialPosition, Hexagon>();
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
        } finally {
            lock.unlock();
        }
    }
}
