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
import java.util.List;
import java.util.Set;

public class Exclusion {

    private List<String> artifacts;

    Set<ArtifactHeader> getExcludedArtifacts() {
        Set<ArtifactHeader> headers = new LinkedHashSet<ArtifactHeader>();
        for (String artifactString : artifacts) {
            headers.add(parseArtifactString(artifactString));
        }
        return headers;
    }

    private ArtifactHeader parseArtifactString(String artifactString) {
        String[] parts = artifactString.split(":");
        if (parts.length != 3) {
            throw new IllegalArgumentException("excluded artifact must be in format 'groupId:artifactId:version' but is '" + artifactString + "'");
        }
        return new ArtifactHeader(parts[0], parts[1], parts[2]);
    }

    public boolean excludes(Violation violation) {
        return getExcludedArtifacts().equals(violation.getOffendingArtifacts()); 
    }
    
    @Override
    public String toString() {
        return "Exclusion" + artifacts;
    }

}
