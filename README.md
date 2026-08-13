# SpesApp 💰

App Android per la gestione delle spese personali.
Progetto per il corso Mobile Programming.

## Funzionalità
- Inserimento di entrate e uscite (importo, tipo, categoria, data, nota) con validazione dei campi
- Lista delle transazioni con filtri per tipo, categoria e mese
- Dashboard con totale entrate, totale uscite e saldo aggiornato in tempo reale
- Dettaglio transazione con modifica ed eliminazione
- Grafico a torta delle uscite per categoria
- Sicurezza: PIN di 4 cifre al primo avvio + sblocco con impronta digitale (se il dispositivo la supporta)
- Persistenza offline con Room

## Architettura (MVVM)
- **UI (Fragment)** → osserva il ViewModel e mostra i dati
- **ViewModel** → espone lo stato con StateFlow, contiene la logica di filtro e totali
- **Repository** → unico punto di accesso ai dati
- **Room (DAO + Database)** → persistenza con Flow per aggiornamenti reattivi

## Librerie e componenti usati
- Room (database locale) con TypeConverters per enum e date
- Lifecycle: ViewModel, StateFlow, repeatOnLifecycle
- Navigation Component (nav graph) per gli spostamenti tra schermate
- ViewBinding per accedere alle viste
- RecyclerView + ListAdapter per la lista
- Material Design (card, FAB, bottoni)
- BiometricPrompt per l'impronta digitale
- SharedPreferences per il PIN
- MPAndroidChart per il grafico a torta
- JUnit per i test unitari

## Struttura del codice
- `model/` → Transazione (Entity Room), TipoTransazione (enum ENTRATA/USCITA)
- `data/` → DAO, Database, Repository
- `viewmodel/` → TransazioneViewModel, TransazioneUiState
- `ui/` → Fragment (PIN, lista, form, dettaglio, grafico) + Adapter

## Avvio
1. Apri il progetto in Android Studio
2. ▶ Run su emulatore o dispositivo fisico
3. Al primo avvio crea il PIN di 4 cifre, poi inserisci le transazioni

## Test
Test unitari in `app/src/test`: creazione transazione, filtro per mese, calcolo totale uscite.
Esecuzione: tasto destro sul file di test → Run.

## Autore
Accurso Alessandro,Aruta Pietro,Castaldo Nunzio
Lavoro svolto in gruppo con contributi di tutti i membri in ogni parte del progetto (database, UI, ViewModel, filtri, sicurezza, grafici, test).
