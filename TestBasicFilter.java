package junit;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

//import bayes.classifier.IClassifier;
//import bayes.classifier.sol.SpamClassifier;
import bayes.classifier.impl.SpamClassifier;


public class TestBasicFilter
{
    SpamClassifier f;
    public static final double EPSILON=0.001;
    
    @Before
    public void setup() throws Exception {
        // This method is like a constructor
        // It is run before every test case.
        f=new SpamClassifier();
        f.addAllHamFilesInDirectory(new File("docs/basictest/ham"));
        f.addAllSpamFilesInDirectory(new File("docs/basictest/spam"));
    }
     
    @Test
    public void testThreshold() throws Exception
    {
        f.setThreshold(0.5);
        assertEquals(0.5, f.getThreshold(), EPSILON);
    }
    
    @Test
    public void testBasicStatsTest1() throws Exception
    {
        // Make sure that we are getting both messages
        assertEquals(2, f.getNumHamMessages());
        assertEquals(3, f.getNumSpamMessages());
    }
    
    @Test
    public void testSpamWords() throws Exception
    {
        // ensure we have the right number of spam words
        assertEquals(26, f.getAllSpamWords().size());
        
        // check the counts for each word
        assertEquals(1, f.getNumSpamOccurrences("replica"));
        assertEquals(1, f.getNumSpamOccurrences("buy"));
        assertEquals(1, f.getNumSpamOccurrences("foo"));
        assertEquals(1, f.getNumSpamOccurrences("available"));
        assertEquals(2, f.getNumSpamOccurrences("your"));
        assertEquals(1, f.getNumSpamOccurrences("Nigerian"));
        assertEquals(1, f.getNumSpamOccurrences("password"));
        assertEquals(1, f.getNumSpamOccurrences("oil"));
        assertEquals(1, f.getNumSpamOccurrences("ham"));
        assertEquals(1, f.getNumSpamOccurrences("me"));
        assertEquals(2, f.getNumSpamOccurrences("enter"));
        assertEquals(1, f.getNumSpamOccurrences("prices"));
        assertEquals(1, f.getNumSpamOccurrences("credit"));
        assertEquals(1, f.getNumSpamOccurrences("totally"));
        assertEquals(1, f.getNumSpamOccurrences("on"));
        assertEquals(1, f.getNumSpamOccurrences("watches"));
        assertEquals(1, f.getNumSpamOccurrences("a"));
        assertEquals(2, f.getNumSpamOccurrences("please"));
        assertEquals(1, f.getNumSpamOccurrences("sandwich"));
        assertEquals(1, f.getNumSpamOccurrences("message"));
        assertEquals(1, f.getNumSpamOccurrences("great"));
        assertEquals(1, f.getNumSpamOccurrences("money"));
        assertEquals(1, f.getNumSpamOccurrences("information"));
        assertEquals(1, f.getNumSpamOccurrences("legitimate"));
        assertEquals(1, f.getNumSpamOccurrences("send"));
        assertEquals(1, f.getNumSpamOccurrences("card"));
    }
    
    @Test
    public void testHamWords() throws Exception
    {
        // check total words
        assertEquals(14, f.getAllHamWords().size());
        
        // check counts for each individual word
        assertEquals(1, f.getNumHamOccurrences("sentence"));
        assertEquals(2, f.getNumHamOccurrences("a"));
        assertEquals(1, f.getNumHamOccurrences("foo"));
        assertEquals(1, f.getNumHamOccurrences("is"));
        assertEquals(1, f.getNumHamOccurrences("please"));
        assertEquals(1, f.getNumHamOccurrences("sandwich"));
        assertEquals(1, f.getNumHamOccurrences("message"));
        assertEquals(1, f.getNumHamOccurrences("great"));
        assertEquals(1, f.getNumHamOccurrences("bar"));
        assertEquals(1, f.getNumHamOccurrences("ham"));
        assertEquals(1, f.getNumHamOccurrences("me"));
        assertEquals(1, f.getNumHamOccurrences("legitimate"));
        assertEquals(1, f.getNumHamOccurrences("totally"));
        assertEquals(1, f.getNumHamOccurrences("send"));
    }
    
    @Test
    public void testSpamOnly() {
        // these words only occur as spam
        assertEquals(0, f.getNumHamOccurrences("replica"));
        assertEquals(0, f.getNumHamOccurrences("buy"));
        assertEquals(0, f.getNumHamOccurrences("available"));
        assertEquals(0, f.getNumHamOccurrences("your"));
        assertEquals(0, f.getNumHamOccurrences("Nigerian"));
        assertEquals(0, f.getNumHamOccurrences("password"));
        assertEquals(0, f.getNumHamOccurrences("oil"));
        assertEquals(0, f.getNumHamOccurrences("enter"));
        assertEquals(0, f.getNumHamOccurrences("prices"));
        assertEquals(0, f.getNumHamOccurrences("credit"));
        assertEquals(0, f.getNumHamOccurrences("on"));
        assertEquals(0, f.getNumHamOccurrences("watches"));
        assertEquals(0, f.getNumHamOccurrences("money"));
        assertEquals(0, f.getNumHamOccurrences("information"));
        assertEquals(0, f.getNumHamOccurrences("card"));
    }
    
    @Test
    public void testHamOnly() {
        assertEquals(0, f.getNumSpamOccurrences("sentence"));
        assertEquals(0, f.getNumSpamOccurrences("is"));
        assertEquals(0, f.getNumSpamOccurrences("bar"));
    }
    
    @Test
    public void testWordsFromBoth() throws Exception
    {
        assertEquals(0.333333, f.probSpamGivenWord("a"), EPSILON);
        assertEquals(0.500000, f.probSpamGivenWord("foo"), EPSILON);
        assertEquals(0.666667, f.probSpamGivenWord("please"), EPSILON);
        assertEquals(0.500000, f.probSpamGivenWord("sandwich"), EPSILON);
        assertEquals(0.500000, f.probSpamGivenWord("message"), EPSILON);
        assertEquals(0.500000, f.probSpamGivenWord("great"), EPSILON);
        assertEquals(0.500000, f.probSpamGivenWord("ham"), EPSILON);
        assertEquals(0.500000, f.probSpamGivenWord("me"), EPSILON);
        assertEquals(0.500000, f.probSpamGivenWord("legitimate"), EPSILON);
        assertEquals(0.500000, f.probSpamGivenWord("totally"), EPSILON);
        assertEquals(0.500000, f.probSpamGivenWord("send"), EPSILON);
    }
    
    @Test
    public void testHamOnlyProbability() throws Exception
    {
        assertEquals(0.000000, f.probSpamGivenWord("sentence"), EPSILON);
        assertEquals(0.000000, f.probSpamGivenWord("is"), EPSILON);
        assertEquals(0.000000, f.probSpamGivenWord("bar"), EPSILON);
    }
    
    @Test
    public void testSpamOnlyProbability() throws Exception
    {
        assertEquals(1.000000, f.probSpamGivenWord("replica"), EPSILON);
        assertEquals(1.000000, f.probSpamGivenWord("buy"), EPSILON);
        assertEquals(1.000000, f.probSpamGivenWord("available"), EPSILON);
        assertEquals(1.000000, f.probSpamGivenWord("your"), EPSILON);
        assertEquals(1.000000, f.probSpamGivenWord("Nigerian"), EPSILON);
        assertEquals(1.000000, f.probSpamGivenWord("password"), EPSILON);
        assertEquals(1.000000, f.probSpamGivenWord("oil"), EPSILON);
        assertEquals(1.000000, f.probSpamGivenWord("enter"), EPSILON);
        assertEquals(1.000000, f.probSpamGivenWord("prices"), EPSILON);
        assertEquals(1.000000, f.probSpamGivenWord("credit"), EPSILON);
        assertEquals(1.000000, f.probSpamGivenWord("on"), EPSILON);
        assertEquals(1.000000, f.probSpamGivenWord("watches"), EPSILON);
        assertEquals(1.000000, f.probSpamGivenWord("money"), EPSILON);
        assertEquals(1.000000, f.probSpamGivenWord("information"), EPSILON);
        assertEquals(1.000000, f.probSpamGivenWord("card"), EPSILON);
    }
    
    @Test
    public void testPleaseMessageMe() throws Exception
    {
        double p=f.probSpamForMessage(stringToInputStream("please message me"));
        assertEquals(0.66666666, p, EPSILON);
    }

    @Test
    public void testPleaseMessageMeALegitimateFoo() throws Exception
    {
        double p=f.probSpamForMessage(stringToInputStream("please message me a legitimate foo"));
        assertEquals(0.5, p, EPSILON);
    }
    
    @Test
    public void testPleasePleasePlease() throws Exception
    {
        double p=f.probSpamForMessage(stringToInputStream("please please please send me legitimate ham message"));
        assertEquals(0.666666, p, EPSILON);
    }
    
    @Test
    public void testUnknownWord() throws Exception
    {
        // make sure that 0.0 is the likelihood that a word we've never seen occurs
        assertEquals(0.0, f.probWord("thisisnotarealword"), EPSILON);
        // now make sure that we return null when we get a word we've never seen
        assertEquals(null, f.probSpamGivenWord("thisisnotarealword"));
    }
    
    @Test
    public void testOnlySpamDoesNotReturn1() throws Exception
    {
        // money only occurs in spam, so this should be 0.95, or something close to 1
        double p=f.probSpamForMessage(stringToInputStream("totally legitimate money"));
        assertEquals(0.95, p, EPSILON);
    }
    
    @Test
    public void testOnlyHamDoesNotReturn0() throws Exception
    {
        // sentence only occurs in ham, so this should be 0.05, or something close to 0
        double p=f.probSpamForMessage(stringToInputStream("totally legitimate sentence"));
        assertEquals(0.05, p, EPSILON);
    }
    
    @Test
    public void testSkipUnknownWords() throws Exception
    {
        // sentence only occurs in ham, so this should be 0.05, or something close to 0
        double p=f.probSpamForMessage(stringToInputStream("totally legitimate thisisnotarealword asdfasdf fasdfasdfasdf"));
        assertEquals(0.5, p, EPSILON);
    }
    
    private static InputStream stringToInputStream(String message) {
        return new ByteArrayInputStream(message.getBytes());
    }
    
  @Test
  public void testcheck() throws Exception
  {
      for (String s : new String[] {"please","buy","great","replica","foo"}) {
          double p1=f.probSpamGivenWord(s);
          System.out.println("P(S|"+s+")="+p1);
      }
  }
  @Test
  public void testBoth() {
      for (String w : f.getAllHamWords()){
          if (f.getAllSpamWords().contains(w)){
              System.out.println(w);
          }
      }
  }
    
    @Test
        public void maketest() {
        for (String word : f.getAllHamWords()){
            System.out.printf("assertEquals(%d, f.getNumHamOccurrences(\"%s\"));\n", f.getNumHamOccurrences(word), word);
        }
        
        for (String word : f.getAllSpamWords()){
            System.out.printf("assertEquals(%d, f.getNumSpamOccurrences(\"%s\"));\n", f.getNumSpamOccurrences(word), word);
        }
    }

    @Test
    public void makeTestCases() throws Exception {

        for (String word : f.getAllHamWords()){
            System.out.printf("assertTrue(hamWords.contains(\"%s\"));\n", word);
        }


        for (String word : f.getAllSpamWords()){
            System.out.printf("assertTrue(spamWords.contains(\"%s\"));\n", word);
        }

        for (String word : f.getAllHamWords()){
            if (f.getAllSpamWords().contains(word)){
                System.out.printf("assertEquals(%f, f.probSpamGivenWord(\"%s\"), EPSILON);\n", f.probSpamGivenWord(word), word);
            }
        }
        for (String w : f.getAllSpamWords()){
            if (!f.getAllHamWords().contains(w)){
                Double p = f.probSpamGivenWord(w);
                if (p == null){
                    System.out.printf("FOO assertEquals(%f, f.probSpamGivenWord(\"%s\"), EPSILON);\n", 0.0, w);
                } else {
                    System.out.printf("assertEquals(%f, f.probSpamGivenWord(\"%s\"), EPSILON);\n", p, w);
                }
            }
        }
        for (String w : f.getAllHamWords()){
            if (!f.getAllSpamWords().contains(w)){
                Double p = f.probSpamGivenWord(w);
                if (p == null){
                    System.out.printf("assertEquals(%f, f.probSpamGivenWord(\"%s\"), EPSILON);\n", 0.0, w);
                } else {
                    System.out.printf("assertEquals(%f, f.probSpamGivenWord(\"%s\"), EPSILON);\n", p, w);
                }
            }
        }
        for (String word : f.getAllSpamWords()){
            if (!f.getAllHamWords().contains(word)){
                System.out.printf("assertEquals(%d, f.getNumHamOccurrences(\"%s\"));\n", f.getNumHamOccurrences(word), word);
            }
        }
        System.out.println();
        
        for (String word : f.getAllHamWords()){
            if (!f.getAllSpamWords().contains(word)){
                System.out.printf("assertEquals(%d, f.getNumSpamOccurrences(\"%s\"));\n", f.getNumSpamOccurrences(word), word);
            }
        }
    }
}
