org.json datatype contributor for Hibernate ORM 5.2+
========

If you need to use `org.json.JSONArray` and `org.json.JSONObject`
objects in your JPA entities and already use Hibernate as your JPA provider,
this is the plugin for you, so long as Hibernate, you are using the ['org.json:json'](https://mvnrepository.com/artifact/org.json/json)
dependency version `20171018` or newer.

Set-up for testing environment:
--------

* PostgreSQL 9.2 server or newer, running on port 5432
* MySQL 8.0.3 server or newer, running on port 3306
* Username, password and database `hibernate_orm_test` with full privileges that database on both servers.

Compiling:
--------

Assuming you have your JAVA_HOME environment variable pointing to JDK8

    ./gradlew clean build

If you want to build without running the tests, use `assemble` instead of `build`.

To make the final jar available for your Maven or compatible project type
that uses the Maven repository, run:

    ./gradlew publishToMavenLocal

Now you just need to add the dependency to your project's `pom.xml`.

    <dependency>
        <groupId>com.mopano</groupId>
        <artifactId>hibernate-json-org-contributor</artifactId>
        <version>1.0</version>
    </dependency>

Or if you're using Gradle:

    dependencies {
        compile group: 'com.mopano', name: 'hibernate-json-org-contributor', version: '1.0'
    }


Configuring against the default:
--------

To use a custom configuration, you must implement the `com.mopano.hibernate.org.json.spi.JsonSettings` class and override any settings for which you do not wish to use the default.

You must list the class you've created as a standard Java service, by creating the file `META-INF/services/com.mopano.hibernate.org.json.spi.JsonSettings` in your resources directory, and adding the fully qualified class name of your implementation in it.

Be sure to read the JavaDoc hints.

Potential pitfall:
--------

If your application is loaded after Hibernate, instead of alongside it,
it might not load the type contributor. This can happen if you use Hibernate
on a Glassfish server. In this case you would need to add it to Glassfish's
classpath the same way you added Hibernate, as it is not the default provider.

Database dialect detection may fail with custom dialects, in which case you need to use a JsonSettings confirguration.
