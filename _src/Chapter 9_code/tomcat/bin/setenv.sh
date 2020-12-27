# run JVM in server mode
JAVA_OPTS="$JAVA_OPTS -server"

# solr.solr.home => solr home for this app instance
# host => hostname, defaults to first localhost address for.eg.: solr1.exmaple.org
# port => app server port no.
# hostContext => wepapp context name
# zkHost => ZooKeeper ensemble host names and its client port no.
# zkClientTimeout => ZooKeeper client timeout
SOLR_OPTS="-Dsolr.solr.home=/home/ubuntu/solr-cores -Dhost=solr1 -Dport=8080 -DhostContext=solr -DzkClientTimeout=20000 -DzkHost=zoo1:2181,zoo2:2181,zoo3:2181"

JAVA_OPTS="$JAVA_OPTS $SOLR_OPTS"
