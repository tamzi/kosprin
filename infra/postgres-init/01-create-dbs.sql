-- Per-service databases. Each service owns its schema; cross-service reads go through APIs or Kafka.
CREATE DATABASE videoapp_db;
CREATE DATABASE feed_db;
CREATE DATABASE analytics_db;
