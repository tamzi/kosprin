## why no load balancer service?

load-balancer is essential in the runtime architecture, but it usually lives in the infrastructure layer, 
not as a Spring-Boot microservice that you build and version in the repository. 

It has already been represented [see the HIGH level architecture in the system diagram](systemdesign.md)

| Layer                     | Typical implementation                                                                                                                                             | Where it belongs                                        |
|---------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------|
| Edge / Traffic-management | - Cloud-provider LB (e.g. GCP HTTP(S) LB created by an Ingress)<br/>- Nginx / HAProxy in front of Docker-Compose stack <br/>- Kubernetes Service type=LoadBalancer | Infra / DevOps repo or Terraform—not in Kotlin codebase |
| Application               | - the other in the microservices (Gateway, Metadata, …)                                                                                                            | source codee                                            |


## Why you normally don’t add a “Load-Balancer microservice”
A load balancer terminates TLS(Transport Layer Security,), distributes traffic, enforces WAF/rate-limits at L7/L4. 
That is best done by a specialised, highly-optimised component (Envoy, Nginx, cloud LB) rather than a JVM process you maintain.

Typically infrastructure teams scale and upgrade LoadBalancers independently of application releases. 
Coupling it to your CI/CD pipeline would slow down your release cadence and application altogether.

It would be be really great to have kernel-level optimisations, not something depending on a managed runtime.

## Where to declare it
(**TO do a project to illustrate this!!!!**)
Docker Compose: 
 - you can rely on Docker’s built-in round-robin when multiple container replicas share the same service name.
 - Alternatively you can add an Nginx container in front if you need sticky sessions).

Kubernetes:

Local kind: 
- install Ingress-Nginx (kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/kind/deploy.yaml) and 
- create an Ingress pointing to gateway—the controller provisions an Nginx LB Pod.

GKE: 
- an Ingress object automatically provisions a Google Cloud HTTP(S) Load Balancer with a global anycast IP.

Cloud Infrastructure as Cloud:
in Terraform modules alongside VPC, subnets, TLS certs etc
