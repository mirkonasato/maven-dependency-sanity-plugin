package com.encodedknowledge.maven.dependency.sanity;

import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;

interface Rule {

    List<Violation> check(Set<Artifact> artifacts);

}
