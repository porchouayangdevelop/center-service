FROM openjdk:22-ea-1-jdk-slim as build

WORKDIR /app


COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

COPY src src

RUN ./mvnw package -DskipTests


FROM openjdk:22-ea-1-slim as production


WORKDIR /app

COPY --from=build /app/target/*.war center-service-0.0.1-SNAPSHOT.war



EXPOSE 9999

ENTRYPOINT ["java", "-jar", "center-service-0.0.1-SNAPSHOT.war"]

# docker build --tag sv-center:v0.0.01 .
# docker build --tag apb.registry-img.com/api-uat/newcore/cbs-center-service:v1.0.01 .
# docker push apb.registry-img.com/api-uat/newcore/cbs-center-service:v1.0.01



# docker build --tag apb.registry-img.com/api/newcore/cbs-app-prd:v0.0.03 .
# docker push apb.registry-img.com/api/newcore/cbs-app-prd:v0.0.03