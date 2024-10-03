# GPX to map

This library can be used to generate batches of nice maps from GPX files.

## Quickstart

### Maven

Add the dependency to your pom.xml :

```xml

<dependency>
    <groupId>io.github.ibethus.gpxtomap</groupId>
    <artifactId>gpx-to-map</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Usage

**Given folders with gpx files :**

```
📦my_folder
 ┣ 📂sub_folder
 ┃ ┗ 📜another_gpx.gpx
 ┗ 📜my_gpx.gpx
```

**This is the base configuration for a running gpxToMapWalker :**

```java
GpxToMapWalker<DefaultGpxRunner, FileRunner> gpxToMapWalker = new GpxToMapWalker<>(null,
        new DefaultGpxRunner(new DefaultGpxMapper.builder().build()), null);

// When you run it, it will create a nice PNG for each GPX file :
Files.walkFileTree("my_folder/",gpxToMapWalker);
```

**Result :**

```
📦my_folder
 ┣ 📂sub_folder
 ┃ ┣ 📜another_gpx.gpx
 ┃ ┗ 📜another_gpx.png
 ┣ 📜my_gpx.gpx
 ┗ 📜my_gpx.png
```

## Maps

Maps are created using [this fork](https://github.com/JMapCreator/StaticMap) of
the [StaticMap](https://github.com/doubotis/StaticMap) project.

### Result

The default settings will product something similar to this :

![test-composed-gr20.png](doc_resource/test-image.png)

## Contributions

If you want to contribute, please open issues, or fork the repo and open PRs. I will happily answer/review them !

## TODOs

* Not much configuration can be done for now -> to be improved (color, size, tile provider, etc.)
* Use TestContainer for ITs