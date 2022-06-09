FROM openjdk:8-jre-alpine
MAINTAINER Ranger wilton.icp@gmail.com

#RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
#  && echo 'Asia/Shanghai' >/etc/timezone
#下载字体
RUN apk add --update font-adobe-100dpi ttf-dejavu fontconfig

WORKDIR /vihacker
VOLUME /tmp
EXPOSE 8130

#COPY bin/  ./bin
#RUN chmod +x ./bin/start.sh

ADD vihacker-uaa-1.0.7.R.jar  /vihacker/vihacker-uaa.jar
ENTRYPOINT ["java", "-Xmx512m", "-Djava.security.egd=file:/dev/./urandom", "-XX:+UnlockExperimentalVMOptions","-XX:+UseCGroupMemoryLimitForHeap", "-Dspring.profiles.active=test", "-jar", "/vihacker/vihacker-uaa.jar"]