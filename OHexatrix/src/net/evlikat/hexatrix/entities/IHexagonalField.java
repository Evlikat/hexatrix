/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.evlikat.hexatrix.entities;

import net.evlikat.hexatrix.axial.MoveDirection;
import net.evlikat.hexatrix.axial.RotateDirection;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 04, 2014)
 */
public interface IHexagonalField {

    boolean tick();

    boolean turn(RotateDirection direction);

    boolean move(MoveDirection direction);
    
    void drop();
}
