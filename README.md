# vivid

This is a picture sharing website where users can follow their friends and discover interesting pictures.

Website Architecture:

We use many Spring tools to implement this project: 
Spring MVC as a overall framework to separate the bussinesss logic and presentation logic,
Spring Boot to simplifies the Spring dependencies and configurations,
Spring Data JPA and Spring Cassandra as tools to make persistence,
Spring Security to provide comprehensive security services.


We use two different databases. PostgreSQL to store user-related information and Cassandra to store user feeds and following relationships.




