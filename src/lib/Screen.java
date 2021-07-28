package lib;

import java.awt.*;

public interface Screen {
	Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
	default Dimension screensize(){return screensize;}
}
