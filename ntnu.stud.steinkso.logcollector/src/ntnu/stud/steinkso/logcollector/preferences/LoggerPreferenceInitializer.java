package ntnu.stud.steinkso.logcollector.preferences;

import ntnu.stud.steinkso.logcollector.LoggerPlugin;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class LoggerPreferenceInitializer extends
		AbstractPreferenceInitializer{

	@Override
	public void initializeDefaultPreferences() {

		IPreferenceStore store = LoggerPlugin.getDefault().getPreferenceStore();

       // String backupPreferenceDisclaimer = "Ved å samle inn anonym data om oppgavevalg og utførelse, kan vi gjøre analyse for å identifisere spesielt vanskelige øvinger og tema.\nDette kan føre til mindre frustrasjon og mer hjelp til dere. All dataen er helt anonym og det er umulig å linke kode til individer.\n\nVed å aktivere logging samtykker du til logging av: tester som kjøres, kode som blir skrevet og feilmeldinger som oppstår. ";
        //String backupDialogDisclaimer = "Ved å samle inn anonym data om oppgavevalg og utførelse, kan vi gjøre analyse for å identifisere spesielt vanskelige øvinger og tema. Dette kan føre til mindre frustrasjon og mer hjelp til dere. All data er helt anonym og det er umulig å linke informasjon til individer. \n\nVed å trykke godta samtykker du til logging av: tester som kjøres, kode som blir skrevet og feilmeldinger som oppstår.  \n\nLoggingen kan skrues av og på i instillingene, hvor det også er mulig å legge inn et kallenavn som gjør dine data tilgjengelig for studass om du skulle trenge hjelp.\n\nPå forhånd takk!\n";
        
        String backupDialogDisclaimer = "Learning Analytics Helper er et Eclipse-tillegg som skal hjelpe oss å få innsikt i hvordan det jobbes med programmeringsoppgaver, så det blir lettere å forbedre dem og gi målrettet støtte.\n\n LA Helper logger kontinuerlig data om hva du gjør i Eclipse og sender det anonymt til en server. Det som logges er\n - filer du redigerer\n - problem-markører i editoren som legges inn av kompilatoren\n - hvilke tester som kjøres og test-resultatene\n Ved å aktivere logging samtykker du til at data kan brukes til analyse av hvordan det jobbes med koding, og til forbedring av oppgaver og øvingsopplegget.\n\n Data logges anonymt, men du har anledning til å legge inn et kallenavn, som knyttes til dataene. Med dette kallenavnet har du muligheten til få innsyn i hva som er logget, og det kan brukes av oss til å gi mer personlig hjelp og støtte, hvis du ønsker det.";
        String backupPreferenceDisclaimer = backupDialogDisclaimer;
		store.setDefault(LoggerPreferences.MARKERS_FILENAME,".markers.json");
        store.setDefault(LoggerPreferences.TEST_FILENAME,".tests.json");
        store.setDefault(LoggerPreferences.SERVER_URL,"http://vm-6126.idi.ntnu.no/learninganalytics/");
        //store.setDefault(LoggerPreferences.SERVER_URL,"http://errorlog.steinsorhus.com/");
        //store.setDefault(LoggerPreferences.SERVER_URL,"http://localhost:50807/");
        store.setDefault(LoggerPreferences.CLIENT_ID_NAME,"");
        store.setDefault(LoggerPreferences.LOGGING_IS_ACTIVATED,LoggerPreferences.LOGGING_IS_DEACTIVATED);
        store.setDefault(LoggerPreferences.ROOT_LOGGING_DIRECTORY,"/");
        store.setDefault(LoggerPreferences.DISCLAIMER_ANSWERED,false);
        store.setDefault(LoggerPreferences.USER_PARTICIPATING,false);

		store.setDefault(LoggerPreferences.MESSAGE_DIALOG_DISCLAIMER,backupDialogDisclaimer);
		store.setDefault(LoggerPreferences.MESSAGE_PREFERENCE_DISCLAIMER,backupPreferenceDisclaimer);
		store.setDefault(LoggerPreferences.MESSAGES_LAST_UPDATE_TIMESTAMP,"0");
	}
}