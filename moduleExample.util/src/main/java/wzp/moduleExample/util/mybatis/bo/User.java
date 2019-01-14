package wzp.moduleExample.util.mybatis.bo;

public class User {
	int user_id;
	String user_loginname;
	String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUser_loginname() {
		return user_loginname;
	}

	public void setUser_loginname(String user_loginname) {
		this.user_loginname = user_loginname;
	}

}