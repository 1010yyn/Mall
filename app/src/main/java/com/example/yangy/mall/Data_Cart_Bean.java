package com.example.yangy.mall;

import java.util.List;

//OpenRecordBean
public class Data_Cart_Bean {
    private List<Data_Shop_Bean> data;//店铺列表

    public List<Data_Shop_Bean> getShopData() {
        return data;
    }

    public void setShopData(List<Data_Shop_Bean> data) {
        this.data = data;
    }

    //DataBean
    public static class Data_Shop_Bean {
        private String shop_name;//店铺名称
        private int amount;//当前商店商品数量
        private List<Data_Goods_Bean> data;//商品列表

        Data_Shop_Bean() {
            amount = 0;
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

        public List<Data_Goods_Bean> getGoodsData() {
            return data;
        }

        public void setGoodsData(List<Data_Goods_Bean> data) {
            this.data = data;
        }


        //LogDOListBean
        public static class Data_Goods_Bean {
            private String shopname;//商店名称
            private String name;//商品名称
            private int price;//商品价格
            private int sum;//商品总数
            private int photo;//商品图片资源id
            private boolean isSelected;//是否被选中
            private String description;//商品描述

            Data_Goods_Bean() {
                isSelected = false;
            }

            public String getShopname() {
                return shopname;
            }

            public void setShopname(String shopname) {
                this.shopname = shopname;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPrice() {
                return Integer.toString(price);
            }

            public void setPrice(int price) {
                this.price = price;
            }

            public String getSum() {
                return Integer.toString(sum);
            }

            public void setSum(int sum) {
                this.sum = sum;
            }

            public int getPhoto() {
                return photo;
            }

            public void setPhoto(int photo) {
                this.photo = photo;
            }

            public boolean getStatus() {
                return isSelected;
            }

            public void setStatus(boolean status) {
                isSelected = status;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getDescription() {
                return description;
            }
        }
    }
}