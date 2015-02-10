package ntnu.stud.steinkso.logcollector.preferences;

import ntnu.stud.steinkso.logcollector.LoggerPlugin;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.help.IWorkbenchHelpSystem;

public class LoggerPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public LoggerPreferencePage() {
		super(GRID);
		LoggerPreferences preferences = LoggerPlugin.getDefault().getPreferences();
		setPreferenceStore(preferences.getPreferenceStore());

		String disclaimer = preferences.getMessagePreferenceDisclaimer();
		setDescription(disclaimer);
	}

	@Override
	public void init(IWorkbench workbench) {

	}

	@Override
	protected void createFieldEditors() {
		Composite parent = getFieldEditorParent();
		IWorkbenchHelpSystem help = PlatformUI.getWorkbench().getHelpSystem();
		String helpContext = LoggerPlugin.getDefault().getBundle().getSymbolicName() + ".LAHelper";
		help.setHelp(parent, helpContext);
		
		//String disclaimer = "Målet med denne pluginen er å forbedre og tilrettelegge undervisningen i TDT4100 til dere studenter. Ved å samle inn anonym data om hvilke oppgaver som blir gjort, og ved å finne elementer i oppgavene som det kan tyde på at flere studenter trenger hjelp med, kan vi ta hensyn til dette i undervisningen og forbedre oppgave teksten om det ser ut til at den kan missforstås. For å nå målet vil vi logge koden, og test kjøringen deres når dere gjør oppgavene for å samle data. All denne dataen er helt anonym og det er umulig for oss å kunne linke kode til individer. Om du deltar er det mulig at du kan få en oversikt over hva det kan se ut til at du har hatt vansker med i semesteret før eksamen, slik at du lettere kan oppdage hva du må øve på, med forbehold om at dette kan utebli om det viser seg å være vanskelig å hente ut denne informasjonen fra dataen som er lagret. Vi setter stor pris på alle som deltar, og de som deltar vil også være de som får påvirket undervisningen mest. Takk!";
		String radioHeader = "Logging er:";
		String[][] radios = new String[][] {
				{"Aktivert", LoggerPreferences.LOGGING_IS_ACTIVATED },
				{"Deaktivert", LoggerPreferences.LOGGING_IS_DEACTIVATED }
			};
		addField(new RadioGroupFieldEditor(LoggerPreferences.LOGGING_IS_ACTIVATED, radioHeader,1,radios, parent));


		Label seperator = new Label(parent, SWT.SHADOW_OUT);
		seperator.setText(" ");
		GridData gridDataSep = new GridData();
		gridDataSep.horizontalSpan = 2;
		seperator.setLayoutData(gridDataSep);

		addField(new StringFieldEditor(LoggerPreferences.CLIENT_ID_NAME, "Oppgi et kallenavn/identifikasjonsnavn: ", parent));

		Label idLabel = new Label(parent, SWT.SHADOW_IN);
		idLabel.setText("Kallenavn blir brukt som en nøkkel for å finne data som er logget av deg.\nNavnet trenger ikke ha noen tilknyttning til deg, det blir kun brukt i forbindelse med \nhjelp eller for fremtidige tjenester hvor du kan få informasjon om dataene dine.\n");

		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		idLabel.setLayoutData(gridData);
		addField(new StringFieldEditor(LoggerPreferences.ROOT_LOGGING_DIRECTORY, "Logg kun filer med sti som matcher(eks. /tdt4100,/oving*):", parent));
	}

}
