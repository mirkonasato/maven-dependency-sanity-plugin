//
// Copyright 2011 Mirko Nasato
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
package com.encodedknowledge.maven.dependency.sanity;

import static com.encodedknowledge.maven.dependency.sanity.ArtifactTestUtils.createArtifacts;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anySetOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.ReflectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CheckMojoTest {

    private CheckMojo mojo;

    @Mock private Rule rule;
    @Mock private ArtifactScopeFilter artifactScopeFilter;
    @Mock private ViolationExclusionFilter violationExclusionFilter;
    @Mock private MavenProject project;

    @Before
    public void setUp() throws IllegalAccessException {
        mojo = new CheckMojo(rule, artifactScopeFilter, violationExclusionFilter);
        ReflectionUtils.setVariableValueInObject(mojo, "project", project);
    }

    @Test
    public void defaultExecution() throws Exception {
        Set<Artifact> artifacts = createArtifacts("foo:foo:1.0", "bar:bar:2.0");
        when(project.getArtifacts()).thenReturn(artifacts);
        Set<Artifact> filteredArtifacts = createArtifacts("foo:foo:1.0");
        when(artifactScopeFilter.filter(artifacts, CheckMojo.DEFAULT_SCOPES)).thenReturn(filteredArtifacts);
        
        mojo.execute();
        
        verify(artifactScopeFilter).filter(artifacts, CheckMojo.DEFAULT_SCOPES);
        verify(rule).check(filteredArtifacts);
    }

    @Test
    public void failsWhenViolationsFound() {
        when(rule.check(anySetOf(Artifact.class))).thenReturn(singletonList(new Violation("test", "")));

        try {
            mojo.execute();
            fail("expected " + MojoFailureException.class.getSimpleName());
        } catch (MojoFailureException mojoFailureException) {
            assertThat(mojoFailureException.getMessage(), is("dependency sanity check failed")); 
        }
    }

}
