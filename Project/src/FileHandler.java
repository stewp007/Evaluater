import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.stemmer.snowball.SnowballStemmer;

/**
 * Helper Class for CS 212 Projects
 * 
 * @author stewartpowell
 *
 */
public class FileHandler {

    /**
     * Class Member to reference the index
     */

    private final InvertedIndex index;

    /** The default stemmer algorithm used by this class. */
    public static final SnowballStemmer.ALGORITHM DEFAULT = SnowballStemmer.ALGORITHM.ENGLISH;

    /**
     * Constructor for FileHandler Class
     * 
     * @param index the InvertedIndex associated with the FileHandler
     */
    public FileHandler(InvertedIndex index) {
        this.index = index;
    }

    /**
     * Searches through files starting at the given Path
     * 
     * @param path the path of the file to handle
     * @throws IOException throws if there is an issue opening the file
     */
    public void handleFiles(Path path) throws IOException {
        List<Path> listPaths = TextFileFinder.list(path);
        for (Path filePath : listPaths) {
            handleIndex(filePath);
        }
    }

    /**
     * 
     * Adds the contents of a file to the Index
     * 
     * @param path the path to collect into the Index
     * 
     * @return boolean
     * @throws IOException Throws if there is an issue opening the file
     */
    public boolean handleIndex(Path path) throws IOException {
        int filePosition = 0;
        int linePosition = 0;
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            SnowballStemmer stemmer = new SnowballStemmer(DEFAULT);
            while ((line = reader.readLine()) != null) {
                ArrayList<String> allStems = TextFileStemmer.listStems(line, stemmer);
                linePosition = 0;
                for (String word : allStems) {
                    linePosition++;
                    this.index.add(word, path.toString(), filePosition + linePosition);
                }
                filePosition += allStems.size();
            }
        }
        return true;
    }

    /**
     * handles exceptions for getting the Json form of the index
     * 
     * @param index the invertedIndex to be turned into json form
     * @param path  the output file
     */
    public void getIndexJson(InvertedIndex index, Path path) {
        try {
            index.getIndex(path);
        } catch (IOException e) {
            System.out.println("Error retrieving Json form of the Index.");
        }
    }
}
