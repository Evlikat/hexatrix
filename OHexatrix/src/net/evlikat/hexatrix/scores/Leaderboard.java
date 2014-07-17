package net.evlikat.hexatrix.scores;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import net.evlikat.hexatrix.utils.IOUtils;
import net.evlikat.hexatrix.views.GameResults;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 17, 2014)
 */
public class Leaderboard implements IScoreStorage {

    private static final String TAG = Leaderboard.class.getSimpleName();

    private List<Scores> topScores = new ArrayList<Scores>();
    private final int topSize;
    private final String filename;

    public Leaderboard(String filename, int topSize) {
        this.filename = filename;
        this.topSize = topSize;
        this.topScores = load(filename);
    }

    public void save(GameResults gameResults) {
        if (!topScores.isEmpty() && topScores.get(topScores.size() - 1).getAmount() > gameResults.getScores()) {
            // there is no need to save not top result
            return;
        }
        topScores.add(new Scores(new Date(), gameResults.getScores()));
        Collections.sort(topScores, new Comparator<Scores>() {

            public int compare(Scores t1, Scores t2) {
                return (int) Math.signum(t2.getAmount() - t1.getAmount());
            }
        });
        topScores = new ArrayList<Scores>(topScores.subList(0, Math.min(topScores.size(), topSize)));
        saveToFile(this.filename);
    }

    public List<Scores> getTopScores() {
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

    private List<Scores> load(String filename) {
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(new File(filename)));
            return (List<Scores>) in.readObject();
        } catch (ClassNotFoundException ex) {
            Log.e(TAG, "File not found: " + filename, ex);
        } catch (FileNotFoundException ex) {
            Log.e(TAG, "File not found: " + filename, ex);
        } catch (IOException ex) {
            Log.e(TAG, "Can not save the leaderboard to file " + filename, ex);
        } finally {
            IOUtils.closeQuietly(in);
        }
        return new ArrayList<Scores>();
    }
}
