#!/usr/bin/env sh

bees app:deploy -a movie-buddy-uthandler -t java -R class=org.ehsavoie.moviebuddies.web.StartMovieBuddy -R java_version=1.8 -R classpath=moviebuddy.jar:lib/javax.json.jar:lib/undertow-core.jar:lib/jboss-logging.jar:lib/xnio-api.jar:lib/xnio-nio.jar:lib/javax.json-api.jar:lib/jboss-annotations-api_1.2_spec.jar:lib/jboss-logmanager.jar target/movie-buddy-uthandler.zip
