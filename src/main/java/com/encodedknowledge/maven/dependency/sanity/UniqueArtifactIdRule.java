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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.artifact.Artifact;

class UniqueArtifactIdRule implements Rule {

    private Map<String, Artifact> artifactById = new HashMap<String, Artifact>();

    public List<Violation> check(Set<Artifact> artifacts) {
        List<Violation> violations = new ArrayList<Violation>();
        for (Artifact artifact : artifacts) {
            if (!isCompileOrRuntimeJar(artifact)) {
                continue;
            }
            if (artifactById.containsKey(artifact.getArtifactId())) {
                Artifact otherArtifact = artifactById.get(artifact.getArtifactId());
                violations.add(new Violation("duplicate artifactId", otherArtifact, artifact));
            }
            artifactById.put(artifact.getArtifactId(), artifact);
        }
        return violations;
    }

    private boolean isCompileOrRuntimeJar(Artifact artifact) {
        return "jar".equals(artifact.getType()) &&
            (Artifact.SCOPE_COMPILE.equals(artifact.getScope()) || Artifact.SCOPE_RUNTIME.equals(artifact.getScope()));
    }

}
