# tutorial explanations
**_[ ] move this to an implementation using this template._**

The architecture follows the design in the [architecture diagram](systemdesign.md).

#### key components and their responsibilities:
**Client:** 
- This is the end-user (or frontend application) that interacts with our system via HTTP requests. 
- In our case, the “client” could be a web app or mobile app that consumes the backend APIs.
- **_We are not implementing this component in this project._** 

**Load Balancer:** 
- Distributes incoming requests across multiple instances of the API gateway or services. 
- In cloud environments, this could be an external load balancer. 
- (For our local setup, Docker/Kubernetes will handle routing, and we won’t implement a separate load balancer service explicitly.)
- [More on why we are not implementing the load balancer as a springboot service](loadbalancer.md) -> **to add content**.
 

**API Gateway:** 
- A single entry point for all client requests. 
- The gateway will route requests to the appropriate microservice. 
- It can also perform common functions i.e: 
  - authentication and authorization check (by validating JWT tokens from Keycloak), 
  - rate limiting (to prevent abuse), 
  - response caching, 
  - request logging and monitoring, 
  - and even act as a reverse proxy to hide internal service details. 
- We will implement the gateway using Spring Cloud Gateway, which allows us to configure routes to our microservices and apply filters (for things like auth and logging).

**Identity Access Management (IAM):** 
- Rather than writing our own auth service, we will use Keycloak 26.2.1, a popular open-source Identity and Access Management tool. 
- Keycloak will handle user registration, login, and token issuance (OpenID Connect tokens for authentication and JWTs for authorization). 
- Our microservices will trust Keycloak to authenticate users and will validate the JWT tokens on each request to secure the endpoints.

**Metadata Service:** 
- This service manages metadata for content (for example, if our system manages videos, this service stores video titles, descriptions, user info, etc., without the binary video files). 
- It has a PostgreSQL database (which can be scaled or sharded as needed; the diagram shows multiple Metadata DB instances and a cache). 
- We’ll start with a single Postgres instance for simplicity and use Redis as a caching layer to speed up read-heavy endpoints (caching recently or frequently accessed metadata). 
- The Metadata service will expose REST API endpoints to create new content records, fetch details, etc.
- It will also produce events to Kafka when new content is created (for other services to react, e.g., the video processing or feed services).

**Video Processing Service:** 
- This service is responsible for handling heavy video processing tasks, 
- such as transcoding videos to different resolutions or generating thumbnails. 
- In a real system, these tasks can be CPU-intensive or take a long time, so it’s best to do them asynchronously. 
- The Metadata service will enqueue a message (event) to a Video Processing Queue (implemented via a Kafka topic) whenever a new video is uploaded. 
- The Video Processing service, which runs independently (and can be scaled out with multiple worker instances), will consume from this queue. 
- It will simulate processing (for example, we might just wait or log instead of actually encoding video in this tutorial) and then store results (e.g. a thumbnail image or a processed video file) into a Distributed File Storage. 
- For our purposes, we can imagine the distributed storage as an external system or simply use the local filesystem or a simple storage service. 
- The diagram shows Image/Thumbnail Storage and Video Storage, which in a real scenario could be cloud storage buckets or a CDN. 
- We will simulate this by writing to a local path and perhaps serving those files via a static content server.

**Search Service (Search Results Aggregator):** 
- This service provides search functionality over our content. 
- We will use Elasticsearch (or OpenSearch) as the search index to allow full-text searching of video metadata (title, description, etc.). 
- The Search service will be responsible for updating the search index when new content comes in (consuming the content event, similar to the video processor) and for handling search queries from clients. 
- The “Search Results Aggregator” might combine results from multiple indexes or shards – in our simplified implementation, we’ll assume a single search index, but the concept of aggregator could apply if we had multiple different sources. 
- We will also incorporate caching here: for example, caching popular search queries in Redis to avoid hitting Elasticsearch for repeated queries.

**Notification Service:** 
- This service sends notifications to users. For example, if a new video is uploaded by someone you follow, or if someone liked your video, a notification should be sent (email, SMS, in-app notification, etc.). 
- The Notification service will listen on a Notification Queue (Kafka topic) for events like “new video for follower” or “new like/comment” and then perform the action of sending out the notification. 
- In our tutorial, we might simulate this by printing to logs or using a dummy email service. 
- The key idea is that this service is decoupled via a queue – producers of notification events (like the Feed service or some Social service) will just drop messages in Kafka, and this service will consume and handle them asynchronously.

**Feed Generation Service:** 
- This service generates the user’s feed (the list of videos they should see when they open the app). 
- The feed is a combination of videos from the user’s own uploads and videos from the users they follow. 
- The Feed service will consume from the Video Processing Queue and generate the feed for each user. 
- It will also consume from the Notification Queue and generate notifications for the user. 
- The Feed service will produce events to Kafka for each user’s feed and notifications.

**CDN/Static Content Server:** 
- Static content (like video files and images) is typically served from a specialized storage system or Content Delivery Network (CDN) rather than directly by microservices. 
- In our design, once the Video Processing service produces output (like processed videos or thumbnails), these files would be stored in a storage service that can deliver them efficiently to users, often via a CDN for global distribution. 
- We will not build a CDN from scratch, but we will discuss how to simulate static content serving. 
- For instance, we might run an Nginx server container that serves files from a directory, to represent our CDN. 
- Clients would get URLs for video playback or images that point to this static server.

**Data Warehousing & Analytics:** 
- Over time, the system will generate a lot of data: user activity logs, content metadata, etc. 
- A Data Warehouse is an offline big-data system that aggregates this information for reporting and analysis. 
- The template shows components like Hadoop/MapReduce, Spark (for big data processing), Distributed Scheduler & Workers (for running batch jobs or periodic tasks), and a separate analytics database that stores metrics and reports. 
- Implementing a full Hadoop or Spark pipeline is beyond our scope, but we will simulate an analytics pipeline on a smaller scale. 
- Specifically, we’ll introduce an Analytics service that listens to certain event streams (for example, we could have all events – video uploads, user signups, etc. – also published to an “analytics” topic). 
- This service could, for example, count events or compile simple statistics (like number of videos uploaded per day) and store them in an Analytics DB (which could just be another PostgreSQL schema/table). 
- We will then demonstrate how those analytics can be used – for instance, feeding data to our frontend analytics dashboard. 
- This approximates how a data warehousing process might produce data for business intelligence. 
- Additionally, we’ll mention tools for distributed logging and tracing: in a real system, services would emit logs to a centralized system (like ELK stack – Elasticsearch/Logstash/Kibana) and use distributed tracing (like Jaeger or OpenTelemetry) to trace requests across service boundaries. 
- We will ensure our services have basic logging, and we might integrate a simple trace ID propagation using Spring Boot’s observability features, but a full tracing system setup might be only briefly discussed.

**Observability:** 
- Monitoring the health and performance of microservices is crucial. 
- We’ll integrate Prometheus for metrics collection – each service will expose metrics (thanks to Spring Boot Actuator and Micrometer integration), and Prometheus will scrape these metrics periodically (things like request rates, error counts, JVM memory, etc.). 
- We will also include Grafana (an optional component) to visualize these metrics on dashboards. 
- Logging, as mentioned, can be centralized, and we’ll ensure that each service’s logs include context (like a correlation ID or trace ID) to help in debugging across services.
