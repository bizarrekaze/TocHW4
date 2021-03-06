/******************************************************
	學號:F74002109
	姓名:蔡翔任
	HW4
	參數如同規定
*******************************************************/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
class Jsonreader
{
	ArrayList<Integer> data = new ArrayList<Integer>();
	JSONTokener jsonParser;
	ArrayList<String> index = new ArrayList<String>();
	ArrayList<ArrayList<String>> month_data = new ArrayList<ArrayList<String>>();
	ArrayList<ArrayList<String>> max_money = new ArrayList<ArrayList<String>>();
	ArrayList<ArrayList<String>> min_money = new ArrayList<ArrayList<String>>();
	ArrayList<String> abc[];
	JSONArray arr = new JSONArray();
	public Jsonreader(URL url) throws IOException, JSONException
	{
		boolean flag = true;
		int i=0,message;
		String str="",str_road,str_money,str_month;
		ArrayList<String> str_list = new ArrayList<String>();
		JSONObject obj = new JSONObject();
		InputStream in = url.openStream();
		InputStreamReader read = new InputStreamReader(in, "UTF-8");
		BufferedReader br = new BufferedReader(read);
		while ((message = br.read()) != -1) 
		{
			 if (((char)message) != '\n') 
			 {
				if(!(message==125 || message==91 || message==93))
				{
					str = str + (char)message;
				}
				else if(message==125)
				{
					str = str + (char)message;
					jsonParser = new JSONTokener(str);
					obj=(JSONObject)jsonParser.nextValue();
					str_road = obj.getString("土地區段位置或建物區門牌");
					str_road = str_deal(str_road);
					if(!(str_road.equals("false")))
					{
						str_money = Integer.toString(obj.getInt("總價元"));
						str_month = Integer.toString(obj.getInt("交易年月"));
						for(int j=0;j<index.size();j++)
							if(index.get(j).equals(str_road))
								flag = false;
						if(flag)
						{
							month_data.add(new ArrayList());
							min_money.add(new ArrayList());
							max_money.add(new ArrayList());
							index.add(str_road);
							data.add(new Integer(0));
						}
						flag = true;
						try{
							check_money(str_road,str_money);//價錢
							if(check_month(str_road,str_month))
							{
								data.set(index.indexOf(str_road),data.get(index.indexOf(str_road))+1);
							}
							}catch(Exception e){;}
					}
					str="";
					obj = new JSONObject();
					message = br.read();
					
					if((char)message=='\n')
						message = br.read();
					else
						break;
				}
				else if(message==93 || message==91 || message==58 ||message==44 ||message==123)
					;
			 }
			 
		}
		read.close();
		br.close();
		in.close();
	}
	public void check_money(String road,String money)
	{
		int max=Integer.parseInt(money);
		int min=Integer.parseInt(money);
		if(max_money.get(index.indexOf(road)).size() == 0)
		{
			min_money.get(index.indexOf(road)).add(Integer.toString(min));	
			max_money.get(index.indexOf(road)).add(Integer.toString(max));
		}
		else
		{
			if(max > Integer.parseInt(max_money.get(index.indexOf(road)).get(0)))
			{
				max_money.get(index.indexOf(road)).clear();
				max_money.get(index.indexOf(road)).add(Integer.toString(max));
			}
			if(min < Integer.parseInt(min_money.get(index.indexOf(road)).get(0)))
			{
				min_money.get(index.indexOf(road)).clear();
				min_money.get(index.indexOf(road)).add(Integer.toString(min));
			}
		}
	}
	public boolean check_month(String road,String month)
	{
		for(int i=0;i<month_data.get(index.indexOf(road)).size();i++)
			if(month_data.get(index.indexOf(road)).get(i).equals(month))
				return false;
		month_data.get(index.indexOf(road)).add(month);
		return true;
	}
	public String str_deal(String str)
	{
		 int pos=str.length()-1,pos2=0;
		 String str2;
		 //路 街
		 if((str.lastIndexOf("路") !=-1))
			 pos = str.lastIndexOf("路");
		 else if((str.lastIndexOf("街") !=-1))
			 pos = str.lastIndexOf("街");
		 else if((str.lastIndexOf("大道") !=-1))
			 pos = str.lastIndexOf("大道");
		 else if((str.lastIndexOf("巷") !=-1))
			 pos = str.lastIndexOf("巷");
		 //市 區 鄉 鎮 里 鄰
		 /*if((str.lastIndexOf("鄉") !=-1))
			 pos2 = str.lastIndexOf("鄉");
		 if((str.lastIndexOf("鄰") !=-1) )
			 pos2 = str.lastIndexOf("鄰");
		 if((str.lastIndexOf("鎮") !=-1) && str.lastIndexOf("鎮")>pos2)
			 pos2 = str.lastIndexOf("鎮");
		 if((str.lastIndexOf("里") !=-1) && str.lastIndexOf("里")>pos2)
			 pos2 = str.lastIndexOf("里");
		 if((str.lastIndexOf("區") !=-1) && str.lastIndexOf("區")>pos2)
			 pos2 = str.lastIndexOf("區");
		 if((str.lastIndexOf("市") !=-1) && str.lastIndexOf("市")>pos2)
			 pos2 = str.lastIndexOf("市");
		 */
		 str2 =str.substring(pos2,pos+1);
		 if(pos!=str.length()-1)
			return str2;
		 else
			 return "false";	 
	}
	public JSONArray get()
	{
		return arr;
	}
	public ArrayList<Integer> get_data()
	{
		return data;	
	}
	public ArrayList<String> get_index()
	{
		return index;	
	}
	public ArrayList<ArrayList<String>> get_min_money()
	{
		return min_money;
	}
	public ArrayList<ArrayList<String>> get_max_money()
	{
		return max_money;
	}
}
public class TocHw4 {
  public static void main(String[] args) throws IOException, JSONException, InterruptedException {
	  	if(args.length != 1)
		{
			System.out.println("Error input !");
			System.exit(1);
		}
		URL url = new URL(args[0]);
		ArrayList<Integer> data = new ArrayList<Integer>();
		int max=0;
		ArrayList<String> index = new ArrayList<String>();
		ArrayList<ArrayList<String>> max_money = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> min_money = new ArrayList<ArrayList<String>>();
		JSONArray arr = new JSONArray();
		Jsonreader jr = new Jsonreader(url);
		arr = jr.get();
		index = jr.get_index();
		data = jr.get_data();
		max_money = jr.get_max_money();
		min_money = jr.get_min_money();
		for(int i=0;i<index.size();i++){		//搜尋月份最多的次數
			if(data.get(i)>max)
			{
				max = data.get(i);
			}
		}
		for(int i=0;i<index.size();i++)			//把次數最多的路都印出來
			if(data.get(i)==max)
			{
				System.out.print(index.get(i) + ", ");
				System.out.print("最高成交價: "+max_money.get(i).get(0));
				System.out.println(", 最低成交價: "+min_money.get(i).get(0));
			}
  }
}
