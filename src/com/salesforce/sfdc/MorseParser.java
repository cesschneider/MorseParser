/*
 * Morse Code parser
 * 
 * Reads two input files with Morse-encoded messages (one message per line) 
 * based on examples found at http://en.wikipedia.org/wiki/Morse_code and
 * context words for string matching.
 * 
 * Implementation based on American code.
 * 
 * @author Cesar Schneider <cesschneider@gmail.com>
 * @version 0.2
 */
package com.salesforce.sfdc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MorseParser {
	
	public static final boolean DEBUG = !true;
	public static void dm(String m) { if (MorseParser.DEBUG) System.out.println("MorseParser: "+ m); }
	public static void main(String[] args) {
		
		Map<String,String> morseMap = new HashMap<String,String>();
		List<String> context = new ArrayList<String>();
		String decodedWord, contextWordsFile, encodedWordsFile;
		
		contextWordsFile = new String("context-words.txt");
		encodedWordsFile = new String("encoded-words.txt");
		
		/*
		if (args.length < 2) {
			System.out.println("Usage: java MorseParser context-words.txt encoded-words.txt");
			return;
		}
		*/
				
	    // Parse file with Morse code mappings
		try {
		    BufferedReader br = new BufferedReader(new FileReader("morse-codes.tsv"));
	        String line = br.readLine();

	        // Store Morse codes mapping
	        while (line != null) {
	            line = Parser.cleanLine(line);
	            String[] codeMap = line.split(" ");
	            morseMap.put(codeMap[1], codeMap[0]);
	            line = br.readLine();
	        }
	        
			dm("morseMap: "+ morseMap.toString());
	        br.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		

		// Parse file with context words to match
		try {
		    BufferedReader br = new BufferedReader(new FileReader(contextWordsFile));
	        String line = br.readLine();

	        while (line != null) {
	        	context.add(Parser.cleanLine(line));
	        	line = br.readLine();
	        }
	        
			dm("context: "+ context.toString());
	        br.close();

		} catch (Exception e) {
			System.out.println("Couldn't open context words file. ("+ e.toString() +")");
		}
	        
	    // Parse file with encoded words
		try {
		    BufferedReader br = new BufferedReader(new FileReader(encodedWordsFile));
        	String encodedWord = Parser.cleanLine(br.readLine());
	        Parser p = new Parser(morseMap);
	        float matchScore = 0;

	        // Parse codes and compare with context list
	        while (encodedWord != null) {

	    		System.out.println("--");
	    		System.out.println("Encoded message: "+ encodedWord.toString());
	    		decodedWord = p.decodeWord(encodedWord);

	    		System.out.println("Decoded message: "+ decodedWord.toString());

	        	//matchScore = Arrays.binarySearch(context.toArray(), encodedWord);
	        	matchScore = p.compareWith(context, decodedWord);

	        	if (matchScore < 5) {
		    		System.out.println("Best guess using given context: "+ p.bestMatchWord +" ("+ p.bestMatchScore +")");
		        } else {
		        	System.out.println("Couldn't find relevant results using given context");
		        }
	        	
	        	encodedWord = Parser.cleanLine(br.readLine());
	        }

	        br.close();
	        
		} catch (Exception e) {
			System.out.println(e.toString());
	    }
		
	}
}

