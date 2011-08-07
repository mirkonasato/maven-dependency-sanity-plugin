Maven Dependency Sanity Plugin
==============================

Performs additional checks on Maven dependencies, to prevent multiple versions
of the same JARs/classes from being packaged into the classpath.

Checks
------

 * Unique artifactId: prevents e.g. having in the classpath both
   aspectj:aspectjrt:1.5.4 and org.aspectj:aspectjrt:1.6.11

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

To Do
-----

 * Conflicts: e.g. log4j-over-slf4j cannot co-exist with log4j
 * Same version for related dependencies: avoid e.g.
   spring-context:2.5.6 and spring-aop:2.0.8; all
   org.springframework:spring-* should have the same version 
 * ...
