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

import java.io.IOException;

import org.fest.assertions.Fail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.co.q3c.v7.base.navigate.StrictURIFragmentHandler;
import uk.co.q3c.v7.base.navigate.URIFragmentHandler;
import uk.co.q3c.v7.base.navigate.sitemap.DefaultFileSitemapLoaderTest2.TestFileSitemapModule;
import uk.co.q3c.v7.base.view.testviews.subview.MoneyInOutView;
import uk.co.q3c.v7.base.view.testviews.subview.TransferView;
import uk.co.q3c.v7.i18n.AnnotationI18NTranslator;
import uk.co.q3c.v7.i18n.I18NTranslator;
import uk.co.q3c.v7.i18n.TestLabelKey;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.mycila.testing.junit.MycilaJunitRunner;
import com.mycila.testing.plugin.guice.GuiceContext;
import com.mycila.testing.plugin.guice.ModuleProvider;

import fixture.testviews2.My_AccountView;
import fixture.testviews2.OptionsView;

/**
 * Tests {@link DefaultFileSitemapLoader} with multiple input files
 * 
 * 
 * 
 * @author dsowerby
 * 
 */
@RunWith(MycilaJunitRunner.class)
@GuiceContext({ TestFileSitemapModule.class })
public class DefaultFileSitemapLoaderTest2 {

	public static class TestFileSitemapModule extends FileSitemapModule {

		@Override
		protected void define() {
			addEntry("a", new SitemapFile("src/test/java/uk/co/q3c/v7/base/navigate/sitemap_good.properties"));
			addEntry("b", new SitemapFile("src/test/java/uk/co/q3c/v7/base/navigate/sitemap_good1.properties"));

		}

	}

	@Inject
	DefaultFileSitemapLoader loader;

	@Inject
	Sitemap sitemap;

	@Before
	public void setup() throws IOException {
	}

	@Test
	public void multipleInputFiles() {

		// given
		// when
		loader.load();
		StringBuilder report = loader.getReport();
		// then

		for (SitemapNode node : sitemap.getAllNodes()) {
			validateNode(node);
		}

		System.out.println(report);

		assertThat(report.toString())
				.contains(
						"parsing source from:		/home/david/git/v7/V7/src/test/java/uk/co/q3c/v7/base/navigate/sitemap_good1.properties");
		assertThat(report.toString())
				.contains(
						"parsing source from:		/home/david/git/v7/V7/src/test/java/uk/co/q3c/v7/base/navigate/sitemap_good.properties");

	}

	private void validateNode(SitemapNode node) {
		String uri = sitemap.uri(node);
		System.out.println("validating " + uri);
		switch (uri) {

		case "my-account":
			assertThat(sitemap.getChildCount(node)).isEqualTo(3);
			assertThat(node.getUriSegment()).isEqualTo("my-account");
			assertThat(node.getViewClass()).isEqualTo(My_AccountView.class);
			assertThat(node.getLabelKey()).isEqualTo(TestLabelKey.My_Account);
			assertThat(node.isPublicPage()).isTrue();
			assertThat(node.hasRoles()).isFalse();
			break;

		case "my-account/transfers":
			assertThat(sitemap.getChildCount(node)).isEqualTo(0);
			assertThat(node.getUriSegment()).isEqualTo("transfers");
			assertThat(node.getViewClass()).isEqualTo(TransferView.class);
			assertThat(node.getLabelKey()).isEqualTo(TestLabelKey.Transfers);
			assertThat(node.isPublicPage()).isTrue();
			assertThat(node.hasRoles()).isFalse();
			break;

		case "my-account/money-in-out":
			assertThat(sitemap.getChildCount(node)).isEqualTo(0);
			assertThat(node.getUriSegment()).isEqualTo("money-in-out");
			assertThat(node.getViewClass()).isEqualTo(MoneyInOutView.class);
			assertThat(node.getLabelKey()).isEqualTo(TestLabelKey.MoneyInOut);
			assertThat(node.isPublicPage()).isFalse();
			assertThat(node.hasRoles()).isTrue();
			break;

		case "my-account/options":
			assertThat(sitemap.getChildCount(node)).isEqualTo(0);
			assertThat(node.getUriSegment()).isEqualTo("options");
			assertThat(node.getViewClass()).isEqualTo(OptionsView.class);
			assertThat(node.getLabelKey()).isEqualTo(TestLabelKey.Opt);
			assertThat(node.isPublicPage()).isFalse();
			assertThat(node.hasRoles()).isTrue();
			break;
		case "information":
			assertThat(sitemap.getChildCount(node)).isEqualTo(3);
			assertThat(node.getUriSegment()).isEqualTo("information");
			assertThat(node.getViewClass()).isEqualTo(null);
			assertThat(node.getLabelKey()).isEqualTo(null);
			assertThat(node.isPublicPage()).isTrue();
			assertThat(node.hasRoles()).isFalse();
			break;
		case "information/offers":
			assertThat(sitemap.getChildCount(node)).isEqualTo(0);
			assertThat(node.getUriSegment()).isEqualTo("offers");
			assertThat(node.getViewClass()).isEqualTo(MoneyInOutView.class);
			assertThat(node.getLabelKey()).isEqualTo(TestLabelKey.MoneyInOut);
			assertThat(node.isPublicPage()).isFalse();
			assertThat(node.hasRoles()).isTrue();
			break;
		case "information/terms":
			assertThat(sitemap.getChildCount(node)).isEqualTo(0);
			assertThat(node.getUriSegment()).isEqualTo("terms");
			assertThat(node.getViewClass()).isEqualTo(TransferView.class);
			assertThat(node.getLabelKey()).isEqualTo(null);
			assertThat(node.isPublicPage()).isTrue();
			assertThat(node.hasRoles()).isFalse();
			break;
		case "information/services":
			assertThat(sitemap.getChildCount(node)).isEqualTo(0);
			assertThat(node.getUriSegment()).isEqualTo("services");
			assertThat(node.getViewClass()).isEqualTo(OptionsView.class);
			assertThat(node.getLabelKey()).isEqualTo(TestLabelKey.Opt);
			assertThat(node.isPublicPage()).isFalse();
			assertThat(node.hasRoles()).isTrue();
			break;

		default:
			Fail.fail("unexpected uri: '" + uri + "'");
		}
	}

	@ModuleProvider
	protected AbstractModule module() {
		return new AbstractModule() {

			@Override
			protected void configure() {
				bind(I18NTranslator.class).to(AnnotationI18NTranslator.class);
				bind(URIFragmentHandler.class).to(StrictURIFragmentHandler.class);
			}

		};
	}

}
