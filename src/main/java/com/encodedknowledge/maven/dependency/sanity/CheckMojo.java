package com.encodedknowledge.maven.dependency.sanity;

import java.util.ArrayList;
import java.util.Collection;
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

    private static final String NL = System.getProperty("line.separator");

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    private final List<Rule> rules;

    public CheckMojo() {
        rules = new ArrayList<Rule>();
        rules.add(new UniqueArtifactIdRule());
        // more to come...
    }
    
    void setProject(MavenProject project) {
        this.project = project;
    }

    public void execute() throws MojoFailureException {
        Set<Artifact> artifacts = project.getArtifacts();
        List<Violation> violations = new ArrayList<Violation>();
        for (Rule rule : rules) {
            violations.addAll(rule.check(artifacts));
        }
        if (!violations.isEmpty()) {
            throw new MojoFailureException("Dependency Sanity Check failed:" + NL + join(violations, NL));
        }
    }

    private String join(Collection<?> values, String separator) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (Object value : values) {
            if (first) {
                first = false;
            } else {
                builder.append(separator);
            }
            builder.append(value.toString());
        }
        return builder.toString();
    }

}
