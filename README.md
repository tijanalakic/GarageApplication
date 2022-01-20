# GarageApplication
Napraviti sistem za analizu troškova proizvoljne kompanije. Sistem treba da podrži automatsku obradu i analizu troškova na osnovu unesenih podataka. 
Jedan račun se može samo jednom obraditi nakon čega se mu se mijenja naziv fajla i dodaje prefiks done_. Računi mogu imati različite sadržaje i formate,
a svi mogući formati su dati na Moodle stranici. Sistemu su važne informacije koje se čuvaju na računu: naziv kupca, datum kupovine, šifra proizvoda, 
prodana količina, pojedinačna cijena svakog proizvoda, ukupna cijena za plaćanje i PDV vrijednost. Jedan račun se nalazi u jednom tekstualnom fajlu. 
Na jednom računu može biti više proizvoda. Folderi koje aplikacija koristi se odnose na različite poslovnice. U aplikaciji se mogu postaviti osluškivači (listeners)
za svaki folder izborom odgovarajuće opcije i foldera. Ovih osluškivača može biti više za jedan folder, a mogu se uklanjati ili prikazivati u aplikaciji. 
Tokom rada aplikacije, svaki osluškivač provjerava sadržaj svog foldera i u zajednični log fajl upisuje sljedeće podatke: datum i vrijeme, vrsta događaja, 
ID osluškivača. Vrsta događaja je kreiranje ili brisanje fajla. Ukoliko se pojavi novi neobrađeni račun, osluškivač poziva modul za uvoz podataka. 
Moduli za uvoz podataka parsiraju račune i na osnovu sadržaja prave odgovarajuće objekte: Račun koji ima Stavke (kolekcija), Kupca (ako postoji) i Poslovnicu. 
Moduli koji parsiraju račune treba da koriste generičke mehanizme gdje god je to moguće i da ispunjavaju osobine polimorfnosti.
Nakon što se jedan račun učita počinje njegova obrada. U ovoj fazi sistem serijalizuje podatke sa računa u odgovarajućim folderima čiji se nazivi nalaze u
konfiguracionom fajlu aplikacije. Osim toga, izvršava se validacija svakog računa, koja podrazumijeva provjeru izračunatih vrijednosti sa računa 
(npr. da li je iznos*količina=ukupno…). Ukoliko se vrijednosti razlikuju sistem prijavljuje grešku i detalje upisuje u fajl sa imenom: naziv_racuna_error.txt.
Obrada tog računa se u tom slučaju završava. Ukoliko je račun uspješno obrađen, podaci se skladište u sistem.
Korisnici sistema imaju sljedeće opcije:
1. Izvoz podataka u CSV fajlove:
o Pregled svih podataka za određenog kupca
o Pregled svih podataka za određeni proizvod
o Pregled ukupne prodaje za određeni mjesec
2. Ručno dodavanje računa u podrazumijevanom formatu
3. Podešavanje valute aplikacije koja se čuva u konfiguracionom fajlu aplikacije
4. Upravljanje korisničkim nalozima (dodavanje, brisanje, pregled)
5. Upravljanje osluškivačima
Za upotrebu aplikacije korisnici se moraju prijaviti unošenjem korisničkog imena i lozinke. Svaki korisnik ima svoj nalog: ime, prezime, korisničko ime, lozinka i korisnička grupa.
Korisničke grupe su administrator i analitičar. Administrator ima pristup opcijama 3, 4 i 5. Analitičar ima pristup opcijama 1 i 2. Napraviti GUI aplikaciju upotrebom 
tehnologija koje su rađene na predmetu, pri čemu je potrebno izvršiti raslojavanje koda na sljedeći način: GUI kontroleri obrađuju samo događaje na formama,
a sva logika obrade podataka i upravljanja poslovnim procesima ide u odgovarajuće servise. Servisi su klase koje imaju metode za obradu podataka koje vraćaju 
boolean rezultat kontroleru koji je pozvao tu metodu. Na osnovu rezultata korisniku se ispisuje poruka o uspješnoj ili neuspješnoj operaciji.
