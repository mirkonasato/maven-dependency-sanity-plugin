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

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.versioning.VersionRange;
import org.junit.Test;

public class UniqueArtifactIdRuleTest {

    private UniqueArtifactIdRule check = new UniqueArtifactIdRule();

    @Test
    public void noDuplicates() {
        Set<Artifact> artifacts = new LinkedHashSet<Artifact>();
        artifacts.add(makeArtifact("com.example", "first-artifact", "1.1"));
        artifacts.add(makeArtifact("com.example", "second-artifact", "2.2"));
        artifacts.add(makeArtifact("com.example", "third-artifact", "3.3"));
        List<Violation> violations = check.check(artifacts);
        assertThat(violations.size(), is(0));
    }

    @Test
    public void oneDuplicate() {
        Set<Artifact> artifacts = new LinkedHashSet<Artifact>();
        artifacts.add(makeArtifact("com.example", "first-artifact", "1.1"));
        artifacts.add(makeArtifact("example", "second-artifact", "2.2"));
        artifacts.add(makeArtifact("com.example", "second-artifact", "2.3"));
        artifacts.add(makeArtifact("com.example", "third-artifact", "3.3"));
        List<Violation> violations = check.check(artifacts);
        assertThat(violations.size(), is(1));
        assertThat(violations.get(0).toString(),
            is("duplicate artifactId: example:second-artifact:2.2, com.example:second-artifact:2.3"));
    }

    @Test
    public void twoDuplicates() {
        Set<Artifact> artifacts = new LinkedHashSet<Artifact>();
        artifacts.add(makeArtifact("com.example", "first-artifact", "1.1"));
        artifacts.add(makeArtifact("example", "second-artifact", "2.2"));
        artifacts.add(makeArtifact("com.example", "second-artifact", "2.3"));
        artifacts.add(makeArtifact("example", "third-artifact", "3.3"));
        artifacts.add(makeArtifact("com.example", "third-artifact", "3.4"));
        List<Violation> violations = check.check(artifacts);
        assertThat(violations.size(), is(2));
        assertThat(violations.get(0).toString(),
                is("duplicate artifactId: example:second-artifact:2.2, com.example:second-artifact:2.3"));
        assertThat(violations.get(1).toString(),
                is("duplicate artifactId: example:third-artifact:3.3, com.example:third-artifact:3.4"));
    }

    @Test
    public void duplicateInRuntimeScopeIsDetected() {
        Set<Artifact> artifacts = new LinkedHashSet<Artifact>();
        artifacts.add(makeArtifact("com.example", "first-artifact", "1.1"));
        artifacts.add(makeArtifact("example", "second-artifact", "2.2"));
        artifacts.add(makeArtifact("com.example", "second-artifact", "2.3", Artifact.SCOPE_RUNTIME));
        artifacts.add(makeArtifact("com.example", "third-artifact", "3.3"));
        List<Violation> violations = check.check(artifacts);
        assertThat(violations.size(), is(1));
    }

    @Test
    public void duplicateInTestScopeIsIgnored() {
        Set<Artifact> artifacts = new LinkedHashSet<Artifact>();
        artifacts.add(makeArtifact("com.example", "first-artifact", "1.1"));
        artifacts.add(makeArtifact("example", "second-artifact", "2.2"));
        artifacts.add(makeArtifact("com.example", "second-artifact", "2.3", Artifact.SCOPE_TEST));
        artifacts.add(makeArtifact("com.example", "third-artifact", "3.3"));
        List<Violation> violations = check.check(artifacts);
        assertThat(violations.size(), is(0));
    }

    private Artifact makeArtifact(String groupId, String artifactId, String version) {
        return makeArtifact(groupId, artifactId, version, Artifact.SCOPE_COMPILE);
    }

    private Artifact makeArtifact(String groupId, String artifactId, String version, String scope) {
        return new DefaultArtifact(groupId, artifactId, VersionRange.createFromVersion(version), scope, "jar", "", null);
    }

}
