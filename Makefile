.PHONY: help
help:
	@echo "make build   # build"
	@echo "make jar     # build jar (build first if required)"
	@echo "make run     # run (build first if required)"
	@echo "make run-jar # run via jar (build first if required)"
	@echo "make clean   # delete build artifacts"

build: src/
	rm -rf build
	mkdir -p build
	javac -O -d build src/*.java

VOOPS.jar: build bsh/ images/
	jar --create --manifest MANIFEST.MF --file VOOPS.jar -C build . bsh -C images .

.PHONY: jar
jar: VOOPS.jar

.PHONY: run
run: build
	java -Xmx400m -Xms400m -cp '.:images:build' VOOPS

.PHONY: run-mac
run-mac: build
	java -Dapple.laf.useScreenMenuBar=true -Xmx400m -Xms400m -cp '.:images:build' VOOPS

.PHONY: run-jar
run-jar: VOOPS.jar
	java -Xmx600m -Xms600m -jar VOOPS.jar

.PHONY: clean
clean:
	rm -rf build
	rm VOOPS.jar
