/*
 * Sonar SCM Stats Plugin
 * Copyright (C) 2012 Patroklos PAPAPETROU
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.scmstats;

import org.apache.maven.model.Scm;
import org.apache.maven.project.MavenProject;
import org.apache.maven.scm.provider.svn.svnexe.SvnExeScmProvider;
import org.junit.*;
import org.sonar.api.config.Settings;
import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ScmConfigurationTest {
  private final Settings settings = new Settings();
  private static final String URL = "scm:svn:http://";
  @Before
  public void setUp() {
    settings.setProperty(ScmStatsPlugin.ENABLED, true);
  }

  @Test
  public void testMavenConfiguration() {
    
    MavenProject mvnProject = new MavenProject();
    final Scm scm = new Scm();
    scm.setConnection(URL);
    scm.setDeveloperConnection(URL);
    mvnProject.setScm(scm);
    MavenScmConfiguration mavenConfonfiguration = new MavenScmConfiguration(mvnProject);
    ScmConfiguration scmConfiguration = new ScmConfiguration(settings, mavenConfonfiguration);
    
    assertThat ( scmConfiguration.isEnabled() , is(true));
    assertThat ( scmConfiguration.getUrl() , is(URL));
    assertThat ( scmConfiguration.getScmProvider() , is(new SvnExeScmProvider().getScmType()));
  }

  @Test
  public void testNonMavenConfiguration() {
    ScmConfiguration scmConfiguration = new ScmConfiguration(settings);
    
    assertThat ( scmConfiguration.isEnabled() , is(true));
    assertNull ( scmConfiguration.getUrl());
    assertNull ( scmConfiguration.getScmProvider());
  }

  @Test
  public void testConfigurationOfSCMActivityPlugin() {
    settings.setProperty("sonar.scm.url", URL);
    ScmConfiguration scmConfiguration = new ScmConfiguration(settings);
    
    assertThat ( scmConfiguration.isEnabled() , is(true));
    assertThat ( scmConfiguration.getUrl() , is(URL));
    assertThat ( scmConfiguration.getScmProvider() , is(new SvnExeScmProvider().getScmType()));
  }
}
