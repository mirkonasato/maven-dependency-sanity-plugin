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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.artifact.Artifact;

class UniqueClassRule implements Rule {

    private Map<String, Artifact> artifactByClassName = new HashMap<String, Artifact>();

    private final ArtifactInspector artifactInspector;

    public UniqueClassRule() {
        this(new ArtifactInspector());
    }
    
    UniqueClassRule(ArtifactInspector artifactInspector) {
        this.artifactInspector = artifactInspector;
    }
    
    public List<Violation> check(Set<Artifact> artifacts) {
        List<Violation> violations = new ArrayList<Violation>();
        for (Artifact artifact : artifacts) {
            List<String> classNames;
            try {
                classNames = artifactInspector.listClasses(artifact);
            } catch (IOException ioException) {
                throw new RuntimeException(ioException);
            }
            for (String className : classNames) {
                if (artifactByClassName.containsKey(className)) {
                    Artifact otherArtifact = artifactByClassName.get(className);
                    violations.add(new Violation("duplicate class " + className, otherArtifact, artifact));
                    // the two artifacts are likely to have many more classes in common
                    // but we're only interested in finding the conflict so we stop here
                    break;
                }
                artifactByClassName.put(className, artifact);
            }
        }
        return violations;
    }

}