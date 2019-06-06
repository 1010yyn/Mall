package com.example.yangy.mall;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

//OpenRecordBean
public class Data_Cart_Bean implements Serializable {
    private List<Data_Shop_Bean> data;//店铺列表

    List<Data_Shop_Bean> getShopData() {
        return data;
    }

    void setShopData(List<Data_Shop_Bean> data) {
        this.data = data;
    }

    //DataBean
    public static class Data_Shop_Bean implements Serializable {
        private String shop_id;//商店id
        private String shop_name;//店铺名称
        private int amount;//当前商店商品数量
        private List<Data_Goods_Bean> data;//商品列表

        Data_Shop_Bean() {
            amount = 0;
        }

        public String getId() {
            return shop_id;
        }

        public void setId(String shop_id) {
            this.shop_id = shop_id;
        }

        public String getName() {
            return shop_name;
        }

        public void setName(String name) {
            this.shop_name = name;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        List<Data_Goods_Bean> getGoodsData() {
            return data;
        }

        void setGoodsData(List<Data_Goods_Bean> data) {
            this.data = data;
        }

        //LogDOListBean
        public static class Data_Goods_Bean implements Serializable {
            private String shop_name;//商店名称
            private String shop_id;//商店id
            private String goods_id;//商品id
            private String goods_name;//商品名称
            private int price;//商品价格
            private String sum;//商品总数
            private String photo;//商品图片资源
            private boolean isSelected;//是否被选中
            private String description;//商品描述
            private String tag;//商品标签

            Data_Goods_Bean() {
                isSelected = false;
            }

            String getShopid() {
                return shop_id;
            }

            void setShopid(String shop_id) {
                this.shop_id = shop_id;
            }

            String getGoodsid() {
                return goods_id;
            }

            void setGoodsid(String goods_id) {
                this.goods_id = goods_id;
            }

            String getShopname() {
                return shop_name;
            }

            void setShopname(String shopname) {
                this.shop_name = shopname;
            }

            String getGoodsname() {
                return goods_name;
            }

            void setGoodsname(String name) {
                this.goods_name = name;
            }

            String getPrice() {
                return Integer.toString(price);
            }

            void setPrice(int price) {
                this.price = price;
            }

            String getSum() {
                return sum;
            }

            void setSum(String sum) {
                this.sum = sum;
            }

            String getPhoto() {
                return photo;
            }

            void setPhoto(String photo) {
                this.photo = photo;
            }

            boolean getStatus() {
                return isSelected;
            }

            void setStatus(boolean status) {
                isSelected = status;
            }

            void setDescription(String description) {
                this.description = description;
            }

            String getDescription() {
                return description;
            }

            void setTag(String tag) {
                this.tag = tag;
            }

            String getTag() {
                return tag;
            }
        }
    }
}