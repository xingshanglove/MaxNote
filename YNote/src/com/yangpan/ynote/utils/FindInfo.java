package com.yangpan.ynote.utils;

import java.util.ArrayList;
import java.util.List;

import com.yangpan.ynote.bean.Alarm;

public class FindInfo {
	public static List<Alarm> findData(String s) {
		List<String> result=new ArrayList<String>();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 48 && c <= 57) {
				for (int j = i; j < s.length(); j++) {
					char c1 = s.charAt(j);
					if (!IsNot(c1)) {
						int start=i-5;
						if(start<0)
							start=0;
						String content = s.substring(start, j);
						if(content.contains("会")||content.contains("议")){
							result.add(content);
						}else if(content.contains("航")||content.contains("班")){
							result.add(content);
						}
						i = j;
						break;
					}else if(j==s.length()-1){
						int start=i-5;
						if(start<0)
							start=0;
						String content = s.substring(start, s.length());
						if(content.contains("会")||content.contains("议")){
							result.add(content);
						}else if(content.contains("航")||content.contains("班")){
							result.add(content);
						}
						i = j;
						break;
					}
				}
			}
		}
		List<Alarm> alarms=new ArrayList<Alarm>();
		for(String item:result){
			StringBuffer typeSb=new StringBuffer();
			StringBuffer timeHSb=new StringBuffer();
			String timeMSb=item.substring(item.length()-2,item.length());
			String rest=item.substring(0, item.length()-2);
			System.out.println(timeMSb);
			for(int i=0;i<rest.length();i++){
				char c=item.charAt(i);
				if(!isDigit(c)){
					typeSb.append(c+"");
				}else if(c==':'||c=='：'||c=='.'){
					continue;
				}else if(isDigit(c)){
					timeHSb.append(c+"");
				}
			}
			String type=typeSb.toString();
			if(type.contains("会")){
				type="会议";
			}else if(type.contains("航")){
				type="航班";
			}
			String time=timeHSb.toString()+":"+timeMSb.toString();
			Alarm alarm=new Alarm(time,type,1);
			alarms.add(alarm);
		}
		return alarms;
	}

	private static boolean isDigit(char c) {
		if(c>=48&&c<=57)
			return true;
		return false;
	}

	private static boolean IsNot(char c1) {
		if ((c1 >= 48 && c1 <= 57) || c1 == ':'||c1=='：'||c1=='.') {
			return true;
		}
		return false;
	}
}
