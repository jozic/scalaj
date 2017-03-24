ScalaJ Converters [![Build Status](https://travis-ci.org/jozic/scalaj.svg?branch=master)](https://travis-ci.org/jozic/scalaj) [![Coverage Status](https://coveralls.io/repos/jozic/scalaj/badge.svg)](https://coveralls.io/r/jozic/scalaj)
=================

### When JavaConverters is not enough...

If you work on a Java/Scala mixed project you can find yourself converting
java collections and/or primitive wrappers to/from corresponding scala classes or vice versa.
JavaConverters is your friend here, but it's not always good enough.

If you are tired of doing something like this

```scala
import scala.collection.JavaConverters._

def iTakeInt(i: Int) = { ... }

val something = someJavaListOfJavaIntegers.asScala.map(iTakeInt(_))
```

or this

```scala
import scala.collection.JavaConverters._

val something: mutable.Map[java.lang.Long, Buffer] = 
  someJavaMapOfJavaLongsToJavaLists.asScala.mapValues(_.asScala)
```

look no more!  
Now you can do

```scala
import com.daodecode.scalaj.collection._

val something: mutable.Map[Long, Buffer] = 
  someJavaMapOfJavaLongsToJavaLists.deepAsScala
```

and 

```scala
import com.daodecode.scalaj.collection._

val something: mutable.Map[Long, Buffer] = 
  someJavaMapOfJavaLongsToJavaLists.deepAsScala
```

ScalaJ Converters will go all the way down converting every nested collection or primitive type.  
Of course you should be ready to pay some cost for all these conversions.

Import `import com.daodecode.scalaj.collection._` aslo brings standard `JavaConverters._` in scope, 
so you can use plain `asJava/asScala` if you don't have nested collections or collections of primitives.

Having `scalaj-google-optional` or `scalaj-java-optional` in classpath you can add [guava Optionals](https://github.com/google/guava/blob/master/guava/src/com/google/common/base/Optional.java) or java.util.Optional (since JDK 8) to your
funky data structures and convert between them and scala versions all the way down and back.

```scala
val foo: java.util.Set[Optional[java.lang.Double] = ...

import com.daodecode.scalaj.googleoptional._

val scalaFoo: mutable.Set[Option[Double]] = foo.deepAsScala
```

If you want you scala collections ~~well-done~~ immutable, you can do it as well

```scala
val boo: java.util.Set[Optional[java.util.List[java.lang.Double]] = ...

import com.daodecode.scalaj.googleoptional._
import com.daodecode.scalaj.collection.immutable._

val immutableScalaBoo: Set[Option[immutable.Seq[Double]]] = boo.deepAsScalaImmutable
```

# scalaj-collection


## Latest stable release

### sbt

```scala
libraryDependencies += "com.daodecode" %% "scalaj-collection" % "0.1.3"
```
### maven
```xml
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalaj-collection_${scala.binary.version}</artifactId>
    <version>0.1.3</version>
</dependency>
```

where `scala.binary.version` is on of `2.10`, `2.11`, `2.12`

## Latest snapshot

First add sonatype snapshots repository to your settings

### sbt

```scala
resolvers += Resolver.sonatypeRepo("snapshots")
```

### maven

```xml
<repository>
    <id>snapshots-repo</id>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    <releases><enabled>false</enabled></releases>
    <snapshots><enabled>true</enabled></snapshots>
</repository>
```

then add snapshot as a dependency

### sbt
```scala
libraryDependencies += "com.daodecode" %% "scalaj-collection" % "0.1.4-SNAPSHOT"
```
### maven
```xml
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalaj-collection_${scala.binary.version}</artifactId>
    <version>0.1.4-SNAPSHOT</version>
</dependency>
```

where `scala.binary.version` is on of `2.10`, `2.11`, `2.12`

# scalaj-googleoptional

## Latest stable release

### sbt
```scala
libraryDependencies += "com.daodecode" %% "scalaj-googleoptional" % "0.1.3"
```
### maven
```xml
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalaj-googleoptional_${scala.binary.version}</artifactId>
    <version>0.1.2</version>
</dependency>
```
where `scala.binary.version` is on of `2.10`, `2.11`, `2.12`

## Latest snapshot

First add sonatype snapshots repository to your settings

### sbt

```scala
resolvers += Resolver.sonatypeRepo("snapshots")
```

### maven

```xml
<repository>
    <id>snapshots-repo</id>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    <releases><enabled>false</enabled></releases>
    <snapshots><enabled>true</enabled></snapshots>
</repository>
```

then add snapshot as a dependency

### sbt
```scala
libraryDependencies += "com.daodecode" %% "scalaj-googleoptional" % "0.1.3-SNAPSHOT"
```
### maven
```xml
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalaj-googleoptional_${scala.binary.version}</artifactId>
    <version>0.1.3-SNAPSHOT</version>
</dependency>
```

where `scala.binary.version` is on of `2.10`, `2.11`, `2.12`


## Related projects

https://github.com/softprops/guavapants  

https://github.com/scalaj/scalaj-collection
