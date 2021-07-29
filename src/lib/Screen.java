/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package lib;

import java.awt.*;

public interface Screen {
	Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
	default Dimension screensize(){return screensize;}
}
