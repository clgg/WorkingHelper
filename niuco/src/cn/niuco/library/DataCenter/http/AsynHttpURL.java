package cn.niuco.library.DataCenter.http;



public class AsynHttpURL {

        public static int HTTP_STATUS_SUCCESS = 0;
        public static String VER = "1.0";// 协议版本
        public static String SERVER;
        public static String TOKEN="eef78794-2a9a-4d3f-a35e-be10ad2ecaa9c9d9bece-9afb-4698-88d5-701b";

        static {
//            if (BuildConfig.DEBUG) {// BuildConfig.DEBUG
//                SERVER = "http://zy.lenovomm.com/data/";
//            } else {
//                SERVER = "http://192.168.1.103:8080/v1/";
//            }
            SERVER = "http://zy.lenovomm.com/data/";
        }
        public static final String GET_PRODUCT_LIST = SERVER + "product/list";

        public static final String USER_LOGIN = SERVER + "user/auth";
        public static final String UPDATE_COMMENT=SERVER+"comment/create";
        public static final String UPDATE_FAVORITE=SERVER+"comment/favorite";
        public static final String UPDATE_FAVORITE_PRODUCT=SERVER+"product/favorite";
        public static final String COMMENT_LIST=SERVER+"comment/list";
        public static final String CATEGORY_LIST=SERVER+"category/list";
        public static final String COMMENT_DELETE=SERVER+"comment/delete";
        public static final String PK_LIST=SERVER+"pk/list";
        public static final String LOGIN=SERVER+"user/auth";
}
