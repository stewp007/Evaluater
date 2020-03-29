import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

// TODO Clean up all the commented out code 

/**
 * This is the InvertedIndex data structure used for USF CS212 Project 1
 *
 * @author stewartpowell
 */
public class InvertedIndex {

    /**
     * the data structure to be used
     */
    private final TreeMap<String, TreeMap<String, TreeSet<Integer>>> invertedIndex;
    // private TreeMap<String, WordIndex> invertedIndex;

    /**
     * Instantiates the InvertedIndex object
     */
    public InvertedIndex() {
        this.invertedIndex = new TreeMap<String, TreeMap<String, TreeSet<Integer>>>();
    }

    /**
     * Adds the word, path, and position to the index
     * 
     * @param word     the word to be added
     * @param path     the path being added
     * @param position the position in the file the word was found in
     * 
     * @return boolean if the Index was changed
     */
    public boolean add(String word, String path, int position) {
        invertedIndex.putIfAbsent(word, new TreeMap<>());
        invertedIndex.get(word).putIfAbsent(path, new TreeSet<>());
        return invertedIndex.get(word).get(path).add(position);

    }

    /**
     * returns an unmodifiable collection of the keys of the Index
     * 
     * @return Collection<String>
     */
    public Collection<String> getWords() {
        return Collections.unmodifiableSet(invertedIndex.keySet());
    }

    /*
     * TODO This is still breaking encapsulation---no safe way to return
     * nested data structures. Remove.
     */
    /**
     * returns an Unmodifiable Map associated with the InvertedIndex
     * 
     * @param word the word to get value from
     * @return Map<String, TreeSet<Integer>> the map associated with the
     *         InvertedIndex
     */
    public Map<String, TreeSet<Integer>> getMap(String word) {
        return Collections.unmodifiableMap(invertedIndex.get(word));
    }

    /**
     * returns an Unmodifiable Set of the locations associated with a given word
     * associated with the InvertedIndex
     * 
     * @param word the word to get locations from
     * @return Set<String> the set of locations associated with the given word
     */
    public Set<String> getLocations(String word) {
      /*
       * TODO This code throws a null pointer exception if the word does not exist. 
       * 
       * if the word is in your index, return what you have.
       * otherwise, return Collections.emptySet()
       */
        return Collections.unmodifiableSet(invertedIndex.get(word).keySet());
    }

    /**
     * returns an Unmodifiable Map associated with the InvertedIndex
     * 
     * @param word     the word to get value from
     * @param location the location to find the positions from
     * @return Set<Integer> the set of positions associated with the given word and
     *         location
     */
    public Set<Integer> getPositions(String word, String location) {
      // TODO Same issue as getLocations
        return Collections.unmodifiableSet(invertedIndex.get(word).get(location));
    }

    /**
     * Checks if the inverted index contains the specified location
     * 
     * @param word     the word associated with the location
     * @param location the location we are looking for
     * @return boolean
     */
    public boolean containsLocation(String word, String location) {
        return invertedIndex.containsKey(word) ? invertedIndex.get(word).get(location) != null : false;
    }

    /**
     * Checks if the inverted index contains the specified position
     * 
     * @param word     the word associated with the location
     * @param location the location association the position
     * @param position the position we are checking for
     * @return boolean
     */
    public boolean containsPosition(String word, String location, Integer position) {
        return this.containsLocation(word, location) ? this.getMap(word).get(location).contains(position) : false;
    }

    /**
     * Return the number of words in the InvertedIndex
     * 
     * @return int
     */
    public int numWords() {
        return invertedIndex.size();
    }

    /**
     * Return the number of locations associated with the word
     * 
     * @param word the word to get the number of locations from
     * @return int
     */
    public int numLocations(String word) {
        return invertedIndex.containsKey(word) ? invertedIndex.get(word).size() : 0;
    }

    /**
     * Return the number of words in the InvertedIndex
     * 
     * @param word     the word associated with the location
     * @param location the location to get the number of positions from
     * @return int
     */
    public int numPositions(String word, String location) {
        return this.containsLocation(word, location) ? invertedIndex.get(word).get(location).size() : 0;
    }

    /**
     * Gets the pretty Json format of the Index
     * 
     * @param path the path to output the Json Index
     * @throws IOException throws IOException
     */
    public void getIndex(Path path) throws IOException {
        SimpleJsonWriter.indexToJsonFile(this.invertedIndex, path);
    }

    @Override
    public String toString() {
        return invertedIndex.toString();
    }

}
