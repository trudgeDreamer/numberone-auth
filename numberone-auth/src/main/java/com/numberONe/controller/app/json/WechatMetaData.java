package com.numberONe.controller.app.json;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "xml")
public class WechatMetaData implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	private String id;
	
	public java.lang.String getId(){
		return this.id;
	}
	public void setId(java.lang.String id){
		this.id = id;
	}
	/**订单类型，0为支付订单，1为对款订单*/
//	private int orderType;
//	
//	public int getOrderType() {
//		return orderType;
//	}
//	public void setOrderType(int orderType) {
//		this.orderType = orderType;
//	}
	
	/**公众账号ID*/
	private String appid;
	/**商户号*/
	private String mch_id;
	/**设备号*/
	private String device_info;
	/**随机字符串*/
	private String nonce_str;
	/**签名*/
	private String sign;
	/**商品描述*/
	private String body;
	/**商品详情*/
	private String detail;
	/**附加数据*/
	private String attach;
	/**微信订单号*/
	private String transaction_id;
	/**商户订单号*/
	private String out_trade_no;
	/**商户退款单号*/
	private String out_refund_no ;
	
	/**货币类型*/
	private String fee_type;
	/**总金额*/
	private BigInteger total_fee;
	/**退款金额*/
	private BigInteger refund_fee ;
	/**结算金额*/
	private BigInteger settlement_total_fee ;
	/**终端IP*/
	private String spbill_create_ip;
	/**交易起始时间*/
	private String time_start;
	/**交易结束时间*/
	private String time_expire;
	/**商品标记*/
	private String goods_tag;
	/**通知地址*/
	private String notify_url;
	/**交易类型*/
	private String trade_type;
	/**商品ID*/
	private String product_id;
	/**指定支付方式*/
	private String limit_pay;
	/**用户标识*/
	private String openid;
	/**返回状态码*/
	private String return_code;
	/**返回信息*/
	private String return_msg;
	/**业务结果*/
	private String result_code;
	/**错误代码*/
	private String err_code;
	/**错误代码描述*/
	private String err_code_des;
	/**预支付交易会话标识*/
	private String prepay_id;
	/**二维码链接*/
	private String code_url;
	/**是否关注公众账号*/
	private String is_subscribe;
	/**交易状态*/
	private String trade_state;
	/**付款银行*/
	private String bank_type;
	/**现金支付金额*/
	private Integer cash_fee;
	/**代金券或立减优惠金额*/
	private Integer coupon_fee;
	/**支付完成时间*/
	private String time_end;
	/**交易状态描述*/
	private String trade_state_desc;
	
	private String op_user_id;

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getDevice_info() {
		return device_info;
	}

	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getOut_refund_no() {
		return out_refund_no;
	}

	public void setOut_refund_no(String out_refund_no) {
		this.out_refund_no = out_refund_no;
	}

	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}

	public BigInteger getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(BigInteger total_fee) {
		this.total_fee = total_fee;
	}

	public BigInteger getRefund_fee() {
		return refund_fee;
	}

	public void setRefund_fee(BigInteger refund_fee) {
		this.refund_fee = refund_fee;
	}

	public BigInteger getSettlement_total_fee() {
		return settlement_total_fee;
	}

	public void setSettlement_total_fee(BigInteger settlement_total_fee) {
		this.settlement_total_fee = settlement_total_fee;
	}

	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}

	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}

	public String getTime_start() {
		return time_start;
	}

	public void setTime_start(String time_start) {
		this.time_start = time_start;
	}

	public String getTime_expire() {
		return time_expire;
	}

	public void setTime_expire(String time_expire) {
		this.time_expire = time_expire;
	}

	public String getGoods_tag() {
		return goods_tag;
	}

	public void setGoods_tag(String goods_tag) {
		this.goods_tag = goods_tag;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getLimit_pay() {
		return limit_pay;
	}

	public void setLimit_pay(String limit_pay) {
		this.limit_pay = limit_pay;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getReturn_code() {
		return return_code;
	}

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}

	public String getReturn_msg() {
		return return_msg;
	}

	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}

	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	public String getErr_code() {
		return err_code;
	}

	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}

	public String getErr_code_des() {
		return err_code_des;
	}

	public void setErr_code_des(String err_code_des) {
		this.err_code_des = err_code_des;
	}

	public String getPrepay_id() {
		return prepay_id;
	}

	public void setPrepay_id(String prepay_id) {
		this.prepay_id = prepay_id;
	}

	public String getCode_url() {
		return code_url;
	}

	public void setCode_url(String code_url) {
		this.code_url = code_url;
	}

	public String getIs_subscribe() {
		return is_subscribe;
	}

	public void setIs_subscribe(String is_subscribe) {
		this.is_subscribe = is_subscribe;
	}

	public String getTrade_state() {
		return trade_state;
	}

	public void setTrade_state(String trade_state) {
		this.trade_state = trade_state;
	}

	public String getBank_type() {
		return bank_type;
	}

	public void setBank_type(String bank_type) {
		this.bank_type = bank_type;
	}

	public Integer getCash_fee() {
		return cash_fee;
	}

	public void setCash_fee(Integer cash_fee) {
		this.cash_fee = cash_fee;
	}

	public Integer getCoupon_fee() {
		return coupon_fee;
	}

	public void setCoupon_fee(Integer coupon_fee) {
		this.coupon_fee = coupon_fee;
	}

	public String getTime_end() {
		return time_end;
	}

	public void setTime_end(String time_end) {
		this.time_end = time_end;
	}

	public String getTrade_state_desc() {
		return trade_state_desc;
	}

	public void setTrade_state_desc(String trade_state_desc) {
		this.trade_state_desc = trade_state_desc;
	}

	public String getOp_user_id() {
		return op_user_id;
	}

	public void setOp_user_id(String op_user_id) {
		this.op_user_id = op_user_id;
	}
	@Override
	public String toString() {
		return "WechatMetaData [sign=" + sign + ", body=" + body + ", total_fee=" + total_fee + ", return_code="
				+ return_code + ", return_msg=" + return_msg + ", result_code=" + result_code + ", err_code=" + err_code
				+ ", err_code_des=" + err_code_des + ", prepay_id=" + prepay_id + ", trade_state=" + trade_state + "]";
	}

	
	
}
