FROM openjdk:11-jre-slim

# 安装 Tomcat
RUN apt-get update && apt-get install -y tomcat9

# 复制 WAR 文件
COPY target/*.war /var/lib/tomcat9/webapps/ROOT.war

# 暴露端口
EXPOSE 8080

# 启动命令
CMD ["catalina.sh", "run"]
