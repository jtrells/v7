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

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;

import uk.co.q3c.v7.base.navigate.StrictURIFragmentHandler;
import uk.co.q3c.v7.base.navigate.URIFragmentHandler;
import uk.co.q3c.v7.i18n.I18NModule;
import uk.co.q3c.v7.i18n.LabelKey;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.mycila.testing.junit.MycilaJunitRunner;
import com.mycila.testing.plugin.guice.GuiceContext;
import com.mycila.testing.plugin.guice.ModuleProvider;

@RunWith(MycilaJunitRunner.class)
@GuiceContext({ SystemAccountManagementPages.class, DefaultStandardPagesModule.class, I18NModule.class })
public class SystemAccountManagementPagesTest {

	@Inject
	Map<String, DirectSitemapEntry> map;

	@Inject
	DefaultDirectSitemapLoader loader;

	@Inject
	Sitemap sitemap;

	@Test
	public void check() {

		// given

		// when
		loader.load();
		// then

		assertThat(sitemap.hasUri("system-account")).isTrue();
		assertThat(sitemap.hasUri("system-account/refresh-account")).isTrue();
		assertThat(sitemap.hasUri("system-account/unlock-account")).isTrue();
		assertThat(sitemap.hasUri("system-account/enable-account")).isTrue();
		assertThat(sitemap.hasUri("system-account/request-account")).isTrue();
		assertThat(sitemap.hasUri("system-account/reset-account")).isTrue();

		SitemapNode node = sitemap.nodeFor("system-account");
		assertThat(node.getLabelKey()).isEqualTo(LabelKey.System_Account);

	}

	@ModuleProvider
	protected AbstractModule moduleProvider() {
		return new AbstractModule() {

			@Override
			protected void configure() {
				bind(URIFragmentHandler.class).to(StrictURIFragmentHandler.class);

			}

		};
	}

}
