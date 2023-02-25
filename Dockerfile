FROM amazoncorretto:17
ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}
COPY . .
RUN ./mvnw package
CMD java -jar target/PeopleManagerAPI-0.0.1-SNAPSHOT.jar --spring.profiles.active=${SPRING_PROFILES_ACTIVE}