package ntnu.stud.steinkso.logcollector;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import ntnu.stud.steinkso.logcollector.backend.HTTPListener;
import ntnu.stud.steinkso.logcollector.backend.ServerCommuncation;
import ntnu.stud.steinkso.logcollector.preferences.LoggerPreferences;

public class DisclaimerDialog {

	private boolean gotDialogDisclaimer = false;
	private boolean gotPreferenceDisclaimer = false;
	private String preferenceDisclaimerResponse;
	private String dialogDisclaimerResponse;

	public DisclaimerDialog() {

		// First fetch latest disclaimers from server,
		// if they are newer than last, prompt user to 
		// agree again.

		ServerCommuncation.getDialogDisclaimer(new HTTPListener() {
			@Override
			public void onResponse(String response) {
				onDialogDisclaimer(response);
			}

			@Override
			public void handleError(int statusCode) {
				ErrorHandler.logError("Could not get Dialog: " + statusCode);
			}
		});

		ServerCommuncation.getPreferenceDisclaimer(new HTTPListener() {
			@Override
			public void onResponse(String response) {
				onPreferenceDisclaimer(response);
			}

			@Override
			public void handleError(int statusCode) {
				ErrorHandler.logError("Could not get Dialog: " + statusCode);
			}
		});
	}

	private void onDialogDisclaimer(String dialogDisclaimer) {
		gotDialogDisclaimer = true;
		this.dialogDisclaimerResponse = dialogDisclaimer;
		showDialogIfAllDisclaimersReceived();
	}

	private void onPreferenceDisclaimer(String preferenceDisclaimer) {
		gotPreferenceDisclaimer = true;
		this.preferenceDisclaimerResponse = preferenceDisclaimer;
		showDialogIfAllDisclaimersReceived();
	}

	private void showDialogIfAllDisclaimersReceived() {
		if (gotDialogDisclaimer && gotPreferenceDisclaimer) {
			updatePreferencesIfNeeded();
			presentDialogIfNotAnswered();
		}
	}

	private void updatePreferencesIfNeeded() {
		LoggerPreferences preferences = LoggerPlugin.getDefault()
				.getPreferences();

		System.out.println("Preferences same:"
				+ this.preferenceDisclaimerResponse.equals("OK"));
		System.out.println("Disclaimer same:"
				+ this.dialogDisclaimerResponse.equals("OK"));

		if (preferenceDisclaimerResponse.equals("OK")
				&& dialogDisclaimerResponse.equals("OK")) {
			return;
		}

		if (!preferenceDisclaimerResponse.equals("OK")) {
			preferences.setMessagePreferenceDisclaimer(preferenceDisclaimerResponse);
		}
		if (!dialogDisclaimerResponse.equals("OK")) {
			preferences.setMessageDialogDisclaimer(dialogDisclaimerResponse);
		}

		long unixTime = System.currentTimeMillis() / 1000L;
		System.out.println("Setting received at unixtime: "+String.valueOf(unixTime));

		preferences.setMessagesLastUpdateTimestamp(String.valueOf(unixTime));
		preferences.setDisclaimerAnswered(false);
		preferences.setLoggingIsActive(false);
		preferences.setUserParticipating(false);
	}

	public void presentDialogIfNotAnswered() {

		LoggerPreferences preferences = LoggerPlugin.getDefault()
				.getPreferences();
		final DisclaimerDialog dialog = this;

		if (preferences.getDisclaimerAnswered()) {
			return;
		}

		// Update the user interface asynchronously
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				dialog.createDialog();

			}
		});
	}

	public void createDialog() {

		LoggerPreferences preferences = LoggerPlugin.getDefault()
				.getPreferences();
		String disclaimer = preferences.getMessageDialogDisclaimer();
		String title = "LA Helper - Tilrettelegging av undervisning(TDT4100) til dere studenter";
		String yesButton = "Ja - jeg godtar";
		String noButton = "Nei - jeg godtar ikke";

		MessageDialog dialog = new MessageDialog(null, title, null, disclaimer,
				MessageDialog.QUESTION, new String[] { yesButton, noButton }, 0);
		boolean participating = false;
		int result = dialog.open();

		// User closed box without answer
		if (result == -1) {
			return;
		}

		// User clicked I accept
		if (result == 0) {
			participating = true;
		}

		handleDisclaimerAnswer(participating);

		System.out.println("EVENT: Dialog box result: " + result
				+ " Participating: " + participating);
	}

	public void handleDisclaimerAnswer(boolean participating) {
		LoggerPreferences preferences = LoggerPlugin.getDefault()
				.getPreferences();
		preferences.setDisclaimerAnswered(true);

		if (participating) {
			preferences.setUserParticipating(true);
			preferences.setLoggingIsActive(true);
		}
	}

}
