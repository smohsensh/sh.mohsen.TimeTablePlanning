package sh.mohsen.tt.general;

public class TimeInterval {

	int  interval, group;

	public TimeInterval(int interval, int group) {
		this.interval = interval;
		this.group = group;
	}
	
	
	public int getInterval() {
		return interval;
	}


	public void setInterval(int interval) {
		this.interval = interval;
	}


	public int getGroup() {
		return group;
	}


	public void setGroup(int group) {
		this.group = group;
	}

	@Override
	public boolean equals(Object obj) {
		if(! (obj instanceof TimeInterval) )
			return false;
		TimeInterval sec = (TimeInterval) obj;
		if(sec.getInterval() == this.interval && sec.getGroup() == this.group)
			return true;
		return false;
	}
	
	
	
}
