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
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.maven.artifact.Artifact;

class ArtifactInspector {

    public List<String> listClasses(Artifact artifact) throws IOException {
        List<String> classNames = new ArrayList<String>();
        JarFile jar = null;
        try {
            jar = new JarFile(artifact.getFile());
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class")) {
                    String className = entry.getName().substring(0, entry.getName().length() - 6).replace('/', '.');
                    classNames.add(className);
                }
            }
        } finally {
            close(jar);
        }
        return classNames;
    }

    private void close(JarFile  jarFile) {
        if (jarFile != null) {
            try {
                jarFile.close();
            } catch (IOException closeException) {
                // pass
            }
        }
    }

}
