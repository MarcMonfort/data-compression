JUNIT_HOME=src/compresor/tests
CLASSPATH=$CLASSPATH:$JUNIT_HOME/junit-4.12.jar:.

algoritmos = src/compresor/dominio/algoritmo/*.java
controladores = src/compresor/dominio/*.java
persistencia = src/compresor/persistencia/*.java
utils = src/compresor/utils/*.java
vistas = src/compresor/vistas/*.java
main = src/compresor/*.java
junits = src/compresor/tests/*.java
jars = src/compresor/tests/*.jar
driver = src/compresor/tests/TernarySearchTree/*.java

files = $(algoritmos) $(controladores) $(algoritmos) $(persistencia) $(utils) $(vistas) $(main) $(junits) $(driver)

algoritmosb = bin/compresor/dominio/algoritmo/*.class
controladoresb = bin/compresor/dominio/*.class
persistenciab = bin/compresor/persistencia/*.class
utilsb = bin/compresor/utils/*.class
vistasb = bin/compresor/vistas/*.class
mainb = bin/compresor/Main.class
junitsb = bin/compresor/tests/*.class
jarsb = bin/compresor/tests/*.class
driverb = bin/compresor/tests/TernarySearchTree/*.class

classes = $(algoritmosb) $(controladoresb) $(algoritmosb) $(persistenciab) $(utilsb) $(vistasb) $(mainb) $(junitsb) $(driverb)


compile:
	javac -cp src/compresor/tests/*:src/compresor/tests $(files) -d bin

	
runGUI:
	java -cp bin compresor.Main
	
runConsole:
	java -cp bin compresor.MainT

runjar:
	java -jar compresor.jar

runtest:
	java -cp bin org.junit.runner.JUnitCore compresor.tests.lz78Test

rundriver:
	java -cp bin compresor.tests.TernarySearchTree.DriverTernarySearchTree

	
clean:
	rm -rf bin/compresor
