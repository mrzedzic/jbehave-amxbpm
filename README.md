jbehave-amxbpm
==============

1. Install jars to local mvn repo
marce@marce:/opt/tibco/ems/6.3/lib$ mvn install:install-file -Dfile=tibjms.jar -DgroupId=com.tibco -DartifactId=tibjms -Dversion=6.3 -Dpackaging=jar
marce@marce:/opt/tibco/ems/6.3/lib$ mvn install:install-file -Dfile=tibjms.jar -DgroupId=com.tibco -DartifactId=jms -Dversion=6.3 -Dpackaging=jar

