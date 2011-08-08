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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.versioning.VersionRange;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UniqueClassRuleTest {

    @Mock
    private ArtifactInspector artifactInspector;

    private UniqueClassRule rule;

    @Before
    public void setUp() {
        rule = new UniqueClassRule(artifactInspector);
    }

    @Test
    public void noDuplicateClasses() throws IOException {
        Set<Artifact> artifacts = new LinkedHashSet<Artifact>();
        Artifact firstArtifact = makeArtifact("com.example", "foo", "1.1");
        Artifact secondArtifact = makeArtifact("com.example", "bar", "1.3");
        artifacts.add(firstArtifact);
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
        assertThat(violations.size(), is(0));
    }

    @Test
    public void duplicateClasses() throws IOException {
        Artifact firstArtifact = makeArtifact("example", "foo", "1.1");
        Artifact secondArtifact = makeArtifact("com.example", "foo", "1.3");
        
        when(artifactInspector.listClasses(firstArtifact)).thenReturn(Arrays.asList(
            "com.example.foo.FirstClass",
            "com.example.foo.SecondClass"
        ));
        when(artifactInspector.listClasses(secondArtifact)).thenReturn(Arrays.asList(
            "com.example.foo.FirstClass",
            "com.example.foo.SecondClass",
            "com.example.foo.ThirdClass"
        ));

        Set<Artifact> artifacts = new LinkedHashSet<Artifact>();
        artifacts.add(firstArtifact);
        artifacts.add(secondArtifact);

        List<Violation> violations = rule.check(artifacts);
        assertThat(violations.size(), is(1));
        assertThat(violations.get(0).toString(),
                is("duplicate class com.example.foo.FirstClass: example:foo:1.1, com.example:foo:1.3"));
    }

    private Artifact makeArtifact(String groupId, String artifactId, String version) {
        return makeArtifact(groupId, artifactId, version, Artifact.SCOPE_COMPILE);
    }

    private Artifact makeArtifact(String groupId, String artifactId, String version, String scope) {
        return new DefaultArtifact(groupId, artifactId, VersionRange.createFromVersion(version), scope, "jar", "", null);
    }

}