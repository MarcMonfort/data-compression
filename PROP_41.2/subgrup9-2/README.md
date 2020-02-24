# Práctica de PROP

### Estructura

- Este README.md` que lleva la documentación menor del proyecto. Links referentes a la implementación por ejemplo
```
src/ --> Dónde va todo el proyecto
    compresor/ --> Package principal dónde cuelga el main
        Main.java
        dominio/ -> Package de los controladores
        persistencia/ -> Package de los modelos
        vista/ -> Package de la presentación
        utils/ -> Clases que queramos crear para comunicación o funciones útiles.
```

### Cómo compilar el proyecto
- Linux / MacOS

```sh
find -name "*.java" > sources.txt
javac @sources.txt
```

- Windows

```
dir /s /B *.java > sources.txt
javac @sources.txt
```


También se puede utilizar el `compile.sh`

### Cómo ejecutar el proyecto

```shell script
java -jar out/<mi_compresor>.jar
```



