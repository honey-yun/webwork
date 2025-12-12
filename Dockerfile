FROM tomcat:9-jdk8
# 清理默认示例，减少体积与冲突
RUN rm -rf /usr/local/tomcat/webapps/*
# 将构建产物部署为 ROOT 应用
COPY target/webwork2.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]

