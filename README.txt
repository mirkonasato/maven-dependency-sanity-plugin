Maven Dependency Sanity Plugin
==============================

Performs additional checks on Maven dependencies, to prevent multiple versions
of the same JARs/classes from being packaged into the classpath.

NOTE: when I wrote this plugin I wasn't aware that something very similar is
already available from Codehaus and in the central Maven repository: 
 
  http://mojo.codehaus.org/extra-enforcer-rules/banDuplicateClasses.html

Checks
------

 * Unique class names: inspects all JARs and fails the build if two JARs
   contain the same class. This should catch all cases where two versions
   of the same library are inadvertently packaged into the classpath.

Usage
-----

Add to your pom.xml:

  <build>
    <plugins>
      ...
      <plugin>
        <groupId>com.encodedknowledge.maven</groupId>
        <artifactId>maven-dependency-sanity-plugin</artifactId>
        <version>0.2</version>
        <executions>
          <execution>
            <goals>
              <goal>check</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>

or with configuration options:

      <plugin>
        <groupId>com.encodedknowledge.maven</groupId>
        <artifactId>maven-dependency-sanity-plugin</artifactId>
        <version>0.1</version>
        <configuration>
          <warnOnly>true</warnOnly>
          <scopes>
            <scope>compile</scope>
            <scope>test</scope>
          </scopes>
          <exclusions>
            <exclusion>
              <artifacts>
                <artifact>foo:foo:2.1</artifact>
                <artifact>bar:bar:1.0</artifact>
              </artifacts>
            </exclusion>
          </exclusions>
        </configuration>
        <executions>
          <execution>
            <goals>
              <goal>check</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
