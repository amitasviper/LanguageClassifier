import java.applet.Applet;
import java.awt.*;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Amit Kumar
 */
public class DigitalClock extends Applet implements Runnable {

    String date;
    Calendar c;
    Thread t;

    public void init() {
        date = "";
        c = Calendar.getInstance();
        setBackground(Color.red);
        setForeground(Color.BLACK);
        Font f = new Font("Times New Roman", Font.BOLD, 30);
        setFont(f);
    }

    @Override
    public void start() {
        t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        try {
            while (true) {
				date = "";
                c.setTimeInMillis(System.currentTimeMillis());
                date = date + c.get(Calendar.DAY_OF_MONTH);
                date = date + "-" + (c.get(Calendar.MONTH) + 1);
                date = date + "-" + c.get(Calendar.YEAR);
                date = date + "  " + c.get(Calendar.HOUR);
                date = date + ":" + c.get(Calendar.MINUTE);
                date = date + ":" + c.get(Calendar.SECOND);
                if (c.get(Calendar.AM_PM) == Calendar.AM) {
                    date = date + "  AM";
                } else {
                    date = date + "  PM";
                }
				repaint();
                t.sleep(990);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(DigitalClock.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void paint(Graphics g) {
		g.setColor(Color.blue);
        g.drawString(date, 90, 110);
    }
}
