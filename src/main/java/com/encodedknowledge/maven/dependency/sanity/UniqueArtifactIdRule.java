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
