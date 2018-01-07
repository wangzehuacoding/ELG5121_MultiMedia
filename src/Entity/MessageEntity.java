package Entity;

public class MessageEntity {
	private String groupID;
	private String userName;
	private String text;
	
	public String getGroupId()
	{
		return groupID;
	}
	
	public void setGroupId(String groupId)
	{
		this.groupID = groupId;
	}
	
	public String getUserName()
	{
		return userName;
	}
	
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	
	public String getText()
	{
		return text;
	}
	
	public void setText(String text)
	{
		this.text = text;
	}
}

