# Full Stack Developer Challenge

## Quick Start

1. Ensure you have [Docker](https://www.docker.com/) installed on your machine.

2. Clone the repository, build, and run.
  
```
$ git clone https://github.com/mattcarpenter/FullStackEngineeringChallenge.git
$ cd FullStackEngineerChallenge
$ docker-compose up
```

3. Navigate your browser to [http://localhost:3080/](http://localhost:3080) and login using the email address `admin@admin.com` and password `Test1234$$`.

> Note: Login sessions are maintained using a Json Web Token set as an HttpOnly cookie by the API upon a login. To reduce the scope of this challenge, the jwt is signed using a private key that is simply auto-generated on service signup.
>
> If you restart the API, any token cookies remaining in your browser will no longer validate. You'll need to visit the login page again or clear your localStorage.

## API Documentation
After starting the service, you may view the Swagger API docs here: [http://localhost:8080/api/v1/swagger-docs.html](http://localhost:8080/api/v1/swagger-docs.html)

## Overview of Technologies Used

### Frontend

* React / Redux
* MaterialUI

### Backend

* Java JDK 11
* Spring Boot
* JPA / Hibernate
* TestNG / RestAssured

### Database

NoSQL options were considered as they're easy to prototype with but I opted for **PostgreSQL**. 

It's possible that a team building an in-house performance review system is a cost center within the company, so I'd be more inclined to choose technologies that can be used by the largest number of people (engineers, DBAs, business analysts) and are relatively inexpensive to build upon in the future.

NoSQL is great for housing non-relational data, but this design specifies a number of relations between different entities such as employees, performance reviews, feeback requests, templates, and template fields. Relationships can certainly be modeled in NoSQL datastores, but the designs become quite a bit more complex. This complexity buys performance, but that may not be a huge concern for this application (assuming it's an in-house system, not commercial SaaS.)

## Requirements

The following gap analysis table highlights the gaps in the original requirements and describes how this design intends to address them.

| Requirement | Gap | Decision |
|-|-|-|
|Delete Employees|The requirement does imply any particular data retention policies|Upon deletion, a delete flag will be set on the employee record in the database. The record will remain for auditing purposes and any feedback requests for the deactivated employee will remain open.
|View Employees|Employee attributes are not specified (i.e. what does a user view?)|First name, last name, and an email address will be stored.|
|Add Performance Review|Does not specify the attributes of a performance review.|Performance review will not have a due date (however, feedback requests assigned to employees will.) Performance reviews will simply contain a memo (e.g.: "Alice's EOY review")|
|Assign employees to participate in anothe employee's performance review|There are no requirements or user stories describing what "participation" means|Admin will create feedback requests for a performance review. Feedback requests have an assignee, due date, and a pre-defined, named questionnaire (e.g.: peer feedback, leader feedback, 360 feedback) consisting of freeform text inputs and multiple choice questions. Templates and questions are stored in the database.|
|Submit feedback|Due date? What defines "feedback"? Assuming freeform text can be inputted, are there length requirements?|Due date will be specified when an admin creates a feedback request on a performance review. Questions come from a pre-defined questionare in the database, of which many can exist. There will be no length requirements on freeform text feedback.

## Assumptions
* This is intended to be an in-house performance review system, not software to be sold as a service.
* Only English language support is expected (no localization or internationalization will be implemented.)
* Lack of HTTPS support is acceptable for this challenge, as to not burden the interview team with certificate creation, browser security exceptions, etc...
* An admin is an employee with elevated privileges.
* Employee organizational structure should be flat (An admin can view/update/delete all employees, including other admins.)
* Sessions should be short-lived (tokens will have a 24 hour expiration)
* Authentication is password-based. Employees will log in using their email address and a password.
* Web application should be responsive.
* Application does not need to be operationalized for the purposes of this challenge. Infrastructure as Code, build and delivery pipeline design, robust database migrations, etc... are out of scope.
