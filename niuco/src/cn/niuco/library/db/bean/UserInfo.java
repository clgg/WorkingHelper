package cn.niuco.library.db.bean;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tb_user")
public class UserInfo {

    @DatabaseField(generatedId = true)
    private int id; //默认的自增id
    @DatabaseField(columnName = "userid")
    private int userID; //用户id
    @DatabaseField(columnName = "nickname")
    private String nickname; //用户昵称
    @DatabaseField(columnName = "portrait")
    private String portrait; //用户头像url
    @DatabaseField(columnName = "description")
    private String description; //用户描述
    @DatabaseField(columnName = "gender")
    private String gender; //用户性别
    @DatabaseField(columnName = "token")
    private String token; //用户token
    @DatabaseField(columnName = "account")
    private String account; //用户account


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public UserInfo() {
    }
}
