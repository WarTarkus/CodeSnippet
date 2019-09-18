package bayes.classifier.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;


/**
 * For CS220 with Professor spacco by Duncan Finch
 * 
 * Bayesian spam filters based on this formulation of Bayes Thm:<p>

    Pr(S|W) =     &nbsp;&nbsp;
         Pr(W|S) * Pr(S)              <br>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
           --------------   <br>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                Pr(W)   <br>
 * 
 */
public class SpamClassifier
{
	//Instance variables here
	private Double numSpam=0.0;
	private Double numHam=0.0;
	private Map<String,Integer> spamMap=new HashMap<String,Integer>();
	private Map<String,Integer> hamMap=new HashMap<String,Integer>();
	private Double threshold=0.0;


	/**
	 * Add the spam file to be read from the given input stream
	 * to our dataset.
	 * @param in The inputstream from which to read the spam file.
	 */
	public void addSpamFile(InputStream in) throws IOException
	{
		Scanner scan=new Scanner(in);
		Set<String> wordsEntered=new HashSet<String>();

		while(scan.hasNext()) {
			String next=scan.next();
			//Check if word has already been entered from this doc
			if(wordsEntered.contains(next)) {
				continue;
			}
			//If it hasn't, add it into the map if its new or increase the map's count if its been in other docs
			else { 
				//Word already in map
				if(spamMap.containsKey(next)) {
					int count=spamMap.get(next);
					spamMap.put(next, count+1);
				}
				//Word is new to map
				else{
					spamMap.put(next, 1);
				}
				//Add this word to the words entered set
				wordsEntered.add(next);
			}
		}
		numSpam++;

	}

	/**
	 * Add the ham file to be read from the given input stream
	 * to our dataset.
	 * @param in The inputstream from which to read the ham file.
	 */
	public void addHamFile(InputStream in) throws IOException
	{
		Scanner scan=new Scanner(in);
		Set<String> wordsEntered=new HashSet<String>();

		while(scan.hasNext()) {
			String next=scan.next();
			//Check if word has already been entered from this doc
			if(wordsEntered.contains(next)) {
				continue;
			}
			//If it hasn't, add it into the map if its new or increase the map's count if its been in other docs
			else { 
				//Word already in map
				if(hamMap.containsKey(next)) {
					int count=hamMap.get(next);
					hamMap.put(next, count+1);
				}
				//Word is new to map
				else{
					hamMap.put(next, 1);
				}
				//Add this word to the words entered set
				wordsEntered.add(next);
			}
		}
		numHam++;
	}

	/**
	 * Add all the files in the given directory to
	 * our dataset as spam files.
	 * @param dir The directory from which to read the files
	 */
	public void addAllSpamFilesInDirectory(File dir) throws IOException
	{
		//Iterate through all files in directory
		for(File f : dir.listFiles()) {
			//Convert file into an input stream then toss into add method
			InputStream s=new FileInputStream(f);
			addSpamFile(s);
		}
	}

	/**
	 * Add all the files in the given directory to
	 * our dataset as ham files.
	 * @param dir The directory from which to read the files
	 */
	public void addAllHamFilesInDirectory(File dir) throws IOException
	{
		//Iterate through all files in directory
		for(File f : dir.listFiles()) {
			//Convert file into an input stream then toss into add method
			InputStream s=new FileInputStream(f);
			addHamFile(s);
		}
	}

	/**
	 * Get the number of spam messages in the data set.
	 * @return The number of spam messages in the data set.
	 */
	public int getNumSpamMessages()
	{
		return (int)(double) numSpam;
	}

	/**
	 * Get the number of ham messages in the data set.
	 * @return The number of ham messages in the data set.
	 */
	public int getNumHamMessages()
	{
		return (int)(double) numHam;
	}

	/**
	 * Return a set of all the words that occur in at least one spam message.
	 */
	public Set<String> getAllSpamWords()
	{
		Set<String> spamWords=spamMap.keySet();
		return spamWords;
	}

	/**
	 * Return a set of all the words that occur in at least one ham message.
	 */
	public Set<String> getAllHamWords()
	{
		Set<String> hamWords=hamMap.keySet();
		return hamWords;
	}

	/**
	 * Return a set of all the unique words the classifier has seen so far.
	 * @return A set of all of the unique words that the classifier has seen so far.
	 */
	public Set<String> getAllWords()
	{
		Set<String> spamWords=hamMap.keySet();
		Set<String> hamWords=hamMap.keySet();
		hamWords.addAll(spamWords);
		return hamWords;
	}

	/**
	 * Get the number of times the given word occurs in 
	 * the spam messages in the data set.
	 * @param word
	 * @return The number of occurrences of the given word
	 * in the spam messages in the data set.
	 */
	public int getNumSpamOccurrences(String word)
	{
		//Word is there
		if(spamMap.containsKey(word)) {
			return spamMap.get(word);
		}
		//Word isn't there
		else {
			return 0;
		}
	}

	/**
	 * Get the number of times the given word occurs in
	 * the ham messages in the data set.
	 * @param word
	 * @return The number of occurrences of the given word
	 * in the ham messages in the data set.
	 */
	public int getNumHamOccurrences(String word)
	{
		//Word is there
		if(hamMap.containsKey(word)) {
			return hamMap.get(word);
		}
		//Word isn't there
		else {
			return 0;
		}
	}

	/**
	 *  Set the threshold at which we classify a message as spam.
	 * @param t the threshold
	 */
	public void setThreshold(double t)
	{
		threshold=t;
	}

	/**
	 * Get the threshold at which we classify a message as spam.
	 */
	public double getThreshold()
	{
		return threshold;
	}

	/**
	 * Return the overall probability that a word occurs in any message,
	 * either spam or ham.
	 * @param word
	 * @return
	 */
	public double probWord(String word)
	{
		Double totalMessages=numHam+numSpam;
		Double wordOccurences=0.0;
		if(spamMap.containsKey(word)) {
			wordOccurences=wordOccurences+spamMap.get(word);
		}
		if(hamMap.containsKey(word)) {
			wordOccurences=wordOccurences+hamMap.get(word);
		}
		if(totalMessages==0) {
			totalMessages=null;
		}
		return wordOccurences/totalMessages;
	}

	/**
	 * Using the probabilities that we have trained into our dataset
	 * determine the score for the given message.
	 * @param in
	 * @return The score for the given file.
	 */
	public double probSpamForMessage(InputStream in)
	{
		Scanner scan=new Scanner(in);
		double totalSum=0;
		Double chance=0.0;
		double probability=0;
		Set<String> alreadyChecked=new HashSet<String>();
		while(scan.hasNext()) {
			String next=scan.next();
			if(alreadyChecked.contains(next)) {
				continue;
			}
			chance=probSpamGivenWord(next);
			//Set min/max values for probability
			if(chance==null) {
				continue;
			}
			if(chance>0.95) {
				chance=0.95;
			}
			if(chance<0.05) {
				chance=0.05;
			}
			totalSum+=(Math.log(1-chance)-Math.log(chance));
			alreadyChecked.add(next);
		}
		probability=(Math.abs(1/(Math.pow(Math.E, totalSum)+1)));
		return probability;
	}

	/**
	 * Compute the probability that a message is spam given that
	 * it contains the given word, using the corpus of messages
	 * 
	 * @param word The word to test.
	 * @return The probability (between 0.0 and 1.0) that a message is spam 
	 * given that it contains the given word, or null if the probability
	 * for the given word cannot be computed.
	 */
	public Double probSpamGivenWord(String word)
	{
		Double spamOccurences=0.0;
		Double totalOccurences=0.0;
		if(spamMap.containsKey(word)) {
			spamOccurences+=spamMap.get(word);
			totalOccurences+=spamMap.get(word);
		}
		if(hamMap.containsKey(word)) {
			totalOccurences+=hamMap.get(word);
		}
		if(totalOccurences==0) {
			totalOccurences=null;
			return null;
		}
		return (spamOccurences/totalOccurences);
	}

	/**
	 * Read a message from the given InputStream.
	 * Return true if the message is spam (i.e. its score
	 * is above the current threshold) and false otherwise.
	 * @param in
	 * @return True if the given message is spam; false if it's ham.
	 */
	public boolean isSpam(InputStream in)
	{
		double chance =probSpamForMessage(in);
		if(chance>threshold) {
			return true;
		}
		else {
			return false;
		}
	}

}
