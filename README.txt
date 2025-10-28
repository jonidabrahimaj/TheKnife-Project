THEKNIFE — README
==================

Requisiti:
- Java (JDK 17+ consigliato)

Compilazione (da /submit):
1) cd src
2) javac theknife/*.java -d ../bin
3) cd ../bin
4) jar --create --file TheKnife.jar --main-class theknife.TheKnife theknife/*.class

Esecuzione:
- java -jar bin/TheKnife.jar

Struttura :
- /src/theknife/*.java
- /data/ristoranti.csv, /data/utenti.txt
- /bin (output compilazione)
- /doc (manuale utente/tecnico)
- /lib (librerie esterne, se servono)

Note:
- Utenti ruhen në data/utenti.txt (hash SHA-256).
- Rruga e file-it të përdoruesve në kod: data/utenti.txt
