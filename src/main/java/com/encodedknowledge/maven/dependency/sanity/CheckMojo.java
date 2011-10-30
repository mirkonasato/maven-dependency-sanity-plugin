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
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 * @goal check
 * @phase package
 * @requiresDependencyResolution test
 */
public class CheckMojo extends AbstractMojo {

    public static final List<String> DEFAULT_SCOPES = Arrays.asList(Artifact.SCOPE_COMPILE, Artifact.SCOPE_RUNTIME);

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * Dependency scopes to be included in the check; defaults to compile and runtime.
     * 
     * @parameter
     */
    private List<String> scopes = DEFAULT_SCOPES;

    /**
     * @parameter
     */
    private List<Exclusion> exclusions;

    /**
     * @parameter default-value="false"
     */
    private boolean warnOnly;

    private final Rule rule;
    private final ArtifactScopeFilter artifactScopeFilter;
    private final ViolationExclusionFilter violationExclusionFilter;

    public CheckMojo() {
        this(new UniqueClassRule(), new ArtifactScopeFilter(), new ViolationExclusionFilter());
    }

    CheckMojo(Rule rule, ArtifactScopeFilter artifactScopeFilter, ViolationExclusionFilter violationExclusionFilter) {
        this.rule = rule;
        this.artifactScopeFilter = artifactScopeFilter;
        this.violationExclusionFilter = violationExclusionFilter;
    }

    public void execute() throws MojoFailureException {
        Set<Artifact> artifacts = artifactScopeFilter.filter(project.getArtifacts(), scopes);
        List<Violation> violations = rule.check(artifacts);
        if (exclusions != null && violations.size() > 0) {
            violationExclusionFilter.filter(violations, exclusions);
        }
        if (violations.size() > 0) {
            for (Violation violation : violations) {
                getLog().warn(violation.toString());
            }
            if (!warnOnly) {
                throw new MojoFailureException("dependency sanity check failed");
            }
        }
    }

}
