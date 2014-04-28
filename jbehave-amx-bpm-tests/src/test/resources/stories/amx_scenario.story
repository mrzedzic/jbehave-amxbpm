
Scenario: Login to AMX BPM local instance
Given As I am PRM logged as tibco-admin 
Then I would like to login to openspace

Mając zalogowanego użytkownika Paweł Kukla
kiedy uruchomi on proces <nazwa procesu>
kiedy możliwe będzie alokowane na niego zadania <nazwa tasku>
wtedy otworzy formularz uzupełnienia danych wniosku
wtedy przed wyslaniem uzupelni formularz wniosku
kiedy możliwe będzie alokowane na niego zadania <nazwa tasku>
kiedy zobaczy formularz uzupełnienia danych wniosku
