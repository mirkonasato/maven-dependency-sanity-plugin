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

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    private final Rule rule = new UniqueClassRule();

    public void execute() throws MojoFailureException {
        Set<Artifact> artifacts = project.getArtifacts();
        List<Violation> violations = rule.check(artifacts);
        if (!violations.isEmpty()) {
            for (Violation violation : violations) {
                getLog().error(violation.toString());
            }
            throw new MojoFailureException("Dependency Sanity Check failed");
        }
    }

}
