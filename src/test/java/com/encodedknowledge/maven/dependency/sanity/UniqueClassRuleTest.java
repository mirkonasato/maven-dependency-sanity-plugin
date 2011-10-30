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

import static com.encodedknowledge.maven.dependency.sanity.ArtifactTestUtils.createArtifact;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UniqueClassRuleTest {

    private UniqueClassRule rule;

    @Mock private ArtifactInspector artifactInspector;

    @Before
    public void setUp() throws IllegalAccessException {
        rule = new UniqueClassRule(artifactInspector);
    }

    @Test
    public void noDuplicatesFound() throws IOException {
        Set<Artifact> artifacts = new LinkedHashSet<Artifact>();
        Artifact firstArtifact = createArtifact("com.example:foo:1.1");
        artifacts.add(firstArtifact);
        Artifact secondArtifact = createArtifact("com.example:bar:1.3");
        artifacts.add(secondArtifact);
        
        when(artifactInspector.listClasses(firstArtifact)).thenReturn(Arrays.asList(
            "com.example.foo.FirstClass",
            "com.example.foo.SecondClass",
            "com.example.foo.ThirdClass"
        ));
        when(artifactInspector.listClasses(secondArtifact)).thenReturn(Arrays.asList(
            "com.example.bar.FirstClass",
            "com.example.bar.SecondClass"
        ));
        
        List<Violation> violations = rule.check(artifacts);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void duplicatesFound() throws IOException {
        Set<Artifact> artifacts = new LinkedHashSet<Artifact>();
        Artifact firstArtifact = createArtifact("example:foo:1.1");
        artifacts.add(firstArtifact);
        Artifact secondArtifact = createArtifact("com.example:foo:1.3");
        artifacts.add(secondArtifact);
        
        when(artifactInspector.listClasses(firstArtifact)).thenReturn(Arrays.asList(
            "com.example.foo.FirstClass",
            "com.example.foo.SecondClass"
        ));
        when(artifactInspector.listClasses(secondArtifact)).thenReturn(Arrays.asList(
            "com.example.foo.FirstClass",
            "com.example.foo.SecondClass",
            "com.example.foo.ThirdClass"
        ));

        List<Violation> violations = rule.check(artifacts);
        assertThat(violations.size(), is(1));
        assertThat(violations.get(0).toString(),
                is("class duplication: [example:foo:1.1, com.example:foo:1.3] both provide com.example.foo.FirstClass"));
    }

}