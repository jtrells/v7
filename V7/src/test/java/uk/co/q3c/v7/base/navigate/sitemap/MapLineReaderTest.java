/*
 * Copyright (C) 2013 David Sowerby
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package uk.co.q3c.v7.base.navigate.sitemap;

import static org.assertj.core.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import uk.co.q3c.v7.base.shiro.PageAccessControl;

public class MapLineReaderTest {

	private MapLineReader reader;
	private Set<String> syntaxErrors;
	private Set<String> indentationErrors;

	@Before
	public void setup() {
		reader = new MapLineReader();
		syntaxErrors = new HashSet<>();
		indentationErrors = new HashSet<>();
	}

	@Test
	public void full_correct_Line() {

		// given
		String line = "--  level2   ; view  ; key  ; permission";
		// when
		MapLineRecord result = reader.processLine(33, line, syntaxErrors, indentationErrors, 1, ";");
		// then
		assertThat(result.getIndentLevel()).isEqualTo(2);
		assertThat(result.getSegment()).isEqualTo("level2");
		assertThat(result.getViewName()).isEqualTo("view");
		assertThat(result.getKeyName()).isEqualTo("key");
		assertThat(result.getRoles()).isEqualTo("permission");
		assertThat(result.getPageAccessControl()).isEqualTo(PageAccessControl.PERMISSION);

		// given
		line = "+-  level2   ; view  ; key  ; permission";
		// when
		result = reader.processLine(33, line, syntaxErrors, indentationErrors, 1, ";");
		// then
		assertThat(result.getIndentLevel()).isEqualTo(2);
		assertThat(result.getSegment()).isEqualTo("level2");
		assertThat(result.getViewName()).isEqualTo("view");
		assertThat(result.getKeyName()).isEqualTo("key");
		assertThat(result.getRoles()).isEqualTo("permission");
		assertThat(result.getPageAccessControl()).isEqualTo(PageAccessControl.PUBLIC);

	}

	@Test
	public void full_correct_Line_leading_spaces() {

		// given
		String line = "  --  level2   ; view  ; key  ; permission";
		// when
		MapLineRecord result = reader.processLine(33, line, syntaxErrors, indentationErrors, 1, ";");
		// then
		assertThat(result.getIndentLevel()).isEqualTo(2);
		assertThat(result.getSegment()).isEqualTo("level2");
		assertThat(result.getViewName()).isEqualTo("view");
		assertThat(result.getKeyName()).isEqualTo("key");
		assertThat(result.getRoles()).isEqualTo("permission");
		assertThat(result.getPageAccessControl()).isEqualTo(PageAccessControl.PERMISSION);

	}

	@Test
	public void allPlus() {

		// given
		String line = "  ++  level2   ; view  ; key  ; permission";
		// when
		MapLineRecord result = reader.processLine(33, line, syntaxErrors, indentationErrors, 1, ";");
		// then
		assertThat(result.getIndentLevel()).isEqualTo(2);
		assertThat(result.getSegment()).isEqualTo("level2");
		assertThat(result.getViewName()).isEqualTo("view");
		assertThat(result.getKeyName()).isEqualTo("key");
		assertThat(result.getRoles()).isEqualTo("permission");
		assertThat(result.getPageAccessControl()).isEqualTo(PageAccessControl.PUBLIC);

	}

	@Test
	public void emptyViewOnly() {

		// given
		String line = "  ++  level2   ;   ; key  ; permission";
		// when
		MapLineRecord result = reader.processLine(33, line, syntaxErrors, indentationErrors, 1, ";");
		// then
		assertThat(result.getIndentLevel()).isEqualTo(2);
		assertThat(result.getSegment()).isEqualTo("level2");
		assertThat(result.getViewName()).isEmpty();
		assertThat(result.getKeyName()).isEqualTo("key");
		assertThat(result.getRoles()).isEqualTo("permission");
		assertThat(result.getPageAccessControl()).isEqualTo(PageAccessControl.PUBLIC);

	}

	@Test
	public void emptyKeyOnly() {

		// given
		String line = "  ++  level2   ; view  ;   ; permission";
		// when
		MapLineRecord result = reader.processLine(33, line, syntaxErrors, indentationErrors, 1, ";");
		// then
		assertThat(result.getIndentLevel()).isEqualTo(2);
		assertThat(result.getSegment()).isEqualTo("level2");
		assertThat(result.getViewName()).isEqualTo("view");
		assertThat(result.getKeyName()).isEmpty();
		assertThat(result.getRoles()).isEqualTo("permission");
		assertThat(result.getPageAccessControl()).isEqualTo(PageAccessControl.PUBLIC);
	}

	@Test
	public void correct_Line_no_key() {

		// given
		String line = "--  level2   ; view  ";
		// when
		MapLineRecord result = reader.processLine(33, line, syntaxErrors, indentationErrors, 1, ";");
		// then
		assertThat(result.getIndentLevel()).isEqualTo(2);
		assertThat(result.getSegment()).isEqualTo("level2");
		assertThat(result.getViewName()).isEqualTo("view");
		assertThat(result.getKeyName()).isEmpty();
		assertThat(result.getRoles()).isEmpty();
		assertThat(result.getPageAccessControl()).isEqualTo(PageAccessControl.PERMISSION);

	}

	@Test
	public void correct_Line_segment_only() {

		// given
		String line = " --  level2  ";
		// when
		MapLineRecord result = reader.processLine(33, line, syntaxErrors, indentationErrors, 1, ";");
		// then
		assertThat(result.getIndentLevel()).isEqualTo(2);
		assertThat(result.getSegment()).isEqualTo("level2");
		assertThat(result.getViewName()).isEmpty();
		assertThat(result.getKeyName()).isEmpty();
		assertThat(result.getRoles()).isEqualTo("");
		assertThat(result.getPageAccessControl()).isEqualTo(PageAccessControl.PERMISSION);

	}

	@Test
	public void invalid_no_hypen() {

		// given
		String line = "   level2   ; view  ; key";
		// when
		MapLineRecord result = reader.processLine(33, line, syntaxErrors, indentationErrors, 1, ";");
		// then
		assertThat(syntaxErrors).contains(MapLineReader.NO_HYPHEN + "33");
		assertThat(result.getIndentLevel()).isEqualTo(0);
		assertThat(result.getSegment()).isNull();
		assertThat(result.getViewName()).isNull();
		assertThat(result.getKeyName()).isNull();
		assertThat(result.getPageAccessControl()).isNull();

	}

	@Test
	public void user() {

		// given
		String line = "~-  level2   ; view  ; key";
		// when
		MapLineRecord result = reader.processLine(33, line, syntaxErrors, indentationErrors, 1, ";");
		// then
		assertThat(result.getIndentLevel()).isEqualTo(2);
		assertThat(result.getSegment()).isEqualTo("level2");
		assertThat(result.getViewName()).isEqualTo("view");
		assertThat(result.getKeyName()).isEqualTo("key");
		assertThat(result.getPageAccessControl()).isEqualTo(PageAccessControl.USER);

	}

	@Test
	public void guest() {

		// given
		String line = "#-  level2   ; view  ; key";
		// when
		MapLineRecord result = reader.processLine(33, line, syntaxErrors, indentationErrors, 1, ";");
		// then
		assertThat(result.getIndentLevel()).isEqualTo(2);
		assertThat(result.getSegment()).isEqualTo("level2");
		assertThat(result.getViewName()).isEqualTo("view");
		assertThat(result.getKeyName()).isEqualTo("key");
		assertThat(result.getPageAccessControl()).isEqualTo(PageAccessControl.GUEST);

	}
}
