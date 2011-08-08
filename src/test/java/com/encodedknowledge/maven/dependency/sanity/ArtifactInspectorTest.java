package com.encodedknowledge.maven.dependency.sanity;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ArtifactInspectorTest {

    @Mock
    private Artifact artifact;

    @Test
    public void listClasses() throws IOException {
        when(artifact.getFile()).thenReturn(new File("src/test/resources/dummy.jar"));
        ArtifactInspector inspector = new ArtifactInspector();
        List<String> classNames = inspector.listClasses(artifact);
        assertThat(classNames.size(), is(2));
        assertThat(classNames.get(0), is("com.example.FirstClass"));
        assertThat(classNames.get(1), is("com.example.SecondClass"));
    }

}
