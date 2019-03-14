package com.example.yangy.mall;

import java.util.List;

//OpenRecordBean
public class Data_Cart_Bean {
    private int amount;//当前购物车内店铺数量
    private List<Data_Shop_Bean> data;//店铺列表

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public List<Data_Shop_Bean> getShopData() {
        return data;
    }

    public void setShopData(List<Data_Shop_Bean> data) {
        this.data = data;
    }

    //DataBean
    public static class Data_Shop_Bean {
        private String shop_name;//店铺名称
        private List<Data_Goods_Bean> data;//商品列表

        public String getName() {
            return shop_name;
        }

        public void setName(String name) {
            this.shop_name = name;
        }

        public List<Data_Goods_Bean> getGoodsData() {
            return data;
        }

        public void setGoodsData(List<Data_Goods_Bean> data) {
            this.data = data;//直接向列表中添加商品项
        }

        //LogDOListBean
        public static class Data_Goods_Bean {
            private String name;//商品名称
            private int price;//商品价格
            private int sum;//商品总数
            private int photo;//商品图片资源id

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
        }
    }
}