package com.quanliren.quan_two.bean.emoticon;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;

public class EmoticonActivityListBean implements Serializable {

    private ArrayList<EBanner> blist;
    private ArrayList<EmoticonZip> plist;

    public ArrayList<EBanner> getBlist() {
        return blist;
    }

    public void setBlist(ArrayList<EBanner> blist) {
        this.blist = blist;
    }

    public ArrayList<EmoticonZip> getPlist() {
        return plist;
    }

    public void setPlist(ArrayList<EmoticonZip> plist) {
        this.plist = plist;
    }

    public EmoticonActivityListBean() {
        super();
        // TODO Auto-generated constructor stub
    }

    public EmoticonActivityListBean(ArrayList<EBanner> blist,
                                    ArrayList<EmoticonZip> plist) {
        super();
        this.blist = blist;
        this.plist = plist;
    }

    public static class EBanner implements Serializable {
        private int id;
        private String bannerUrl;

        public EBanner() {
            super();
            // TODO Auto-generated constructor stub
        }

        public EBanner(int id, String bannerUrl) {
            super();
            this.id = id;
            this.bannerUrl = bannerUrl;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getBannerUrl() {
            return bannerUrl;
        }

        public void setBannerUrl(String bannerUrl) {
            this.bannerUrl = bannerUrl;
        }
    }

    @DatabaseTable(tableName = "EmoticonZip")
    public static class EmoticonZip implements Serializable {

        @DatabaseField(id = true)
        private int id;
        @DatabaseField
        private String iconfile;
        @DatabaseField
        private String userId;
        private String name;
        private String title;
        private String icoUrl;
        private int type;
        private boolean have;
        private double price;
        private int isBuy;
        private String downUrl;
        private String size;
        private String bannerUrl;
        private String remark;

        public String getIconfile() {
            return iconfile;
        }

        public void setIconfile(String iconfile) {
            this.iconfile = iconfile;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public boolean isHave() {
            return have;
        }

        public void setHave(boolean have) {
            this.have = have;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        @DatabaseField(dataType = DataType.SERIALIZABLE)
        private ArrayList<EmoticonImageBean> imglist;

        public String getBannerUrl() {
            return bannerUrl;
        }

        public void setBannerUrl(String bannerUrl) {
            this.bannerUrl = bannerUrl;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public ArrayList<EmoticonImageBean> getImglist() {
            return imglist;
        }

        public void setImglist(ArrayList<EmoticonImageBean> imglist) {
            this.imglist = imglist;
        }

        public EmoticonZip() {
            super();
            // TODO Auto-generated constructor stub
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getIsBuy() {
            return isBuy;
        }

        public void setIsBuy(int isBuy) {
            this.isBuy = isBuy;
        }

        public String getDownUrl() {
            return downUrl;
        }

        public void setDownUrl(String downUrl) {
            this.downUrl = downUrl;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIcoUrl() {
            return icoUrl;
        }

        public void setIcoUrl(String icoUrl) {
            this.icoUrl = icoUrl;
        }

        public static class EmoticonJsonBean implements Serializable {
            @Expose
            public String gifUrl;
            @Expose
            public String flagName;
            public String gifFile;

            public String getGifFile() {
                return gifFile;
            }

            public void setGifFile(String gifFile) {
                this.gifFile = gifFile;
            }

            public String getGifUrl() {
                return gifUrl;
            }

            public void setGifUrl(String gifUrl) {
                this.gifUrl = gifUrl;
            }

            public String getFlagName() {
                return flagName;
            }

            public void setFlagName(String flagName) {
                this.flagName = flagName;
            }

            public EmoticonJsonBean(String gifUrl, String flagName,
                                    String gifFile) {
                super();
                this.gifUrl = gifUrl;
                this.flagName = flagName;
                this.gifFile = gifFile;
            }

            public EmoticonJsonBean() {
                super();
                // TODO Auto-generated constructor stub
            }
        }

        public static class EmoticonImageBean implements Serializable {
            private int imgid;
            private String nickname;
            private String flagName;
            private String pngUrl;
            private String gifUrl;
            private String pngfile;

            public String getPngfile() {
                return pngfile;
            }

            public void setPngfile(String pngfile) {
                this.pngfile = pngfile;
            }

            public String getGiffile() {
                return giffile;
            }

            public void setGiffile(String giffile) {
                this.giffile = giffile;
            }

            private String giffile;

            public int getImgid() {
                return imgid;
            }

            public void setImgid(int imgid) {
                this.imgid = imgid;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getFlagName() {
                return flagName;
            }

            public void setFlagName(String flagName) {
                this.flagName = flagName;
            }

            public String getPngUrl() {
                return pngUrl;
            }

            public void setPngUrl(String pngUrl) {
                this.pngUrl = pngUrl;
            }

            public String getGifUrl() {
                return gifUrl;
            }

            public void setGifUrl(String gifUrl) {
                this.gifUrl = gifUrl;
            }

            public static class EmoticonRes extends EmoticonImageBean implements Serializable {
                private int res;

                public int getRes() {
                    return res;
                }

                public void setRes(int res) {
                    this.res = res;
                }
            }
        }
    }
}
