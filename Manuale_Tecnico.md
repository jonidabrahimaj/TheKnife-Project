# Manuale Tecnico — TheKnife

## Architettura
- Package: `theknife`
- Classi principali: `TheKnife` (main), `TheKnifeApp` (logica menu/IO), `Ristorante`, `Recensione`, `Utente` (+ `Cliente`, `Ristoratore`), `HashUtil`.

## Dati
- `data/ristoranti.csv`: dataset iniziale (campi: nome, indirizzo, città, lat, long, prezzo, delivery, prenotazione, cucina).
- `data/utenti.txt`: `username;passwordHash;tipo`.
- Recensioni in memoria: `Map<String, List<Recensione>>` key = nome ristorante.

## Algoritmi/Logica
- Filtri ricerca: sequenziali con AND sugli attributi forniti.
- Media stelle: ricalcolata ad ogni modifica recensione.
- Password: SHA-256 (`HashUtil`).

## Build
- `javac theknife/*.java -d ../bin`
- `jar --create --file TheKnife.jar --main-class theknife.TheKnife theknife/*.class`

## Limiti (Lab A)
- Persistenza recensioni solo in memoria (senza DB).
- Concorrenza/servizi web: non richiesti.
