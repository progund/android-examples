
PKG_PATH=se/juneday/gitrepoviewer
TEST_SRC= $(PKG_PATH)/domain/test/RepositoryTest.java
#$(PKG_PATH)/util/test/RepositoryCacheTest.java \
"          $(PKG_PATH)/util/test/JsonParserTest.java \

ANDROID_DIR=../../app/src/main/java/
ANDROID_STUBS_DIR=android
ORG_JSON=org.json.jar
TEST_CLASSES=$(TEST_SRC:%.java=%.class)
TEST_CLASS_NAMES=$(shell echo $(TEST_CLASSES) | sed -e 's,\.class,,g' -e 's,/,\.,g')
CLASSPATH=$(ANDROID_STUBS_DIR):$(ANDROID_DIR):$(ORG_JSON):.


%.class:%.java
	@echo " ---=== Compile: $< ===---"
	javac -cp $(CLASSPATH) $<


all: $(TEST_CLASSES)

check: all
	@echo " ---=== Test all classes ===---"
	$(foreach var,$(TEST_CLASS_NAMES), java -cp $(CLASSPATH) $(var))

clean:
	@echo " ---=== Cleaning up ===---"
	rm -fr $(TEST_CLASSES)

info:
	@echo "Test files:                $(TEST_SRC)"
	@echo "CLASSPATH:                 $(CLASSPATH)"
	@echo " * Android app source dir: $(ANDROID_DIR)"
	@echo " * JSON jar:               $(ORG_JSON)"
	@echo " * Android stub files:     $(ANDROID_STUBS_DIR)"
