FROM maven:3.9.9-eclipse-temurin-8 AS builder
WORKDIR /app
COPY pom.xml ./
COPY src ./src
# 构建 WAR 包，跳过测试以加快构建
RUN mvn -DskipTests package

FROM tomcat:9.0-jdk8-temurin
ENV TZ=Asia/Shanghai
WORKDIR /usr/local/tomcat

# 将构建产物部署为 ROOT.war
COPY --from=builder /app/target/*.war ./webapps/ROOT.war

# 在启动时替换端口为 Railway 提供的 PORT
CMD ["sh", "-c", "sed -ri 's/port=\"8080\"/port=\"'\"'\"${PORT:-8080}'\"'\"/' conf/server.xml && catalina.sh run"]

