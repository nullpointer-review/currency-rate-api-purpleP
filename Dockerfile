FROM java:8
RUN apt-get update && apt-get install -y wget unzip
RUN wget https://services.gradle.org/distributions/gradle-2.7-bin.zip
RUN unzip -d /tmp/ ./gradle*.zip
RUN rm ./gradle*.zip
RUN mv /tmp/gradle* /opt/
ENV GRADLE_HOME /opt/gradle-2.7/
ENV PATH $PATH:$GRADLE_HOME/bin
COPY . /usr/src/app
WORKDIR /usr/src/app
RUN gradle build
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/usr/src/app/build/libs/currency-service-1.0-SNAPSHOT.jar"]