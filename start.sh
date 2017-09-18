#!/bin/sh
set -x
home_job="/dati/dataload/job/Retriever"
cd $home_job
/usr/bin/jdk1.8.0_91/bin/java -cp "reportretriever.jar:./lib/*" it.bcc.sinergia.retriever.Retriever

exit 0
