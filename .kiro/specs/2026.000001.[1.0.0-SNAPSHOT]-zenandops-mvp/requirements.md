# Requirements Document

## Introduction

ZenAndOps is an IT Service Management (ITSM) platform that bridges ITIL best practices with modern Site Reliability Engineering (SRE) principles. This MVP delivers the foundational authentication system (login, refresh token, logoff) and an operational dashboard that aggregates ITIL and SRE metrics. The backend follows a microservices architecture with hexagonal design and DDD, built on Java 25 with Quarkus, MongoDB, and Kafka. The frontend is a React PWA using JWT-based authentication, derived from the `.frontend-template` TailAdmin template. All services are containerized with Docker and orchestrated via Docker Compose.

## Glossary

- **Auth_Service**: The backend microservice responsible for user authentication, token issuance, token refresh, and session termination
- **Dashboard_Service**: The backend microservice responsible for aggregating data from multiple sources and producing a summary payload for the dashboard page
- **Frontend_App**: The React Progressive Web Application that provides the user interface for login, logoff, and dashboard visualization
- **User**: A person who interacts with the ZenAndOps platform through the Frontend_App
- **JWT**: JSON Web Token, a compact token format used for stateless authentication between the Frontend_App and backend services
- **Access_Token**: A short-lived JWT issued by the Auth_Service that grants access to protected resources
- **Refresh_Token**: A long-lived token issued by the Auth_Service that allows obtaining a new Access_Token without re-authentication
- **RBAC**: Role-Based Access Control, an authorization model where permissions are assigned to roles and roles are assigned to users
- **ABAC**: Attribute-Based Access Control, an authorization model where access decisions are based on attributes of the user, resource, action, and environment
- **Policy_Engine**: The component within the Auth_Service that evaluates RBAC and ABAC rules to determine authorization decisions
- **Dashboard_Payload**: The JSON data structure returned by the Dashboard_Service containing all aggregated metrics for the dashboard page
- **Ticket**: An ITIL work item representing an incident or service request, tracked through a defined lifecycle
- **SLI**: Service Level Indicator, a quantitative measure of a specific aspect of service quality
- **SLO**: Service Level Objective, a target value or range for an SLI over a time window
- **MTTR**: Mean Time to Recovery, the average time to restore service after an incident
- **MTTD**: Mean Time to Detect, the average time to detect an incident after it begins
- **Error_Budget**: The allowed amount of unreliability within an SLO window, calculated as 1 minus the SLO target
- **Hexagonal_Architecture**: A software architecture pattern that isolates the core domain logic from external concerns through ports and adapters
- **DDD**: Domain-Driven Design, a software design approach that models software based on the business domain

## Requirements

### Requirement 1: User Authentication via Login

**User Story:** As a User, I want to authenticate with my credentials, so that I can access the ZenAndOps platform securely.

#### Acceptance Criteria

1. WHEN a User submits valid login and password credentials, THE Auth_Service SHALL issue an Access_Token and a Refresh_Token to the Frontend_App
2. WHEN a User submits invalid credentials, THE Auth_Service SHALL return an HTTP 401 status code with an error message indicating authentication failure
3. THE Auth_Service SHALL store User records in MongoDB with the following fields: login, name, email, and a securely hashed password
4. THE Auth_Service SHALL hash all User passwords using the bcrypt algorithm before storing them in MongoDB
5. WHEN the Auth_Service issues an Access_Token, THE Access_Token SHALL contain the User identity, assigned roles, and user attributes as JWT claims
6. WHEN the Auth_Service issues an Access_Token, THE Auth_Service SHALL set the token expiration to 15 minutes
7. WHEN the Auth_Service issues a Refresh_Token, THE Auth_Service SHALL set the token expiration to 8 hours

### Requirement 2: Token Refresh

**User Story:** As a User, I want my session to be extended transparently, so that I do not need to re-authenticate frequently during active use.

#### Acceptance Criteria

1. WHEN the Frontend_App sends a valid, non-expired Refresh_Token to the Auth_Service, THE Auth_Service SHALL issue a new Access_Token and a new Refresh_Token
2. WHEN the Frontend_App sends an expired Refresh_Token, THE Auth_Service SHALL return an HTTP 401 status code and the Frontend_App SHALL redirect the User to the login page
3. WHEN the Frontend_App sends a Refresh_Token that has been revoked, THE Auth_Service SHALL return an HTTP 401 status code
4. THE Auth_Service SHALL invalidate the previous Refresh_Token after a successful token refresh (rotation)
5. WHEN the Frontend_App detects that the Access_Token has expired, THE Frontend_App SHALL automatically attempt a token refresh before redirecting to the login page

### Requirement 3: User Logoff

**User Story:** As a User, I want to terminate my session explicitly, so that my account remains secure when I stop using the platform.

#### Acceptance Criteria

1. WHEN a User initiates a logoff action, THE Auth_Service SHALL revoke the active Refresh_Token associated with the session
2. WHEN a User initiates a logoff action, THE Frontend_App SHALL remove the Access_Token and Refresh_Token from local storage
3. WHEN a User initiates a logoff action, THE Frontend_App SHALL redirect the User to the login page
4. WHEN a logoff request is received with an invalid or missing Access_Token, THE Auth_Service SHALL return an HTTP 401 status code

### Requirement 4: Role-Based and Attribute-Based Access Control

**User Story:** As an administrator, I want to control access to platform features using roles and attributes, so that users only access resources appropriate to their authorization level.

#### Acceptance Criteria

1. THE Auth_Service SHALL support assigning one or more roles to each User record in MongoDB
2. THE Policy_Engine SHALL evaluate RBAC rules by matching User roles against the required roles for a requested resource
3. THE Policy_Engine SHALL evaluate ABAC rules by matching User attributes, resource attributes, and environment attributes against defined access policies
4. WHEN a User requests a protected resource without the required role or matching attributes, THE Policy_Engine SHALL deny access and the Auth_Service SHALL return an HTTP 403 status code
5. THE Auth_Service SHALL include User roles and attributes in the Access_Token JWT claims so that the Frontend_App can perform client-side authorization checks
6. THE Frontend_App SHALL hide or disable UI elements for which the authenticated User does not have the required role or attributes

### Requirement 5: Frontend Login Page

**User Story:** As a User, I want a login page with a clear and accessible form, so that I can authenticate and access the platform.

#### Acceptance Criteria

1. THE Frontend_App SHALL display a login page with input fields for login and password, and a submit button
2. WHEN the User submits the login form with empty fields, THE Frontend_App SHALL display inline validation messages indicating required fields
3. WHEN the Auth_Service returns a successful authentication response, THE Frontend_App SHALL store the Access_Token and Refresh_Token securely and redirect the User to the dashboard page
4. WHEN the Auth_Service returns an authentication error, THE Frontend_App SHALL display an error message on the login page without revealing whether the login or password was incorrect
5. WHILE the login request is in progress, THE Frontend_App SHALL display a loading indicator and disable the submit button to prevent duplicate submissions
6. THE Frontend_App SHALL derive its login page layout and styling from the `.frontend-template` SignIn page component

### Requirement 6: Frontend Protected Routes

**User Story:** As a User, I want unauthenticated access to be blocked from protected pages, so that the platform remains secure.

#### Acceptance Criteria

1. WHEN an unauthenticated User attempts to access a protected route, THE Frontend_App SHALL redirect the User to the login page
2. WHEN an authenticated User navigates to the login page, THE Frontend_App SHALL redirect the User to the dashboard page
3. THE Frontend_App SHALL attach the Access_Token as a Bearer token in the Authorization header of every API request to protected backend endpoints
4. WHEN an API response returns an HTTP 401 status code, THE Frontend_App SHALL attempt a token refresh and retry the original request once before redirecting to the login page

### Requirement 7: Operational Dashboard Page

**User Story:** As a User, I want to see an operational dashboard with ITIL and SRE metrics, so that I can monitor service health and operational performance at a glance.

#### Acceptance Criteria

1. WHEN an authenticated User navigates to the dashboard page, THE Frontend_App SHALL request the Dashboard_Payload from the Dashboard_Service
2. THE Dashboard_Service SHALL return a Dashboard_Payload containing: an executive summary, ticket counts grouped by state, SLI/SLO compliance percentages, MTTR and MTTD values, error budget consumption percentage, and change failure rate
3. THE Frontend_App SHALL display the executive summary as a set of metric cards showing key operational indicators
4. THE Frontend_App SHALL display ticket counts grouped by ITIL lifecycle state (New, Processing, Pending, Solved, Closed) using a bar chart or equivalent visualization
5. THE Frontend_App SHALL display SLI/SLO compliance as a gauge or percentage indicator
6. THE Frontend_App SHALL display MTTR and MTTD values as metric cards with trend indicators
7. THE Frontend_App SHALL display error budget consumption as a progress bar or gauge visualization
8. THE Frontend_App SHALL derive its dashboard layout and component styling from the `.frontend-template` Dashboard Home page and ecommerce metric components

### Requirement 8: Dashboard Data Aggregation with Mocked Sources

**User Story:** As a developer, I want the dashboard service to aggregate data from mocked sources, so that the MVP dashboard is functional before upstream services are available.

#### Acceptance Criteria

1. THE Dashboard_Service SHALL aggregate data from mock data providers that simulate: ticket management, SLI/SLO monitoring, incident metrics, and change management sources
2. THE Dashboard_Service SHALL produce realistic mock data that follows ITIL ticket lifecycle states (New, Processing Assigned, Processing Planned, Pending, Solved, Closed)
3. THE Dashboard_Service SHALL produce realistic mock SRE metrics including: availability SLI percentage, latency SLI percentage, MTTR in minutes, MTTD in minutes, error budget remaining percentage, and change failure rate percentage
4. THE Dashboard_Service SHALL expose a single REST endpoint that returns the complete Dashboard_Payload as a JSON response
5. WHEN the Dashboard_Service is unable to aggregate data from a mock provider, THE Dashboard_Service SHALL return a partial Dashboard_Payload with an error indicator for the unavailable section

### Requirement 9: Backend Hexagonal Architecture and DDD Structure

**User Story:** As a developer, I want the backend services to follow hexagonal architecture with DDD, so that the domain logic is decoupled from infrastructure concerns and the codebase is maintainable.

#### Acceptance Criteria

1. THE Auth_Service SHALL organize its codebase into domain, application, and infrastructure layers following Hexagonal_Architecture principles
2. THE Dashboard_Service SHALL organize its codebase into domain, application, and infrastructure layers following Hexagonal_Architecture principles
3. THE Auth_Service SHALL define ports (interfaces) for all external interactions including persistence, messaging, and token generation
4. THE Auth_Service SHALL implement adapters for MongoDB persistence, Kafka messaging, and JWT token operations
5. THE Dashboard_Service SHALL define ports (interfaces) for all data source interactions
6. THE Dashboard_Service SHALL implement adapters for mock data providers and REST endpoint exposure

### Requirement 10: Infrastructure and Containerization

**User Story:** As a developer, I want all services containerized and orchestrated with Docker Compose, so that the entire MVP can be started with a single command.

#### Acceptance Criteria

1. THE Auth_Service SHALL include a Dockerfile that produces a container image based on a Java 25 runtime
2. THE Dashboard_Service SHALL include a Dockerfile that produces a container image based on a Java 25 runtime
3. THE Frontend_App SHALL include a Dockerfile that produces a container image serving the built React application via a web server
4. THE Docker_Compose configuration SHALL define services for: Auth_Service, Dashboard_Service, Frontend_App, MongoDB, and Kafka
5. THE Docker_Compose configuration SHALL configure network connectivity so that the Frontend_App can reach backend services and backend services can reach MongoDB and Kafka
6. THE Docker_Compose configuration SHALL include health checks for MongoDB and Kafka to ensure dependent services start only after infrastructure is ready
7. THE Docker_Compose configuration SHALL use environment variables for all configurable values including database connection strings, Kafka broker addresses, JWT secret keys, and service ports

### Requirement 11: Kafka Event Publishing for Authentication Events

**User Story:** As a developer, I want authentication events published to Kafka, so that other services can react to login, logoff, and token refresh events asynchronously.

#### Acceptance Criteria

1. WHEN a User successfully authenticates, THE Auth_Service SHALL publish a login event to a Kafka topic
2. WHEN a User successfully logs off, THE Auth_Service SHALL publish a logoff event to a Kafka topic
3. WHEN a token refresh is successfully completed, THE Auth_Service SHALL publish a token refresh event to a Kafka topic
4. THE Auth_Service SHALL include the User identity, event type, and timestamp in every published Kafka event
5. IF the Kafka broker is unavailable, THEN THE Auth_Service SHALL log the event publishing failure and continue processing the authentication request without blocking

### Requirement 12: Frontend Progressive Web Application Configuration

**User Story:** As a User, I want the frontend to be a Progressive Web Application, so that I can install it on my device and have an app-like experience.

#### Acceptance Criteria

1. THE Frontend_App SHALL include a web app manifest file with application name, icons, theme color, and display mode set to standalone
2. THE Frontend_App SHALL register a service worker that caches static assets for offline access to the login page shell
3. THE Frontend_App SHALL be installable on supported browsers and devices as a PWA
