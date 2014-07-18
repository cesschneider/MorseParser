package com.salesforce.samples;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class MorseParser {

	private Map<String,String> morseMap = new HashMap<String,String>();
	
	MorseParser(Map<String,String> map) {
		morseMap.putAll(map);
	}
	
	public String decodeWord(String encodedWord) {
        String encodedChar, decodedChar = new String(), decodedWord = new String();
        String[] encodedChars = encodedWord.split(" ");
        
        for (int i = 0; i < encodedChars.length; i++) {
        	encodedChar = encodedChars[i];
            //System.out.println("Encoded char: "+ encodedChar);
        	
        	// Look for a valid Morse code
    		//decodedChar = morseMap.get(encodedChar.toString());

    		if (morseMap.containsKey(encodedChar)) {
        		decodedChar = morseMap.get(encodedChar.toString());
        	}
        	
        	decodedWord = decodedWord + decodedChar.toString();
        }
        
        System.out.println("Decoded word: "+ decodedWord);
        return decodedWord;
	}
	
	public int compareWith(List<String> context, String encodedWord) {
        Iterator<String> ci = context.iterator();
        String decodedWord = new String();
        
        decodedWord = decodeWord(encodedWord);
        
    	// Iterate through context to find closest string match
        while (ci.hasNext()) {

        	if (ci.next().equals(decodedWord)) {
                return 1;
        	} 

        }

        return 0;
	}
}

public class Parser {

	public static void main(String[] args) {
		
		Map<String,String> morseMap = new HashMap<String,String>();
		List<String> context = new ArrayList<String>();
		
	    // Parse input file
		try {
		    BufferedReader br = new BufferedReader(new FileReader("progc.dat"));
	        String line = br.readLine().replaceAll("\\s+"," ").trim();
            System.out.println("line: "+ line);

	        // Store Morse codes mapping
	        while (line != null && line.equals(new String("*")) == false) {
	            String[] codeMap = line.split(" ");
	        	morseMap.put(codeMap[1], codeMap[0]);
	            line = br.readLine().replaceAll("\\s+"," ").trim();
	        }
			System.out.println(morseMap.toString());
        	line = br.readLine().replaceAll("\\s+"," ").trim();
	        	        
	        // Store context words to match
	        while (line != null && line.equals(new String("*")) == false) {
	        	context.add(line);
	        	line = br.readLine().replaceAll("\\s+"," ").trim();
	        }
	        
	        int matchScore = 0;
    		MorseParser m = new MorseParser(morseMap);
        	line = br.readLine().replaceAll("\\s+"," ").trim();
	        
	        // Parse codes and compare with context list
	        while (line != null && line.equals(new String("*")) == false) {

	        	//matchScore = Arrays.binarySearch(context.toArray(), line);
	        	matchScore = m.compareWith(context, line.toString());

	        	if (matchScore > 0) {
		    		System.out.print("Found word ("+ matchScore*100 +"% match): "+ line.toString());
		        }

	        	line = br.readLine().replaceAll("\\s+"," ").trim();
	        }

	        br.close();
		} catch (Exception e) {
			System.out.println("Context file not found ("+ e.toString() +")");
	    }
		
	}
}

