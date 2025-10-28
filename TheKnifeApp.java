/**
 * Progetto: TheKnife (Lab A)
 * Autori: [NOME COGNOME 1, Matricola, Sede]; [NOME COGNOME 2, Matricola, Sede] ...
 * Corso: Laboratorio Interdisciplinare A (a.a. 2024/2025)
 */
package theknife;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class TheKnifeApp {
    private static final Path FILE_UTENTI_PATH = Paths.get("data", "utenti.txt");
    private static final Path FILE_RISTORANTI_PATH = Paths.get("data", "ristoranti.csv");

    private static final List<Ristorante> ristoranti = new ArrayList<>();
    private static final List<Utente> utenti = new ArrayList<>();
    private static Utente utenteCorrente = null;

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Benvenuto su The Knife App!");
        Path csvPath = resolveRistorantiPath();
        if (csvPath != null) {
            caricaRistorantiDaCSV(csvPath);
        } else {
            System.out.println("Impossibile trovare il file dei ristoranti: controlla la cartella data/ o src/.");
        }
        System.out.println("Ristoranti caricati: " + ristoranti.size());
        caricaUtenti();

        menuIniziale();
    }

    public static void caricaRistorantiDaCSV(Path csvPath) {
        String line;
        String cvsSplitBy = ";";

        if (!Files.exists(csvPath)) {
            System.out.println("File ristoranti non trovato: " + csvPath.toAbsolutePath());
            return;
        }

        ristoranti.clear();

        try (BufferedReader br = Files.newBufferedReader(csvPath)) {
            // Salta l'intestazione
            br.readLine();

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue; // ignora righe vuote
                }

                String[] fields = line.split(cvsSplitBy);

                if (fields.length < 10) {
                    System.out.println("ATTENZIONE: Riga ignorata per numero colonne insufficiente (" + fields.length + "): " + line);
                    continue;
                }

                String nome = fields[0].trim();
                String indirizzo = fields[3].trim();
                String citta = fields[2].trim();
                String tipologiaCucina = fields[9].trim();
                String fasciaPrezzo = fields[6].trim() + "€";
                boolean delivery = parseYesNo(fields[7].trim());
                boolean prenotazioneOnline = parseYesNo(fields[8].trim());

                Ristorante r = new Ristorante(
                        nome,
                        indirizzo,
                        citta,
                        tipologiaCucina,
                        fasciaPrezzo,
                        delivery,
                        prenotazioneOnline
                );
                try { double prezzoMedio = Double.parseDouble(fields[6].trim()); r.setPrezzoMedio(prezzoMedio);} catch (Exception _e) { /* ignore */ }

                ristoranti.add(r);
            }

            System.out.println("Ristoranti caricati correttamente da " + csvPath.toAbsolutePath());
        } catch (IOException e) {
            System.out.println("Errore nella lettura del CSV: " + e.getMessage());
        }
    }




    public static void menuIniziale() {
        while (true) {
            System.out.println("\n*** MENU PRINCIPALE ***");
            System.out.println("1. Registrati");
            System.out.println("2. Login");
            System.out.println("3. Accedi come Guest");
            System.out.println("0. Esci");
            System.out.print("Scelta: ");
            String scelta = scanner.nextLine();

            switch (scelta) {
                case "1":
                    registrazione();
                    break;
                case "2":
                    login();
                    break;
                case "3":
                    utenteCorrente = null; // guest
                    menuGuest();
                    break;
                case "0":
                    System.out.println("Arrivederci! Spero di riverderti presto!");
                    return;
                default:
                    System.out.println("Scelta non valida. Riprova.");
            }
        }
    }

    public static void registrazione() {
        System.out.print("Inserisci username: ");
        String username = scanner.nextLine();
        if (getUtenteByUsername(username) != null) {
            System.out.println("Username già esistente.");
            return;
        }

        System.out.print("Inserisci password: ");
        String rawPassword = scanner.nextLine();
        String password = HashUtil.hashPassword(rawPassword);


        System.out.print("Tipo utente (cliente/ristoratore): ");
        String tipo = scanner.nextLine().toLowerCase();

        if (tipo.equals("cliente")) {
            Cliente cliente = new Cliente(username, password);
            utenti.add(cliente);
            System.out.println("Cliente registrato con successo.");
        } else if (tipo.equals("ristoratore")) {
            Ristoratore ristoratore = new Ristoratore(username, password);
            utenti.add(ristoratore);
            System.out.println("Ristoratore registrato con successo.");
        } else {
            System.out.println("Tipo utente non valido.");
            return;
        }

        salvaUtenti();
    }

    public static void login() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Utente u = getUtenteByUsername(username);
        if (u == null) {
            System.out.println("Utente non trovato.");
            return;
        }
        if (!u.verificaPassword(password)) {
            System.out.println("Password errata.");
            return;
        }


        utenteCorrente = u;
        System.out.println("Login effettuato come " + utenteCorrente.getUsername() + " (" + utenteCorrente.getTipo() + ")");

        if (utenteCorrente instanceof Cliente) {
            menuCliente();
        } else if (utenteCorrente instanceof Ristoratore) {
            menuRistoratore();
        }
    }

    public static Utente getUtenteByUsername(String username) {
        for (Utente u : utenti) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }

    public static void menuGuest() {
        while (true) {
            System.out.println("\n*** MENU GUEST ***");
            System.out.println("1. Cerca ristoranti");
            System.out.println("0. Torna al menu principale");
            System.out.print("Scelta: ");
            String scelta = scanner.nextLine();

            switch (scelta) {
                case "1":
                    cercaRistoranti();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Scelta non valida.");
            }
        }
    }

    public static void cercaRistoranti() {
        System.out.print("Nome ristorante (invio per ignorare): ");
        String nomeInput = scanner.nextLine().trim().toLowerCase();

        System.out.print("Città (invio per ignorare): ");
        String cittaInput = scanner.nextLine().trim().toLowerCase();

        System.out.print("Tipologia cucina (invio per ignorare): ");
        String cucinaInput = scanner.nextLine().trim().toLowerCase();

        System.out.print("Fascia prezzo massima (solo numero, es: 30, invio per ignorare): ");
        String prezzoInput = scanner.nextLine().trim();

        System.out.print("Solo con delivery? (si/no/invio per ignorare): ");
        String deliveryInput = scanner.nextLine().trim().toLowerCase();

        System.out.print("Solo con prenotazione online? (si/no/invio per ignorare): ");
        String prenotazioneInput = scanner.nextLine().trim().toLowerCase();

        List<Ristorante> trovati = new ArrayList<>();

        for (Ristorante r : ristoranti) {

            // Filtro Nome
            if (!nomeInput.isEmpty() && !r.getNome().toLowerCase().contains(nomeInput)) {
                continue;
            }

            // Filtro Città
            if (!cittaInput.isEmpty() && !r.getCitta().toLowerCase().contains(cittaInput)) {
                continue;
            }

            // Filtro Tipologia cucina
            if (!cucinaInput.isEmpty() && !r.getTipologiaCucina().toLowerCase().contains(cucinaInput)) {
                continue;
            }

            // Filtro Fascia Prezzo
            if (!prezzoInput.isEmpty()) {
                try {
                    int prezzoMassimo = Integer.parseInt(prezzoInput);

                    // Prendiamo il primo numero che troviamo nella stringa Fascia Prezzo
                    String prezzoRiga = r.getFasciaPrezzo().replaceAll("[^0-9]", "");
                    if (!prezzoRiga.isEmpty()) {
                        int prezzoRistorante = Integer.parseInt(prezzoRiga);
                        if (prezzoRistorante > prezzoMassimo) {
                            continue;
                        }
                    }
                } catch (NumberFormatException e) {
                    // Se input non è un numero, ignora il filtro prezzo
                }
            }

            // Filtro Delivery
            if (deliveryInput.equals("si") && !r.isDelivery()) {
                continue;
            }
            if (deliveryInput.equals("no") && r.isDelivery()) {
                continue;
            }

            // Filtro Prenotazione
            if (prenotazioneInput.equals("si") && !r.isPrenotazioneOnline()) {
                continue;
            }
            if (prenotazioneInput.equals("no") && r.isPrenotazioneOnline()) {
                continue;
            }

            trovati.add(r);
        }

        // Mostra risultati
        if (trovati.isEmpty()) {
            System.out.println("Nessun ristorante trovato.");
            /*System.out.println("\n--- Ristoranti disponibili per verifica ---");
            for (Ristorante r : ristoranti) {
                System.out.println("- " + r.getNome()
                        + " | " + r.getCitta()
                        + " | " + r.getTipologiaCucina()
                        + " | " + r.getFasciaPrezzo()
                        + " | Delivery: " + (r.isDelivery() ? "si" : "no")
                        + " | Prenotazione: " + (r.isPrenotazioneOnline() ? "si" : "no"));
            }*/
            return;
        }

        System.out.println("\n*** Ristoranti trovati ***");
        for (Ristorante r : trovati) {
            System.out.println("- " + r.getNome()
                    + " | " + r.getCitta()
                    + " | " + r.getTipologiaCucina()
                    + " | " + r.getFasciaPrezzo()
                    + " | Delivery: " + (r.isDelivery() ? "si" : "no")
                    + " | Prenotazione: " + (r.isPrenotazioneOnline() ? "si" : "no"));
        }
    }



    public static void menuCliente() {
        Cliente cliente = (Cliente) utenteCorrente;

        while (true) {
            System.out.println("\n*** MENU CLIENTE ***");
            System.out.println("1. Cerca ristoranti");
            System.out.println("2. Visualizza preferiti");
            System.out.println("3. Aggiungi recensione");
            System.out.println("4. Modifica recensione");
            System.out.println("5. Elimina recensione");
            System.out.println("0. Logout");
            System.out.print("Scelta: ");
            String scelta = scanner.nextLine();

            switch (scelta) {
                case "1":
                    cercaRistorantiCliente(cliente);
                    break;
                case "2":
                    visualizzaPreferiti(cliente);
                    break;
                case "3":
                    aggiungiRecensione(cliente);
                    break;
                case "4":
                    modificaRecensione(cliente);
                    break;
                case "5":
                    eliminaRecensione(cliente);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Scelta non valida.");
            }
        }
    }
    public static void cercaRistorantiCliente(Cliente cliente) {
        cercaRistoranti();

        System.out.print("Vuoi aggiungere un ristorante ai preferiti? (si/no): ");
        String risposta = scanner.nextLine().toLowerCase();
        if (risposta.equals("si")) {
            System.out.print("Inserisci nome ristorante esatto: ");
            String nomeRisto = scanner.nextLine();
            Ristorante r = getRistoranteByNome(nomeRisto);
            if (r != null) {
                if (!cliente.getPreferiti().contains(r)) {
                    cliente.getPreferiti().add(r);
                    System.out.println("Aggiunto ai preferiti.");
                } else {
                    System.out.println("Ristorante già nei preferiti.");
                }
            } else {
                System.out.println("Ristorante non trovato.");
            }
        }
    }

    public static void visualizzaPreferiti(Cliente cliente) {
        List<Ristorante> preferiti = cliente.getPreferiti();
        if (preferiti.isEmpty()) {
            System.out.println("Nessun ristorante nei preferiti.");
        } else {
            System.out.println("*** I tuoi preferiti ***");
            for (Ristorante r : preferiti) {
                System.out.println(r);
            }
        }
    }

    public static void aggiungiRecensione(Cliente cliente) {
        System.out.print("Inserisci nome ristorante: ");
        String nomeRisto = scanner.nextLine();
        Ristorante r = getRistoranteByNome(nomeRisto);
        if (r == null) {
            System.out.println("Ristorante non trovato.");
            return;
        }

        System.out.print("Testo recensione: ");
        String testo = scanner.nextLine();
        System.out.print("Numero stelle (1-5): ");
        int stelle = Integer.parseInt(scanner.nextLine());

        Recensione rec = new Recensione(cliente.getUsername(), testo, stelle);
        cliente.getRecensioni().add(rec);
        r.aggiornaValutazione(
                (r.getMediaStelle() * r.getNumeroRecensioni() + stelle) / (r.getNumeroRecensioni() + 1),
                r.getNumeroRecensioni() + 1
        );

        System.out.println("Recensione aggiunta.");
    }

    public static void modificaRecensione(Cliente cliente) {
        System.out.print("Inserisci nome ristorante della recensione da modificare: ");
        String nomeRisto = scanner.nextLine();
        Recensione rec = getRecensioneCliente(cliente, nomeRisto);
        if (rec == null) {
            System.out.println("Recensione non trovata.");
            return;
        }

        System.out.print("Nuovo testo recensione: ");
        String testo = scanner.nextLine();
        System.out.print("Nuovo numero stelle (1-5): ");
        int stelle = Integer.parseInt(scanner.nextLine());

        rec.setTesto(testo);
        rec.setStelle(stelle);

        System.out.println("Recensione modificata.");
    }

    public static void eliminaRecensione(Cliente cliente) {
        System.out.print("Inserisci nome ristorante della recensione da eliminare: ");
        String nomeRisto = scanner.nextLine();
        Recensione rec = getRecensioneCliente(cliente, nomeRisto);
        if (rec == null) {
            System.out.println("Recensione non trovata.");
            return;
        }

        cliente.getRecensioni().remove(rec);
        System.out.println("Recensione eliminata.");
    }

    public static Ristorante getRistoranteByNome(String nome) {
        for (Ristorante r : ristoranti) {
            if (r.getNome().toLowerCase().contains(nome.toLowerCase().trim())) {
                return r;
            }
        }
        return null;
    }


    public static Recensione getRecensioneCliente(Cliente cliente, String nomeRistorante) {
        for (Recensione rec : cliente.getRecensioni()) {
            if (rec.getUtente().equals(cliente.getUsername()) &&
                    rec.getTesto().toLowerCase().contains(nomeRistorante.toLowerCase())) {
                return rec;
            }
        }
        return null;
    }


    public static void menuRistoratore() {
        Ristoratore ristoratore = (Ristoratore) utenteCorrente;

        while (true) {
            System.out.println("\n*** MENU RISTORATORE ***");
            System.out.println("1. Aggiungi ristorante");
            System.out.println("2. Visualizza riepilogo recensioni");
            System.out.println("3. Visualizza recensioni dettagliate");
            System.out.println("4. Rispondi a recensione");
            System.out.println("0. Logout");
            System.out.print("Scelta: ");
            String scelta = scanner.nextLine();

            switch (scelta) {
                case "1":
                    aggiungiRistorante(ristoratore);
                    break;
                case "2":
                    visualizzaRiepilogo(ristoratore);
                    break;
                case "3":
                    visualizzaRecensioniRistoratore(ristoratore);
                    break;
                case "4":
                    rispostaRecensioni(ristoratore);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Scelta non valida.");
            }
        }
    }

    public static void aggiungiRistorante(Ristoratore ristoratore) {
        System.out.print("Nome ristorante: ");
        String nome = scanner.nextLine();

        if (getRistoranteByNome(nome) != null) {
            System.out.println("Esiste già un ristorante con questo nome.");
            return;
        }

        System.out.print("Indirizzo: ");
        String indirizzo = scanner.nextLine();

        System.out.print("Città: ");
        String citta = scanner.nextLine();

        System.out.print("Tipologia cucina: ");
        String cucina = scanner.nextLine();

        System.out.print("Fascia prezzo: ");
        String fasciaPrezzo = scanner.nextLine();

        System.out.print("Offre delivery? (si/no): ");
        boolean delivery = scanner.nextLine().equalsIgnoreCase("si");

        System.out.print("Offre prenotazione online? (si/no): ");
        boolean prenotazione = scanner.nextLine().equalsIgnoreCase("si");

        Ristorante nuovo = new Ristorante(nome, indirizzo, citta, cucina, fasciaPrezzo, delivery, prenotazione);
        ristoranti.add(nuovo);
        ristoratore.getRistorantiGestiti().add(nuovo);

        System.out.println("Ristorante aggiunto con successo.");
    }

    public static void visualizzaRiepilogo(Ristoratore ristoratore) {
        List<Ristorante> gestiti = ristoratore.getRistorantiGestiti();
        if (gestiti.isEmpty()) {
            System.out.println("Non gestisci ancora nessun ristorante.");
            return;
        }

        System.out.println("*** Riepilogo recensioni ***");
        for (Ristorante r : gestiti) {
            System.out.println(r.getNome() + " - Media stelle: " + r.getMediaStelle() +
                    " (" + r.getNumeroRecensioni() + " recensioni)");
        }
    }

    public static void visualizzaRecensioniRistoratore(Ristoratore ristoratore) {
        List<Ristorante> gestiti = ristoratore.getRistorantiGestiti();
        if (gestiti.isEmpty()) {
            System.out.println("Non gestisci ristoranti.");
            return;
        }

        for (Ristorante r : gestiti) {
            System.out.println("\n*** Recensioni di " + r.getNome() + " ***");
            boolean trovata = false;
            for (Utente u : utenti) {
                if (u instanceof Cliente) {
                    Cliente c = (Cliente) u;
                    for (Recensione rec : c.getRecensioni()) {
                        if (rec.getTesto().toLowerCase().contains(r.getNome().toLowerCase())) {
                            trovata = true;
                            System.out.println("- " + rec);
                        }
                    }
                }
            }
            if (!trovata) {
                System.out.println("Nessuna recensione.");
            }
        }
    }

    public static void rispostaRecensioni(Ristoratore ristoratore) {
        System.out.print("Nome ristorante: ");
        String nomeRisto = scanner.nextLine();

        Ristorante r = getRistoranteByNome(nomeRisto);
        if (r == null || !ristoratore.getRistorantiGestiti().contains(r)) {
            System.out.println("Non gestisci questo ristorante.");
            return;
        }

        System.out.print("Testo di una recensione da rispondere: ");
        String testoRec = scanner.nextLine();

        for (Utente u : utenti) {
            if (u instanceof Cliente) {
                Cliente c = (Cliente) u;
                for (Recensione rec : c.getRecensioni()) {
                    if (rec.getTesto().equalsIgnoreCase(testoRec)) {
                        if (rec.getRispostaRistoratore() != null) {
                            System.out.println("Hai già risposto a questa recensione.");
                            return;
                        }
                        System.out.print("Scrivi la tua risposta: ");
                        String risposta = scanner.nextLine();
                        rec.setRispostaRistoratore(risposta);
                        System.out.println("Risposta aggiunta.");
                        return;
                    }
                }
            }
        }
        System.out.println("Recensione non trovata.");
    }


    public static void visualizzaRistorante(Ristorante r) {
        System.out.println("\n*** Dettagli Ristorante ***");
        System.out.println("Nome: " + r.getNome());
        System.out.println("Città: " + r.getCitta());
        System.out.println("Tipologia cucina: " + r.getTipologiaCucina());
        System.out.println("Fascia prezzo: " + r.getFasciaPrezzo());
        System.out.println("Delivery: " + (r.isDelivery() ? "Sì" : "No"));
        System.out.println("Prenotazione online: " + (r.isPrenotazioneOnline() ? "Sì" : "No"));
        System.out.println("Media stelle: " + r.getMediaStelle() + " (" + r.getNumeroRecensioni() + " recensioni)");

        System.out.println("\nRecensioni:");
        if (r.getNumeroRecensioni() == 0) {
            System.out.println("Nessuna recensione disponibile.");
        }
        // Qui in futuro aggiungeremo il caricamento delle recensioni vere
    }


    private static boolean parseYesNo(String s) {
        if (s == null) {
            return false;
        }
        String normalized = s.trim().toLowerCase(Locale.ITALIAN);
        return normalized.equals("si")
                || normalized.equals("sì")
                || normalized.equals("yes")
                || normalized.equals("true");
    }

    private static void caricaUtenti() {
        utenti.clear();
        if (!Files.exists(FILE_UTENTI_PATH)) {
            return;
        }

        try (BufferedReader br = Files.newBufferedReader(FILE_UTENTI_PATH)) {
            String header = br.readLine(); // opzionale
            String line;

            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(";");
                if (tokens.length < 3) {
                    continue;
                }

                String username = tokens[0].trim();
                String passwordHash = tokens[1].trim();
                String tipo = tokens[2].trim().toLowerCase(Locale.ITALIAN);

                Utente utente = "ristoratore".equals(tipo)
                        ? new Ristoratore(username, passwordHash)
                        : new Cliente(username, passwordHash);
                utenti.add(utente);
            }
        } catch (IOException e) {
            System.out.println("Impossibile caricare gli utenti: " + e.getMessage());
        }
    }

    private static void salvaUtenti() {
        try {
            Files.createDirectories(FILE_UTENTI_PATH.getParent());
            try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(FILE_UTENTI_PATH))) {
                pw.println("username;passwordHash;tipo");
                for (Utente u : utenti) {
                    pw.println(u.getUsername() + ";" + u.password + ";" + u.getTipo());
                }
            }
        } catch (IOException e) {
            System.out.println("Errore salvataggio utenti: " + e.getMessage());
        }
    }

    private static Path resolveRistorantiPath() {
        List<Path> candidates = Arrays.asList(
                FILE_RISTORANTI_PATH,
                Paths.get("src", "ristoranti.csv"),
                Paths.get("src", "theknife", "ristoranti.csv")
        );

        for (Path candidate : candidates) {
            Path normalized = candidate.toAbsolutePath().normalize();
            if (Files.exists(normalized)) {
                return normalized;
            }
        }

        return null;
    }
}
