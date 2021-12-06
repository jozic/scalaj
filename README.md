ScalaJ Converters [![Build Status](https://github.com/jozic/scalaj/actions/workflows/checks.yml/badge.svg?branch=main)](https://github.com/jozic/scalaj/actions/workflows/checks.yml) [![Coverage Status](https://coveralls.io/repos/jozic/scalaj/badge.svg)](https://coveralls.io/r/jozic/scalaj)
=================

### When JavaConverters is not enough...

If you work on a Java/Scala mixed project you can find yourself converting
java collections and/or primitive wrappers to/from corresponding scala classes or vice versa.
JavaConverters is your friend here, but it's not always good enough.

If you are tired of doing something like this

```scala
import scala.jdk.CollectionConverters._

def iTakeInt(i: Int) = { ... }

val something = someJavaListOfJavaIntegers.asScala.map(iTakeInt(_))
```

or this

```scala
import scala.jdk.CollectionConverters._

val something: mutable.Map[java.lang.Long, Buffer] = 
  someJavaMapOfJavaLongsToJavaLists.asScala.mapValues(_.asScala)
```

look no more!  
Now you can do

```scala
import com.daodecode.scalaj.collection._

def iTakeInt(i: Int) = { ... }

val something = someJavaListOfJavaIntegers.deepAsScala.map(iTakeInt)
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

Having `scalaj-googloptional` in classpath you can add [guava Optionals](https://github.com/google/guava/blob/master/guava/src/com/google/common/base/Optional.java) to your
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


# Latest stable release

## scalaj-collection

| 2.10 | 2.11 | 2.12 | 2.13                                                                                                                                                                                                       |
|------|------|------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalaj-collection_2.10/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalaj-collection_2.10) | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalaj-collection_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalaj-collection_2.11) | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalaj-collection_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalaj-collection_2.12) | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalaj-collection_2.13/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalaj-collection_2.13) |

### sbt
```scala
libraryDependencies += "com.daodecode" %% "scalaj-collection" % "0.3.1"
```
### maven

set `<scala.binary.version>` property to scala version you need, like

```xml
<properties>
    <scala.binary.version>2.13</scala.binary.version>
</properties>
```

 and then in `dependencies` add

```xml
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalaj-collection_${scala.binary.version}</artifactId>
    <version>0.3.1</version>
</dependency>
```

## scalaj-googleoptional

| 2.10 | 2.11 | 2.12 | 2.13                                                                                                                                                                                                                |
|------|------|------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalaj-googleoptional_2.10/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalaj-googleoptional_2.10) | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalaj-googleoptional_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalaj-googleoptional_2.11) | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalaj-googleoptional_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalaj-googleoptional_2.12) | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalaj-googleoptional_2.13/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalaj-googleoptional_2.13) |

### sbt

```scala
libraryDependencies += "com.daodecode" %% "scalaj-googleoptional" % "0.3.1"
```
### maven

```xml
<properties>
    <scala.binary.version>2.13</scala.binary.version>
</properties>
```

 and then in `dependencies` add

```xml
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalaj-googleoptional_${scala.binary.version}</artifactId>
    <version>0.3.1</version>
</dependency>
```

# Latest snapshot

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

## scalaj-collection

### sbt

```scala
libraryDependencies += "com.daodecode" %% "scalaj-collection" % "0.3.2-SNAPSHOT"
```

### maven

```xml
<properties>
    <scala.binary.version>2.13</scala.binary.version>
</properties>
```

 and then in `dependencies` add

```xml
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalaj-collection_${scala.binary.version}</artifactId>
    <version>0.3.2-SNAPSHOT</version>
</dependency>
```

## scalaj-googleoptional

### sbt

```scala
libraryDependencies += "com.daodecode" %% "scalaj-googleoptional" % "0.3.2-SNAPSHOT"
```
### maven

```xml
<properties>
    <scala.binary.version>2.13</scala.binary.version>
</properties>
```

 and then in `dependencies` add

```xml
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalaj-googleoptional_${scala.binary.version}</artifactId>
    <version>0.3.2-SNAPSHOT</version>
</dependency>
```

# Related projects

https://github.com/softprops/guavapants  

https://github.com/scalaj/scalaj-collection
