import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
* <p>Title: 博能位置数据服务器 - UTCConvert</p>
*
* <p>Description:
* 	UTC时间格式转化demo
* </p>
*
* <p>Copyright: Copyright bnkj(c) 2018</p>
*
* <p>Company: 北京博能科技股份有限公司</p>
*
* @author william
* @version 1.0
*/
public class UTCConvert {
	public static void main(String args[]) {
		
        //SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");  
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");  
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        
        try { 
    		String x= "2018-02-04 15:30:40";
            SimpleDateFormat dfx = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date m = dfx.parse(x);

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            String k = df.format(m);
            System.out.println("UTC = " + k);
        	
         	Date date = sdf1.parse(k.replace("Z", " UTC"));
        	String str = sdf2.format(date);
              
        	System.out.println(str);  
        	
        	k = df.format(m);
        	k="2018-06-26T03:58:19.9629572Z";
        	k = k.substring(0,22) + " UTC";
        	SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");  
         	date = sdf3.parse(k);
        	str = sdf2.format(date);
              
        	System.out.println(str);  
        	
          } catch (Exception e) {  
              e.printStackTrace();  
          }  
	}
}
