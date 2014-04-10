
Scenario: Login to AMX BPM local instance
Given As I am PRM logged as tibco-admin 
Then I would like to login to openspace

Mając zalogowanego użytkownika Paweł Kukla
kiedy uruchomi on proces prosty <nazwa procesu>
kiedy możliwe będzie alokowane na niego zadania <nazwa tasku>
wtedy zobaczy formularz uzupełnienia danych wniosku
wtedy przed wyslaniem uzupelni formularz wniosku
kiedy możliwe będzie alokowane na niego zadania <nazwa tasku>
kiedy zobaczy formularz uzupełnienia danych wniosku


Scenario: Account has sufficient funds
Given the Account balance is <account_balance>
When the card is valid
When the machine contains <atm_available>
When the Account Holder requests <request>
Then the ATM should dispense <result>
Then the account balance should be <newBalance>
Then the card should be returned