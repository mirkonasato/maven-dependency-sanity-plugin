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

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;

class Violation {

    private final String name;
    private final String details;
    private final Set<ArtifactHeader> offendingArtifacts;

    public Violation(String name, String details, Artifact... offendingArtifacts) {
        this(name, details, Arrays.asList(offendingArtifacts));
    }

    public Violation(String name, String details, List<Artifact> offendingArtifacts) {
        this.name = name;
        this.details = details;
        this.offendingArtifacts = toArtifactHeaders(offendingArtifacts);
    }

    public Set<ArtifactHeader> getOffendingArtifacts() {
        return offendingArtifacts;
    }

    private Set<ArtifactHeader> toArtifactHeaders(List<Artifact> artifacts) {
        Set<ArtifactHeader> headers = new LinkedHashSet<ArtifactHeader>();
        for (Artifact artifact : artifacts) {
            headers.add(new ArtifactHeader(artifact));
        }
        return headers;
    }

    @Override
    public String toString() {
        return name + ": " + offendingArtifacts + " " + details;
    }

}
