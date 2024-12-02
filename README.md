mvn clean package
java -jar target/iletimerkezi-java-1.0.0.jar

<build>
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-jar-plugin</artifactId>
        <version>3.2.0</version>
        <configuration>
            <archive>
                <manifest>
                    <mainClass>com.iletimerkezi.examples.Example</mainClass>
                    <addClasspath>true</addClasspath>
                    <classpathPrefix>dependency/</classpathPrefix>
                </manifest>
            </archive>
        </configuration>
    </plugin>
</build>