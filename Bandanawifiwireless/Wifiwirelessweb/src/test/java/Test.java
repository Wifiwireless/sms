import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Test {
	private static String DATE_FORMAT="EEE, dd MMM yyyy HH:mm:ss";
public static void main(String[] args) throws ParseException {
	Date today=new Date();
	SimpleDateFormat dateFormatGmt = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
	Date d =dateFormatGmt.parse("Wed, 12 Oct 2016 14:34:40 +0000");
	
	if (today.after(d)) {
		System.out.println("old dataa");
		// updatecustomer.add(cus);

	} else {
	}
	

}
}
