Maven Dependency Sanity Plugin
==============================

Performs additional checks on Maven dependencies, to prevent multiple versions
of the same JARs/classes from being packaged into the classpath.

Checks
------

 * Unique class names: inspects all JARs and fails the build if two JARs
   contain the same class. This should catch all cases where two versions
   of the same library are inadvertently packaged into the classpath.

Usage
-----

Add to your pom.xml

  <build>
    <plugins>
      ...
      <plugin>
        <groupId>com.encodedknowledge.maven</groupId>
        <artifactId>maven-dependency-sanity-plugin</artifactId>
        <version>0.1</version>
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

Future
------

 * Same version for related dependencies: avoid e.g.
   spring-context:2.5.6 and spring-aop:2.0.8; all
   org.springframework:spring-* should have the same version
 * Configuration options
 * ...
