# currency-service
Service for job interview

Assuming you have [docker](https://docs.docker.com/installation/) intalled you need to do

docker build -t <choose_image_name> .

docker run -itd <choosen_image_name> -p 8080:8080

Alternatively you can build it with gradle (assuming you have [gradle] (https://services.gradle.org/distributions/gradle-2.7-bin.zip) installed)

And then execute

gradle build
