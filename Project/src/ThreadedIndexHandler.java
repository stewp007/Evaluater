import java.io.IOException;
import java.nio.file.Path;

import opennlp.tools.stemmer.snowball.SnowballStemmer;

/**
 * Helper Class for CS 212 Projects
 * 
 * @author stewartpowell
 *
 */
public class ThreadedIndexHandler extends IndexHandler {

    /** The Thread-safe invertedindex */
    private final ThreadedInvertedIndex index;
    /** The WorkQueue used for this class */
    private WorkQueue queue; // TODO final
    /** The default stemmer algorithm used by this class. */
    public static final SnowballStemmer.ALGORITHM DEFAULT = SnowballStemmer.ALGORITHM.ENGLISH;

    /**
     * Constructor for FileHandler Class
     * 
     * @param index the InvertedIndex associated with the FileHandler
     * @param queue the work queue used to delegate tasks
     */
    public ThreadedIndexHandler(ThreadedInvertedIndex index, WorkQueue queue) {
        super(index);
        this.index = index;
        this.queue = queue;
    }

    /**
     * Searches through files starting at the given Path
     * 
     * @param path the path of the file to handle
     * @throws IOException throws if there is an issue opening the file
     */
    @Override
    public void handleFiles(Path path) throws IOException {
        super.handleFiles(path);
        queue.finish();
    }

    /**
     * Adds the contents of a file to an InvertedIndex
     * 
     * @param path the path to collect into the Index
     * @throws IOException throws an IOException
     */
    @Override
    public void handleIndex(Path path) throws IOException {
        // TODO queue.execute(new IndexBuilder(path));
        queue.execute(new IndexBuilder(path, this.index));
    }

    /**
     * Runnable Object used for building an index
     * 
     * @author stewartpowell
     *
     */
    private class IndexBuilder implements Runnable {
        /** The path used for building */
        private final Path path;

        /** The Thread safe InvertedIndex */
        private final ThreadedInvertedIndex index; // TODO Remove, can access index in parent class already!

        /**
         * Constructer for IndexBuilder
         * 
         * @param path  the path used for building
         * @param index the ThreadedInvertedIndex
         */
        public IndexBuilder(Path path, ThreadedInvertedIndex index) {
            this.path = path;
            this.index = index;
        }

        @Override
        public void run() {
            InvertedIndex local = new InvertedIndex();
            try {
                handleIndex(path, local);
            } catch (IOException e) {
                System.out.println("Unable to Handle this Index");
            }
            index.addAll(local);
        }
    }
}
