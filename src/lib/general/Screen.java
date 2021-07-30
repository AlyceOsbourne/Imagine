/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package lib.general;

import java.awt.*;

public interface Screen {
	Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
	default Dimension screensize(){return screensize;}
}
