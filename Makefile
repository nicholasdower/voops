.PHONY: help
help:
	@echo "make build       - build"
	@echo "make jar         - build jar                 (build if required)"
	@echo "make run         - run                       (build if required)"
	@echo "make run-mac     - run with Mac menu         (build if required)"
	@echo "make run-jar     - run via jar               (build if required)"
	@echo "make run-jar-mac - run via jar with Mac menu (build if required)"
	@echo "make clean       - delete build artifacts"

build: src/ lib/
	rm -rf build
	mkdir -p build
	javac -O -cp 'lib' -d build src/*.java

VOOPS.jar: build images/
	jar --create --manifest MANIFEST.MF --file VOOPS.jar -C build . -C lib bsh -C images .

.PHONY: jar
jar: VOOPS.jar

.PHONY: run
run: build
	java -Xmx400m -Xms400m -cp 'lib:images:build' VOOPS

.PHONY: run-mac
run-mac: build
	java -Dapple.laf.useScreenMenuBar=true -Xmx400m -Xms400m -cp 'lib:images:build' VOOPS

.PHONY: run-jar
run-jar: VOOPS.jar
	java -Xmx600m -Xms600m -jar VOOPS.jar

.PHONY: run-jar-mac
run-jar-mac: VOOPS.jar
	java -Dapple.laf.useScreenMenuBar=true -Xmx600m -Xms600m -jar VOOPS.jar

.PHONY: clean
clean:
	rm -rf build
	rm -rf VOOPS.jar
