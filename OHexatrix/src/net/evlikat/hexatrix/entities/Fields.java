package net.evlikat.hexatrix.entities;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.evlikat.hexatrix.axial.AxialPosition;

/**
 *
 * @author Evlikat
 */
public class Fields {

    private final Map<Hexagon, AxialPosition> hexToPos;
    private final Map<AxialPosition, Hexagon> posToHex;

    public Fields() {
        this.hexToPos = new HashMap<Hexagon, AxialPosition>();
        this.posToHex = new HashMap<AxialPosition, Hexagon>();
    }

    public void put(AxialPosition position, Hexagon hexagon) {
        hexToPos.put(hexagon, position);
        posToHex.put(position, hexagon);
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
        posToHex.remove(position);
        for (Map.Entry<Hexagon, AxialPosition> entry : hexToPos.entrySet()) {
            if (entry.getValue().equals(position)) {
                hexToPos.remove(entry.getKey());
            }
        }
    }

    public boolean isEmpty() {
        return hexToPos.isEmpty();
    }
    
    public void move(AxialPosition newPosition, Hexagon hexagon) {
        
    }
}
