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
Given rozpoczety proces BzWbkTTY przez <processOwner>
When system zaoferuje zadanie <taskName1> na <taskOwner1>
Then otworzy <taskOwner1> formularz i uzupelni go danymi <formName1> oraz zatwierdzi
When system zaoferuje zadanie <taskName2> na <taskOwner2>
Then otworzy <taskOwner2> formularz i uzupelni go danymi <formName2> oraz zatwierdzi
When system zaoferuje zadanie <taskName3> na <taskOwner3>
Then otworzy <taskOwner3> formularz i uzupelni go danymi <formName3> oraz zatwierdzi

Examples:
|processOwner|taskName1		  |taskOwner1 |formName1	  |taskName2		|taskOwner2 |formName2			  |taskName3		|taskOwner3 |formName3			  |
|Pawel Kukla|WybrKlientaiGrupy|Pawel Kukla|PKPWybrKlienta1|KolejkaprzedRONES|Pawel Kukla|PKPKolejkaPrzedRones1|RONESAkceptacjadyrektorabiznesowegoTeamLeadera|Dyrektor Biznesowy|PKPKolejkaPrzedRones1|
