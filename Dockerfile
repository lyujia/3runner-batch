FROM eclipse-temurin:21-jre

WORKDIR /batch

COPY ./target/batch.jar /batch/batch.jar

EXPOSE 8084

COPY entrypoint.sh /usr/local/bin/entrypoint.sh
RUN chmod +x /usr/local/bin/entrypoint.sh

# Run the entrypoint script
ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]