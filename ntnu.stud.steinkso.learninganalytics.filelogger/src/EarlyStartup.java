import org.eclipse.core.internal.runtime.Activator;
import org.eclipse.ui.IStartup;


public class EarlyStartup implements IStartup {

	@Override
	public void earlyStartup() {
		Activator activator = Activator.getDefault();

	}

}
