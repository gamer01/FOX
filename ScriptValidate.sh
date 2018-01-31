#!/bin/sh
export MAVEN_OPTS="-Xmx6G -Dlog4j.configuration=file:data/fox/log4j.properties"

nohup mvn exec:java -Dexec.mainClass="org.aksw.fox.ui.FoxCLI" -Dexec.args="-lpt -avalidate -iinput/Wikiner/aij-wikiner-pt-wp3.bz2" > ../validate\ logs/pt_combined.log &
