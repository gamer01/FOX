#!/bin/bash
# =======================================
DIRECTORY="./agdistis"
if [ ! -d "$DIRECTORY" ]; then
	#
	echo "Downloading Agdistis data..."
	#
	mkdir $DIRECTORY
	cd $DIRECTORY

	langs=(de en es fr nl pt)

    for ln in ${langs[@]}
    do
        (
        wget http://hobbitdata.informatik.uni-leipzig.de/agdistis/dbpedia_index_2016-04/${ln}/indexdbpedia_${ln}_2016.zip
        unzip indexdbpedia_${ln}_2016.zip
	    rm indexdbpedia_${ln}_2016.zip
        )&
    done
    wait

	cd ..
	echo "Downloaded Agdistis data." 
fi
