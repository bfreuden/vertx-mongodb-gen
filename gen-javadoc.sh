#javadoc -classpath target/dependencies/bson-4.1.2.jar:target/dependencies/reactive-streams-1.0.2.jar:target/classes -sourcepath merged-sources -d html -public -exclude "**/com/mongodb/**/internal/**" -tagletpath target/classes:src/main/java -taglet ServerReleaseTaglet com.mongodb.reactivestreams.client
javadoc -link https://javadoc.io/doc/io.vertx/vertx-core/latest/ \
  -link https://mongodb.github.io/mongo-java-driver/3.6/javadoc/ \
  -link https://mongodb.github.io/mongo-java-driver-reactivestreams/1.13/javadoc \
  -verbose \
  -classpath /home/bruno/.sdkman/candidates/java/8.0.302-open/lib/tools.jar:target/dependencies/mongodb-driver-core-4.1.2.jar:target/dependencies/mongodb-driver-reactivestreams-4.1.2.jar:target/dependencies/vertx-core-4.3.2.jar:target/dependencies/vertx-codegen-4.3.2.jar:target/dependencies/bson-4.1.2.jar:target/dependencies/reactive-streams-1.0.2.jar:target/dependencies/netty-buffer-4.1.78.Final.jar:target/classes -sourcepath src/main/java:src/main/generated-java \
  -d javadoc \
  -public \
  -exclude "**/com/mongodb/**/internal/**" \
  -tagletpath target/classes:src/main/java \
  -taglet taglets.ServerReleaseTaglet \
  -taglet taglets.ManualTaglet \
  -taglet taglets.DochubTaglet \
  io.vertx.mongo io.vertx.mongo.client io.vertx.mongo.client.model io.vertx.mongo.client.vault io.vertx.mongo.client.model.vault io.vertx.mongo.client.model.changestream io.vertx.mongo.client.gridfs io.vertx.mongo.client.gridfs.model io.vertx.mongo.client.result io.vertx.mongo.bulk io.vertx.mongo.connection

#javadoc -doclet org.bfreuden.SourceGenDoclet -docletpath target/classes -sourcepath merged-sources -public -exclude "**/com/mongodb/**/internal/**" com.mongodb.reactivestreams.client
