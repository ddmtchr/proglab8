package client.localization;

import java.util.ListResourceBundle;

public class Locale_de_DE extends ListResourceBundle {
    private Object[][] contents = {
            {"Enter the X coordinate (X > -934):", "Geben Sie die X-Koordinate ein (X > -934):"},
            {"The coordinate must be > -934, please try again:", "Die Koordinate muss > -934 sein. Bitte versuchen Sie es erneut:"},
            {"Input format - integer number, please try again:", "Eingabeformat - Ganzzahl, bitte versuchen Sie es erneut:"},
            {"Input completed by user", "Eingabe abgeschlossen"},
            {"Enter the Y coordinate (Y <= 946):", "Geben Sie die Y-Koordinate ein (Y <= 946):"},
            {"The coordinate must be <= 946, please try again:", "Die Koordinate muss <= 946 sein. Bitte versuchen Sie es erneut:"},
            {"Input format - floating-point number, please try again:", "Eingabeformat - Dezimalzahl, bitte versuchen Sie es erneut:"},
            {"Enter the name field:", "Geben Sie das Namensfeld ein:"},
            {"Name cannot be empty, please try again:", "Name darf nicht leer sein. Bitte versuchen Sie es erneut:"},
            {"Enter the minimum point:", "Geben Sie den Mindestwert ein:"},
            {"The point must be > 0, please try again:", "Der Wert muss > 0 sein. Bitte versuchen Sie es erneut:"},
            {"Enter the average point:", "Geben Sie den Durchschnittswert ein:"},
            {"Enter the difficulty. Possible options:", "Geben Sie die Schwierigkeit ein. Mögliche Optionen:"},
            {"Value must be specified, please try again:", "Wert muss angegeben werden. Bitte versuchen Sie es erneut:"},
            {"No such option, please try again:", "Keine solche Option vorhanden. Bitte versuchen Sie es erneut:"},
            {"Enter the discipline name (empty if discipline is not filled):", "Geben Sie den Disziplinnamen ein (leer, wenn die Disziplin nicht ausgefüllt ist):"},
            {"Enter the number of lecture hours:", "Geben Sie die Anzahl der Vorlesungsstunden ein:"},
            {"Enter the number of practical hours:", "Geben Sie die Anzahl der praktischen Stunden ein:"},
            {"Enter the number of self-study hours:", "Geben Sie die Anzahl der Selbststudienstunden ein:"},
            {"Exiting the program...", "Programm wird beendet..."},
            {"Usage: exit", "Verwendung: exit"},
            {"Usage: execute_script {file_name}", "Verwendung: execute_script {Dateiname}"},
            {"Usage: show", "Verwendung: show"},
            {"The collection is empty", "Die Sammlung ist leer"},
            {"Input error in script", "Eingabefehler im Skript"},
            {"Command ", "Befehl "},
            {" does not exist, enter 'help' for help", " existiert nicht. Geben Sie 'help' ein, um Hilfe zu erhalten"},
            {"Executing script ", "Skript wird ausgeführt "},
            {"...", "..."},
            {"Script ", "Skript "},
            {" executed successfully!", " erfolgreich ausgeführt!"},
            {"File not found", "Datei nicht gefunden"},
            {"No permission to read the file", "Keine Berechtigung, die Datei zu lesen"},
            {"The script file is empty", "Die Skriptdatei ist leer"},
            {"Recursive call detected in the script", "Rekursiver Aufruf im Skript erkannt"},
            {"We apologize, the server has moved the horses", "Wir entschuldigen uns, der Server hat die Pferde verschoben"},
            {"The username is too short", "Der Benutzername ist zu kurz"},
            {"The password is too short", "Das Passwort ist zu kurz"},
            {"Enter the username (minimum 3 characters):", "Geben Sie den Benutzernamen ein (mindestens 3 Zeichen):"},
            {"Enter the username:", "Geben Sie den Benutzernamen ein:"},
            {"Enter the password (minimum 6 characters):", "Geben Sie das Passwort ein (mindestens 6 Zeichen):"},
            {"Enter the password:", "Geben Sie das Passwort ein:"},
            {"Password encryption error", "Fehler bei der Passwortverschlüsselung"},
            {"Lab repository", "Labor-Repository"},
            {"Log in", "Anmelden"},
            {"Register", "Registrieren"},
            {"Username", "Benutzername"},
            {"Password", "Passwort"},
            {"Select an action...", "Wählen Sie eine Aktion..."},
            {"Filter", "Filtern"},
            {"Sorting", "Sortierung"},
            {"Visualization", "Visualisierung"},
            {"Name", "Name"},
            {"Creation Date", "Erstellungsdatum"},
            {"Minimum point", "Mindestwert"},
            {"Average point", "Durchschnittswert"},
            {"Difficulty", "Schwierigkeit"},
            {"Discipline Name", "Disziplinname"},
            {"Lecture Hours", "Vorlesungsstunden"},
            {"Practice Hours", "Praktikumsstunden"},
            {"Self-study Hours", "Selbststudienstunden"},
            {"Owner", "Besitzer"},
            {"Add", "Hinzufügen"},
            {"Add if Min", "Hinzufügen, wenn Minimum"},
            {"Update by ID", "Aktualisieren nach ID"},
            {"Remove", "Löschen"},
            {"Remove by ID", "Löschen nach ID"},
            {"Remove Greater", "Größeren löschen"},
            {"Clear", "Leeren"},
            {"Average Minimum point", "Durchschnittlicher Mindestwert"},
            {"Minimum by ID", "Minimum nach ID"},
            {"Minimum point Ascending", "Mindestwert aufsteigend"},
            {"Execute Script", "Skript ausführen"},
            {"Information", "Informationen"},
            {"The collection is empty", "Die Sammlung ist leer"},
            {"Error connecting to the server", "Fehler bei der Verbindung zum Server"},
            {"Object addition", "Objekt hinzufügen"},
            {"Object editing", "Objekt bearbeiten"},
            {"Object removal", "Objekt löschen"},
            {"This user is already registered", "Dieser Benutzer ist bereits registriert"},
            {"The server is temporarily unavailable. Please try again later.", "Der Server ist vorübergehend nicht verfügbar. Bitte versuchen Sie es später erneut."},
            {"Invalid username or password", "Ungültiger Benutzername oder ungültiges Passwort"},
            {"User:", "Benutzer:"},
            {"No element in the collection with this ID", "Kein Element in der Sammlung mit dieser ID"},
            {"No element selected for removal", "Kein Element zum Löschen ausgewählt"},
            {"Are you sure you want to clear your collection?", "Sind Sie sicher, dass Sie Ihre Sammlung leeren möchten?"},
            {"Object with the minimum ID", "Objekt mit der kleinsten ID"},
            {"Minimum point in ascending order:", "Mindestwert in aufsteigender Reihenfolge:"},
            {"Collection Editor", "Sammlungseditor"},
            {"Enter ID", "ID eingeben"},
            {"Sorting and Filtering", "Sortieren und Filtern"},
            {"Sorting", "Sortieren"},
            {"Filtering", "Filtern"},
            {"Name (not empty):", "Name (nicht leer):"},
            {"X Coordinate (greater than -934):", "X-Koordinate (größer als -934):"},
            {"Y Coordinate (not greater than 946):", "Y-Koordinate (nicht größer als 946):"},
            {"Minimum point (greater than 0):", "Mindestwert (größer als 0):"},
            {"Average point (greater than 0):", "Durchschnittswert (größer als 0):"},
            {"Difficulty:", "Schwierigkeit:"},
            {"Discipline Name (empty if not filled):", "Disziplinname (leer, wenn nicht ausgefüllt):"},
            {"Lecture Hours:", "Vorlesungsstunden:"},
            {"Practice Hours:", "Praktikumsstunden:"},
            {"Self-study Hours:", "Selbststudienstunden:"},
            {"Save", "Speichern"},
            {"Cancel", "Abbrechen"},
            {"Invalid values entered", "Ungültige Werte eingegeben"},
            {"Not all required fields are filled", "Nicht alle erforderlichen Felder sind ausgefüllt"},
            {"The server is unavailable, please try again later", "Der Server ist nicht verfügbar. Bitte versuchen Sie es später erneut"},
            {"Ascending", "Aufsteigend"},
            {"Descending", "Absteigend"},
            {"Enter a value for the filter", "Geben Sie einen Wert für den Filter ein"},
            {"Select a field for sorting", "Wählen Sie ein Feld für die Sortierung"},
            {"Reset", "Zurücksetzen"},
            {"Ok", "Ok"},
            {"Open script file", "Skriptdatei öffnen"}
    };

    @Override
    protected Object[][] getContents() {
        return contents;
    }
}
