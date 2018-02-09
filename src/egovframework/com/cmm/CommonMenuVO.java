package egovframework.com.cmm;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown=true)
public class CommonMenuVO extends ComDefaultVO {
    private String menuId;
    private String menuNm;
    private String uprMenuId;
    private String uprMenuNm;
    private String menuOrd;
    private String menuType;
    private String level;
    private String pgmId;
    private String pgmNm;
    private String menuEpl;
    private String useYn;
    private String url;
    
    private String id;
    private String text;
    private String parent;
    private String topMenuId;
    private String usrId;
    private String data;
    private String className;
    private String dockClassName;
    private String controller;
    private String type;
    public String getMenuId() {
        return menuId;
    }
    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
    public String getMenuNm() {
        return menuNm;
    }
    public void setMenuNm(String menuNm) {
        this.menuNm = menuNm;
    }
    public String getUprMenuId() {
        return uprMenuId;
    }
    public void setUprMenuId(String uprMenuId) {
        this.uprMenuId = uprMenuId;
    }
    public String getUprMenuNm() {
        return uprMenuNm;
    }
    public void setUprMenuNm(String uprMenuNm) {
        this.uprMenuNm = uprMenuNm;
    }
    public String getMenuOrd() {
        return menuOrd;
    }
    public void setMenuOrd(String menuOrd) {
        this.menuOrd = menuOrd;
    }
    public String getMenuType() {
        return menuType;
    }
    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }
    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
    }
    public String getPgmId() {
        return pgmId;
    }
    public void setPgmId(String pgmId) {
        this.pgmId = pgmId;
    }
    public String getPgmNm() {
        return pgmNm;
    }
    public void setPgmNm(String pgmNm) {
        this.pgmNm = pgmNm;
    }
    public String getMenuEpl() {
        return menuEpl;
    }
    public void setMenuEpl(String menuEpl) {
        this.menuEpl = menuEpl;
    }
    public String getUseYn() {
        return useYn;
    }
    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getParent() {
        if("-".equals(parent)){
            parent = "#";
        }
        return parent;
    }
    public void setParent(String parent) {
        if("-".equals(parent)){
            parent = "#";
        }
        this.parent = parent;
    }
    public String getTopMenuId() {
        return topMenuId;
    }
    public void setTopMenuId(String topMenuId) {
        this.topMenuId = topMenuId;
    }
    public String getUsrId() {
        return usrId;
    }
    public void setUsrId(String usrId) {
        this.usrId = usrId;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public String getDockClassName() {
        return dockClassName;
    }
    public void setDockClassName(String dockClassName) {
        this.dockClassName = dockClassName;
    }
    public String getController() {
        return controller;
    }
    public void setController(String controller) {
        this.controller = controller;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    
    @Override
    public String toString() {
        return "CommonMenuVO [menuId=" + menuId + ", menuNm=" + menuNm
                + ", uprMenuId=" + uprMenuId + ", uprMenuNm=" + uprMenuNm
                + ", menuOrd=" + menuOrd + ", menuType=" + menuType
                + ", level=" + level + ", pgmId=" + pgmId + ", pgmNm=" + pgmNm
                + ", menuEpl=" + menuEpl + ", useYn=" + useYn + ", url=" + url
                + ", id=" + id + ", text=" + text + ", parent=" + parent
                + ", topMenuId=" + topMenuId + ", usrId=" + usrId + ", data="
                + data + ", className=" + className + ", dockClassName="
                + dockClassName + ", controller=" + controller + ", type="
                + type + "]";
    }
}
