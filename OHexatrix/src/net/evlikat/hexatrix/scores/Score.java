package net.evlikat.hexatrix.scores;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 17, 2014)
 */
public class Score implements Serializable {

    private final Date date;
    private final int amount;

    public Score(Date date, int amount) {
        this.date = date;
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public int getAmount() {
        return amount;
    }
}
