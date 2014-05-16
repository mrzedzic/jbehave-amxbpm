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
When system zaoferuje zadanie <taskName4> na <taskOwner4>
Then otworzy <taskOwner4> formularz i uzupelni go danymi <formName4> oraz zatwierdzi
When system zaoferuje zadanie <taskName5> na <taskOwner5>
Then otworzy <taskOwner5> formularz i uzupelni go danymi <formName5> oraz zatwierdzi
When system zaoferuje zadanie <taskName6> na <taskOwner6>
Then otworzy <taskOwner6> formularz i uzupelni go danymi <formName6> oraz zatwierdzi
When system zaoferuje zadanie <taskName7> na <taskOwner7>
Then otworzy <taskOwner7> formularz i uzupelni go danymi <formName7> oraz zatwierdzi
When system zaoferuje zadanie <taskName8> na <taskOwner8>
Then otworzy <taskOwner8> formularz i uzupelni go danymi <formName8> oraz zatwierdzi
When system zaoferuje zadanie <taskName9> na <taskOwner9>
Then otworzy <taskOwner9> formularz i uzupelni go danymi <formName9> oraz zatwierdzi
When system zaoferuje zadanie <taskName10> na <taskOwner10>
Then otworzy <taskOwner10> formularz i uzupelni go danymi <formName10> oraz zatwierdzi
When system zaoferuje zadanie <taskName11> na <taskOwner11>
Then otworzy <taskOwner11> formularz i uzupelni go danymi <formName11> oraz zatwierdzi
Examples:
|processOwner|taskName1	|taskOwner1	|formName1	|taskName2	|taskOwner2	|formName2	|taskName3	|taskOwner3	|formName3	|taskName4	|taskOwner4	|formName4	|taskName5	|taskOwner5	|formName5	|taskName6	|taskOwner6	|formName6	|taskName7	|taskOwner7	|formName7	|taskName8	|taskOwner8	|formName8	|taskName9	|taskOwner9	|formName9	|taskName10	|taskOwner10	|formName10	|taskName11	|taskOwner11	|formName11|
|Pawel Kukla|WybrKlientaiGrupy	|Pawel Kukla	|PKPWybrKlienta1	|KolejkaprzedRONES	|Pawel Kukla	|PKPKolejkaPrzedRones1	|RONESAkceptacjadyrektorabiznesowegoTeamLeadera	|Dyrektor Biznesowy	|PKPKolejkaPrzedRones1	|RONESAkceptacjadyrektorakredytowegoSPK	|Dyrektor Kredytowy	|PKPKolejkaPrzedRones1	|Uzupenianiedokumentw	|Pawel Kukla	|PKPKolejkaPrzedRones1	|Strukturyzowanietransakcji	|Marek Kozierski	|PKPKolejkaPrzedRones1	|RekomendacjaPartneraBiznesowego	|Pawel Kukla	|PKPKolejkaPrzedRones1	|RekomendacjaPartneraKredytowego	|Marek Kozierski	|PKPKolejkaPrzedRones1	|AkceptacjaDyrektoraBiznesowego	|Dyrektor Biznesowy	|PKPKolejkaPrzedRones1	|AkceptacjaDyrektoraKredytowego	|Dyrektor Kredytowy	|PKPKolejkaPrzedRones1	|PotwierdzeniezKlientem	|Pawel Kukla	|PKPKolejkaPrzedRones1|
|Marek Kozierski|WybrKlientaiGrupy	|Pawel Kukla	|PKPWybrKlienta1	|KolejkaprzedRONES	|Pawel Kukla	|PKPKolejkaPrzedRones1	|RONESAkceptacjadyrektorabiznesowegoTeamLeadera	|Dyrektor Biznesowy	|PKPKolejkaPrzedRones1	|RONESAkceptacjadyrektorakredytowegoSPK	|Dyrektor Kredytowy	|PKPKolejkaPrzedRones1	|Uzupenianiedokumentw	|Pawel Kukla	|PKPKolejkaPrzedRones1	|Strukturyzowanietransakcji	|Marek Kozierski	|PKPKolejkaPrzedRones1	|RekomendacjaPartneraBiznesowego	|Pawel Kukla	|PKPKolejkaPrzedRones1	|RekomendacjaPartneraKredytowego	|Marek Kozierski	|PKPKolejkaPrzedRones1	|AkceptacjaDyrektoraBiznesowego	|Dyrektor Biznesowy	|PKPKolejkaPrzedRones1	|AkceptacjaDyrektoraKredytowego	|Dyrektor Kredytowy	|PKPKolejkaPrzedRones1	|PotwierdzeniezKlientem	|Pawel Kukla	|PKPKolejkaPrzedRones1|