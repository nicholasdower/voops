version ?= $(shell cat .release-version)

.PHONY: help
help:
	@echo "make build       - build"
	@echo "make jar         - build jar                 (build if required)"
	@echo "make run         - run                       (build if required)"
	@echo "make run-mac     - run with Mac menu         (build if required)"
	@echo "make run-jar     - run via jar               (build if required)"
	@echo "make run-jar-mac - run via jar with Mac menu (build if required)"
	@echo "make dmg         - build a Mac DMG           (build if required)"
	@echo "make clean       - delete build artifacts"

build: src lib
	rm -rf build
	mkdir -p build
	javac -O -cp 'lib' -d build src/*.java

VOOPS-$(version).jar: build images
	jar --create --manifest MANIFEST.MF --file $@ -C build . -C lib bsh -C images .

.PHONY: jar
jar: VOOPS-$(version).jar

.PHONY: run
run: build
	java -Xmx400m -Xms400m -cp 'lib:images:build' VOOPS

.PHONY: run-mac
run-mac: build
	java -Dapple.laf.useScreenMenuBar=true -Xmx400m -Xms400m -cp 'lib:images:build' VOOPS

.PHONY: run-jar
run-jar: VOOPS-$(version).jar
	java -Xmx600m -Xms600m -jar VOOPS-$(version).jar

.PHONY: run-jar-mac
run-jar-mac: VOOPS-$(version).jar
	java -Dapple.laf.useScreenMenuBar=true -Xmx600m -Xms600m -jar VOOPS-$(version).jar

VOOPS-$(version).dmg: mac/options VOOPS-$(version).jar
	rm -rf target
	mkdir -p target
	cp VOOPS-$(version).jar target/
	jpackage --app-version $(version) --main-jar VOOPS-$(version).jar @mac/options
	rm -rf target
	open $@

dmg: VOOPS-$(version).dmg

.PHONY: clean
clean:
	rm -rf build
	rm -rf target
	rm -rf VOOPS*.jar
	rm -rf VOOPS*.dmg
