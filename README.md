# Integration Test for Test Directory Service
This is the integration app for testing the test Directory service API endpoints in automated way.

Means that it can be added as part of the Jenkins build and deployment pipeline allowing for proper CI/CD process.

## Trigger Tests

Tests can be triggered with the following command

~~~~
./gradlew clean test 
~~~~

## Standard Requirements for Test Directory Alpha

Brief for Back-end of Test Directory Alpha – Release 1
* Design and implement a consolidated data model to store the Test Directory (for both Cancer and Rare Diseases)
* Select and implement a basic solution for the ongoing management of Test Directory data that can also be incorporated into the backend build pipeline (enabling all development and test builds to include the latest Test Directory data without additional effort)
* Design and implement a working version of the Test Selection Service backend

The service should receive via API:
* A free text search string entered by the user (min. 3 characters), and/or
* Structured criteria entered by the user, and/or
* Structured criteria extracted from the patient record

The service should:
* Parse the free text search string to identify the structured criteria it likely represents
* Combine the parsed structured criteria with any other structured criteria provided, and identify (and prioritise) potential matches from the available Clinical Indications in the system

The service should return:
* The prioritised list of potentially-matching Clinical Indications
* The list of structured criteria potentially-matching the free text search string (to support auto-complete)
* The list of all structured criteria associated with the prioritised list of Clinical Indications (to support facetted search)
* The service should expect to be called as a background process while the user is typing in the interface, and should be sufficiently performant for on-screen results to update in real-time as the user is typing
* Implement a basic user interface for the purpose of demonstrating the backend features listed above
