package net.evlikat.hexatrix.scores;

import android.util.Log;
import net.evlikat.hexatrix.utils.IOUtils;
import net.evlikat.hexatrix.views.GameResults;

import java.io.*;
import java.util.*;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 17, 2014)
 */
public class Leaderboard implements IScoreStorage {

    private static final String TAG = Leaderboard.class.getSimpleName();

    private List<Score> topScores = new ArrayList<>();
    private final int topSize;
    private final String filename;

    public Leaderboard(String filename, int topSize) {
        this.filename = filename;
        this.topSize = topSize;
        this.topScores = load(filename);
    }

    public void save(GameResults gameResults) {
        if (!topScores.isEmpty() && topScores.get(topScores.size() - 1).getAmount() > gameResults.getScore()) {
            // there is no need to save not top result
            return;
        }
        topScores.add(new Score(new Date(), gameResults.getScore()));
        Collections.sort(topScores, new Comparator<Score>() {

            public int compare(Score t1, Score t2) {
                return (int) Math.signum(t2.getAmount() - t1.getAmount());
            }
        });
        topScores = new ArrayList<>(topScores.subList(0, Math.min(topScores.size(), topSize)));
        saveToFile(this.filename);
    }

    public List<Score> getTopScores() {
        return topScores;
    }

    private void saveToFile(String filename) {
        ObjectOutputStream out = null;
        try {
            final File file = new File(filename);
            if (!file.exists()) {
                new File(file.getParent()).mkdirs();
            }
            out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(topScores);
        } catch (FileNotFoundException ex) {
            Log.e(TAG, "File not found: " + filename, ex);
        } catch (IOException ex) {
            Log.e(TAG, "Can not save the leaderboard to file " + filename, ex);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    private List<Score> load(String filename) {
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(new File(filename)));
            return (List<Score>) in.readObject();
        } catch (ClassNotFoundException | FileNotFoundException ex) {
            Log.e(TAG, "File not found: " + filename, ex);
        } catch (IOException ex) {
            Log.e(TAG, "Can not load the leaderboard from file " + filename, ex);
        } finally {
            IOUtils.closeQuietly(in);
        }
        return new ArrayList<>();
    }
}
