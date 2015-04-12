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

or this

```
import scala.collection.JavaConverters._

val something: mutable.Map[java.lang.Long, Buffer] = 
  someJavaMapOfJavaLongsToJavaLists.asScala.mapValues(_.asScala)
```

look no more!  
Now you can do

```
import com.daodecode.scalaj.collection._

val something: mutable.Map[Long, Buffer] = 
  someJavaMapOfJavaLongsToJavaLists.deepAsScala
```

scalaj will go all the way down converting every nested collection or primitive type.  
Of course you should be ready to pay some cost for all this conversions

having `scalaj-googloptional` in classpath you can add [guava Optionals](https://github.com/google/guava/blob/master/guava/src/com/google/common/base/Optional.java) to your
funky data structures and convert between them and scala versions all the way down and back

```
val foo: java.util.Set[Optional[java.lang.Double] = ...

import com.daodecode.scalaj.googleoptional._

val scalaFoo: mutable.Set[Option[Double]] = foo.deepAsScala
```

If you want you scala collections ~~well-done~~ immutable, you can do it as well

```
val boo: java.util.Set[Optional[java.util.List[java.lang.Double]] = ...

import com.daodecode.scalaj.googleoptional._
import com.daodecode.scalaj.collection.immutable._

val immutableScalaBoo: Set[Option[immutable.Seq[Double]]] = boo.deepAsScalaImmutable
```

#scalaj-collection


##Latest stable release

### sbt
```
libraryDependencies += "com.daodecode" %% "scalaj-collection" % "0.1.1"
```
### maven
``` xml
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalaj-collection_2.10</artifactId>
    <version>0.1.1</version>
</dependency>
```
or
``` xml
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalaj-collection_2.11</artifactId>
    <version>0.1.1</version>
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
libraryDependencies += "com.daodecode" %% "scalaj-collection" % "0.1.2-SNAPSHOT"
```
### maven
``` xml
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalaj-collection_2.10</artifactId>
    <version>0.1.2-SNAPSHOT</version>
</dependency>
```
or
``` xml
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalaj-collection_2.11</artifactId>
    <version>0.1.2-SNAPSHOT</version>
</dependency>
```


#scalaj-googleoptional

##Latest stable release

### sbt
```
libraryDependencies += "com.daodecode" %% "scalaj-googleoptional" % "0.1.1"
```
### maven
``` xml
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalaj-googleoptional_2.10</artifactId>
    <version>0.1.1</version>
</dependency>
```
or
``` xml
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalaj-googleoptional_2.11</artifactId>
    <version>0.1.1</version>
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
libraryDependencies += "com.daodecode" %% "scalaj-googleoptional" % "0.1.2-SNAPSHOT"
```
### maven
``` xml
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalaj-googleoptional_2.10</artifactId>
    <version>0.1.2-SNAPSHOT</version>
</dependency>
```
or
``` xml
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalaj-googleoptional_2.11</artifactId>
    <version>0.1.2-SNAPSHOT</version>
</dependency>
```


##Related projects

https://github.com/softprops/guavapants  

https://github.com/scalaj/scalaj-collection

