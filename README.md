scalaj [![Build Status](https://travis-ci.org/jozic/scalaj.svg?branch=master)](https://travis-ci.org/jozic/scalaj) [![Coverage Status](https://coveralls.io/repos/jozic/scalaj/badge.svg)](https://coveralls.io/r/jozic/scalaj)
=================

###When JavaConverters is not enough...

If you work on a Java/Scala mixed project you can find yourself converting
java collections and/or primitives to/from corresponding scala classes or vice versa.
JavaConverters is your friends here, but it's not always enough

If you are tired of doing something like this

```
import scala.collection.JavaConverters._

def iTakeInt(i: Int) = { ... }

val something = someJavaListOfJavaIntegers.asScala.map(iTakeInt(_))
```

or

```
import scala.collection.JavaConverters._

val something: Map[java.lang.Long, Buffer] = someJavaMapOfJavaLongsToJavaLists.asScala.mapValues(_.asScala)

```

look no more!  
Now you can do

```
import com.daodecode.scalaj.collection._

val something: Map[Long, Buffer] = someJavaMapOfJavaLongsToJavaLists.deepAsScala

```

scalaj will go all the way down converting every nested collection or primitive type.
But beware, it will cost you something

having `scalaj-googloptional` in classpath you can add [guava Optionals](http://guava) to your
funky data structures and convert between them and scala versions and all the way back

```
val javaSetOfOptionalsOfJavaDoubles: java.util.Set[Optional[java.lang.Double] = ...

import com.daodecode.scalaj.googleoptional._

val scalaOne: Set[Option[Double]] = javaSetOfOptionalsOfJavaDoubles.deepAsScala
```


#scalaj-collection


##Latest stable release  NOT YET READY 

### sbt
```
libraryDependencies += "com.daodecode" %% "scalaj-collection" % "0.1.0"
```
### maven
``` xml
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalaj-collection_2.10</artifactId>
    <version>0.1.0</version>
</dependency>
```
or
``` xml
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalaj-collection_2.11</artifactId>
    <version>0.1.0</version>
</dependency>
```

##Latest snapshot

First add sonatype snapshots repository to your settings

### sbt

`resolvers += Resolver.sonatypeRepo("snapshots")`

### maven

``` xml
<repository>
    <id>snapshots-repo</id>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    <releases><enabled>false</enabled></releases>
    <snapshots><enabled>true</enabled></snapshots>
</repository>
```

then add snapshot as a dependency

### sbt
```
libraryDependencies += "com.daodecode" %% "scalaj-collection" % "0.1.0-SNAPSHOT"
```
### maven
``` xml
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalaj-collection_2.10</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```
or
``` xml
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalaj-collection_2.11</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```


#scalaj-googleoptional

##Latest stable release NOT YET READY

### sbt
```
libraryDependencies += "com.daodecode" %% "scalaj-googleoptional" % "0.1.0"
```
### maven
``` xml
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalaj-googleoptional_2.10</artifactId>
    <version>0.1.0</version>
</dependency>
```
or
``` xml
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalaj-googleoptional_2.11</artifactId>
    <version>0.1.0</version>
</dependency>
```

##Latest snapshot

First add sonatype snapshots repository to your settings

### sbt

`resolvers += Resolver.sonatypeRepo("snapshots")`

### maven

``` xml
<repository>
    <id>snapshots-repo</id>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    <releases><enabled>false</enabled></releases>
    <snapshots><enabled>true</enabled></snapshots>
</repository>
```

then add snapshot as a dependency

### sbt
```
libraryDependencies += "com.daodecode" %% "scalaj-googleoptional" % "0.1.0-SNAPSHOT"
```
### maven
``` xml
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalaj-googleoptional_2.10</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```
or
``` xml
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalaj-googleoptional_2.11</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```


##Related projects

https://github.com/softprops/guavapants
https://github.com/scalaj/scalaj-collection

