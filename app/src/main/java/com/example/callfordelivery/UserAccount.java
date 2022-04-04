package com.example.callfordelivery;

//사용자 계정 정보 모델 클래ㅅ
public class UserAccount
{
    private  String idToken;    //파베 유저아이디인데 고유임 토큰정보
    private String emailId;     //이메일 아이디
    private  String password;   //비번

    public UserAccount () { }

    public String getIdToken() { return idToken; }

    public void setIdToken(String idToken) { this.idToken = idToken; }

    public String getEmailId() { return emailId; }

    public void setEmailId(String emailId) { this.emailId = emailId; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }
}
