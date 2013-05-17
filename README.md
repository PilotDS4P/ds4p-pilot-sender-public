ds4p-pilot-sender-public
=================

Running the ds4p-sender Code
=================
 
The ds4p-sender project is a modular Maven project. There is a parent POM file and submodules with their own child POM files. To run the SENDER's Maven project file, do the following:

1. Download and install the Java SE Development Kit 6 (JDK 1.6.0)
2. Download and install Maven 3
3. Download and install the JBoss AS7 application server (jboss-as-7.1.1.Final)
4. Download and unzip the ds4p-sender Maven project from GitHub
5. Go to the context-handler-client sub-folder and run (1) mvn clean install, (2) mvn assembly:assembly, and (3) inside target folder run mvn install:install-file -DgroupId=context-handler-client-jar-with-dependencies -DartifactId=context-handler-client-jar-with-dependencies -Dversion=1.0 -Dfile=context-handler-client-jar-with-dependencies.jar -Dpackaging=jar -DgeneratePom=true
6. Go to the clinically-adaptive-rules-client sub-folder and run (1) mvn clean install, (2) mvn assembly:assembly, and (3) inside target folder run mvn install:install-file -DgroupId=clinically-adaptive-rules-client-jar-with-dependencies -DartifactId=clinically-adaptive-rules-client-jar-with-dependencies -Dversion=1.0 -Dfile=clinically-adaptive-rules-client-jar-with-dependencies.jar -Dpackaging=jar -DgeneratePom=true
7. Go to the c32-service-client sub-folder and run (1) mvn clean install, (2) mvn assembly:assembly, and (3) inside target folder run mvn install:install-file -DgroupId=c32-client-jar-with-dependencies -DartifactId=c32-client-jar-with-dependencies -Dversion=1.0 -Dfile=c32-client-jar-with-dependencies.jar -Dpackaging=jar -DgeneratePom=true
8. Go to the document-processor/client sub-folder and run (1) mvn clean install, (2) mvn assembly:assembly, and (3) inside target folder run mvn install:install-file -DgroupId=document-processor-client-jar-with-dependencies -DartifactId=document-processor-client-jar-with-dependencies -Dversion=1.0 -Dfile=document-processor-client-jar-with-dependencies.jar -Dpackaging=jar -DgeneratePom=true
9. Go to the common-library sub-folder and run (1) mvn clean install, (2) mvn assembly:assembly, and (3) inside target folder run mvn install:install-file -DgroupId=common-library-jar-with-dependencies -DartifactId=common-library-jar-with-dependencies -Dversion=1.0 -Dfile=common-library-jar-with-dependencies.jar -Dpackaging=jar -DgeneratePom=true
10. Go to the healthcare-classification-service/client sub-folder and run (1) mvn clean install, (2) mvn assembly:assembly, and (3) inside target folder run mvn install:install-file -DgroupId=healthcare-classification-client-jar-with-dependencies -DartifactId=healthcare-classification-client-jar-with-dependencies -Dversion=1.0 -Dfile=healthcare-classification-client-jar-with-dependencies.jar -Dpackaging=jar -DgeneratePom=true
11. Go to the audit-service-client sub-folder and run (1) mvn clean install, (2) mvn assembly:assembly, and (3) inside target folder run mvn install:install-file -DgroupId=audit-service-client-jar-with-dependencies -DartifactId=audit-service-client-jar-with-dependencies -Dversion=1.0 -Dfile=audit-service-client-jar-with-dependencies.jar -Dpackaging=jar -DgeneratePom=true
12. Get the DS4PCommonLibrary.jar JAR file from https://github.com/PilotDS4P/ds4p-pilot-public/blob/master/lib/DS4PCommonLibrary.jar and run mvn install:install-file -DgroupId=DS4PCommonLibrary -DartifactId=DS4PCommonLibrary -Dversion=1.0 -Dfile=DS4PCommonLibrary.jar -Dpackaging=jar -DgeneratePom=true
13. Go to the xdsb-repository-client sub-folder and run (1) mvn clean install, (2) mvn assembly:assembly, and (3) inside target folder run mvn install:install-file -DgroupId=xdsb-repository-client-jar-with-dependencies -DartifactId=xdsb-repository-client-jar-with-dependencies -Dversion=1.0 -Dfile=xdsb-repository-client-jar-with-dependencies.jar -Dpackaging=jar -DgeneratePom=true
14. Go to the xdsb-registry-client sub-folder and run (1) mvn clean install, (2) mvn assembly:assembly, and (3) inside target folder run mvn install:install-file -DgroupId=xdsb-registry-client-jar-with-dependencies -DartifactId=xdsb-registry-client-jar-with-dependencies -Dversion=1.0 -Dfile=xdsb-registry-client-jar-with-dependencies.jar -Dpackaging=jar -DgeneratePom=true
15. Go to the secured-context-handler sub-folder and run (1) mvn clean install, (2) mvn assembly:assembly, and (3) inside target folder run mvn install:install-file -DgroupId=secured-context-handler-jar-with-dependencies -DartifactId=secured-context-handler-jar-with-dependencies -Dversion=1.0 -Dfile=secured-context-handler-1.0-jar-with-dependencies.jar -Dpackaging=jar -DgeneratePom=true
16. Open the command prompt and go to the root folder (the parent POM file is located in the root folder) of the SENDER
17. Execute the following on the command line to build the entire ds4p-sender project: mvn clean install.
