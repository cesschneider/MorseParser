package com.salesforce.sfdc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Parser {

	public String bestMatchWord;
    public int bestMatchScore;
	public static final boolean DEBUG = MorseParser.DEBUG;
	public static void dm(String m) { if (Parser.DEBUG) System.out.println("Parser: "+ m); }
	private Map<String,String> morseMap = new HashMap<String,String>();
	
	Parser(Map<String,String> map) {
		morseMap.putAll(map);
	}
	
	public String decodeWord(String encodedWord) {
        String encodedChar, decodedChar = new String(), decodedWord = new String();
        String[] encodedChars = encodedWord.split(" ");

        for (int i = 0; i < encodedChars.length; i++) {
        	encodedChar = encodedChars[i];
        	
        	// Looking for a valid Morse code
    		decodedChar = morseMap.get(encodedChar.toString());
    		dm("decoding character: "+ decodedChar +" <== "+ encodedChar);
    		
    		decodedWord += (decodedChar != null) ? decodedChar : "?";
        }
        
        return decodedWord;
	}
	
	public float compareWith(List<String> contextList, String decodedWord) {
        int matchScore;
        bestMatchScore = -1;
    	bestMatchWord = null;
        
    	try {

    		// Iterate through context to find closest string match
            for (String contextWord : contextList) {
            	matchScore = contextWord.compareTo(decodedWord);
            	matchScore = (matchScore < 0) ? matchScore * -1 : matchScore;
            	
            	if (bestMatchScore == -1 || matchScore < bestMatchScore) {
            		bestMatchScore = matchScore;
            		bestMatchWord = contextWord;
            	}
            	
            	dm("comparing with context word: "+ contextWord +" ("+ matchScore +")");
            }

    	} catch (Exception e) {
    		System.out.println(e.toString());
    	}

        return bestMatchScore;
	}
	
	static public String cleanLine(String line) {
		if (line != null)
			line = line.replaceAll("(\\s+)"," ").trim();
		return line;
	}
}

