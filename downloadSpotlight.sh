#!/bin/bash

DIRECTORY="spotlight"
if [ ! -d "$DIRECTORY" ]; then
	echo "Downloading Spotlight data..."

	mkdir $DIRECTORY
	cd $DIRECTORY

	wget http://downloads.dbpedia-spotlight.org/spotlight/dbpedia-spotlight-0.7.1.jar
	# wget http://spotlight.sztaki.hu/downloads/dbpedia-spotlight-latest.jar 

    langs=(de,DE en_2+2,EN es,ES fr,FR nl,NL pt,PT)

    # executed in parallel
	for ln in ${langs[@]}
    do
        IFS=","
        set -- $ln
        # wget http://spotlight.sztaki.hu/downloads/latest_models/$1.tar.gz
	    wget http://downloads.dbpedia-spotlight.org/2016-04/$1/model/$1.tar.gz
	    tar -xzf $1.tar.gz
	    rm $1.tar.gz

	    touch run$2.sh
        echo "nohup java -Xmx4G -Dfile.encoding=utf-8 -jar dbpedia-spotlight-0.7.1.jar $1 http://localhost:4449/rest  > log$2.log &">> run$2.sh
        chmod +x run$2.sh
    done

	echo "Downloaded Spotlight data."
	
	cd ..
fi