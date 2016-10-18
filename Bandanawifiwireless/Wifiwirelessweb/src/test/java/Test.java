import java.math.BigInteger;
import java.util.Date;

public class Test {
public static void main(String[] args) {
	Date today=new Date();
	Date d =new Date("Wed, 12 Oct 2016 14:34:40 +0000");
	if(today.after(d))
	{
		System.out.println("in if");
		
	}
	else{
		System.out.println("in else");
	}
}
}
