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

import org.apache.maven.artifact.Artifact;

class ArtifactHeader {

    private final String groupId;
    private final String artifactId;
    private final String version;

    public ArtifactHeader(Artifact artifact) {
        this(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion());
    }

    public ArtifactHeader(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }
    
    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((artifactId == null) ? 0 : artifactId.hashCode());
        result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null) {
            return false;
        }
        if (getClass() != otherObject.getClass()) {
            return false;
        }
        ArtifactHeader other = (ArtifactHeader) otherObject;
        if (artifactId == null) {
            if (other.artifactId != null) {
                return false;
            }
        } else if (!artifactId.equals(other.artifactId)) {
            return false;
        }
        if (groupId == null) {
            if (other.groupId != null) {
                return false;
            }
        } else if (!groupId.equals(other.groupId)) {
            return false;
        }
        if (version == null) {
            if (other.version != null) {
                return false;
            }
        } else if (!version.equals(other.version)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return groupId + ":" + artifactId + ":" + version;
    }

}
