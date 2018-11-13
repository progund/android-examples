
PKG_PATH=se/juneday/gitrepoviewer
TEST_SRC= $(PKG_PATH)/domain/test/RepositoryTest.java
#$(PKG_PATH)/util/test/RepositoryCacheTest.java \
"          $(PKG_PATH)/util/test/JsonParserTest.java \

ANDROID_DIR=../../app/src/main/java/
ANDROID_STUBS_DIR=android/android-stubs-master
ORG_JSON=org.json.jar
TEST_CLASSES=$(TEST_SRC:%.java=%.class)
TEST_CLASS_NAMES=$(shell echo $(TEST_CLASSES) | sed -e 's,\.class,,g' -e 's,/,\.,g')
CLASSPATH=$(ANDROID_STUBS_DIR):$(ANDROID_DIR):$(ORG_JSON):.


ANDROID_LOG=$(ANDROID_STUBS_DIR)/android/util/Log.java


%.class:%.java
	@echo " ---=== Compile: $< ===---"
	javac -cp $(CLASSPATH) $<


all: $(TEST_CLASSES) $(ANDROID_LOG)

check: all
	@echo " ---=== Test all classes ===---"
	$(foreach var,$(TEST_CLASS_NAMES), java -cp $(CLASSPATH) $(var))

clean:
	@echo " ---=== Cleaning up ===---"
	rm -fr $(TEST_CLASSES)
	-find . -name "*~" | xargs rm -f 

$(ANDROID_LOG):
	mkdir -p android
	wget https://github.com/progund/android-stubs/archive/master.zip
	cd android && unzip ../master.zip

info:
	@echo "Test files:                $(TEST_SRC)"
	@echo "CLASSPATH:                 $(CLASSPATH)"
	@echo " * Android app source dir: $(ANDROID_DIR)"
	@echo " * JSON jar:               $(ORG_JSON)"
	@echo " * Android stub files:     $(ANDROID_STUBS_DIR)"
