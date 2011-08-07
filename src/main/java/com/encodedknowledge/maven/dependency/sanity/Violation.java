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
import java.util.List;

import org.apache.maven.artifact.Artifact;

class Violation {

    private final String description;
    private final List<Artifact> offendingArtifacts;

    public Violation(String description, Artifact... offendingArtifacts) {
        this(description, Arrays.asList(offendingArtifacts));
    }

    public Violation(String description, List<Artifact> offendingArtifacts) {
        this.description = description;
        this.offendingArtifacts = offendingArtifacts;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(description + ": ");
        for (int i = 0; i < offendingArtifacts.size(); i++) {
            if (i > 0) {
                builder.append(", ");
            }
            Artifact artifact = offendingArtifacts.get(i);
            builder.append(artifact.getGroupId() + ":" + artifact.getArtifactId() + ":" + artifact.getVersion());
        }
        return builder.toString();
    }

}
