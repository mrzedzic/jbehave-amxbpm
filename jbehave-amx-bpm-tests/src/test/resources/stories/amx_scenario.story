Narrative:
In order to make story development easier
As a Story Developer
I want to test if all users are able to start process

Scenario: Users are registered in LDAP on proper positions
Given zarejestrowanego <user>
Then system sprawdzi czy jest on na pozycji <position>

Examples:
|user				|position			|
|Pawel Kukla		|Partner Biznesowy	|
|Marek Kozierski	|Partner Kredytowy	|
|Dyrektor Biznesowy	|Dyrektor Biznesowy	|
|Dyrektor Kredytowy	|Dyrektor Kredytowy	|


Scenario: "Business partner" is able to start process
Given rozpoczety proces BzWbkTTY przez
|processOwner		|
|Pawel Kukla		|
|Marek Kozierski	|
When system alokuje zadanie <taskName> na <taskOwner>
Then <taskOwner> otworzy formularz wyszukania Klienta w CIS i wyszuka <clientName>


Examples:
|taskName			|taskOwner	|clientName|
|WybrKlientaiGrupy	|Pawel Kukla|PKP|
|WybrKlientaiGrupy	|Pawel Kukla|PKP|