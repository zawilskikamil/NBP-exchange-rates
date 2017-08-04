NBP-exchange-rates
==================
EN
==
TODO

PL
==
Opis
----
Projekt tworzy i monitoruje folder plików o nazwie 'nbp'. 
W momencie pojawienia się pliku następuje jego otwarcie 
i zaczytanie kolejnych linii,
a następnie zapis do bazy danych.

Kursy walut pobierane są ze strony http://www.nbp.pl/kursy/

Na podstawie zawartości (informacjach o kursach walut) 
prezentowany jest wykres (oraz trendy) walut.
„Frontend” daje możliwość wyboru kursów 
(jeden lub wiele) oraz zakresu dat.

Jako dodatkową funkcję zostanie przygotowana 
predykcja kursu na kolejny odcinek czasu (tydzień, miesiąc,…).

Założenia
---------
+   Do budowy projektu wykorzystane zostało narzędzie Maven.

+   Do monitorowania folderu służy biblioteka WatchService API
zawarta w pakiecie java.nio
    
    Metoda monitorująca folder otworzona zostaje 
    w osobnym wątku i po każdym pojawieniu się
    nowego pliku z kursami walut, zapisuje 
    odpowiednie dane do bazy danych. 
    
+   Do zapisu danych służy baza danych PostgreSQL.

+   Do komunikacji z bazą danych służy biblioteka Hbernate.
    
    Pozwala to na zastosowanie dowolnej bazy danych do działania programu.
    
+   Do utworzenia warstwy prezentacji służy biblioteka JavaFX.
    
    Interfejs użytkownika składa się z jednego okna, w którym umieszczone są:
    + przycisk otwierający dialog do wyboru i pobrania pliku,
    + wykres kursów walut,
    + pola wyboru dat oraz walut.
    
Uruchomienie
------------

Przed uruchomieniem programu należy utworzyć bazę w PostgreSQL
nazwa bazy:  nbp
user:        nbp
hasło:       nbp

Następnie wystarczy zbudować paczkę .jar przy pomocy Mavena i uruchomić plik NBP-exchange-rates-jar-with-dependencies.jar

    
GUI
---
    
    Okno programu
    
    ![alt text](https://github.com/zawilskikamil/NBP-exchange-rates/blob/master/image/program.png?raw=true)
    
    Dialog wyboru pliku do pobrania
    
    ![alt text](https://github.com/zawilskikamil/NBP-exchange-rates/blob/master/image/dialog.png?raw=true)
