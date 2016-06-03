package com.yangpan.ynote.db;

import java.util.List;

import com.yangpan.ynote.bean.Alarm;

public interface IAlarmImpl {
	public int findCount();
	public boolean addAlarm(Alarm alarm);
	public boolean updateAlarm(Alarm alarm);
	public boolean deleteAlarm(Alarm alarm);
	public List<Alarm> findAll();
}
