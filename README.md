# JSON Resume

Tiimi: Iiro Niemi (HEROKU frontend), Pekka Korhonen (Tietokanta+REST-suunnittelu, suurin osa funktioista), Miikka Mononen (OAuth 2.0 implementointi)

## Johdanto

Projektin tavoitteena on tehdä helppo palvelu, jossa työnhakija voi helposti generoida itselleen visuaalisesti näyttävän CV:n graafisen käyttöliittymän avulla jolla hallinnoidaan JSON Resumea.

Projekti toteutetaan Spring Bootilla, jota varten tehdään REST-rajapinnalla toimiva erillinen päätesovellus tietokoneella käytettäväksi. Tietokantajärjestelmänä toimii JPA, ja tietokantatyyppinä SQL.

## Järjestelmän määrittely

Järjestelmä palvelee ihmisiä, jotka haluavat helposti hyvännäköisen verkkosivu-CV:n. Tällainen voi olla esimerkiksi Maija, 32-vuotias uutta uraa kahvakuulan maailmanmestaruuden voittamisen jälkeen tavoitteleva merkonomi. Hänellä ei ole kokemista koodauksesta, mutta verkko-CV on hyvä keino erottautua massasta. Maijan mestaruusvoittorahat ovat kiinni sijoitusrahastoissa toistaiseksi, joten hän siis tarvitsee **helposti** käytettävän **ilmaisen** palvelun, joka luo hänelle sellaisen.

## Käyttöliittymä

Käyttöliittymän keskeinen näkymä on CV:n yleisnäkymä, jossa näkyy perustiedot sekä listat työpaikoista, koulutuksesta, taidoista, kiinnostuksen kohteista jne. Jokaisessa listassa on siirtymät kyseisen listan muokkaamiseen (Lisää uusi, Muokkaa, Poista). Yleisnäkymän kautta pääsee myös avaamaan omien CV-tietojen perusteella generoidun JSON-tiedoston.
Valmiin sovelluksen käyttöliittymäkaavio on esitetty alla:

![](https://github.com/i1r0/ohjelmistoprojekti-hh/blob/master/img/ui/UI_mockup.png "Käyttöliittymäkaavio")


## Tietokanta

Tietokanta on suunniteltu noudattaen JSON Resume -projektissa määriteltyä JSON Schemaa (https://jsonresume.org/schema/).

### Tietokantakaavio
![](https://github.com/i1r0/ohjelmistoprojekti-hh/blob/master/img/database/database-model.png "Tietokantamalli")

### User
**_User_**-taulu sisältää käyttäjätilit.

| Avain | Kenttä       | Tyyppi      | Huomioitavaa |
|:-----:| ------------ |:-----------:| --- |
| PK    | userId       | int         ||
|       | username     | varchar(20) ||
|       | passwordHash | binary(60)  | Spring Securityn oletuksena käyttämän bCryptin hashien pituus 60 merkkiä |

### Resume
**_Resume_**-taulu sisältää käyttäjän CV- eli Resume-tiedot. JSON Resume Schemasta tämä taulu sisältää CV:n perus- ja osoitetiedot eli suurimman osan _basics_ osiosta sekä sen sisältämän _location_ osion. _Profiles_ lista on toteutettu omaan tauluunsa **_OnlineProfile_**.

![](https://github.com/i1r0/ohjelmistoprojekti-hh/blob/master/img/database/JSONcontent_resume_table.PNG "Resume-taulun sisältö")

| Avain | Kenttä        | Tyyppi      | Huomioitavaa |
|:-----:| ------------- |:-----------:| --- |
| PK    | resumeId      | int         ||
| FK    | userId        | int         | viittaus _User_-tauluun |
|       | personName    | varchar(50) ||
|       | personLabel   | varchar(50) ||
|       | personPicture | blob        ||
|       | email         | varchar(50) ||
|       | phone         | varchar(50) ||
|       | website       | varchar(50) ||
|       | summary       | varchar(300) ||
|       | address       | varchar(150) ||
|       | postalCode    | varchar(50) ||
|       | city          | varchar(50) ||
|       | countryCode   | varchar(50) ||
|       | region        | varchar(50) ||

### OnlineProfile
**_OnlineProfile_**-taulu sisältää käyttäjän online-profiilit jotka halutaan listata CV:ssä (esim. Twitter, GitHub tms). JSON Resume Schemasta tämä taulu sisältää _basics_ osioon kuuluvan _profiles_ listan.

![](https://github.com/i1r0/ohjelmistoprojekti-hh/blob/master/img/database/JSONcontent_profile_table.PNG "OnlineProfile-taulun sisältö")

| Avain | Kenttä        | Tyyppi      | Huomioitavaa |
|:-----:| ------------- |:-----------:| --- |
| PK    | profileId     | int         ||
| FK    | resumeId      | int         | viittaus _Resume_-tauluun |
|       | networkName   | varchar(50) ||
|       | userName      | varchar(50) ||
|       | url           | varchar(50) ||

### Position
**_Position_**-taulu sisältää käyttäjän työpaikat ja vapaaehtoistyöt. Nämä erotetaan toisistaan _positionType_ kentällä, joka viittaa tauluun **_PositionType_**. JSON Resume Schemasta tämä taulu sisältää _work_ ja _volunteer_ osiot lukuunottamatta molemmissa esiintyvää _highlights_ listaa, joka on toteutettu omaan tauluunsa **_Highlights_**.

![](https://github.com/i1r0/ohjelmistoprojekti-hh/blob/master/img/database/JSONcontent_position_table.PNG "Position-taulun sisältö")

| Avain | Kenttä        | Tyyppi      | Huomioitavaa |
|:-----:| ------------- |:-----------:| --- |
| PK    | positionId    | int         ||
| FK    | positionType  | varchar(9)  | viittaus _PositionType_-tauluun |
| FK    | resumeId      | int         | viittaus _Resume_-tauluun |
|       | companyName   | varchar(50) ||
|       | positionName  | varchar(50) ||
|       | companyWebsite | varchar(50) ||
|       | startDate     | date        ||
|       | endDate       | date        ||
|       | summary       | varchar(300) ||


### PositionType
**_PositionType_**-taulu sisältää tiedon siitä, onko **_Position_** taulussa oleva rivi työ- vai vapaaehtoistoimenkuva.


| Avain | Kenttä        | Tyyppi      | Huomioitavaa |
|:-----:| ------------- |:-----------:| --- |
| PK    | positionType  | varchar(9)  | kentän arvo voi olla vain joko `Work` tai `Volunteer`


### Highlight
**_Highlight_**-taulu sisältää käyttäjän työpaikkoihin tai vapaaehtoistöihin liittyvät merkittävät kohokohdat. JSON Resume Schemasta tämä taulu sisältää sekä _work_ että _volunteer_ osioiden sisältämät _highlights_ listat. Erottelu työ- ja vapaaehtoiskohokohtien välillä tapahtuu taulussa **_Position_** viittauksella tauluun **_PositionType_**.

![](https://github.com/i1r0/ohjelmistoprojekti-hh/blob/master/img/database/JSONcontent_highlight_table.PNG "Highlight-taulun sisältö")

| Avain | Kenttä        | Tyyppi      | Huomioitavaa |
|:-----:| ------------- |:-----------:| --- |
| PK    | highlightId   | int         ||
| FK    | positionId    | int         | viittaus _Position_-tauluun |
|       | summary       | varchar(300) ||

### Education
**_Education_**-taulu sisältää tiedot käyttäjän suorittamista tutkinnoista. JSON Resume Schemasta tämä taulu sisältää _education_ osion lukuunottamatta _courses_ listaa, joka on toteutettu omaan tauluunsa **_Course_**.


![](https://github.com/i1r0/ohjelmistoprojekti-hh/blob/master/img/database/JSONcontent_education_table.PNG "Education-taulun sisältö")

| Avain | Kenttä        | Tyyppi      | Huomioitavaa |
|:-----:| ------------- |:-----------:| --- |
| PK    | educationId   | int         ||
| FK    | resumeId      | int         | viittaus _Resume_-tauluun|
|       | institution   | varchar(50) ||
|       | area          | varchar(50) ||
|       | studyType     | varchar(50) ||
|       | startDate     | date        ||
|       | endDate       | date        ||
|       | gpa           | decimal(1,1) ||

### Course
**_Course_**-taulu sisältää tiedot käyttäjäntutkintoisinh sisältyvistä merkittävistä kursseista. JSON Resume Schemasta tämä taulu sisältää _education_ osion sisältämän _courses_ listan.

![](https://github.com/i1r0/ohjelmistoprojekti-hh/blob/master/img/database/JSONcontent_course_table.PNG "Course-taulun sisältö")

| Avain | Kenttä        | Tyyppi      | Huomioitavaa |
|:-----:| ------------- |:-----------:| --- |
| PK    | courseId      | int         ||
| FK    | educationId   | int         | viittaus _Education_-tauluun|
|       | courseName    | varchar(50) ||

### Award
**_Award_**-taulu sisältää tiedot käyttäjän saamista tunnustuksista. JSON Resume Schemasta tämä taulu sisältää _awards_ osion.

![](https://github.com/i1r0/ohjelmistoprojekti-hh/blob/master/img/database/JSONcontent_award_table.PNG "Award-taulun sisältö")

| Avain | Kenttä        | Tyyppi      | Huomioitavaa |
|:-----:| ------------- |:-----------:| --- |
| PK    | awardId       | int         ||
| FK    | resumeId      | int         | viittaus _Resume_-tauluun |
|       | awardTitle    | varchar(50) ||
|       | awardDate     | date        ||
|       | awarder       | varchar(50) ||
|       | summary       | varchar(300) ||

### Publication
**_Publication_**-taulu sisältää tiedot käyttäjän julkaisemista teoksista. JSON Resume Schemasta tämä taulu sisältää _publications_ osion.

![](https://github.com/i1r0/ohjelmistoprojekti-hh/blob/master/img/database/JSONcontent_publication_table.PNG "Publication-taulun sisältö")

| Avain | Kenttä             | Tyyppi       | Huomioitavaa |
|:-----:| ------------------ |:------------:| --- |
| PK    | publicationId      | int          ||
| FK    | resumeId           | int          | viittaus _Resume_-tauluun |
|       | publicationName    | varchar(50)  ||
|       | publisher          | varchar(50)  ||
|       | releaseDate        | date         ||
|       | publicationWebsite | varchar(50)  ||
|       | summary            | varchar(300) ||

### Skill
**_Skill_**-taulu sisältää tiedot käyttäjän hallitsemista taidoista. JSON Resume Schemasta tämä taulu sisältää _skills_ osion lukuunottamatta _keywords_ listaa, joka on toteutettu erilliseen tauluun **_Keyword_**.

![](https://github.com/i1r0/ohjelmistoprojekti-hh/blob/master/img/database/JSONcontent_skill_table.PNG "Skill-taulun sisältö")

| Avain | Kenttä        | Tyyppi      | Huomioitavaa |
|:-----:| ------------- |:-----------:| --- |
| PK    | skillId       | int         ||
| FK    | resumeId      | int         | viittaus _Resume_-tauluun |
|       | skillName     | varchar(50) ||
|       | skillLevel    | varchar(50) ||

### Language
**_Language_**-taulu sisältää tiedot käyttäjän osaamista kielistä. JSON Resume Schemasta tämä taulu sisältää _languages_ osion.

![](https://github.com/i1r0/ohjelmistoprojekti-hh/blob/master/img/database/JSONcontent_language_table.PNG "Language-taulun sisältö")

| Avain | Kenttä        | Tyyppi      | Huomioitavaa |
|:-----:| ------------- |:-----------:| --- |
| PK    | languageId    | int         ||
| FK    | resumeId      | int         | viittaus _Resume_-tauluun |
|       | languageName  | varchar(50) ||
|       | fluency       | varchar(50) ||

### Interest
**_Interest_**-taulu sisältää tiedot käyttäjän kiinnostuksen kohteista. JSON Resume Schemasta tämä taulu sisältää _interests_ osion lukuunottamatta _keywords_ listaa, joka on toteutettu erilliseen tauluun **_Keyword_**.

![](https://github.com/i1r0/ohjelmistoprojekti-hh/blob/master/img/database/JSONcontent_interest_table.PNG "Interest-taulun sisältö")

| Avain | Kenttä        | Tyyppi      | Huomioitavaa |
|:-----:| ------------- |:-----------:| --- |
| PK    | interestId    | int         ||
| FK    | resumeId      | int         | viittaus _Resume_-tauluun |
|       | interestName  | varchar(50) ||

### Reference
**_Reference_**-taulu sisältää tiedot käyttäjän suosituksista ja referensseistä. JSON Resume Schemasta tämä taulu sisältää _references_ osion.

![](https://github.com/i1r0/ohjelmistoprojekti-hh/blob/master/img/database/JSONcontent_reference_table.PNG "Reference-taulun sisältö")

| Avain | Kenttä           | Tyyppi       | Huomioitavaa |
|:-----:| ---------------- |:------------:| --- |
| PK    | referenceId      | int          ||
| FK    | resumeId         | int          | viittaus _Resume_-tauluun |
|       | referenceName    | varchar(50)  ||
|       | referenceSummary | varchar(300) ||

### Keyword
**_Keyword_**-taulu sisältää avain- ja hakusanoja joita käyttäjä voi lisätä taitoihinsta **_Skill_**-taulussa tai kiinnostuksen kohteisiinsa **_Interest_**-taulussa. Edellä mainittujen ja tämän taulun välillä on many-to-many -suhde, joten viittaukset kulkevat välitaulujen **_SkillKeywords_** ja **_InterestKeywords_** kautta. JSON Resume Schemasta tämä taulu sisältää sekä _skills_ että _interests_ osioiden sisältämät _keywords_ listat.

![](https://github.com/i1r0/ohjelmistoprojekti-hh/blob/master/img/database/JSONcontent_keyword_table.PNG "Keyword-taulun sisältö")

| Avain | Kenttä           | Tyyppi       | Huomioitavaa |
|:-----:| ---------------- |:------------:| --- |
| PK    | keyword          | varchar(20)  ||

### SkillKeywords
**_SkillKeywords_** on välitaulu **_Skill_** ja **_Keyword_** taulujen välillä.

| Avain | Kenttä           | Tyyppi       | Huomioitavaa |
|:-----:| ---------------- |:------------:| --- |
| FK    | skillId          | int          | viittaus _Skill_-tauluun |
| FK    | keyword          | varchar(20)  | viittaus _Keyword_-tauluun |


### InterestKeywords
**_InterestKeywords_** on välitaulu **_Interest_** ja **_Keyword_** taulujen välillä.

| Avain | Kenttä           | Tyyppi       | Huomioitavaa |
|:-----:| ---------------- |:------------:| --- |
| FK    | interestId       | int          | viittaus _Interest_-tauluun |
| FK    | keyword          | varchar(20)  | viittaus _Keyword_-tauluun |


## Tekninen kuvaus

Järjestelmän komponentit ovat:
- AWS-palvelun SQL-tietokanta
- Spring Boot -sovellus
- React -käyttöliittymä
![](https://github.com/i1r0/ohjelmistoprojekti-hh/blob/master/img/doc/komponenttikaavio.png "Järjestelmän komponentit")

Sekvenssikaavio

Kirjautuminen järjestelmään tapahtuu kolmannen osapuolen palvelun (Facebook, Github, Google) kautta. Tämä on toteutettu OAuth 2.0 -palvelulla. Käyttäjän kirjautuessa kolmannen osapuolen palvelu palauttaa vierailu-ID:n, joka toimii paitsi SQL-tallennuksen identifioivana osana, myös kirjautumisesta käyttäjän koneella tallennettavan keksin tietona, jonka avulla keksin ollessa olemassa käyttäjä tunnistetaan ilman uutta kirjautumista.

## Testaus

Jokainen Spring Boot -sovelluksen olio ajettiin yksikkötestauksen läpi. Tässä testauksessa ei paljastunut ongelmia.

**OAuth-moduulin GitHub-kirjautuminen ei toimi toistaiseksi.**

## Asennustiedot

AWS-palvelun SQL-tietokanta toimii itsenäisestä palvelimella eikä tarvitse erillistä deploy- tai build-toimintoa.
Spring Boot -sovelluksen ajamisen edellytyksenä on tuotantoympäristöön sovellukselle yhteensopivan Javan (versio 8.0 tai uudempi), Spring Frameworkin (5.2.2.RELEASE tai uudempi), Mavenin (versio 3.3 tai uudempi) ja Tomcat 9.0n (Servlet versio 4.0) asentaminen.
React-käyttöliittymän ajamisen edellytyksenä on tuotantoympäristöön sovellukselle yhteensopivan NodeJS:n (versio 16.0 tai uudempi) ja sen avulla Reactin, React-Domin ja Webpackin, Webpack-dev-serverin ja Webpack-CLI:n asentaminen.

Ylläolevat ohjeet käyvät sellaisenaan järjestelmän kehitysympäristön toteuttamiseen.

Tietokannan määrittely tapahtuu Spring Boot -sovelluksen application.yaml-tiedostossa, johon voidaan kirjata sekä palvelimen osoite että kirjautumistiedot. Samasta tiedostosta löytyvät myös kolmannen osapuolen palveluiden API-avaimet ja niiden määrittelyt.

## Käynnistys- ja käyttöohje

Spring Boot -sovellus pyörii käynnistettäessä osoitteessa http://localhost:8080/ ja React-käyttöliittymä osoitteessa http://localhost:3000/
