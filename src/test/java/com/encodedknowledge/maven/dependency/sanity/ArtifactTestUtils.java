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

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.versioning.VersionRange;

class ArtifactTestUtils {

    private ArtifactTestUtils() {
        // utility class
    }

    public static Artifact createArtifact(String artifactString) {
        String[] parts = artifactString.split(":");
        if (parts.length < 3 || parts.length > 4) {
            throw new IllegalArgumentException("artifactString must be in the format groupId:artifactId:version[:scope]");
        }
        String groupId = parts[0];
        String artifactId = parts[1];
        String version = parts[2];
        String scope = parts.length > 3 ? parts[3] : "compile";
        return new DefaultArtifact(groupId, artifactId, VersionRange.createFromVersion(version), scope, "jar", "", null);
    }

    public static Set<Artifact> createArtifacts(String... artifactStrings) {
        Set<Artifact> artifacts = new LinkedHashSet<Artifact>();
        for (String artifactString : artifactStrings) {
            artifacts.add(createArtifact(artifactString));
        }
        return artifacts;
    }

}
