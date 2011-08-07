package com.encodedknowledge.maven.dependency.sanity;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CheckMojoTest {

    @Mock
    private MavenProject project;

    @Test
    public void execute() {
        Set<Artifact> artifacts = new LinkedHashSet<Artifact>();
        artifacts.add(makeArtifact("com.example", "first-artifact", "1.1"));
        artifacts.add(makeArtifact("example", "second-artifact", "2.2"));
        artifacts.add(makeArtifact("com.example", "second-artifact", "2.3"));
        artifacts.add(makeArtifact("com.example", "third-artifact", "3.3"));
        when(project.getArtifacts()).thenReturn(artifacts);
        
        CheckMojo mojo = new CheckMojo();
        mojo.setProject(project);
        try {
            mojo.execute();
            fail("MojoFailureException not thrown");
        } catch (MojoFailureException exception) {
            String message = exception.getMessage();
            assertThat(message, is("Dependency Sanity Check failed:\n"
                + "duplicate artifactId: example:second-artifact:2.2, com.example:second-artifact:2.3"));
        }
    }

    private Artifact makeArtifact(String groupId, String artifactId, String version) {
        return makeArtifact(groupId, artifactId, version, Artifact.SCOPE_COMPILE);
    }

    private Artifact makeArtifact(String groupId, String artifactId, String version, String scope) {
        return new DefaultArtifact(groupId, artifactId, VersionRange.createFromVersion(version), scope, "jar", "", null);
    }

}
