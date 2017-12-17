#
# Android app's java directory
#
A_PATH=../main/java

#
# My domain file (what's in the list)
#
PROD=$(A_PATH)/se/juneday/objectcacheexample/Product.java

#
# ObjectCace and its Reader
#
OC=$(A_PATH)/se/juneday/ObjectCache.java
OCR=$(A_PATH)/se/juneday/ObjectCacheReader.java
PROG=se.juneday.ObjectCacheReader

#
# ADHD
#
ADHD=/home/hesa/opt/prog-unbook/adhd/bin/adhd.sh
SER_FILE=adhd/apps/se.juneday.objectcacheexample/se.juneday.objectcacheexample.MainActivity

all: clean build dload run

#
# Compile
#
build: 
	javac -cp $(A_PATH) $(OCR) $(PROD)


#
# Download serialized file from Android device using ADHD
#
dload:
	$(ADHD) se.juneday.objectcacheexample serialized

#
# Runt the ObjectCacheReader
#
run:
	java -cp .:$(A_PATH) $(PROG) $(SER_FILE)

clean:
	-rm *.class *~

really-clean: clean
	-rm -fr adhd

