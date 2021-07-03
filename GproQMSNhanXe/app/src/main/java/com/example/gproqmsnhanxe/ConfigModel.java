package com.example.gproqmsnhanxe;

import java.io.Serializable;

public class ConfigModel  implements Serializable {
    private String APIIP, QMSIP,HeadCode,UserCode,CustCode,UserName,Password,ButWidth,ButHeight,ButFonSize,TOKEN,LogoWidth,LogoHeight,TitleFonSize,Title ;

    public ConfigModel(){}

    public ConfigModel(String _apiIP, String _qmsIp,String _headCode, String _userCode, String _custCode,String _userName, String _password,String _butWidth, String _butHeight,String _butFonSize, String _token,String _logoWidth, String _logoHeight,String _titleFonSize, String _title ){
        APIIP = _apiIP;
        QMSIP = _qmsIp;
        HeadCode = _headCode;
        UserCode = _userCode;
        CustCode = _custCode;
        UserName = _userName;
        Password = _password;
        ButWidth = _butWidth;
        ButHeight = _butHeight;
        ButFonSize = _butFonSize;
        TOKEN = _token;

        LogoHeight = _logoHeight;
        LogoWidth = _logoWidth;
        TitleFonSize = _titleFonSize;
        Title = _title;
    }
    public String getAPIIP() {
        return APIIP;
    }
    public void setAPIIP(String _apiIP) {  APIIP = _apiIP;  }

    public String getQMSIP() {
        return QMSIP;
    }
    public void setQMSIP(String _qmsIp) {
        QMSIP = _qmsIp;
    }

    public String getHeadCode() {
        return HeadCode;
    }
    public void setHeadCode(String _headCode) {
        HeadCode = _headCode;
    }

    public String getUserCode() {
        return UserCode;
    }
    public void setUserCode(String _userCode) {
        UserCode = _userCode;
    }

    public String getCustCode() {
        return CustCode;
    }
    public void setCustCode(String _custCode) {
        CustCode = _custCode;
    }

    public String getUserName() {
        return UserName;
    }
    public void setUserName(String _userName) {
        UserName = _userName;
    }

    public String getPassword() {
        return Password;
    }
    public void setPassword(String _password) {
        Password = _password;
    }

    public String getButWidth() {
        return ButWidth;
    }
    public void setButWidth(String _butWidth) {
        ButWidth = _butWidth;
    }

    public String getButHeight() {
        return ButHeight;
    }
    public void setButHeight(String _butHeight) {
        ButHeight = _butHeight;
    }

    public String getButFonSize() {
        return ButFonSize;
    }
    public void setButFonSize(String _butFonSize) {
        ButFonSize = _butFonSize;
    }

    public String getTOKEN() {
        return TOKEN;
    }
    public void setTOKEN(String _token) {
        TOKEN = _token;
    }

    public String getLogoWidth() {
        return  LogoWidth;
    }
    public void setLogoWidth(String _logoWidth) {
        LogoWidth = _logoWidth;
    }

    public String getLogoHeight() {
        return LogoHeight;
    }
    public void setLogoHeight(String _logoHeight) {
        LogoHeight = _logoHeight;
    }

    public String getTitleFonSize() {
        return TitleFonSize;
    }
    public void setTitleFonSize(String _titleFonSize) {
        TitleFonSize = _titleFonSize;
    }

    public String getTitle() {
        return Title;
    }
    public void setTitle(String _title) {
        Title = _title;
    }
}
