package it.unibz.testhunter.model;

public class TestStatusTimeModel {

	private long testId;
	private long statusId;
	private long time;
	
	public TestStatusTimeModel(Long testId, Long statusId, Long time) {
		this.testId = testId.longValue();
		this.statusId = statusId.longValue();
		this.time = time.longValue();
	}
	
	public long getTestId() {
		return testId;
	}
	
	public long getStatusId() {
		return statusId;
	}
	
	@Override  
    public boolean equals(Object other)  
    {  
        return (other != null) && (getClass() == other.getClass()) &&   
        getHashString().equals(((TestStatusTimeModel)other).getHashString());  
    }  
      	
	@Override  
    public int hashCode()  
    {  
        return toString().hashCode();  
    } 
	
	private String getHashString() {
		return String.format("test: %d status: %d", testId, statusId);
	}
      
    @Override  
    public String toString()  
    {  
        return String.format("test: %d status: %d time: %d", testId, statusId, time);  
    }  

	
}
