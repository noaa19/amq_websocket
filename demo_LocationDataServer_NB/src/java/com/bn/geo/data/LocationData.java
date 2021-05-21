package com.bn.geo.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.bn.geo.LocationDataServerUdpHandler;

/**
* <p>Title: 博能位置数据服务器 - LocationData</p>
*
* <p>Description:
* 	位置数据对象，系统会根据位置数据类型，通过字符串构造出LocationData对象，并将这个对象发布到不同的AMQ的topic中
* </p>
*
* <p>Copyright: Copyright bnkj(c) 2018</p>
*
* <p>Company: 北京博能科技股份有限公司</p>
*
* @author william
* @version 1.0
*/
public class LocationData implements Serializable {
	/**
	 * log4j日志输出对象
	 */
	private static Logger log4j = Logger.getLogger(LocationDataServerUdpHandler.class);
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 10000L;

	/**
	 * 位置类型，01-航空器，02-车辆，03-施工人员,04-无动力设备,05-旅客。。。。。。
	 */
	private String locationType;
	
	/**
	 * 设备编号，如航班流水号、车辆编号以及其它定位设备编号
	 */
	private String id;
	
	/**
	 * 定位数据获取时间
	 */
	private Date locationTime;
	
	/**
	 * 定位数据x值
	 */
	private double x;
	
	/**
	 * 定位数据y值
	 */
	private double y;
	
	/**
	 * 定位数据地理坐标dx值
	 */
	private double dx;
	
	/**
	 * 定位数据地理坐标dy值
	 */
	private double dy;
	
	/**
	 * 定位数据z值
	 */
	private double z;
	
	/**
	 * 水平方向速度，通过vx和vy可以计算出speed和direct
	 */
	private double vx;
	
	/**
	 * 垂直方向速度，通过vx和vy可以计算出speed和direct
	 */
	private double vy;
	
	/**
	 * 当前速度
	 */
	private double speed;
	
	/**
	 * 当前方向
	 */
	private double direct;
	
	/**
	 * 定位数据的wkid
	 */
	private int wkid;
	
	/**
	 * 定位状态：通常定位状态为1的才有效(通过定位质量计算出来)
	 */
	private int state;
	
	/**
	 * 是否在线
	 */
	private boolean online;
	
	/**
	 * 数据提供方编号
	 */
	private String provider;
	
	/**
	 * 定位扩展属性(保留)
	 */
	private String ext;

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getLocationTime() {
		return locationTime;
	}

	public void setLocationTime(Date locationTime) {
		this.locationTime = locationTime;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getDx() {
		return dx;
	}

	public void setDx(double dx) {
		this.dx = dx;
	}

	public double getDy() {
		return dy;
	}

	public void setDy(double dy) {
		this.dy = dy;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getDirect() {
		return direct;
	}

	public void setDirect(double direct) {
		this.direct = direct;
	}

	public int getWkid() {
		return wkid;
	}

	public void setWkid(int wkid) {
		this.wkid = wkid;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}
	
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public double getVx() {
		return vx;
	}

	public void setVx(double vx) {
		this.vx = vx;
	}

	public double getVy() {
		return vy;
	}

	public void setVy(double vy) {
		this.vy = vy;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	/**
	 * 将指定的字符串解析成LocationData对象
	 * @param locationStr 需要解析的字符串
	 */
	public static LocationData parse(String locationStr) throws LocationDataFormatException, Exception {
		//如果传入的是空字符串，返回null
		if(locationStr == null || "".equals(locationStr)) return null;
		
		String locationDataArray[] = locationStr.split(",");
		
		//对格式进行基本的验证
		if(locationDataArray.length < 15) throw new LocationDataFormatException("位置数据格式不正确，每行必须有15个数据项！");
		
		//将字符串转化成对象
		//时间格式: yyyy-MM-dd HH:mm:ss
		SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//UTC格式的时间，如：2018-06-26T03:58:19.962Z
		SimpleDateFormat dateFmtUTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");  
		
		LocationData dataObj = null;
		
		//locationType必须是在[01-06]中的某一个类型编号
		if("01".equals(locationDataArray[0]) || "02".equals(locationDataArray[0]) || 
		   "03".equals(locationDataArray[0]) || "04".equals(locationDataArray[0]) || 
		   "05".equals(locationDataArray[0]) || "06".equals(locationDataArray[0]) ||
		   "08".equals(locationDataArray[0])) {
			
			//创建对象并将数据注入
			dataObj = new LocationData();
			//[0]定位类型01-08
			dataObj.setLocationType(locationDataArray[0]);
			//[1]定位设备的唯一编号
			dataObj.setId(locationDataArray[1]);
			
			Date locTime = null;
			//[2]定位时间类型：1表示yyyy-mm-dd hh:mm:ss或者UTC格式，2表示1970-01-01起的毫秒数
			String locTimeType = locationDataArray[2];
			if("1".equals(locTimeType)) {
				if(locationDataArray[3] != null) {
					//判断时间格式是否是UTC
					if(locationDataArray[3].endsWith("Z")) 
						//UTC格式时间的转化
						locTime = dateFmtUTC.parse(locationDataArray[3].substring(0, 22) + " UTC");
					else
						//北京时间格式
						locTime = dateFmt.parse(locationDataArray[3]);
				}
			}
			else {
				//从1970-01-01 00:00开始的毫秒数
				locTime = new Date(Long.parseLong(locationDataArray[3]));
			}
			
			dataObj.setLocationTime(locTime);
			
			//设置x、y、z以及空间参考
			dataObj.setX(Double.parseDouble(locationDataArray[4]));
			dataObj.setY(Double.parseDouble(locationDataArray[5]));
			dataObj.setDx(Double.parseDouble(locationDataArray[4]));
			dataObj.setDy(Double.parseDouble(locationDataArray[5]));
			dataObj.setZ(Double.parseDouble(locationDataArray[6]));
			
			//设置空间参考，默认是4326
			String wkid = locationDataArray[7];
			if(wkid == null || "".equals(wkid))
				dataObj.setWkid(4326);
			else
				dataObj.setWkid(Integer.parseInt(wkid));
			
			//获取vx、vy，即便没有值，也需要传入0
			double vx = Double.parseDouble(locationDataArray[8]);
			double vy = Double.parseDouble(locationDataArray[9]);
			
			//速度
			double speed = 0.0;
			//方向
			double direct = 0.0;
			if(vx != 0.0 && vy != 0.0) {
				//x和y方向任何一个方向有速度，则可以计算出速度、方向
				speed = Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2));
				direct = Math.atan2(vy, vx);
			}
			else {
				speed = Double.parseDouble(locationDataArray[10]);
				direct = Double.parseDouble(locationDataArray[11]);
			}
			
			//设置速度和方位
			dataObj.setSpeed(speed);
			dataObj.setDirect(direct);
			
			//定位质量
			int quality = Integer.parseInt(locationDataArray[12]);
			//是否在线：1在线，0不在线
			String online = locationDataArray[13];
			//数据提供方id
			String provider = locationDataArray[14];
			
			//根据定位质量确定state，当定位质量大于5的时候，state=1表示位置可信度比较高
			dataObj.setState(quality > 0 ? 1 : 0);
			dataObj.setOnline("1".equals(online) ? true : false);
			dataObj.setProvider(provider);
			//如果传入的数据中有扩展属性，则将数据保存到dataObj.ext属性中
			if(locationDataArray.length > 15) 
				dataObj.setExt(locationDataArray[15]);
		}
		else 
			throw new LocationDataFormatException("位置数据格式不正确，只能接收特定定位设备传入的数据！");
		
		return dataObj;
	}
	
	/**
	 * 将LocationData对象转化成字符串
	 */
	public String toString() {
		//时间格式: yyyy-MM-dd HH:mm:ss
		SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return "locationType=" +locationType + ",id=" + id + ",locationTime=" +
			   dateFmt.format(locationTime) + ",x=" + 
			   x + ",y=" + y + ",z=" + z + 
			   ",wkid=" + wkid +
			   ",dx=" + dx +
			   ",dy=" + dy +
			   ",vx=" + vx + ",vy=" + vy +
			   ",speed=" + speed + ",direct=" + direct + 
			   ",state=" + state + ",provider=" + provider + ",ext=" + ext;
	}
	
	public static void main(String args[]) {
		String sData = "02,V27708102,1,2018-07-27 12:00:30,106.5136528015, 29.5429956963,0,4326,0,0,45,90,10,1,BJBN";
		try {
			LocationData data = LocationData.parse(sData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
