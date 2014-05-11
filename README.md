jbehave-amxbpm
==============

1. Install jars to local mvn repo
marce@marce:/opt/tibco/ems/6.3/lib$ mvn install:install-file -Dfile=tibjms.jar -DgroupId=com.tibco -DartifactId=tibjms -Dversion=6.3 -Dpackaging=jar
marce@marce:/opt/tibco/ems/6.3/lib$ mvn install:install-file -Dfile=tibjms.jar -DgroupId=com.tibco -DartifactId=jms -Dversion=6.3 -Dpackaging=jar
marce@marce:/opt/tibco/ems/6.3/lib$ mvn install:install-file -Dfile=jms.jar -DgroupId=javax.jms -DartifactId=jms -Dversion=1.1 -Dpackaging=jar 
c:\java\apache-maven-3.1.1\bin\mvn install:install-file -Dfile=amx-bpm-java-service-connector-1.4.jar -DgroupId=com.tibco -DartifactId=amx-bpm-java-service-connector -Dversion=1.4 -Dpackaging=jar 


http://mvnrepository.com/artifact/org.jbehave/jbehave-jenkins-plugin/4.0-beta-4

