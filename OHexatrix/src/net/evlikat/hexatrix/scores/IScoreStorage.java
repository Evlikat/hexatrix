package net.evlikat.hexatrix.scores;

import java.util.List;
import net.evlikat.hexatrix.views.GameResults;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 17, 2014)
 */
public interface IScoreStorage {

    void save(GameResults newResult);

    List<Score> getTopScores();
}
