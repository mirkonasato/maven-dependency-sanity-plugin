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
