JAVAC=javac
JAVA=java
JAVAC_FLAGS=--module-path $(PATH_TO_FX) --add-modules javafx.controls,javafx.fxml

# Chemin vers le dossier javafx-sdk
PATH_TO_FX=/home/yany/Téléchargements/javafx-sdk-21/lib

# Liste des fichiers sources
SRCS=$(wildcard *.java)

# Liste des fichiers .class correspondants
CLASSES=$(SRCS:.java=.class)

all: client

Main.class: IHM.java Main.java

# Compilation des fichiers sources
%.class: %.java
	$(JAVAC) $(JAVAC_FLAGS) $<

# Compilation du client
client: $(CLASSES) IHM.class
	$(JAVA) --module-path $(PATH_TO_FX) --add-modules javafx.controls,javafx.fxml Main

# Compilation de l'IHM
ihm: $(CLASSES) IHM.class
	$(JAVA) --module-path $(PATH_TO_FX) --add-modules javafx.controls,javafx.fxml IHM

# Nettoyage des fichiers .class
clean:
	rm -f *.class

