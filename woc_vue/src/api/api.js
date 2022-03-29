import {
  get,
  post
} from './request';

/**
 * 上传图片
 * @param object
 * @returns {Promise | Promise<unknown>}
 */
const upload = (object) => post(`/api/oss/upload`, object); // 文件上传

/**
 *  admin
 * @param object
 * @returns {Promise | Promise<unknown>}
 */
const login = (object) => post(`/api/admin/login`, object); //登录
const setAdmin = (object) => post(`/api/admin/setAdmin`, object); // 修改管理员
const getAdmin = (object) => get(`/api/admin/getAdmin`, object); // 查询管理员
const delAdminById = (object) => get(`/api/admin/delById`, object); // 删除管理员

/**
 * applets
 * @param object
 * @returns {Promise | Promise<unknown>}
 */
const getApplets = (object) => get(`/api/applets/getApplets`, object); // 查询小程序APPID
const setApplets = (object) => post(`/api/applets/setApplets`, object);// 修改小程序APPID

/**
 * user
 * @param object
 * @returns {Promise | Promise<unknown>}
 */
const getUserList = (object) => get(`/api/user/getUserList`, object); // 查询用户列表

/**
 * menuBar
 * @param object
 * @returns {Promise | Promise<unknown>}
 */
const getMenuBar = (object) => get(`/api/menuBar/getMenuBar`, object); // 查询菜单栏

/**
 * activity
 * @param object
 * @returns {Promise | Promise<unknown>}
 */
const setActivity = (object) => post(`/api/activity/setActivity`, object); // 添加和修改活动
const getActivity = (object) => get(`/api/activity/getActivity`, object); // 查询活动
const getActivityById = (object) => get(`/api/activity/getActivityById`, object); // 查询详情
const delActivityById = (object) => get(`/api/activity/delActivityById`, object); // 删除

/**
 *  classifys
 *  商品分类
 * @param object
 * @returns {Promise | Promise<unknown>}
 */
const setClassifys = (object) => post(`/api/classifys/setClassifys`, object); // 添加和修改
const getClassifyList = (object) => get(`/api/classifys/getClassifyList`, object); // 分类和子类一起展示
const getClassifys = (object) => get(`/api/classifys/getClassifys`, object); // 查询分类
const getClassifysSon = (object) => get(`/api/classifys/getClassifysSon`, object); // 查询分类的下级
const delClassifyById = (object) => get(`/api/classifys/delClassifyById`, object); // 删除

/**
 * item
 * 商品
 * @param object
 * @returns {Promise | Promise<unknown>}
 */
const setItem = (object) => post(`/api/item/setItem`, object); // 添加或修改商品
const getItem = (object) => get(`/api/item/getItem`, object); // 查询商品
const getItemById = (object) => get(`/api/item/getItemById`, object); // 查询商品详情
const delItemById = (object) => get(`/api/item/delItemById`, object); // 删除商品
const setSpecs = (object) => post(`/api/item/setSpecs`, object); // 添加或修改规格
const getSpecs = (object) => get(`/api/item/getSpecs`, object); // 获取商品的规格
const delSpecsById = (object) => get(`/api/item/delSpecsById`, object); // 删除规格
const batchItem = (object) => get(`/api/item/batchItem`, object); // 商品批量操作

/**
 * region
 * 地区管理
 * @param object
 * @returns {Promise | Promise<unknown>}
 */
const setRegion = (object) => post(`/api/region/setRegion`, object); // 添加和修改地区
const getRegionAndCircle = (object) => get(`/api/region/getRegionAndCircle`, object); // 后台select接口
const getRegion = (object) => get(`/api/region/getRegion`, object); // 查询地区
const delRegionById = (object) => get(`/api/region/delRegionById`, object); // 删除地区
const setCircle = (object) => post(`/api/region/setCircle`, object); // 添加和修改地区商圈
const getCircle = (object) => get(`/api/region/getCircle`, object); // 查询商圈
const delCircleById = (object) => get(`/api/region/delCircleById`, object); // 删除地区的商圈

/**
 * shop
 * 店铺管理API
 * @param object
 * @returns {Promise | Promise<unknown>}
 */
const setShop = (object) => post(`/api/shop/setShop`, object); // 添加和修改
const delShopById = (object) => get(`/api/shop/delShopById`, object); // 删除
const getShopById = (object) => get(`/api/shop/getShopById`, object); // 查询详情
const getShop = (object) => get(`/api/shop/getShop`, object); // 查询

/**
 * box
 * 盲盒API
 * @param object
 * @returns {Promise | Promise<unknown>}
 */
const setBoxClassify = (object) => post(`/api/box/setBoxClassify`, object); // 盲盒分类的添加和修改
const getBoxClassify = (object) => get(`/api/box/getBoxClassify`, object); // 查询分类
const delBoxClassifyById = (object) => get(`/api/box/delBoxClassifyById`, object); // 删除分类
const setBox = (object) => post(`/api/box/setBox`, object); // 盲盒的添加和修改
const getBoxById = (object) => get(`/api/box/getBoxById`, object); // 获取盲盒详情
const getBox = (object) => get(`/api/box/getBox`, object); // 查询盲盒列表
const delBoxById = (object) => get(`/api/box/delBoxById`, object); // 删除盲盒
const setBoxItem = (object) => post(`/api/box/setBoxItem`, object); // 盲盒下商品的添加和修改
const getBoxItem = (object) => get(`/api/box/getBoxItem`, object); // 获取盲盒下的商品
const delBoxItemById = (object) => get(`/api/box/delBoxItemById`, object); // 删除盲盒下的商品
const batchBox = (object) => get(`/api/box/batchBox`, object); // 盲盒的批量操作

/**
 * merchant
 * 商户API
 * @param object
 * @returns {Promise | Promise<unknown>}
 */
const setMerchant = (object) => post(`/api/merchant/setMerchant`, object); // 添加和修改商户信息
const delMerchantById = (object) => get(`/api/merchant/delMerchantById`, object); // 删除商户
const getMerchant = (object) => get(`/api/merchant/getMerchant`, object); // 获取商户列表
const getMerchantById = (object) => get(`/api/merchant/getMerchantById`, object); // 获取商户详情
const setDisable = (object) => get(`/api/merchant/setDisable`, object); // 封禁与解封商户
const setMerchantShop = (object) => post(`/api/merchant/setMerchantShop`, object); // 添加和修改商户下的店铺
const getMerchantShop = (object) => get(`/api/merchant/getMerchantShop`, object); // 查询商户下的店铺
const delMerchantShopById = (object) => get(`/api/merchant/delMerchantShopById`, object); // 删除商户下的店铺

/**
 * coupon
 * 优惠券API
 * @param object
 * @returns {Promise | Promise<unknown>}
 */
const setCoupon = (object) => post(`/api/coupon/setCoupon`, object); // 添加和修改
const getCoupon = (object) => get(`/api/coupon/getCoupon`, object); // 获取优惠券列表
const delCouponById = (object) => get(`/api/coupon/delCouponById`, object); //  删除优惠券
const getCouponById = (object) => get(`/api/coupon/getCouponById`, object); //  查询优惠券详情
const offCouponById = (object) => get(`/api/coupon/offCouponById`, object); //  关闭优惠券


/**
 * banner
 * 轮播图API
 * @param object
 * @returns {Promise | Promise<unknown>}
 */
const setBanner = (object) => post(`/api/banner/setBanner`, object)
const getBanner = (object) => get(`/api/banner/getBanner`, object)
const delBannerById = (object) => get(`/api/banner/delBannerById`, object)

/**
 * configs
 * 基础配置APi
 * @param object
 * @returns {Promise | Promise<unknown>}
 */
const setConfigs = (object) => post(`/api/configs/setConfigs`, object)
const getConfigs = (object) => get(`/api/configs/getConfigs`, object)

/**
 * balance
 * 余额API
 * @param object
 * @returns {Promise | Promise<unknown>}
 */
const setUserBalance = (object) => post(`/api/balance/setUserBalance`, object)
const setCashOut = (object) => post(`/api/balance/setCashOut`, object)
const getBalanceById = (object) => get(`/api/balance/getBalanceById`, object)
const getBalanceAndAdmin = (object) => get(`/api/balance/getBalanceAndAdmin`, object)
const getUserBalance = (object) => get(`/api/balance/getUserBalance`, object)
const delBalanceById = (object) => get(`/api/balance/delBalanceById`, object)

/**
 * feedbacks
 * 反馈API
 * @param object
 * @returns {Promise | Promise<unknown>}
 */
const setFeedbacks = (object) => post(`/api/feedbacks/setFeedbacks`, object)
const delFeedbacksById = (object) => get(`/api/feedbacks/delFeedbacksById`, object)
const getFeedbacks = (object) => get(`/api/feedbacks/getFeedbacks`, object)

/**
 * integralGoods
 * 积分商品API
 * @param object
 * @returns {Promise | Promise<unknown>}
 */
const setIntegralClassify = (object) => post(`/api/integralGoods/setIntegralClassify`, object); // 分类的添加和修改
const getIntegralClassify = (object) => get(`/api/integralGoods/getIntegralClassify`, object); // 查询分类
const delIntegralClassifyById = (object) => get(`/api/integralGoods/delIntegralClassifyById`, object); // 删除分类
const setIntegralGoods = (object) => post(`/api/integralGoods/setIntegralGoods`, object); // 积分商品的添加和修改
const getIntegralGoodsById = (object) => get(`/api/integralGoods/getIntegralGoodsById`, object); // 获取积分商品详情
const getIntegralGoods = (object) => get(`/api/integralGoods/getIntegralGoods`, object); // 查询积分商品列表
const delIntegralGoodsById = (object) => get(`/api/integralGoods/delIntegralGoodsById`, object); // 删除积分商品
const batchIntegral = (object) => get(`/api/integralGoods/batchIntegral`, object); // 批量操作积分商品


/**
 * nsOrder
 * 盲盒订单API
 * @param object
 * @returns {Promise | Promise<unknown>}
 */
const createOrder = (object) => post(`/api/nsOrder/createOrder`, object); // 创建订单
const getPinTuanList = (object) => get(`/api/nsOrder/getPinTuanList`, object); // 查询盲盒下拼单订单列表
const getNsOrder = (object) => get(`/api/nsOrder/getNsOrder`, object); // 获取盲盒订单信息
const getOrderById = (object) => get(`/api/nsOrder/getOrderById`, object); // 查询详情
const setShipping = (object) => get(`/api/nsOrder/setShipping`, object); // 发货
const setTheGoods = (object) => get(`/api/nsOrder/setTheGoods`, object); // 收货
const delNsOderById = (object) => get(`/api/nsOrder/delNsOderById`, object); // 删除订单


/**
 * 盲盒商城订单
 * @param object
 * @returns {Promise | Promise<unknown>}
 */
const getSellOrder = (object) => get(`/api/nsOrderSellOrder/getSellOrder`, object); // 查询商城订单
const getSellOrderById = (object) => get(`/api/nsOrderSellOrder/getSellOrderById`, object); // 查询盲盒订单详情
const setSellOrder = (object) => post(`/api/nsOrderSellOrder/setSellOrder`, object); // 修好盲盒商城订单信息
const createSellOrder = (object) => post(`/api/nsOrderSellOrder/createSellOrder`, object); // 创建商城订单
const delSellOrderById = (object) => get(`/api/nsOrderSellOrder/delSellOrderById`, object); // 删除订单

/**
 * 积分订单
 * @param object
 * @returns {Promise | Promise<unknown>}
 */
const createIntegralOrder = (object) => post(`/api/nsIntegralOrder/createIntegralOrder`, object); // 创建积分兑换订单
const getIntegralOrder = (object) => get(`/api/nsIntegralOrder/getIntegralOrder`, object); // 查询积分订单
const getIntegralOrderById = (object) => get(`/api/nsIntegralOrder/getIntegralOrderById`, object); // 查询积分订单详情
const setIntegralOrder = (object) => post(`/api/nsIntegralOrder/setIntegralOrder`, object); // 修好积分订单信息
const delIntegralOrderById = (object) => get(`/api/nsIntegralOrder/delIntegralOrderById`, object); // 删除订单

/**
 * 系统通知
 * @param object
 * @returns {Promise | Promise<unknown>}
 */
const setNotices = (object) => post(`/api/notices/setNotices`, object); // 添加和修改通知
const getNotices = (object) => get(`/api/notices/getNotices`, object); // 查询通知
const delNoticesById = (object) => get(`/api/notices/delNoticesById`, object); // 查询通知


/**
 * 优惠券领取
 * @param object
 * @returns {Promise | Promise<unknown>}
 */
const getUserCouponToCouponId = (object) => get(`/api/userCoupon/getUserCouponToCouponId`, object); // 查看优惠券的领取情况


/**
 * 后台系统的概况
 * @param object
 * @returns {Promise | Promise<unknown>}
 */
const doHome = (object) => get(`/api/home/doHome`, object); // 后台系统的概况
const getNsOrderCount = (object) => get(`/api/home/getNsOrderCount`, object); // 获取7天订单数

/**
 * 话题列表
 * @param object
 * @returns {Promise | Promise<unknown>}
 */
const setTopics = (object) => post(`/api/topics/setTopics`, object); // 话题的添加和修改
const getTopics = (object) => get(`/api/topics/getTopics`, object); // 查询话题
const delTopics = (object) => get(`/api/topics/delTopics`, object); // 删除话题


/**
 * dynamic
 * 动态
 * @param object
 * @returns {Promise | Promise<unknown>}
 */
const delDynamicById = (object) => get(`/api/dynamic/delDynamicById`, object); // 删除
const getDynamic = (object) => get(`/api/dynamic/getDynamic`, object); // 获取动态列表
const getCommentAndAdmin = (object) => get(`/api/comment/getCommentAndAdmin`, object); // 获取评论列表
const delCommentById = (object) => get(`/api/comment/delCommentById`, object); // 删除评论




export default {
  login, upload,
  setAdmin, getAdmin, delAdminById,
  getApplets, setApplets,
  getUserList,
  getMenuBar,
  setActivity, getActivity, getActivityById, delActivityById,
  setClassifys, getClassifyList, delClassifyById, getClassifys, getClassifysSon,
  setItem, getItem, getItemById, delItemById, setSpecs, getSpecs, delSpecsById,batchItem,
  setRegion, getRegionAndCircle, getRegion, delRegionById, setCircle, getCircle, delCircleById,
  setShop, delShopById, getShopById, getShop,
  setBoxClassify, getBoxClassify, delBoxClassifyById, setBox, getBoxById, getBox, delBoxById, setBoxItem, getBoxItem, delBoxItemById,batchBox,
  setMerchant,delMerchantById,getMerchant,getMerchantById,setDisable,setMerchantShop,getMerchantShop,delMerchantShopById,
  setCoupon,getCoupon,delCouponById,getCouponById,offCouponById,
  setBanner,getBanner,delBannerById,
  setConfigs,getConfigs,
  setUserBalance,setCashOut,getBalanceById,getBalanceAndAdmin,getUserBalance,delBalanceById,
  setFeedbacks,delFeedbacksById,getFeedbacks,
  setIntegralClassify,getIntegralClassify,delIntegralClassifyById,setIntegralGoods,getIntegralGoodsById,getIntegralGoods,delIntegralGoodsById,batchIntegral,
  createOrder,getPinTuanList,getNsOrder,getOrderById,setShipping,setTheGoods,delNsOderById,
  getSellOrder,getSellOrderById,setSellOrder,delSellOrderById,
  createIntegralOrder,getIntegralOrder,getIntegralOrderById,setIntegralOrder,delIntegralOrderById,
  setNotices,getNotices,delNoticesById,
  getUserCouponToCouponId,
  doHome,getNsOrderCount,
  setTopics,getTopics,delTopics,
  delDynamicById,getDynamic,getCommentAndAdmin,delCommentById
};
