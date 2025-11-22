# Start from the official MySQL image
FROM mysql:8.0

# Set environment variables
ENV MYSQL_ROOT_PASSWORD=rootpassword
ENV MYSQL_DATABASE=foodallergy
ENV MYSQL_USER=foodie
ENV MYSQL_PASSWORD=springpass

# Expose the default MySQL port
EXPOSE 3306

# Use the default entrypoint provided by the MySQL image
CMD ["mysqld"]