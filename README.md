Carpooling 
Share the ride. 

Project Description

Carpooling is a web application that enables you to share your travel from one location to another with other passengers. Every user can either organize a shared travel or request to join someone else’s travel.

Functional Requirements

Entities

· Each user must have a username, password, first name, last name, email, and phone number.

o Username must be unique and between 2 and 20 symbols.

o Password must be at least 8 symbols and should contain capital letter, digit and special symbol (+, -, *, &, ^, …)

o First and last names must be between 2 and 20 symbols.

o Email must be valid email and unique in the system.

o Phone number must be 10 digits and unique in the system.

· Each travel must have a starting and ending points, departure time, number of free spots and, optional comment (e.g. no smoking/luggage)

Public Part

The public part must be accessible without authentication i.e., for anonymous users.

Anonymous users must be able to register and login. Registration of a new user should use email verification flow.

Anonymous users must see detailed information about Carpooling and its features as well as how many people are using the platform and how many travels have happened.

Anonymous users should be able to see a list of the top 10 travel organizers and top 10 passengers.

Private part

Accessible only if the user is authenticated.

Users must be able to login/logout, update their profile, set a profile photo/avatar.

Each user must be able to create a new travel that he is planning.

Each user must be able to browse the available trips created by other users with an option to sort and filter them. List with trips should support pagination.

Each user must be able to apply for a trip as a passenger. The driver can approve/decline passengers from the candidates’ pool. After approval from the driver, a passenger still can cancel his participation and driver still can reject a passenger.

The driver must be able to cancel a trip before the departure time. He also must be able to mark the trip as complete.

When the trip is complete the passengers must be able to leave feedback about the driver as well as the driver must be able to leave feedback for every passenger. Feedback must include numeric rating (from 0 to 5) and optional text comment.

Each user must be able to view all his travels (with options to filter and sort them), all his feedback and all feedback for any other user. List with travels/feedback should support pagination.

Administrative part

Accessible to users with administrative privileges.

Admin users must be able to see the list of all users and search them by phone number, username or email and block or unblock them. A blocked user must not be able to create travels and apply for travels. List with users should support pagination.

Admin users must be able to view a list of all travels with option to filter and sort them. Lists with travels should support pagination.

Optional features

Email Verification – In order for the registration to be completed, the user must verify their email by clicking on a link sent to their email by the application. Before verifying their email, users cannot participate in rides.

Easter eggs – Creativity is always welcome and appreciated. Find a way to add something fun and/or interesting, maybe an Easter egg or two to your project to add some variety.

REST API

To provide other developers with your service, you need to develop a REST API. It should leverage HTTP as a transport protocol and clear text JSON for the request and response payloads.

A great API is nothing without great documentation. The documentation holds the information that is required to successfully consume and integrate with an API. You must use Swagger to document yours.

The REST API provides the following capabilities:

1. Users

· CRUD operations (must)

· Block/unblock user (must)

· Search by username, email, or phone (must)

2. Travels

· CRUD operations (must)

· Apply for a travel (must)

· List and manage (approve/reject) applicants (must)

· Filter and sort travels (must)

External Services

The Capooling web application will consume one public REST services to achieve the main functionality.

Microsoft Bing Maps

Maps External Service – In order to have a precise location and calculate the ride duration between two addresses, you can use Microsoft Bing Maps External Service. You can see instructions on how to use it below.

Technical Requirements

General · Follow OOP principles when coding · Follow KISS, SOLID, DRY principles when coding · Follow REST API design best practices when designing the REST API (see Appendix)

· Use tiered project structure (separate the application in layers)

· The service layer (i.e., "business" functionality) must have at least 80% unit test code coverage · Follow BDD when writing unit tests

· You should implement proper exception handling and propagation

· Try to think ahead. When developing something, think – “How hard would it be to change/modify this later?”

Database

The data of the application must be stored in a relational database. You need to identify the core domain objects and model their relationships accordingly. Database structure should avoid data duplication and empty data (normalize your database).

Your repository must include two scripts – one to create the database and one to fill it with data.

Git

Commits in the GitHub repository should give a good overview of how the project was developed, which features were created first and the people who contributed. Contributions from all team members must be evident through the git commit history! The repository must contain the complete application source code and any scripts (database scripts, for example).

Provide a link to a GitHub repository with the following information in the README.md file:

· Project description

· Link to the Swagger documentation (must)

· Link to the hosted project (if hosted online)

· Instructions how to setup and run the project locally

· Images of the database relations (must)

Optional Requirements

Besides all requirements marked as should and could, here are some more optional requirements:

· Use branching while working with Git.

· Integrate your app with a Continuous Integration server (e.g., GitHub’s own) and configure your unit tests to run on each commit to the master branch.

· Host your application’s backend in a public hosting provider of your choice (e.g., AWS, Azure, Heroku).

Teamwork Guidelines

Please see the Teamwork Guidelines document.

Appendix · Guidelines for designing good REST API · Guidelines for URL encoding · Always prefer constructor injection · Git commits - an effective style guide

· How to Write a Git Commit Message · Microsoft Bing Maps External Service Docs

Microsoft Bing Maps External Service

You can use this external service to calculate the ride duration between two addresses.

Note: We’re using this service because it’s free. It doesn’t require any payment information to be used, the registration process is straight forward as well as the generation and usage of API Key.

Because of the service limitations, for addresses outside of the US, we can only use travelMode=driving. We can’t use walking or transit. The point is to get familiar with consuming a REST service, understanding its domain and do some data transformations.

API Key

Each team needs to register at least one account and get a free API key, which will allow them to make HTTP calls to the REST service. Each API key is limited in the number of requests etc. so it’s not advisable to share your API key with another team as it may lead to locking or completely disabling the API key from Bing Maps. The API key is passed to every HTTP request in “key” query parameter.

Integration Guidelines

Locations

The first API endpoint that needs to be called is “Locations” and we will use it to find a Location by address. (official docs). We will use the structured URL form, which specifies the location query parameters as part of the URL path:

http://dev.virtualearth.net/REST/v1/Locations/{countryRegion}/{adminDi strict}/{postalCode}/{locality}/{addressLine}?key=<YOUR_API_KEY>

For example, the structured URL query for Telerik Academy’s Location will look like:

http://dev.virtualearth.net/REST/v1/Locations/BG/Sofia%20City/Mladost/ 1729/Alexandar%20Malinov%2031?key=<YOUR_API_KEY>

The result contains a collection of “geocodePoints”. If there’s more than one Geo Point returned, we want the one of usageType “route”.

Note: You can use the lat/long values returned as a result in any mapping service (e.g. Google Maps) just to test if your results are correct.

Distance Matrix

The next API endpoint that we will use is the Route’s API distance matrix. which can calculate the time to travel from point A (lat, long) to point B (lat, long) for us. Official docs. For example, the URL to calculate the distance between The National Place of Culture and Telerik Academy will look like:

https://dev.virtualearth.net/REST/v1/Routes/DistanceMatrix?origins=42.685428619384766,23.318979263305664&destinations=42.6508241,23.3790428&travelMode=driving&key=<YOUR_API_KEY>

Legend

· Must – Implement these first.

· Should – if you have time left, try to implement these.

· Could – only if you are ready with everything else give these a go.
