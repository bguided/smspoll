package com.novoda.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Strings {

	private static final String	ANSWER_PATTERN	= "#\\D*(\\d+)";

	public static String getChoice(String str) {
		Matcher matcher = Pattern.compile(ANSWER_PATTERN).matcher(str);
		
		String answer = null;
		if (str.contains("#")) {
			
			int mIdx = 0;
		    matcher.find();

	    	boolean foundHashWithFollowingNo = matcher.groupCount() > 0;
			if(foundHashWithFollowingNo){
//	    		for( int groupIdx = 0; groupIdx < matcher.groupCount()+1; groupIdx++ ){
//	    			System.out.println( "[" + mIdx + "][" + groupIdx + "] = " + matcher.group(groupIdx));
//	    		}
//	    		mIdx++;
				answer = matcher.group(1);
			}
		}

		return answer;
	}
}
