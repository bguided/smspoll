package com.novoda.util;

import junit.framework.TestCase;

public class StringsTest extends TestCase{

	public void testGetChoice() throws Exception {
		assertEquals("4", Strings.getChoice("#4"));
		assertEquals("4", Strings.getChoice("blah blah blah #4 sadhasdhjk jakhdkjahs"));
		assertEquals("4", Strings.getChoice("blah blah blah #4 sadhasdhjk jakhdkjahs"));
		assertEquals("4", Strings.getChoice("blah blah blah sdfksdfdsfsk#4"));
		assertEquals("7", Strings.getChoice("bl3ah bah blda45h sdfksdfdsfsk #  hhghghbhb 7 #45"));
		assertEquals("8", Strings.getChoice("bl3a# 8h bah blda45h sdfksdfdsfsk #  hhghghbhb 7 #45"));
	}
}
